package com.epam.esm.exceptions.validation;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ValidationErrorMessage {
    private final String errorMessage;
    private Object errorAttribute;
    private final String errorDetails;

    public ValidationErrorMessage(String errorMessage, Object errorAttribute, String errorDetails) {
        this.errorMessage = errorMessage;
        this.errorAttribute = errorAttribute;
        this.errorDetails = errorDetails;
    }

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

    @JsonIgnore
    public Object getErrorAttribute() {
        return errorAttribute;
    }
}
