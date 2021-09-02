package com.epam.esm.validation;

import java.util.List;

public class AbstractValidator {
    protected void validateNameAndId(List<ValidationErrorMessage> validationErrorMessages, long id, String idErrorMessage,
                                     String idDetails, String name, String nameErrorMessage,
                                     String nameDetails) {
        if (id < 0) {
            addValidationErrorMessage(validationErrorMessages,
                    id, idErrorMessage, idDetails);
        }

        if (name != null && name.isEmpty()) {
            addValidationErrorMessage(validationErrorMessages,
                    name, nameErrorMessage, nameDetails);
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
