package ru.sbtqa.tag.swingback.exceptions;

public class AppManagerException extends RuntimeException {
    
    public AppManagerException() {
        super();
    }
    
    public AppManagerException(String message) {
        super(message);
    }
    
    public AppManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}

