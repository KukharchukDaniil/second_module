package com.epam.esm.services;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.exceptions.service.TagAlreadyExistsException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    public static final String NO_TAG_WITH_ID = "No tag with id: ";
    public static final String TAG_WITH_THIS_NAME_ALREADY_EXISTS = "Tag with this name already exists: ";
    private static final String NO_TAG_WITH_NAME = "No tag with name: ";
    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public List<Tag> getAll() {
        return tagDao.getAll();
    }

    public void deleteById(long id) throws ServiceException {
        try {
            Optional<Tag> tagOptional = tagDao.getById(id);
            if (!tagOptional.isPresent()) {
                throw new TagNotFoundException(NO_TAG_WITH_ID + id);
            }
            tagDao.deleteById(id);
        } catch (MultipleRecordsWereFoundException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public Optional<Tag> getById(long id) throws ServiceException {
        try {
            return tagDao.getById(id);
        } catch (MultipleRecordsWereFoundException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void update(Tag tag) throws ServiceException {
        try {
            if (!tagDao.getById(tag.getId()).isPresent()) {
                throw new TagNotFoundException(NO_TAG_WITH_ID + tag.getId());
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        tagDao.update(tag);
    }

    public void create(Tag tag) throws ServiceException {
        try {
            if (tagDao.getByName(tag.getName()).isPresent()) {
                throw new TagAlreadyExistsException(TAG_WITH_THIS_NAME_ALREADY_EXISTS + tag.getName());
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        tagDao.create(tag);
    }

    public Optional<Tag> getByName(String name) throws ServiceException {
        try {
            Optional<Tag> tagOptional = tagDao.getByName(name);
            if (!tagOptional.isPresent()) {
                throw new TagNotFoundException(NO_TAG_WITH_NAME + name);
            }
            return tagOptional;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
