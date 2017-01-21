package ru.sbt.qa.swingback.jemmy;

/**
 * @author sbt-varivoda-ia
 * @date 12.01.2017
 */
public class NoSuchElementException extends RuntimeException {
    
    public NoSuchElementException() {
    }
    
    public NoSuchElementException(String message) {
        super(message);
    }
    
    public NoSuchElementException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NoSuchElementException(Throwable cause) {
        super(cause);
    }
}
