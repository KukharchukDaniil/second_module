package com.epam.esm.exceptions.service;

public class TagAlreadyExistsException extends ServiceException{
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
