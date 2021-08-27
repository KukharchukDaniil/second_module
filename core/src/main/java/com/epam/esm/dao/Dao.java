package com.epam.esm.dao;

import com.epam.esm.exceptions.dao.DaoException;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    long create(T entity);

    List<T> getAll();

    public Optional<T> getById(long id) throws DaoException;

    void deleteById(long id);

    void update(T entity);
}
