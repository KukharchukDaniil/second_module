package com.epam.esm.validation;

import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.validation.ValidationErrorMessage;
import com.epam.esm.exceptions.validation.ValidationInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TagValidator extends AbstractValidator {
    private static final String ID_ERROR_MESSAGE = "Tag id is not valid {id = %s}";
    private static final String ID_DETAILS = "ID parameter can't be negative";
    private static final String NAME_ERROR_MESSAGE = "Tag name is not valid {name = %s}";
    private static final String NAME_DETAILS = "Name parameter can't be empty";

    public Optional<ValidationInfo> validateTags(Tag... tags) {
        List<ValidationErrorMessage> validationErrorMessages = new ArrayList<>();
        if (tags != null) {
            processTags(Arrays.asList(tags), validationErrorMessages);
        }
        return validationErrorMessages.isEmpty() ? Optional.empty() :
                Optional.of(new ValidationInfo(validationErrorMessages));
    }

    public Optional<ValidationInfo> validateTags(List<Tag> tagList) {
        List<ValidationErrorMessage> validationErrorMessages = new ArrayList<>();
        if (tagList != null) {
            processTags(tagList, validationErrorMessages);
        }
        return validationErrorMessages.isEmpty() ? Optional.empty() :
                Optional.of(new ValidationInfo(validationErrorMessages));
    }

    private void processTags(List<Tag> tagList, List<ValidationErrorMessage> validationErrorMessages) {
        for (Tag tag : tagList) {
            validateId(validationErrorMessages, tag.getId(), ID_ERROR_MESSAGE, ID_DETAILS);
            validateName(validationErrorMessages, tag.getName(), NAME_ERROR_MESSAGE, NAME_DETAILS);
        }
    }
}
