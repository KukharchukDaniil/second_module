package com.epam.esm.validation;

import com.epam.esm.entities.Certificate;
import com.epam.esm.exceptions.validation.ValidationErrorMessage;
import com.epam.esm.exceptions.validation.ValidationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CertificateValidator extends AbstractValidator {

    private static final String ID_ERROR_MESSAGE = "Certificate id is not valid {id = %s}";
    private static final String ID_DETAILS = "ID parameter can't be negative";
    private static final String NAME_ERROR_MESSAGE = "Certificate name is not valid {name = %s}";
    private static final String NAME_DETAILS = "Name parameter can't be empty";
    private static final String PRICE_IS_NOT_VALID = "Certificate price is not valid {price = %s}";
    private static final String PRICE_DETAILS = "Price parameter can't be negative";
    private static final String DESCRIPTION_IS_NOT_VALID = "Certificate description is not valid {description = %s}";
    private static final String DESCRIPTION_DETAILS = "Description parameter can't be empty";
    private static final String DURATION_IS_NOT_VALID = "Certificate duration is not valid {duration = %s}";
    private static final String DURATION_DETAILS = "Duration parameter should be more than 1";

    @Resource
    private final TagValidator tagValidator;

    @Autowired
    public CertificateValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    public Optional<ValidationInfo> validateCertificate(Certificate certificate) {
        List<ValidationErrorMessage> validationErrorMessages = new ArrayList<>();

        validateId(validationErrorMessages, certificate.getId(), ID_ERROR_MESSAGE, ID_DETAILS);

        validateName(validationErrorMessages, certificate.getName(), NAME_ERROR_MESSAGE, NAME_DETAILS);

        validateDescription(certificate, validationErrorMessages);

        validatePrice(certificate, validationErrorMessages);

        validateDuration(certificate, validationErrorMessages);

        processTagListValidation(certificate, validationErrorMessages);

        return validationErrorMessages.isEmpty() ? Optional.empty() : Optional.of(
                new ValidationInfo(
                        validationErrorMessages
                )
        );
    }

    private void validateDuration(Certificate certificate, List<ValidationErrorMessage> validationErrorMessages) {
        Integer duration = certificate.getDuration();
        if (duration != null && duration < 1) {
            addValidationErrorMessage(validationErrorMessages, duration, DURATION_IS_NOT_VALID, DURATION_DETAILS);
        }
    }

    private void validatePrice(Certificate certificate, List<ValidationErrorMessage> validationErrorMessages) {
        Integer price = certificate.getPrice();
        if (price != null && price < 0) {
            addValidationErrorMessage(validationErrorMessages, price, PRICE_IS_NOT_VALID, PRICE_DETAILS);
        }
    }

    private void validateDescription(Certificate certificate, List<ValidationErrorMessage> validationErrorMessages) {
        String description = certificate.getDescription();
        if (description != null && description.isEmpty()) {
            addValidationErrorMessage(validationErrorMessages, description, DESCRIPTION_IS_NOT_VALID, DESCRIPTION_DETAILS);
        }
    }

    private void processTagListValidation(Certificate certificate, List<ValidationErrorMessage> validationErrorMessages) {
        Optional<ValidationInfo> tagValidationErrorOptional = tagValidator.validateTags(certificate.getTagList());
        if (tagValidationErrorOptional.isPresent()) {
            ValidationInfo tagValidationInfo = tagValidationErrorOptional.get();
            validationErrorMessages.addAll(tagValidationInfo.getValidationErrorMessages());
        }
    }
}


