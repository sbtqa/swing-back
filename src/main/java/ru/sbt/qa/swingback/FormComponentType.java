package ru.sbt.qa.swingback;

/**
 * Перечисление предназначено для определения типа компонента.
 * Используется для определения образа взаимодействия с компонентом. Например, его заполнение, проверка на доступность.
 * @author sbt-varivoda-ia
 * @date 26.12.2016
 */
public enum FormComponentType {
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
