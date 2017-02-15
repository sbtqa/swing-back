package ru.sbtqa.tag.swingback;

import ru.sbtqa.tag.swingback.exceptions.FormInitializationException;
import ru.sbtqa.tag.swingback.util.ReflectionUtil;
import ru.sbtqa.tag.qautils.properties.Props;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Static helper containing test content (current form, title)
 */
public class TestContext {

    private static Form currentForm;
    private static String currentFormTitle;
    private static String formsPackage;

    /**
     * Initialize test context. Reading from property file required data.
     * Must be executed before test start.
     */
    public static void init() {
        formsPackage = Props.getInstance().get("swingback.forms.package");
    }

    public static Form getCurrentForm() {
        return currentForm;
    }


    /**
     * Initialize form with specified title and save its instance to {@link TestContext#currentForm} for further use
     *
     * @param title form title
     * @return page instance
     * @throws FormInitializationException if failed to execute corresponding page constructor
     */
    public static Form setForm(String title) throws FormInitializationException {
        return setForm(formsPackage, title);
    }

    // Возвращает класс формы в пакете с именем
    private static Class<?> getFormClass(String packageName, String title) {
        return ReflectionUtil.getClassByFormEntryTitle(packageName, title);
    }

    // Устанавливает форму по пакету и тайтлу
    public static Form setForm(String packageName, String title) throws FormInitializationException {
        Class<?> formClass = getFormClass(packageName, title);
        if (formClass != null) {
            return bootstrapForm(formClass);
        }
        throw new FormInitializationException("The form with the title '" + title + "' is not founded in the packacge '" + packageName + "'");
    }


    // Устанавливает форму по классу
    public static Form setForm(Class<? extends Form> formClass) throws FormInitializationException {
        return bootstrapForm(formClass);
    }

    /**
     * create and set form by class
     * @throws FormInitializationException
     */
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
