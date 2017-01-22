package ru.sbt.qa.swingback.download;

/**
 * Created by Varivoda Ivan on 21.01.2017.
 */
public class ApplicationDownloadException extends RuntimeException {

    public ApplicationDownloadException() {
    }

    public ApplicationDownloadException(String message) {
        super(message);
    }

    public ApplicationDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
