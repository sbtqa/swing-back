package ru.sbt.qa.swingback;

import ru.sbt.qa.swingback.jemmy.FormInitializationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author sbt-varivoda-ia
 * @date 26.12.2016
 */
public class TestContext {

    private Form currentForm;
    private String currentFormTitle;
    private String formsPackage;

    public TestContext(String packageName) {
        this.formsPackage = packageName;
    }

    // Возвращает класс формы в пакете с именем
    private Class<?> getFormClass(String packageName, String title){ return null;}

    // Устанавливает форму по пакету и тайтлу
    public Form getForm(String packageName, String title){ return null;}


    // Устанавливает форму по классу
    public Form getForm(Class<? extends Form> formClass) throws FormInitializationException {
        return bootstrapForm(formClass);
    }

    //create and set form by class
    private Form bootstrapForm(Class<?> form) throws FormInitializationException {
        if (form != null) {
            try{
            @SuppressWarnings("unchecked")
            Constructor<Form> constructor = ((Constructor<Form>) form.getConstructor());
            constructor.setAccessible(true);
            currentForm = constructor.newInstance();
            currentFormTitle = currentForm.getTitle();
        }catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new FormInitializationException("Failed to initialize form '" + form + "'", e);
            }
            return currentForm;
        }
        return null;
    }
}
