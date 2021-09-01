package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

/**
 * Class for storing exception info
 */
public class ErrorInfo {
    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    public ErrorInfo(HttpStatus status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
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

}
