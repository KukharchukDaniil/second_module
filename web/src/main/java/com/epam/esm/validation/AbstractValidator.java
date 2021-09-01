package com.epam.esm.validation;

import java.util.List;

public class AbstractValidator {
    protected void validateNameAndId(List<ValidationErrorMessage> validationErrorMessages, long id, String idIsNotValid, String idParameterCanTBeNegative, String name, String nameIsNotValid, String nameParameterCanTBeEmpty) {
        if (id < 0) {
            ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage(
                    String.format(idIsNotValid, id), idParameterCanTBeNegative
            );
            validationErrorMessages.add(validationErrorMessage);
        }

        if (name != null && name.isEmpty()) {
            ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage(
                    String.format(nameIsNotValid, name), nameParameterCanTBeEmpty
            );
            validationErrorMessages.add(validationErrorMessage);
        }
    }
}
