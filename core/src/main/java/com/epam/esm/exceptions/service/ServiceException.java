package com.epam.esm.exceptions.service;

import com.epam.esm.exceptions.LocalizedException;

/**
 * Base exception for Service layer
 */
public class ServiceException extends LocalizedException {
    public ServiceException(Object attribute) {
        super(attribute);
    }

    public ServiceException(String message, Object attribute) {
        super(message, attribute);
    }

    public ServiceException(String message, Throwable cause, Object attribute) {
        super(message, cause, attribute);
    }

    public ServiceException(Throwable cause, Object attribute) {
        super(cause, attribute);
    }

}
