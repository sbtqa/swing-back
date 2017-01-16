package ru.sbt.qa.swingback;

/**
 * @author sbt-varivoda-ia
 * @date 16.01.2017
 */
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

