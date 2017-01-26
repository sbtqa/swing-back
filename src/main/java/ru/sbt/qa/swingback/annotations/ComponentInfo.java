package ru.sbt.qa.swingback.annotations;

import ru.sbt.qa.swingback.Form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Contains swing component information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ComponentInfo {

    /**
     * Component title
     */
    String title();

    /**
     * Component type
     */
    Form.ComponentType type();
}
