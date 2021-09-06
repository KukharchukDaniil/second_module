package com.epam.esm.exceptions;

public class LocalizedException extends RuntimeException {

    private Object attribute;

    public LocalizedException(Object attribute) {
        this.attribute = attribute;
    }

    public LocalizedException(String message, Object attribute) {
        super(message);
        this.attribute = attribute;
    }

    public LocalizedException(String message, Throwable cause, Object attribute) {
        super(message, cause);
        this.attribute = attribute;
    }

    public LocalizedException(Throwable cause, Object attribute) {
        super(cause);
        this.attribute = attribute;
    }

    public Object getAttribute() {
        return attribute;
    }
}
