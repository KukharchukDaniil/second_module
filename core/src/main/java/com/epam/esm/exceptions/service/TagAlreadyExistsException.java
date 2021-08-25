package com.epam.esm.exceptions.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
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
