package com.epam.esm.services;

import com.epam.esm.dao.TagJdbcDao;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.service.TagAlreadyExistsException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer class. Provides methods for CRUD operations on {@link Tag} using {@link TagDao}
 */
@Service
public class TagService {

    private static final String NO_TAG_WITH_ID = "No tag was found{id = %s}";
    private static final String TAG_WITH_THIS_NAME_ALREADY_EXISTS = "Tag name is already used {name = %s}";
    private static final String NO_TAG_WITH_NAME = "Invalid tag name.No tag was found {name = %s}";
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
    public void deleteById(long id) {
        Optional<Tag> tagOptional = tagDao.getById(id);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException(String.format(NO_TAG_WITH_ID, id));
        }
        tagDao.deleteById(id);
    }

    /**
     * Returns entity from DB with corresponding ID
     *
     * @param id entity id
     * @return {@link Tag}
     */
    public Tag getById(long id) {
        Optional<Tag> tagOptional = tagDao.getById(id);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException(String.format(NO_TAG_WITH_ID, id));
        }
        return tagOptional.get();
    }

    /**
     * Creates new record in DB
     *
     * @param tag {@link Tag tag entity}
     */
    public void create(Tag tag) {

        if (tagDao.getByName(tag.getName()).isPresent()) {
            throw new TagAlreadyExistsException(String.format(TAG_WITH_THIS_NAME_ALREADY_EXISTS, tag.getName()));
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
            throw new TagNotFoundException(String.format(NO_TAG_WITH_NAME, name));
        }
        return tagOptional.get();
    }
}
