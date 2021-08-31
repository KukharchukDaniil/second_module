package com.epam.esm.exceptions.dao;

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
