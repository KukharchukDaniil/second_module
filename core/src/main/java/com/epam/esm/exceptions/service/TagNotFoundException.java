package com.epam.esm.exceptions.service;

import com.epam.esm.entities.Tag;

/**
 * Service layer exception. Needs to be thrown when no {@link Tag} objects were found during DB query
 */
public class TagNotFoundException extends ServiceException {
    public TagNotFoundException(Object attribute) {
        super(attribute);
    }

    public TagNotFoundException(String message, Object attribute) {
        super(message, attribute);
    }

    public TagNotFoundException(String message, Throwable cause, Object attribute) {
        super(message, cause, attribute);
    }

    public TagNotFoundException(Throwable cause, Object attribute) {
        super(cause, attribute);
    }
}
