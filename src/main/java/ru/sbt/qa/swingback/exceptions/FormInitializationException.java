package ru.sbt.qa.swingback.exceptions;

public class FormInitializationException extends Exception {
    public FormInitializationException(String s, Exception e) {
        super(s, e);
    }

    public FormInitializationException(String s) {
        super(s);
    }
}
