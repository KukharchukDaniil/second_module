package com.epam.esm.services;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {

    /**
     * Returns all {@link Certificate} entities from data source
     *
     * @return certificates list
     */
    List<Certificate> getAll();

    /**
     * Returns all {@link Certificate} attached to corresponding {@link Tag} in data source
     *
     * @param tagName
     * @return certificates list
     */
    List<Certificate> getByTagName(String tagName);

    /**
     * Returns list of certificates which contain a namePart in the "name" field
     *
     * @param namePart
     * @return certificates list
     */
    List<Certificate> getByNamePart(String namePart);


    /**
     * Returns Optional of single {@link Certificate} object.
     *
     * @param id identifier of {@link Certificate} to find by
     * @return Optional of Tag if Tag was found. Else returns Optional.empty()
     */
    Optional<Certificate> getById(long id);

    /**
     * Updates {@link Certificate} entity in data source.
     *
     * @param entity {@link Certificate} entity that needs to be updated
     * @throws MultipleRecordsWereFoundException if more than one record was found
     */
    void update(Certificate entity);

    /**
     * Creates {@link Certificate} entity in data source.
     *
     * @param entity {@link Certificate} entity needs to be created
     */
    long create(Certificate entity);

    /**
     * @param certificateId certificate id of corresponding row in DB
     * @param tagId         {@link Tag} id of corresponding row in DB
     * @return true, if there is a record in "certificate_tag" table with corresponding data. Otherwise returns false.
     */

    boolean isAttachedToTag(Long certificateId, Long tagId);

    /**
     * Attaches certificate to tag in a data source
     *
     * @param certificateId identifier of certificate to be inserted
     * @param tagId         identifier of {@link Tag} to be inserted
     */
    void attachCertificateToTag(long certificateId, long tagId);

    /**
     * Deletes a record from DB by "id" field
     *
     * @param id identifier of the record
     */
    void deleteById(long id);
}
