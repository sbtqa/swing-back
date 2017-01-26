package ru.sbt.qa.swingback.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ActionTitles.class)
public @interface ActionTitle {

    /**
     * Title text
     */
    String value();
}
