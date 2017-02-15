package ru.sbtqa.tag.swingback.download;

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
