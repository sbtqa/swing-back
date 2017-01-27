package ru.sbt.qa.swingback;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.junit.Assert;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.*;
import ru.sbt.qa.swingback.annotations.ActionTitle;
import ru.sbt.qa.swingback.annotations.ActionTitles;
import ru.sbt.qa.swingback.annotations.Component;
import ru.sbt.qa.swingback.annotations.FormEntry;
import ru.sbt.qa.swingback.jemmy.CommonActions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * Base form object class. Contains basic actions with elements, search methods
 */
public abstract class Form {

    private String title;
    protected ContainerOperator currentCont;
    protected JTabbedPaneOperator currentTabbedPane;
    private List<Field> formFields;
    private List<Method> formMethods;
    private List<Field> formTabbedPanes;
    private boolean isCurrentTabbedPane;

    public String getTitle() {
        return title;
    }

    /**
     * form component types
     */
    public enum ComponentType {
        TEXT_FIELD,
        TEXT_AREA,
        BUTTON,
        CONTAINER,
        COMBO_BOX,
        TABBED_PANE,
        TABLE,
        CHECK_BOX,
        TREE,
        LABEL,
        RADIO_BUTTON,
        OTHER
    }

    public Form() {
        title = this.getClass().getAnnotation(FormEntry.class).title();
        formMethods = Core.getDeclaredMethods(this.getClass());
        formFields = Core.getDeclaredFields(this.getClass());
        formTabbedPanes = new LinkedList<>();
        for (Field field : formFields) {
            if (Core.isRequiredField(field, ComponentType.TABBED_PANE)) {
                formTabbedPanes.add(field);
            }
        }
    }


    @ActionTitle("нажимает на кнопку")
    @ActionTitle("push button")
    public void pushButton(String title) throws NoSuchFieldException {
        ComponentChooser ch = getComponentChooser(title);
        new JButtonOperator(getCurrentContainerOperator(), ch).push();
    }

    @ActionTitle("выбирает первую запись в таблице")
    @ActionTitle("select first table row")
    public void m2(String title) throws NoSuchFieldException {
        ComponentChooser ch = getComponentChooser(title);
        CommonActions.selectFistTableElem(new JTableOperator(getCurrentContainerOperator(), ch));
    }

    @ActionTitle("")
    @ActionTitle("expand tree")
    public void m3(String title, String path) throws NoSuchFieldException {
        ComponentChooser ch = getComponentChooser(title);
        String[] paths = path.split("->");
        CommonActions.chooseTreeNode(new JTreeOperator(getCurrentContainerOperator(), ch), paths);
    }

    @ActionTitle("")
    @ActionTitle("fill field")
    public void fiilField(String title, String value) throws NoSuchFieldException {
        ComponentChooser ch = getComponentChooser(title);
        new JTextComponentOperator(getCurrentContainerOperator(), ch).setText(value);
    }

    @ActionTitle("")
    @ActionTitle("set check box")
    public void setCheckBox(String title, String value) throws NoSuchFieldException {
        ComponentChooser ch = getComponentChooser(title);
        CommonActions.setCheckBox(getCurrentContainerOperator(), ch, Boolean.valueOf(value));
    }

    @ActionTitle("")
    @ActionTitle("choose combo box item")
    public void chooseComboBoxItem(String title, String value) throws NoSuchFieldException {
        ComponentChooser ch = getComponentChooser(title);
        CommonActions.chooseComboBoxItem(new JComboBoxOperator(getCurrentContainerOperator(), getComponentChooser(title)), value, String::equals);
    }

    @ActionTitle("")
    @ActionTitle("table is empty")
    public void tableIsEmpty(String title) throws NoSuchFieldException {
        Assert.assertThat(new JTableOperator(getCurrentContainerOperator(), getComponentChooser(title)).getRowCount(), is(0));
    }

//    ----------------------------------------------------

    /**
     * Find method with corresponding title on current from, and execute it
     *
     * @param title title of the method to call
     * @param param parameters that will be passed to method
     * @throws java.lang.NoSuchMethodException if required method couldn't be found
     */
    public Object executeMethodByTitle(String title, Object... param) throws NoSuchMethodException {
        for (Method method : formMethods) {
            if (Core.isRequiredAction(method, title)) {
                try {
                    method.setAccessible(true);
                    return MethodUtils.invokeMethod(this, method.getName(), param);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new SwingBackRuntimeException("Failed to invoke method", e);
                }
            }
        }
        throw new NoSuchMethodException("There is no '" + title + "' method on '" + this.getTitle() + "' form object");
    }


