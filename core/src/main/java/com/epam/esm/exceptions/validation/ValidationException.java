package com.epam.esm.exceptions.validation;

public class ValidationException extends RuntimeException {
    private final ValidationInfo validationInfo;

    public ValidationException(ValidationInfo validationInfo) {
        this.validationInfo = validationInfo;
    }

    public ValidationException(String message, ValidationInfo validationInfo) {
        super(message);
        this.validationInfo = validationInfo;
    }

    public ValidationException(String message, Throwable cause, ValidationInfo validationInfo) {
        super(message, cause);
        this.validationInfo = validationInfo;
    }

    public ValidationException(Throwable cause, ValidationInfo validationInfo) {
        super(cause);
        this.validationInfo = validationInfo;
    }

    public ValidationInfo getValidationInfo() {
        return validationInfo;
    }

}
