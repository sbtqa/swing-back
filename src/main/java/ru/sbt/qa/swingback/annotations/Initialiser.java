package ru.sbt.qa.swingback.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Вешаем на методы, инициализурующие компоненты в банке.
 * @author Varivoda Ivan.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Initialiser {
    
    String title();
}