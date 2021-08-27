package com.epam.esm.exceptions.dao;

public class DaoException extends Exception{
    public DaoException() {
    } //TODO: ExceptionHandler

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
