package com.epam.esm.exceptions.dao;

import com.epam.esm.exceptions.LocalizedException;

/**
 * Base exception for DAO layer
 */
public class DaoException extends LocalizedException {
    public DaoException(Object attribute) {
        super(attribute);
    }

    public DaoException(String message, Object attribute) {
        super(message, attribute);
    }

    public DaoException(String message, Throwable cause, Object attribute) {
        super(message, cause, attribute);
    }

    public DaoException(Throwable cause, Object attribute) {
        super(cause, attribute);
    }
}
