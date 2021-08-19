package com.epam.esm.dao;

import java.util.List;

public interface Dao<T> {
    void create(T entity);

    List<T> getAll();

    void deleteById(long id);

    void update(T entity);
}
