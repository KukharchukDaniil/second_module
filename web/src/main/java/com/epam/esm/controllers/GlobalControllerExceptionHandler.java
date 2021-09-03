package com.epam.esm.controllers;

import com.epam.esm.errors.ErrorInfo;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;
import com.epam.esm.exceptions.service.CertificateNotFoundException;
import com.epam.esm.exceptions.service.TagAlreadyExistsException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import com.epam.esm.exceptions.validation.ValidationException;
import com.epam.esm.exceptions.validation.ValidationInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final String CERTIFICATE_NOT_FOUND_ERROR_CODE = "40402";
    private static final String TAG_NOT_FOUND_ERROR_CODE = "40401";
    private static final String MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE = "50001";

    @ExceptionHandler(MultipleRecordsWereFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    private ErrorInfo handleMultipleRecordsWereFoundException(MultipleRecordsWereFoundException exception) {
        return new ErrorInfo(
                HttpStatus.INTERNAL_SERVER_ERROR,
                MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
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
    private ErrorInfo handleCertificateNotFoundException(CertificateNotFoundException exception) {
        return new ErrorInfo(
                HttpStatus.NOT_FOUND,
                CERTIFICATE_NOT_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorInfo handleTagNotFoundException(TagNotFoundException exception) {
        return new ErrorInfo(
                HttpStatus.NOT_FOUND,
                TAG_NOT_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
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
    private ErrorInfo handleTagAlreadyExistsException(TagAlreadyExistsException exception) {
        return new ErrorInfo(
                HttpStatus.CONFLICT,
                MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ValidationInfo handleValidationException(ValidationException exception) {
        return exception.getValidationInfo();
    }
}

