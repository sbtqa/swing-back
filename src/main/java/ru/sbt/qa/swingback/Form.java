package ru.sbt.qa.swingback;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ComponentOperator;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import ru.sbt.qa.swingback.annotations.ActionTitle;
import ru.sbt.qa.swingback.annotations.ActionTitles;
import ru.sbt.qa.swingback.annotations.ComponentInfo;
import ru.sbt.qa.swingback.annotations.Initializer;
import ru.sbt.qa.swingback.jemmy.CommonActions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Base form object class. Contains basic actions with elements, search methods
 */
public abstract class Form {

    private String title;
    protected ContainerOperator currentCont;
    protected ContainerOperator currentTabbedPane;
    private List<Field> formFields;
    private List<Method> formMethods;
//    private Field currentContainer;
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
    public void pushButton(String title) throws NoSuchFieldException {
        ComponentChooser ch = getComponentChooser(title);
        CommonActions.pushButtonIfIsEnabled(new JButtonOperator(getCurrentContainerOperator(), ch));
    }




//    ----------------------------------------------------

    public List<Method> getFormMethods() {
//        if (formMethods == null) {
//            formMethods = Core.getDeclaredMethods(this.getClass());
//        }
        return formMethods;
    }

    public List<Field> getFormFields() {
//        if (formFields == null) {
//            formFields = Core.getDeclaredFields(this.getClass());
//        }
        return formFields;
    }

    public List<Field> getFormTabbedPanes() {
//        if (formTabbedPanes == null) {
//            formTabbedPanes = new LinkedList<>();
//            List<Field> fields = Core.getDeclaredFields(this.getClass());
//            for (Field field : fields) {
//                if (Core.isRequiredField(field, ComponentType.TABBED_PANE)) {
//                    formTabbedPanes.add(field);
//                }
//            }
//        }
        return formTabbedPanes;
    }

    /**
     * Find method with corresponding title on current from, and execute it
     *
     * @param title title of the method to call
     * @param param parameters that will be passed to method
     * @throws java.lang.NoSuchMethodException if required method couldn't be found
     */
    public Object executeMethodByTitle(String title, Object... param) throws NoSuchMethodException {
        List<Method> methods = getFormMethods();
        for (Method method : methods) {
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
        List<Field> fields = getFormFields();
        for (Field field : fields) {
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
        List<Field> fields = getFormFields();
        for (Field field : fields) {
            if (Core.isRequiredField(field, title)) {
                field.setAccessible(true);
                return field.getAnnotation(ComponentInfo.class).type();
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
        List<Field> fields = getFormFields();
        for (Field field : fields) {
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
     * Switch current container to a pane of the tapped pane.
     * Required pane is looked up by title at all tabbed panes this form.
     *
     * @param title title of the required pane
     */
    public void selectTabPane(String title) {
        List<Field> tabbedPanes = getFormTabbedPanes();

        JTabbedPaneOperator tpo;
        for (Field tpf : tabbedPanes) {
            try {
                tpf.setAccessible(true);
                tpo = (JTabbedPaneOperator) FieldUtils.readField(tpf, this);
            } catch (IllegalAccessException e) {
                throw new SwingBackRuntimeException("Failed to get tabbed pane from the current form.", e);
            }
            if (tpo != null && tpo.findPage(title) != -1) {
//                currentContainer = tpf;
                currentTabbedPane = tpo;
                tpo.selectPage(title);
                isCurrentTabbedPane = true;
                return;
            }
        }
    }

    /**
     * Initialization form components by execution the initialize method {@link Initializer}
     *
     * @param title initializer title
     * @throws NoSuchMethodException if initializer is not founded
     */
    public void initializeComponent(String title) throws NoSuchMethodException {
        List<Method> methods = getFormMethods();
        for (Method method : methods) {
            if (Core.isRequiredInitializer(method, title)) {
                try {
                    method.setAccessible(true);
                    MethodUtils.invokeMethod(this, method.getName(), null);
                    return;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new SwingBackRuntimeException("Failed to invoke initializer with title '" + title + "'", e);
                }
            }
        }
        throw new NoSuchMethodException("There is no '" + this.title + "' initializer on '" + this.getTitle() + "' form object");
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
         * Check whether given method has {@link Initializer} annotation with required title
         *
         * @param method method to check
         * @param title  required title
         * @return true|false
         */
        private static Boolean isRequiredInitializer(Method method, String title) {
            Initializer initializer = method.getAnnotation(Initializer.class);
            List<Initializer> initializersList = new ArrayList<>();
            if (initializer != null) {
                initializersList.add(initializer);
            }
            return initializersList.stream().filter(action -> action.title().equals(title)).findFirst().isPresent();
        }

        /**
         * Check whether given field has {@link ComponentInfo} annotation with required title
         *
         * @param field field to check
         * @param title required title
         * @return true|false
         */
        private static boolean isRequiredField(Field field, String title) {
            ComponentInfo componentInfo = field.getAnnotation(ComponentInfo.class);
            List<ComponentInfo> componentInfoListList = new ArrayList<>();
            if (componentInfo != null) {
                componentInfoListList.add(componentInfo);
            }
            return componentInfoListList.stream().filter(comInf -> comInf.title().equals(title)).findFirst().isPresent();
        }

        /**
         * Check whether given field has {@link ComponentInfo} annotation with required type
         *
         * @param field field to check
         * @param type  required type
         * @return true|false
         */
        private static boolean isRequiredField(Field field, ComponentType type) {
            ComponentInfo componentInfo = field.getAnnotation(ComponentInfo.class);
            List<ComponentInfo> componentInfoListList = new ArrayList<>();
            if (componentInfo != null) {
                componentInfoListList.add(componentInfo);
            }
            return componentInfoListList.stream().filter(comInf -> comInf.type().equals(type)).findFirst().isPresent();
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


