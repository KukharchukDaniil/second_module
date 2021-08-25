package com.epam.esm.exceptions.dao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
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
