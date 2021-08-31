package com.epam.esm.exceptions.dao;

/**
 * DAO layer exceptions. Needs to be thrown when multiple records were found, but single one was expected
 */
public class MultipleRecordsWereFoundException extends DaoException {
    public MultipleRecordsWereFoundException() {
    }

    public MultipleRecordsWereFoundException(String message) {
        super(message);
    }

    public MultipleRecordsWereFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleRecordsWereFoundException(Throwable cause) {
        super(cause);
    }
}
