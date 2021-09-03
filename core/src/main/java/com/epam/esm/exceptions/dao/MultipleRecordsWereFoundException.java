package com.epam.esm.exceptions.dao;

/**
 * DAO layer exceptions. Needs to be thrown when multiple records were found, but single one was expected
 */
public class MultipleRecordsWereFoundException extends DaoException {
    public MultipleRecordsWereFoundException(Object attribute) {
        super(attribute);
    }

    public MultipleRecordsWereFoundException(String message, Object attribute) {
        super(message, attribute);
    }

    public MultipleRecordsWereFoundException(String message, Throwable cause, Object attribute) {
        super(message, cause, attribute);
    }

    public MultipleRecordsWereFoundException(Throwable cause, Object attribute) {
        super(cause, attribute);
    }
}
