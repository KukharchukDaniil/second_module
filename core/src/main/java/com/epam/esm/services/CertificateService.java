package com.epam.esm.services;

import com.epam.esm.dao.CertificateJdbcDao;
import com.epam.esm.dao.TagJdbcDao;
import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.enums.CertificateSortingOrder;
import com.epam.esm.exceptions.service.CertificateNotFoundException;
import com.epam.esm.exceptions.validation.ValidationException;
import com.epam.esm.exceptions.validation.ValidationInfo;
import com.epam.esm.validation.CertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service layer class. Provides methods for CRUD operations on {@link Certificate},
 * filtering certificates by name, name part,
 * sorting certificates
 */
@Service
public class CertificateService {

    private static final String NO_CERTIFICATE_FOUND_BY_THIS_ID = "certificate.noId";

    private final CertificateDao certificateDao;

    private final TagDao tagJdbcDao;

    private final CertificateValidator certificateValidator;

    @Autowired
    public CertificateService(CertificateJdbcDao certificateDao, TagJdbcDao tagJdbcDao, CertificateValidator certificateValidator) {
        this.certificateDao = certificateDao;
        this.tagJdbcDao = tagJdbcDao;
        this.certificateValidator = certificateValidator;
    }

    /**
     * Returns {@link Certificate} from data base. If no records were found, throws {@link CertificateNotFoundException}
     *
     * @param id record identifier
     * @return {@link Certificate}
     * @throws CertificateNotFoundException if no certificates with specified id were found
     */
    public Certificate getById(long id) {

        Optional<Certificate> certificateOptional = certificateDao.getById(id);
        if (!certificateOptional.isPresent()) {
            throw new CertificateNotFoundException(NO_CERTIFICATE_FOUND_BY_THIS_ID, id);
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
        if (certificateList != null) {
            order.sort(certificateList);
        }
        return certificateList;
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
        if (certificates != null) {
            sortingOrder.sort(certificates);
        }
        return certificates;
    }

    /**
     * Updates record in data base using data from "certificate".
     * If some new tags passed during update, they will be inserted into "tags" table
     *
     * @param certificate {@link Certificate} object that needs to be updated
     */
    @Transactional
    public void update(Certificate certificate) {

        validateCertificate(certificate);

        long certificateId = certificate.getId();
        if (!certificateDao.getById(certificateId).isPresent()) {
            throw new CertificateNotFoundException(NO_CERTIFICATE_FOUND_BY_THIS_ID, certificateId);
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
            throw new CertificateNotFoundException(NO_CERTIFICATE_FOUND_BY_THIS_ID, certificateId);
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

        validateCertificate(certificate);

        LocalDateTime localDateTime = LocalDateTime.now();

        certificate.setCreateDate(localDateTime);
        certificate.setLastUpdateDate(localDateTime);

        List<Tag> tags = certificate.getTagList();
        long certificateId = certificateDao.create(certificate);
        if (tags != null) {
            processTags(tags, certificateId);
        }
    }

    private void validateCertificate(Certificate certificate) {
        Optional<ValidationInfo> validationInfoOptional = certificateValidator.validateCertificate(certificate);
        if (validationInfoOptional.isPresent()) {
            throw new ValidationException(validationInfoOptional.get());
        }
    }

    private void processTags(List<Tag> tags, long certificateId) {
        deleteRedundantTags(tags, certificateId);
        for (Tag tag : tags) {
            Optional<Tag> tagOptional = tagJdbcDao.getByName(tag.getName());
            Tag checkedTag = getTag(tag.getName(), tagOptional);
            if (!certificateDao.isAttachedToTag(certificateId, checkedTag.getId())) {
                certificateDao.attachCertificateToTag(certificateId, checkedTag.getId());
            }
        }
    }

    private void deleteRedundantTags(List<Tag> tags, long certificateId) {
        List<Long> idList = new ArrayList<>();
        tags.forEach((Tag tag) -> {
            idList.add(tag.getId());
        });
        certificateDao.detachTagsFromCertificateExceptPresented(certificateId, idList.toArray(new Long[0]));
    }

    private Tag getTag(String tagName, Optional<Tag> tagOptional) {

        Tag tag;
        if (tagOptional.isPresent()) {
            tag = tagOptional.get();
        } else {
            tag = new Tag(tagName);
            long tagId = tagJdbcDao.create(tag);
            tag.setId(tagId);
        }
        return tag;
    }


}
