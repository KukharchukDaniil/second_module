package com.epam.esm.exceptions.service;

import com.epam.esm.entities.Tag;

/**
 * Should be thrown when identical {@link Tag} was found during CREATE operation
 */
public class TagAlreadyExistsException extends ServiceException {
    public TagAlreadyExistsException(Object attribute) {
        super(attribute);
    }

    public TagAlreadyExistsException(String message, Object attribute) {
        super(message, attribute);
    }

    public TagAlreadyExistsException(String message, Throwable cause, Object attribute) {
        super(message, cause, attribute);
    }

    public TagAlreadyExistsException(Throwable cause, Object attribute) {
        super(cause, attribute);
    }
}
