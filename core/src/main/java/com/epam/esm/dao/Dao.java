package com.epam.esm.dao;

import com.epam.esm.exceptions.dao.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * Provides an interface for DAO layer. Contains six methods for CRUD operations
 *
 * @param <T> type of data DAO should work with
 */
public interface Dao<T> {
    /**
     * Creates a new record in DB using entity. Return id field of created row.
     *
     * @param entity entity to add
     * @return id field of recently added row
     */
    long create(T entity);

    /**
     * Returns all T entities contained in database
     *
     * @return T list
     */
    List<T> getAll();

    /**
     * Returns {@link Optional} of {@link T} if any record containing id in "id" was found.
     * If no records were found returns Optional.empty()
     *
     * @param id identifier of {@link T}
     * @return {@link Optional} of {@link T}
     * @throws DaoException if more than one record was found
     */
    public Optional<T> getById(long id) throws DaoException;

    /**
     * Deletes a record from DB by "id" field
     *
     * @param id identifier of the record
     */
    void deleteById(long id);

    /**
     * Updates row in DB. Updates only not-null fields in DB
     *
     * @param entity T entity to update
     */
    void update(T entity);
}
