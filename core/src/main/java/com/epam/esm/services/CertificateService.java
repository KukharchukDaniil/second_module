package com.epam.esm.services;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.enums.SortingOrder;
import com.epam.esm.exceptions.dao.DaoException;
import com.epam.esm.exceptions.service.CertificateNotFoundException;
import com.epam.esm.exceptions.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    private static final String NO_CERTIFICATE_FOUND_BY_THIS_ID = "No certificate found by this id: ";

    private final CertificateDao certificateDao;

    private final TagDao tagDao;

    @Autowired
    public CertificateService(CertificateDao certificateDao, TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
    }

    public Certificate getById(long id) throws ServiceException {
        try {
            Optional<Certificate> certificateOptional = certificateDao.getById(id);
            if (!certificateOptional.isPresent()) {
                throw new CertificateNotFoundException(NO_CERTIFICATE_FOUND_BY_THIS_ID + id);
            }
            return certificateOptional.get();
        } catch (DaoException exception) {
            throw new ServiceException(exception.getMessage(), exception);
        }
    }

    public List<Certificate> getAll(SortingOrder order) throws ServiceException {
        List<Certificate> certificateList = certificateDao.getAll();
        switch (order) {
            case ASC:
                certificateList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
                break;
            case DESC:
                certificateList.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
                break;
            case NONE:
                break;
            default:
                throw new ServiceException("Invalid sorting order");
        }
        return certificateList;
    }


    public List<Certificate> getByTagName(String tagName) {
        return certificateDao.getByTagName(tagName);
    }

    public List<Certificate> getByNamePart(String namePart) {
        return certificateDao.getByNamePart(namePart);
    }

    public void update(Certificate certificate, String[] tagNames) throws ServiceException {
        try {
            long certificateId = certificate.getId();
            if (!certificateDao.getById(certificateId).isPresent()) {
                throw new CertificateNotFoundException(NO_CERTIFICATE_FOUND_BY_THIS_ID + certificateId);
            }
            processTags(tagNames, certificateId);
            certificateDao.update(certificate);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void delete(long certificateId) throws ServiceException {
        try {
            Optional<Certificate> certificateOptional = certificateDao.getById(certificateId);
            if (!certificateOptional.isPresent()) {
                throw new CertificateNotFoundException(NO_CERTIFICATE_FOUND_BY_THIS_ID + certificateId);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void create(Certificate certificate, String[] tagNames) throws ServiceException {
        long certificateId = certificateDao.create(certificate);
        try {
            processTags(tagNames, certificateId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void processTags(String[] tagNames, long certificateId) throws DaoException {
        for (String tagName : tagNames) {
            Optional<Tag> tagOptional = tagDao.getByName(tagName);
            Tag tag = getTag(tagName, tagOptional);
            if (!certificateDao.isAttachedToTag(certificateId, tag.getId())) {
                certificateDao.attachCertificateToTag(certificateId, tag.getId());
            }
        }
    }

    private Tag getTag(String tagName, Optional<Tag> tagOptional) throws DaoException {

        Tag tag;
        if (tagOptional.isPresent()) {
            tag = tagOptional.get();
        } else {
            tagDao.create(new Tag(tagName));
            tag = tagDao.getByName(tagName).get();
        }
        return tag;
    }
}
