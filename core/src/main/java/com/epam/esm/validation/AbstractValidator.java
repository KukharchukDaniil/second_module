package com.epam.esm.validation;

import com.epam.esm.exceptions.validation.ValidationErrorMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public abstract class AbstractValidator {

    protected void validateName(List<ValidationErrorMessage> validationErrorMessages, String name, String nameErrorMessage, String nameDetails) {
        if (StringUtils.isEmpty(name)) {
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
                errorMessage, value, errorDetails
        );
        validationErrorMessages.add(validationErrorMessage);
    }
}
