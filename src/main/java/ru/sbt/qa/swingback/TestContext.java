package ru.sbt.qa.swingback;

import ru.sbt.qa.swingback.jemmy.FormInitializationException;
import ru.sbt.qa.swingback.util.ReflectionUtil;
import ru.sbtqa.tag.qautils.properties.Props;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class TestContext {

    private static Form currentForm;
    private static String currentFormTitle;
    private static String formsPackage;

    public static void init() {
        formsPackage = Props.getInstance().get("swingback.forms.package");
    }

    public static Form getCurrentForm() {
//         TODO: 24.01.2017
        return currentForm;
    }


    public static Form getForm(String title) throws FormInitializationException {

        return getForm(formsPackage, title);
    }

    // Возвращает класс формы в пакете с именем
    private static Class<?> getFormClass(String packageName, String title) {
        return ReflectionUtil.getClassByFormEntryTitle(packageName, title);
    }

    // Устанавливает форму по пакету и тайтлу
    public static Form getForm(String packageName, String title) throws FormInitializationException {
        return bootstrapForm(getFormClass(packageName, title));
    }


    // Устанавливает форму по классу
    public static Form getForm(Class<? extends Form> formClass) throws FormInitializationException {
        return bootstrapForm(formClass);
    }

    //create and set form by class
    private static Form bootstrapForm(Class<?> form) throws FormInitializationException {
        if (form != null) {
            try {
                @SuppressWarnings("unchecked")
                Constructor<Form> constructor = ((Constructor<Form>) form.getConstructor());
                constructor.setAccessible(true);
                currentForm = constructor.newInstance();
                currentFormTitle = currentForm.getTitle();
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new FormInitializationException("Failed to initialize form '" + form + "'", e);
            }
            return currentForm;
        }
        return null;
    }
}
