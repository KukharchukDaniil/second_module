package com.epam.esm.exceptions.service;

/**
 * Service layer exception. Needs to be thrown when no Certificates were found during DB query
 */
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
