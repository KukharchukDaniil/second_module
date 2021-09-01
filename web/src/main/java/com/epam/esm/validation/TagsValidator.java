package com.epam.esm.validation;

import com.epam.esm.entities.Tag;
import com.epam.esm.errors.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TagsValidator extends AbstractValidator {
    private static final String ID_IS_NOT_VALID = "Tag id is not valid {id = %s}";
    private static final String ID_PARAMETER_CAN_T_BE_NEGATIVE = "ID parameter can't be negative";
    private static final String NAME_IS_NOT_VALID = "Tag name is not valid {name = %s}";
    private static final String VALIDATION_ERROR_CODE = "validation-001";
    private static final String ERROR_MESSAGE = "Validation error has occurred";
    private static final String NAME_PARAMETER_CAN_T_BE_EMPTY = "Name parameter can't be empty";

    public Optional<ValidationError> validateTags(List<Tag> tagList) {
        List<ValidationErrorMessage> validationErrorMessages = new ArrayList<>();
        if (tagList != null) {
            for (Tag tag : tagList) {
                validateNameAndId(validationErrorMessages, tag.getId(), ID_IS_NOT_VALID,
                        ID_PARAMETER_CAN_T_BE_NEGATIVE, tag.getName(), NAME_IS_NOT_VALID, NAME_PARAMETER_CAN_T_BE_EMPTY);
            }
        }
        return validationErrorMessages.isEmpty() ? Optional.empty() :
                Optional.of(new ValidationError(HttpStatus.BAD_REQUEST,
                        VALIDATION_ERROR_CODE, ERROR_MESSAGE, validationErrorMessages));
    }
}
