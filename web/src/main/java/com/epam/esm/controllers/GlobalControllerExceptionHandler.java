package com.epam.esm.controllers;

import com.epam.esm.errors.ErrorInfo;
import com.epam.esm.exceptions.ResponseException;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;
import com.epam.esm.exceptions.service.CertificateNotFoundException;
import com.epam.esm.exceptions.service.TagAlreadyExistsException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import com.epam.esm.exceptions.validation.ValidationErrorMessage;
import com.epam.esm.exceptions.validation.ValidationException;
import com.epam.esm.exceptions.validation.ValidationInfo;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final String CERTIFICATE_NOT_FOUND_ERROR_CODE = "40402";
    private static final String TAG_NOT_FOUND_ERROR_CODE = "40401";
    private static final String MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE = "50001";
    public static final String ERROR_CODE = "validation-500";
    public static final String ERROR_HAS_OCCURRED = "validation.errorOccurred";

    @Resource
    private MessageSource messageSource;

    @ExceptionHandler(MultipleRecordsWereFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    private ErrorInfo handleMultipleRecordsWereFoundException(MultipleRecordsWereFoundException exception, Locale locale) {
        return new ErrorInfo(
                HttpStatus.INTERNAL_SERVER_ERROR,
                MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE,
                messageSource.getMessage(exception.getMessage(), new Object[]{exception.getAttribute()}, locale)
        );
    }

    /**
     * Handles exception and returns ErrorInfo object
     *
     * @param exception {@link CertificateNotFoundException exception} to handle
     * @return ErrorInfo object containing exception info with errorCode = 40402. Response status: 404 Not Found.
     */
    @ExceptionHandler(CertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    private ErrorInfo handleCertificateNotFoundException(CertificateNotFoundException exception, Locale locale) {
        return new ErrorInfo(
                HttpStatus.NOT_FOUND,
                CERTIFICATE_NOT_FOUND_ERROR_CODE,
                messageSource.getMessage(exception.getMessage(), new Object[]{exception.getAttribute()}, locale)
        );
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorInfo handleTagNotFoundException(TagNotFoundException exception, Locale locale) {
        return new ErrorInfo(
                HttpStatus.NOT_FOUND,
                TAG_NOT_FOUND_ERROR_CODE,
                messageSource.getMessage(exception.getMessage(), new Object[]{exception.getAttribute()}, locale)
        );
    }

    /**
     * Handles exception and returns ErrorInfo object
     *
     * @param exception {@link TagAlreadyExistsException exception} to handle
     * @return ErrorInfo object containing exception info with errorCode = 50001. Response status: 500 Internal Server Error.
     */
    @ExceptionHandler(TagAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorInfo handleTagAlreadyExistsException(TagAlreadyExistsException exception, Locale locale) {
        return new ErrorInfo(
                HttpStatus.CONFLICT,
                MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE,
                messageSource.getMessage(exception.getMessage(), new Object[]{exception.getAttribute()}, locale)
        );
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorInfo handleValidationException(ValidationException exception, Locale locale) {
        ValidationInfo validationInfo = exception.getValidationInfo();
        List<ValidationErrorMessage> validationErrorMessages = validationInfo.getValidationErrorMessages();

        List<ValidationErrorMessage> localizedMessages = new ArrayList<>();
        for (ValidationErrorMessage message : validationErrorMessages) {
            localizedMessages.add(localizeErrorMessage(message, locale));
        }
        return new ErrorInfo(
                HttpStatus.BAD_REQUEST,
                ERROR_CODE,
                messageSource.getMessage(ERROR_HAS_OCCURRED, null, locale),
                localizedMessages
        );
    }

    @ExceptionHandler(ResponseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity handleResponseException(ResponseException exception, Locale locale) {
        Object attribute = exception.getAttribute();

        if (attribute.getClass() == ErrorInfo.class) {
            return ResponseEntity.badRequest().body(localizeErrorInfo((ErrorInfo) attribute, locale));
        } else {
            return ResponseEntity.badRequest().body(localizeErrorMessage(((ValidationErrorMessage) attribute), locale));
        }
    }

    private ValidationErrorMessage localizeErrorMessage(ValidationErrorMessage message, Locale locale) {
        return new ValidationErrorMessage(
                messageSource.getMessage(message.getErrorMessage(), new Object[]{message.getErrorAttribute()}, locale),
                messageSource.getMessage(message.getErrorDetails(), null, locale)
        );
    }

    private ErrorInfo localizeErrorInfo(ErrorInfo info, Locale locale) {
        return new ErrorInfo(
                info.getStatus(),
                info.getErrorCode(),
                messageSource.getMessage(info.getErrorMessage(), null, locale)
        );
    }
}

