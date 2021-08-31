package com.epam.esm.exceptions.service;

import com.epam.esm.entities.Tag;

/**
 * Should be thrown when identical {@link Tag} was found during CREATE operation
 */
public class TagAlreadyExistsException extends ServiceException {
    public TagAlreadyExistsException() {
    }

    public TagAlreadyExistsException(String message) {
        super(message);
    }

    public TagAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
