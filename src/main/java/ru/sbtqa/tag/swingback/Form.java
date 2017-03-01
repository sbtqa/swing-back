package ru.sbtqa.tag.swingback;

import org.netbeans.jemmy.operators.ComponentOperator;
import ru.sbtqa.tag.swingback.annotations.ActionTitle;
import ru.sbtqa.tag.swingback.annotations.ActionTitles;
import ru.sbtqa.tag.swingback.annotations.Component;
import ru.sbtqa.tag.swingback.annotations.FormEntry;
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

    private String title;
    private List<Field> formFields;
    private List<Method> formMethods;

    public String getTitle() {
        return title;
    }


    public Form() {
        title = this.getClass().getAnnotation(FormEntry.class).title();
        formMethods = Core.getDeclaredMethods(this.getClass());
        formFields = Core.getDeclaredFields(this.getClass());
    }


    @ActionTitle("нажимает на кнопку")
    @ActionTitle("push button")
    public void pushButton(String title) throws NoSuchFieldException {
        ((Button) getComponentOperator(title)).push();
    }

    @ActionTitle("проверяет, что таблица пуста")
    @ActionTitle("check table is empty")
    public void tableIsEmpty(String title) throws NoSuchFieldException {
        assertThat("The table with title '" + title + "' is not empty.",
                ((Table) getComponentOperator(title)).getRowCount(), is(0));
    }

    @ActionTitle("нажимает на заголовок столбца")
    @ActionTitle("click on table column title")
    public void clickOnTableColumnTitle(String title, String columnTitle) throws NoSuchFieldException {
        ((Table) getComponentOperator(title)).clickOnTableColumnTitle(columnTitle);
    }

    @ActionTitle("выделяет первую запись таблицы")
    @ActionTitle("select first table row")
    public void selectFistTableElem(String title) throws NoSuchFieldException {
        ((Table) getComponentOperator(title)).selectFistTableElem();
    }

    @ActionTitle("разворачивает дерево")
    @ActionTitle("expand tree")
    public void expandTree(String title, String path) throws NoSuchFieldException {
        String[] paths = path.split("->");
        ((Tree) getComponentOperator(title)).chooseTreeNode(paths);
    }

    @ActionTitle("заполняет поле")
    @ActionTitle("fill field")
    public void fillField(String title, String value) throws NoSuchFieldException {
        ((TextField) getComponentOperator(title)).setText(value);
    }


    @ActionTitle("устанавливает чекбокс")
    @ActionTitle("set checkbox")
    public void setCheckBox(String title, String value) throws NoSuchFieldException {
        ((CheckBox) getComponentOperator(title)).setCheckBox(Boolean.valueOf(value));
    }

    @ActionTitle("проверяет, что чекбокс выставлен")
    @ActionTitle("check that checkbox is selected")
    public void checkSelectedCheckBox(String title) throws NoSuchFieldException {
        assertThat("The checkbox with title '" + title + "' is not selected.",
                ((CheckBox) getComponentOperator(title)).isSelected(), is(true));
    }

    @ActionTitle("проверяет, что чекбокс невыставлен")
    @ActionTitle("check that checkbox is not selected")
    public void checkUnSelectedCheckBox(String title) throws NoSuchFieldException {
        assertThat("The checkbox with title '" + title + "' is selected.",
                ((CheckBox) getComponentOperator(title)).isSelected(), is(false));
    }

    @ActionTitle("выбирает элемент из выпадающего списка")
    @ActionTitle("choose combobox item")
    public void chooseComboBoxItem(String title, String value) throws NoSuchFieldException {
        ((ComboBox) getComponentOperator(title)).chooseComboBoxItem(value, String::equals);
    }

//    @ActionTitle("проверяет наличие элемента на форме")
//    @ActionTitle("check component presence")
//    public void checkComponentPresence(String title, String value) throws NoSuchFieldException {
//        assertThat("The component with title '" + title + "' is " +(Boolean.valueOf(value) ? "not " : "") +"presence on form ' " + getTitle() + "'.",
//                CommonActions.isComponentPresence(getCurrentContainerOperator(), getComponentType(title), getComponentChooser(title)), is(Boolean.valueOf(value)));
//    }

//    @ActionTitle("проверяет редактируемость элемента")
//    @ActionTitle("check component editable")
//    public void checkComponentEditable(String title, String value) throws NoSuchFieldException {
//        assertThat("The component with title '" + title + "' is " +(Boolean.valueOf(value) ? "not " : "") + "editable.",
//                CommonActions.isComponentEditable(getCurrentContainerOperator(), getComponentType(title), getComponentChooser(title)), is(Boolean.valueOf(value)));
//    }

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
                    return method.invoke(this, param);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new SwingBackRuntimeException("Failed to invoke method", e);
                }
            }
        }
        throw new NoSuchMethodException("There is no '" + title + "' method on '" + this.getTitle() + "' form object");
    }


    /**
     * Find component operator {@link ComponentOperator} element which has specified type and title
     *
     * @param title component title
     * @return ComponentOperator with specified type and title
     * @throws NoSuchFieldException if required field couldn't be found
     */
    public ComponentOperator getComponentOperator(String title) throws NoSuchFieldException {
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
        throw new NoSuchFieldException("There is no '" + title + "' field on '" + this.getTitle() + "' form object");
    }

    /**
     * Helper methods for manipulations on fields and objects
     */
    private static class Core {

        private Core() {
        }

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