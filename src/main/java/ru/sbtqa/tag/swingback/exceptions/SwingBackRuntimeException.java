package ru.sbtqa.tag.swingback.exceptions;

public class SwingBackRuntimeException extends RuntimeException {

    public SwingBackRuntimeException(String message) {
        super(message);
    }

    public SwingBackRuntimeException(Throwable cause) {
        super(cause);
    }

    public SwingBackRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
