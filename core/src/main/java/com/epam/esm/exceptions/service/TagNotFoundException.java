package com.epam.esm.exceptions.service;

import com.epam.esm.entities.Tag;

/**
 * Service layer exception. Needs to be thrown when no {@link Tag} objects were found during DB query
 */
public class TagNotFoundException extends ServiceException {
    public TagNotFoundException() {
    }

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotFoundException(Throwable cause) {
        super(cause);
    }
}
