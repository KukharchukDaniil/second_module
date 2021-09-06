package com.epam.esm.exceptions.service;

import com.epam.esm.exceptions.LocalizedException;

public class ResponseException extends LocalizedException {
    public ResponseException(Object attribute) {
        super(attribute);
    }

    public ResponseException(String message, Object attribute) {
        super(message, attribute);
    }

    public ResponseException(String message, Throwable cause, Object attribute) {
        super(message, cause, attribute);
    }

    public ResponseException(Throwable cause, Object attribute) {
        super(cause, attribute);
    }
}
