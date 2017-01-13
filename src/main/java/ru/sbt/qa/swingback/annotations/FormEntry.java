package ru.sbt.qa.swingback.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аналог PageEntry в BDD. Вешается на классы форм банка.
 * @author Varivoda Ivan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FormEntry {
    String title();
}
