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

    protected void validateId(List<ValidationErrorMessage> validationErrorMessages, long id, String idErrorMessage, String idDetails) {
        if (id < 0) {
            addValidationErrorMessage(validationErrorMessages,
                    id, idErrorMessage, idDetails);
        }
    }

    protected void addValidationErrorMessage(List<ValidationErrorMessage> validationErrorMessages,
                                             Object value, String errorMessage, String errorDetails) {
        ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage(
                String.format(errorMessage, value), errorDetails
        );
        validationErrorMessages.add(validationErrorMessage);
    }
}