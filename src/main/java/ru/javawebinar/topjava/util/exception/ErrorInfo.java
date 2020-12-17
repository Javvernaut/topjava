package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String url;
    private final String errorType;
    private final String[] details;

    public ErrorInfo(CharSequence url, String ErrorType, String[] details) {
        this.url = url.toString();
        this.errorType = ErrorType;
        this.details = details;
    }
}