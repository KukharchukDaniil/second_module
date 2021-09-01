package com.epam.esm.validation;

import com.epam.esm.entities.Certificate;
import com.epam.esm.errors.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CertificateValidator extends AbstractValidator {

    private static final String ID_IS_NOT_VALID = "Certificate id is not valid {id = %s}";
    private static final String ID_DETAILS = "ID parameter can't be negative";
    private static final String NAME_IS_NOT_VALID = "Certificate name is not valid {name = %s}";
    private static final String NAME_DETAILS = "Name parameter can't be empty";
    private static final String PRICE_IS_NOT_VALID = "Certificate price is not valid {price = %s}";
    private static final String PRICE_DETAILS = "Price parameter can't be negative";
    private static final String DESCRIPTION_IS_NOT_VALID = "Certificate description is not valid {description = %s}";
    private static final String DESCRIPTION_DETAILS = "Description parameter can't be empty";
    private static final String DURATION_IS_NOT_VALID = "Certificate duration is not valid {duration = %s}";
    private static final String DURATION_DETAILS = "Duration parameter should be more than 1";
    private static final String VALIDATION_ERROR_CODE = "validation-002";
    private static final String ERROR_MESSAGE = "Validation error has occurred";

    private TagsValidator tagsValidator;

    @Autowired
    public CertificateValidator(TagsValidator tagsValidator) {
        this.tagsValidator = tagsValidator;
    }

    public Optional<ValidationError> validateCertificate(Certificate certificate) {
        List<ValidationErrorMessage> validationErrorMessages = new ArrayList<>();
        validateNameAndId(validationErrorMessages, certificate.getId(), ID_IS_NOT_VALID, ID_DETAILS,
                certificate.getName(), NAME_IS_NOT_VALID, NAME_DETAILS);

        String description = certificate.getDescription();
        if (description != null && description.isEmpty()) {
            ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage(
                    String.format(DESCRIPTION_IS_NOT_VALID, description), DESCRIPTION_DETAILS
            );
            validationErrorMessages.add(validationErrorMessage);
        }

        Integer price = certificate.getPrice();
        if (price != null && price < 0) {
            ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage(
                    String.format(PRICE_IS_NOT_VALID, price), PRICE_DETAILS
            );
            validationErrorMessages.add(validationErrorMessage);
        }

        Integer duration = certificate.getDuration();
        if (duration != null && duration < 1) {
            ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage(
                    String.format(DURATION_IS_NOT_VALID, duration), DURATION_DETAILS
            );
            validationErrorMessages.add(validationErrorMessage);
        }

        Optional<ValidationError> tagValidationErrorOptional = tagsValidator.validateTags(certificate.getTagList());
        if (tagValidationErrorOptional.isPresent()) {
            ValidationError tagValidationError = tagValidationErrorOptional.get();
            validationErrorMessages.addAll(tagValidationError.getValidationErrorMessages());
        }
        return validationErrorMessages.isEmpty() ? Optional.empty() : Optional.of(
                new ValidationError(
                        HttpStatus.BAD_REQUEST, VALIDATION_ERROR_CODE,
                        ERROR_MESSAGE, validationErrorMessages
                )
        );
    }


}
