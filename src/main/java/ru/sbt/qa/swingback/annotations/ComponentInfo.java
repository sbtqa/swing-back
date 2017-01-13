package ru.sbt.qa.swingback.annotations;

import ru.sbt.qa.swingback.FormComponentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Аннотация предназначена для хранения информации о компоненте формы.
 * Например, тип поля и его имя.
 * @author Varivoda Ivan.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ComponentInfo {
    
    String title();
    
    FormComponentType type();
}
