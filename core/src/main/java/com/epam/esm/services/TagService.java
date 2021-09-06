package com.epam.esm.services;

import com.epam.esm.dao.TagJdbcDao;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.service.ResponseException;
import com.epam.esm.exceptions.service.TagAlreadyExistsException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import com.epam.esm.exceptions.validation.ValidationErrorMessage;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer class. Provides methods for CRUD operations on {@link Tag} using {@link TagDao}
 */
@Service
public class TagService {

    private static final String NO_TAG_WITH_ID = "tag.noId";
    private static final String TAG_WITH_THIS_NAME_ALREADY_EXISTS = "tag.nameBusy";
    private static final String NO_TAG_WITH_NAME = "tag.noName";
    private static final String ERROR_MESSAGE = "response.idErrorMessage";
    private static final String ERROR_DETAILS = "response.idErrorDetails";
    private final TagDao tagDao;

    @Autowired
    public TagService(TagJdbcDao tagJdbcDao) {
        this.tagDao = tagJdbcDao;
    }

    /**
     * @return {@link List<Tag>} which contains all Tags in DB
     */
    public List<Tag> getAll() {
        return tagDao.getAll();
    }

    /**
     * Deletes row with corresponding id. If no row was found, throws an Exception
     *
     * @param id identifier of row to delete
     * @throws TagNotFoundException if no tags with such ID were found
     */
    public void deleteById(String id) {
        Tag tag = processId(id).get();
        tagDao.deleteById(tag.getId());
    }

    /**
     * Returns entity from DB with corresponding ID
     *
     * @param id entity id
     * @return {@link Tag}
     */
    public Tag getById(String id) {
        Optional<Tag> tagOptional = processId(id);
        return tagOptional.get();
    }

    private Optional<Tag> processId(String id) {
        if (!isIdValid(id)) {
            throw new ResponseException(new ValidationErrorMessage(ERROR_MESSAGE, id, ERROR_DETAILS));
        }
        Long tagId = Long.parseLong(id.trim());
        Optional<Tag> tagOptional = tagDao.getById(tagId);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException(NO_TAG_WITH_ID, tagId);
        }
        return tagOptional;
    }

    private boolean isIdValid(String id) {

        return (id != null && NumberUtils.isParsable(id.trim()) && Long.parseLong(id.trim()) >= 0);
    }

    /**
     * Creates new record in DB
     *
     * @param tag {@link Tag tag entity}
     */
    public void create(Tag tag) {

        if (tagDao.getByName(tag.getName()).isPresent()) {
            throw new TagAlreadyExistsException(TAG_WITH_THIS_NAME_ALREADY_EXISTS, tag.getName());
        }
        tagDao.create(tag);
    }

    /**
     * Returns Tag with corresponding name
     *
     * @param name name of the {@link Tag}
     * @return {@link Tag} entity
     */
    public Tag getByName(String name) {
        Optional<Tag> tagOptional = tagDao.getByName(name);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException(NO_TAG_WITH_NAME, name);
        }
        return tagOptional.get();
    }
}
