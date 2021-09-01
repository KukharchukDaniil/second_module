package com.epam.esm.services;

import com.epam.esm.entities.Tag;

import java.util.List;
import java.util.Optional;


public interface TagDao {
    /**
     * Creates a new record in DB using entity. Return id field of created row.
     *
     * @param entity entity to add
     * @return id field of recently added row
     */
    long create(Tag entity);

    /**
     * Returns all T entities contained in database
     *
     * @return T list
     */
    List<Tag> getAll();

    /**
     * Returns {@link Optional} of {@link Tag} if any record containing id in "id" was found.
     * If no records were found returns Optional.empty()
     *
     * @param id identifier of {@link Tag}
     * @return {@link Optional} of {@link Tag}
     */
    Optional<Tag> getById(long id);

    /**
     * Deletes a record from DB by "id" field
     *
     * @param id identifier of the record
     */
    void deleteById(long id);

    /**
     * Returns {@link Optional} of {@link Tag} if any record containing name in "name" field was found.
     * If no records were found returns Optional.empty()
     *
     * @param name name field of {@link Tag}
     * @return {@link Optional} of {@link Tag}
     */
    Optional<Tag> getByName(String name);
}
