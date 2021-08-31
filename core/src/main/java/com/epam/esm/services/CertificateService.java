package com.epam.esm.services;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.enums.CertificateSortingOrder;
import com.epam.esm.exceptions.service.CertificateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer class. Provides methods for CRUD operations on {@link Certificate},
 * filtering certificates by name, name part,
 * sorting certificates
 */
@Service
public class CertificateService {

    private static final String NO_CERTIFICATE_FOUND_BY_THIS_ID = "No certificate found{id = %s}";

    private final CertificateDao certificateDao;

    private final TagDao tagDao;

    @Autowired
    public CertificateService(CertificateDao certificateDao, TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
    }


    /**
     * Returns {@link Certificate} from data base. If no records were found, throws {@link CertificateNotFoundException}
     *
     * @param id record identifier
     * @return {@link Certificate}
     */
    public Certificate getById(long id) {

        Optional<Certificate> certificateOptional = certificateDao.getById(id);
        if (!certificateOptional.isPresent()) {
            throw new CertificateNotFoundException(String.format(NO_CERTIFICATE_FOUND_BY_THIS_ID, id));
        }
        return certificateOptional.get();

    }


    /**
     * Returns all {@link Certificate} records from data base in some {@link CertificateSortingOrder order}
     *
     * @param order sorting order
     * @return {@link List<Certificate>} sorted according to {@link CertificateSortingOrder order}
     */
    public List<Certificate> getAll(CertificateSortingOrder order) {
        List<Certificate> certificateList = certificateDao.getAll();
        return order.sort(certificateList);
    }


    /**
     * Returns all {@link Certificate} entities attached to {@link Tag} with the field tagName
     *
     * @param tagName name of {@link Tag}
     * @return {@link List<Certificate>}
     */
    public List<Certificate> getByTagName(String tagName) {
        return certificateDao.getByTagName(tagName);
    }

    /**
     * Returns list of certificates which contain a namePart in the "name" field
     *
     * @param namePart name part of {@link Certificate} to return
     * @return {@link List<Certificate>}
     */
    public List<Certificate> getByNamePartSorted(CertificateSortingOrder sortingOrder, String namePart) {
        List<Certificate> certificates = certificateDao.getByNamePart(namePart);
        return sortingOrder.sort(certificates);
    }


    /**
     * Updates record in data base using data from "certificate".
     * If some new tags passed during update, they will be inserted into "tags" table
     *
     * @param certificate {@link Certificate} object that needs to be updated
     */
    @Transactional
    public void update(Certificate certificate) {

        long certificateId = certificate.getId();
        if (!certificateDao.getById(certificateId).isPresent()) {
            throw new CertificateNotFoundException(String.format(NO_CERTIFICATE_FOUND_BY_THIS_ID, certificateId));
        }
        List<Tag> tags = certificate.getTagList();
        if (tags != null) {
            processTags(tags, certificateId);
        }
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificateDao.update(certificate);
    }


    /**
     * Deletes certificate row with corresponding id. If no rows with this id value stored in DB, throws
     * {@link CertificateNotFoundException}
     *
     * @param
     */
    @Transactional
    public void delete(long certificateId) {
        Optional<Certificate> certificateOptional = certificateDao.getById(certificateId);
        if (!certificateOptional.isPresent()) {
            throw new CertificateNotFoundException(String.format(NO_CERTIFICATE_FOUND_BY_THIS_ID, certificateId));
        }
        certificateDao.deleteById(certificateId);
    }

    /**
     * Creates a new record in DB using entity. Return id field of created row.
     *
     * @param certificate {@link Certificate} object that needs to be added to the DB
     */
    @Transactional
    public void create(Certificate certificate) {
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());
        List<Tag> tags = certificate.getTagList();
        long certificateId = certificateDao.create(certificate);
        if (tags != null) {
            processTags(tags, certificateId);
        }
    }

    private void processTags(List<Tag> tags, long certificateId) {
        for (Tag tag : tags) {
            Optional<Tag> tagOptional = tagDao.getByName(tag.getName());
            Tag checkedTag = getTag(tag.getName(), tagOptional);
            if (!certificateDao.isAttachedToTag(certificateId, checkedTag.getId())) {
                certificateDao.attachCertificateToTag(certificateId, checkedTag.getId());
            }
        }
    }

    private Tag getTag(String tagName, Optional<Tag> tagOptional) {

        Tag tag;
        if (tagOptional.isPresent()) {
            tag = tagOptional.get();
        } else {
            tag = new Tag(tagName);
            long tagId = tagDao.create(tag);
            tag.setId(tagId);
        }
        return tag;
    }


}
