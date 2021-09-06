package com.epam.esm.validation;

import com.epam.esm.exceptions.validation.ValidationErrorMessage;

import java.util.List;

public abstract class AbstractValidator {

    protected void validateName(List<ValidationErrorMessage> validationErrorMessages, String name, String nameErrorMessage, String nameDetails) {
        if (name != null && name.isEmpty()) {
            addValidationErrorMessage(validationErrorMessages,
                    name, nameErrorMessage, nameDetails);
        }
    }

    protected void validateId(List<ValidationErrorMessage> validationErrorMessages, Long id, String idErrorMessage, String idDetails) {
        if (id != null && id < 1) {
            addValidationErrorMessage(validationErrorMessages,
                    id, idErrorMessage, idDetails);
        }
    }

    protected void addValidationErrorMessage(List<ValidationErrorMessage> validationErrorMessages,
                                             Object value, String errorMessage, String errorDetails) {
        ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage(
                errorMessage, value, errorDetails
        );
        validationErrorMessages.add(validationErrorMessage);
    }
}
