package ru.sbtqa.tag.swingback;

import org.netbeans.jemmy.operators.ComponentOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JCheckBoxOperator;
import org.netbeans.jemmy.operators.JTextComponentOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.qautils.i18n.I18N;
import ru.sbtqa.tag.qautils.i18n.I18NRuntimeException;
import ru.sbtqa.tag.swingback.annotations.ActionTitle;
import ru.sbtqa.tag.swingback.annotations.ActionTitles;
import ru.sbtqa.tag.swingback.annotations.Component;
import ru.sbtqa.tag.swingback.annotations.FormEntry;
import ru.sbtqa.tag.swingback.exceptions.SwingBackRuntimeException;
import ru.sbtqa.tag.swingback.jemmy.components.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Base form object class. Contains basic actions with elements, search methods
 */
public abstract class Form {

    private static final Logger LOG = LoggerFactory.getLogger(Form.class);

    private String title;
    private List<Field> formFields;
    private List<Method> formMethods;

    public Form() {
        title = this.getClass().getAnnotation(FormEntry.class).title();
        formMethods = Core.getDeclaredMethods(this.getClass());
        formFields = Core.getDeclaredFields(this.getClass());
    }

    public String getTitle() {
        return title;
    }


    @ActionTitle("push.button")
    public void pushButton(String title) {
        ((Button) getComponentOperator(title)).push();
    }

    @ActionTitle("table.is.empty")
    public void tableIsEmpty(String title) {
        assertThat("The table with title '" + title + "' is not empty.",
                ((Table) getComponentOperator(title)).getRowCount(), is(0));
    }

    @ActionTitle("click.table.column")
    public void clickOnTableColumnTitle(String title, String columnTitle) {
        ((Table) getComponentOperator(title)).clickOnTableColumnTitle(columnTitle);
    }

    @ActionTitle("select.first.table.row")
    public void selectFistTableElem(String title) {
        ((Table) getComponentOperator(title)).selectFistTableElem();
    }

    @ActionTitle("expand.tree")
    public void expandTree(String title, String path) {
        String[] paths = path.split("->");
        ((Tree) getComponentOperator(title)).chooseTreeNode(paths);
    }

    @ActionTitle("fill.field")
    public void fillField(String title, String value) {
        ((TextField) getComponentOperator(title)).setText(value);
    }


    @ActionTitle("set.checkbox")
    public void setCheckBox(String title, String value) {
        ((CheckBox) getComponentOperator(title)).setCheckBox(Boolean.valueOf(value));
    }

    @ActionTitle("check.checkbox")
    public void checkSelectedCheckBox(String title, String value) {
        assertThat("The component with title '" + title + "' is " + (Boolean.valueOf(value) ? "not " : "") + "selected.",
                ((CheckBox) getComponentOperator(title)).isSelected(), is(Boolean.valueOf(value)));
    }

    @ActionTitle("choose.combobox.item")
    public void chooseComboBoxItem(String title, String value) {
        ((ComboBox) getComponentOperator(title)).chooseComboBoxItem(value, String::equals);
    }


    @ActionTitle("check.component.editable")
    public void checkComponentEditable(String title, String value) {
        boolean isEditable = false;
        ComponentOperator componentOperator = getComponentOperator(title);
        if (componentOperator instanceof JTextComponentOperator) {
            isEditable = ((JTextComponentOperator) componentOperator).isEditable();
        }
        if (componentOperator instanceof JCheckBoxOperator) {
            isEditable = ((JCheckBoxOperator) componentOperator).isEnabled();
        }
        if (componentOperator instanceof JButtonOperator) {
            isEditable = ((JButtonOperator) componentOperator).isEnabled();
        }
        assertThat("The component with title '" + title + "' is " + (Boolean.valueOf(value) ? "not " : "") + "editable.", isEditable, is(Boolean.valueOf(value)));
    }

//    ----------------------------------------------------

