package com.epam.esm.exceptions.service;

/**
 * Service layer exception. Needs to be thrown when no Certificates were found during DB query
 */
public class CertificateNotFoundException extends ServiceException {
    public CertificateNotFoundException(Object attribute) {
        super(attribute);
    }

    public CertificateNotFoundException(String message, Object attribute) {
        super(message, attribute);
    }

    public CertificateNotFoundException(String message, Throwable cause, Object attribute) {
        super(message, cause, attribute);
    }

    public CertificateNotFoundException(Throwable cause, Object attribute) {
        super(cause, attribute);
    }
}
