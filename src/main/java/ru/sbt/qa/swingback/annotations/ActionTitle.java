package ru.sbt.qa.swingback.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аналог ActionTitle в BDD. Вешается на методы из форм банка.
 * @author Varivoda Ivan.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionTitle {
    
    String value();
}
