package com.epam.esm.errors;

import com.epam.esm.validation.ValidationErrorMessage;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ValidationError extends ErrorInfo {
    private List<ValidationErrorMessage> validationErrorMessages;

    public ValidationError(HttpStatus status, String errorCode, String errorMessage,
                           List<ValidationErrorMessage> validationErrorMessages) {
        super(status, errorCode, errorMessage);
        this.validationErrorMessages = validationErrorMessages;
    }

    public List<ValidationErrorMessage> getValidationErrorMessages() {
        return validationErrorMessages;
    }

    public void setValidationErrorMessages(List<ValidationErrorMessage> validationErrorMessages) {
        this.validationErrorMessages = validationErrorMessages;
    }
}