    /**
     * Find method with corresponding title on current from, and execute it
     *
     * @param title title of the method to call
     * @param param parameters that will be passed to method
     */
    public Object executeMethodByTitle(String title, Object... param) {
        for (Method method : formMethods) {
            if (Core.isRequiredAction(method, title)) {
                try {
                    method.setAccessible(true);
                    return method.invoke(this, param);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new SwingBackRuntimeException("Failed to invoke method", e);
                }
            }
        }
        throw new SwingBackRuntimeException(new NoSuchMethodException("There is no '" + title + "' method on '" + this.getTitle() + "' form object"));
    }


    /**
     * Find component operator {@link ComponentOperator} element which has specified type and title
     *
     * @param title component title
     * @return ComponentOperator with specified type and title
     */
    public ComponentOperator getComponentOperator(String title) {
        for (Field field : formFields) {
            if (Core.isRequiredField(field, title)) {
                try {
                    field.setAccessible(true);
                    return (ComponentOperator) field.get(this);
                } catch (IllegalAccessException e) {
                    throw new SwingBackRuntimeException("Failed to read field (ComponentOperator) with name '" + title + "'.", e);
                }
            }
        }
        throw new SwingBackRuntimeException(new NoSuchFieldException("There is no '" + title + "' field on '" + this.getTitle() + "' form object"));
    }

    /**
     * Helper methods for manipulations on fields and objects
     */
    private static class Core {

        private Core() {
        }

        /**
         * Check whether given method has {@link ActionTitle} or
         * {@link ActionTitles} annotation with required title
         *
         * @param method method to check
         * @param title required title
         * @return true|false
         */
        private static Boolean isRequiredAction(Method method, final String title) {
            ActionTitle actionTitle = method.getAnnotation(ActionTitle.class);
            ActionTitles actionTitles = method.getAnnotation(ActionTitles.class);
            List<ActionTitle> actionList = new ArrayList<>();

            if (actionTitles != null) {
                actionList.addAll(Arrays.asList(actionTitles.value()));
            }
            if (actionTitle != null) {
                actionList.add(actionTitle);
            }

            for (ActionTitle action : actionList) {
                String actionValue = action.value();
                try {
                    I18N i18n = I18N.getI18n(method.getDeclaringClass(), Bridge.getLocale(), I18N.DEFAULT_BUNDLE_PATH);
                    actionValue = i18n.get(action.value());
                } catch (I18NRuntimeException e) {
                    LOG.debug("There is no bundle for translation class. Leave it as is", e);
                }

                if (actionValue.equals(title)) {
                    return true;
                }
            }

            return false;
        }

        /**
         * Check whether given field has {@link Component} annotation with required title
         *
         * @param field field to check
         * @param title required title
         * @return true|false
         */
        private static boolean isRequiredField(Field field, String title) {
            Component component = field.getAnnotation(Component.class);
            List<Component> componentListList = new ArrayList<>();
            if (component != null) {
                componentListList.add(component);
            }
            return componentListList.stream().filter(comInf -> comInf.title().equals(title)).findFirst().isPresent();
        }


        /**
         * Return a list of methods declared in the given class and its superclasses
         *
         * @param clazz class to check
         * @return list of methods. could be empty list
         */
        public static List<Method> getDeclaredMethods(Class<? extends Form> clazz) {
            List<Method> methods = new ArrayList<>();
            methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));

            Class supp = clazz.getSuperclass();

            while (supp != java.lang.Object.class) {
                methods.addAll(Arrays.asList(supp.getDeclaredMethods()));
                supp = supp.getSuperclass();
            }

            return methods;

        }

        /**
         * Return a list of fields declared in the given class and its superclasses
         *
         * @param clazz class to check
         * @return list of fields. could be empty list
         */
        public static List<Field> getDeclaredFields(Class<? extends Form> clazz) {
            List<Field> fields = new ArrayList<>();
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

            Class supp = clazz.getSuperclass();

            while (supp != java.lang.Object.class) {
                fields.addAll(Arrays.asList(supp.getDeclaredFields()));
                supp = supp.getSuperclass();
            }

            return fields;
        }
    }

}