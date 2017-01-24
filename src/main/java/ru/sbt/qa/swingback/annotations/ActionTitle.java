package ru.sbt.qa.swingback.annotations;

import java.lang.annotation.*;

/**
 * Аналог ActionTitle в BDD. Вешается на методы из форм банка.
 * @author Varivoda Ivan.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ActionTitles.class)
public @interface ActionTitle {
    
    String value();
}
