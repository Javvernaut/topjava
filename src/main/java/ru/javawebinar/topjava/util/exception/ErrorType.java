package ru.javawebinar.topjava.util.exception;

public enum ErrorType {
    APP_ERROR("error.application"),
    DATA_NOT_FOUND("error.notFound"),
    DATA_ERROR("error.validation"),
    VALIDATION_ERROR("error.validation");

    private final String typeInfo;

    ErrorType(String typeInfo) {
        this.typeInfo = typeInfo;
    }

    public String getTypeInfo() {
        return typeInfo;
    }
}
