package ru.sbtqa.tag.swingback.exceptions;

public class ApplicationDownloadException extends RuntimeException {

    public ApplicationDownloadException(String message) {
        super(message);
    }

    public ApplicationDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
