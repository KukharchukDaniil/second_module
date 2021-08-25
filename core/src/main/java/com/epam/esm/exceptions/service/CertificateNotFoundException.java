package com.epam.esm.exceptions.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CertificateNotFoundException extends ServiceException {
    public CertificateNotFoundException() {
    }

    public CertificateNotFoundException(String message) {
        super(message);
    }

    public CertificateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateNotFoundException(Throwable cause) {
        super(cause);
    }
}
