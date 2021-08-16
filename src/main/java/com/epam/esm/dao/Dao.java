package com.epam.esm.dao;

import com.epam.esm.entities.Entity;

import java.util.List;

public interface Dao<T> {
    void create(T entity);

    List<T> getAll();

    void deleteById(Long id);

    void update(T entity);

    void save(T entity);
}
