package ru.sbt.qa.swingback;

public class SwingBackRuntimeException extends RuntimeException {

    public SwingBackRuntimeException() {
    }

    public SwingBackRuntimeException(String message) {
        super(message);
    }

    public SwingBackRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
