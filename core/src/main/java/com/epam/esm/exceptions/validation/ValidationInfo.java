package com.epam.esm.exceptions.validation;

import java.util.List;

public class ValidationInfo {
    private List<ValidationErrorMessage> validationErrorMessages;

    public ValidationInfo(List<ValidationErrorMessage> validationErrorMessages) {
        this.validationErrorMessages = validationErrorMessages;
    }

    public List<ValidationErrorMessage> getValidationErrorMessages() {
        return validationErrorMessages;
    }

    public void setValidationErrorMessages(List<ValidationErrorMessage> validationErrorMessages) {
        this.validationErrorMessages = validationErrorMessages;
    }
}
