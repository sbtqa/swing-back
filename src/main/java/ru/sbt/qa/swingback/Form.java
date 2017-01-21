package ru.sbt.qa.swingback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author sbt-varivoda-ia
 * @date 26.12.2016
 */
public abstract class Form {

    private Class<?> currentClass;

    // Form fields and methods
    private List<Field> formFields;

    private List<Method> formMethods;
    // Containers
    private Field currentContainer;
    private Field currentTabbedPane;

    private List<Field> formTabbedPanes;

    // If current container is tabbedPane
    private boolean isCurrentTabbedPane;

    /**
     * enum for components types
     * @author sbt-varivoda-ia
     * @date 26.12.2016
     */
    public enum ComponentType {
        TEXT_FIELD,
        TEXT_AREA,
        BUTTON,
        CONTAINER,
        COMBO_BOX,
        TABBED_PANE,
        TABLE,
        CHECK_BOX,
        TREE,
        LABEL,
        RADIO_BUTTON,
        OTHER
    }
}


