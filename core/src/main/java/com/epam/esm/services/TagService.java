package com.epam.esm.services;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.exceptions.service.TagAlreadyExistsException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    public static final String NO_TAG_WITH_ID = "No tag was found{id = %s}";
    public static final String TAG_WITH_THIS_NAME_ALREADY_EXISTS = "Tag name is already used {name = %s}";
    private static final String NO_TAG_WITH_NAME = "Invalid tag name.No tag was found {name = %s}";
    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
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
     * @throws TagNotFoundException
     */
    public void deleteById(long id) throws ServiceException {
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
     * @throws TagNotFoundException if no tags with such ID were found
     */
    public Tag getById(long id) throws TagNotFoundException {
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
     * @throws ServiceException if tag with this name already exists
     */
    public void create(Tag tag) throws ServiceException {

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
     * @throws ServiceException if no tag with this name was found
     */
    public Tag getByName(String name) throws ServiceException {
        Optional<Tag> tagOptional = tagDao.getByName(name);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException(String.format(NO_TAG_WITH_NAME, name));
        }
        return tagOptional.get();
    }
}
