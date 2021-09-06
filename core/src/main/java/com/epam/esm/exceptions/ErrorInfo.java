package com.epam.esm.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Class for storing exception info
 */
public class ErrorInfo {
    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;
    private final List<?> errorObjects;

    public ErrorInfo(HttpStatus status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorObjects = null;
    }

    public ErrorInfo(HttpStatus status, String errorCode, String errorMessage, List<?> errorObjects) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorObjects = errorObjects;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<?> getErrorObjects() {
        return errorObjects;
    }
}
