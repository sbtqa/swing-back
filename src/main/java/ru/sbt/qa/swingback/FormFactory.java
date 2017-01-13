package ru.sbt.qa.swingback;

import java.lang.reflect.InvocationTargetException;

/**
 * @author sbt-varivoda-ia
 * @date 26.12.2016
 */
public class FormFactory {
    
    private Form curtentForm;
    private String currentFormTitle;
    
    //TODO через проперти
    private final String packageName;
    
    public FormFactory(String packageName) {
        this.packageName = packageName;
    }
    
    // Возвращает класс формы в пакете с именем
    private Class<?> getFormClass(String packageName, String title){ return null;}
    
    //Создание формы по классу
    private Form bootstrapForm(Class<?> form) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        //
        //        if (form != null) {
        //            @SuppressWarnings("unchecked")
        //            Constructor<Form> constructor = ((Constructor<Form>) form.getConstructor());
        //            constructor.setAccessible(true);
        //            current = constructor.newInstance();
        //            currentPageTitle = currentPage.getTitle();
        //            return currentPage;
        //        }
        return null;
    }
    
    // Устанавливает текущую форму
    public Form getForm(String title){ return null;}
    
    // Устанавливает форму по пакету и тайтлу
    public Form getForm(String packageName, String title){ return null;}
    
    // Устанавливает форму по классу
    public Form getForm(Class<? extends Form> formClass){ return null;}
}
