package com.epam.esm.exceptions.validation;

public class ValidationErrorMessage {
    private final String errorMessage;
    private final String errorDetails;

    public ValidationErrorMessage(String errorMessage, String errorDetails) {
        this.errorMessage = errorMessage;
        this.errorDetails = errorDetails;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }
}