    /**
     * Return {@link ComponentChooser} component which has a specified title
     *
     * @param title a component name
     * @throws NoSuchFieldException if required field couldn't be found
     */
    public ComponentChooser getComponentChooser(String title) throws NoSuchFieldException {
        for (Field field : formFields) {
            if (Core.isRequiredField(field, title)) {
                try {
                    field.setAccessible(true);
                    return (ComponentChooser) FieldUtils.readField(field, this);
                } catch (IllegalAccessException e) {
                    throw new SwingBackRuntimeException("Failed to read field (ComponentChooser) with name '" + title + "'", e);
                }
            }
        }
        throw new NoSuchFieldException("There is no '" + title + "' field on '" + this.getTitle() + "' form object");
    }

    /**
     * Find the component type {@link ComponentType} a component with specified title
     *
     * @param title component title
     * @return component type
     * @throws NoSuchFieldException if required field couldn't be found
     */
    public ComponentType getComponentType(String title) throws NoSuchFieldException {
        for (Field field : formFields) {
            if (Core.isRequiredField(field, title)) {
                field.setAccessible(true);
                return field.getAnnotation(Component.class).type();
            }
        }
        throw new NoSuchFieldException("There is no '" + title + "' field on '" + this.getTitle() + "' form object");
    }

    /**
     * Find component operator {@link ComponentOperator} element which has specified type and title
     *
     * @param type  component type
     * @param title component title
     * @return ComponentOperator with specified type and title
     * @throws NoSuchFieldException if required field couldn't be found
     */
    public ComponentOperator getComponentOperator(ComponentType type, String title) throws NoSuchFieldException {
        for (Field field : formFields) {
            if (Core.isRequiredField(field, title) & Core.isRequiredField(field, type)) {
                try {
                    field.setAccessible(true);
                    return (ComponentOperator) FieldUtils.readField(field, this);
                } catch (IllegalAccessException e) {
                    throw new SwingBackRuntimeException("Failed to read field (ComponentOperator) with name '" + title + "' and type '" + type + "'.", e);
                }
            }
        }
        throw new NoSuchFieldException("There is no '" + title + "' field on '" + this.getTitle() + "' form object");
    }

    /**
     * Return current form container.
     */
    public ContainerOperator getCurrentContainerOperator() {
            return isCurrentTabbedPane ? currentTabbedPane : currentCont;
    }

    /**
     * Switch current container to the container.
     */
    public void switchToContainer() {
        isCurrentTabbedPane = false;
    }

    /**
     * Switch current container to a pane of the tapped pane.
     * Required pane is looked up by title at all tabbed panes this form.
     *
     * @param title title of the required pane
     */
    public void selectTabPane(String title, String page) throws NoSuchFieldException {
        System.err.println("Inside select " + title + " " + page);
        ComponentChooser tpChooser = getComponentChooser(title);
        System.err.println("Inside select " + tpChooser);
        currentTabbedPane = new JTabbedPaneOperator(currentCont, tpChooser);
        System.err.println("currentTabbedPane.findPage(page) " + currentTabbedPane.findPage(page));
        if (currentTabbedPane.findPage(page) != -1) {
            currentTabbedPane.selectPage(page);
            isCurrentTabbedPane = true;
            return;
        }
        throw new NoSuchFieldException("There is no tabbed pane with title '" + title + "' on '" + this.getTitle() + "' form object");
    }

    /**
     * Helper methods for manipulations on fields and objects
     */
    private static class Core {

        /**
         * Check whether given method has {@link ActionTitle} annotation with required title
         *
         * @param method method to check
         * @param title  required title
         * @return true|false
         */
        private static Boolean isRequiredAction(Method method, String title) {
            ActionTitle actionTitle = method.getAnnotation(ActionTitle.class);
            ActionTitles actionTitles = method.getAnnotation(ActionTitles.class);
            List<ActionTitle> actionList = new ArrayList<>();
            if (actionTitles != null) {
                actionList.addAll(Arrays.asList(actionTitles.value()));
            }
            if (actionTitle != null) {
                actionList.add(actionTitle);
            }
            return actionList.stream().filter(action -> action.value().equals(title)).findFirst().isPresent();
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
         * Check whether given field has {@link Component} annotation with required type
         *
         * @param field field to check
         * @param type  required type
         * @return true|false
         */
        private static boolean isRequiredField(Field field, ComponentType type) {
            Component component = field.getAnnotation(Component.class);
            List<Component> componentListList = new ArrayList<>();
            if (component != null) {
                componentListList.add(component);
            }
            return componentListList.stream().filter(comInf -> comInf.type().equals(type)).findFirst().isPresent();
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


