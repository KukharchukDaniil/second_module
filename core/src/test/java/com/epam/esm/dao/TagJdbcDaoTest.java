package com.epam.esm.dao;

import com.epam.esm.entities.Tag;
import com.epam.esm.spring.SpringTestConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})
@Transactional
public class TagJdbcDaoTest {
    private static final int FREE_TAG_ID = 1;
    private static final int PRO_TAG_ID = 2;
    private static final int ADVANCED_TAG_ID = 3;
    private static final String FREE_TAG_NAME = "free";
    private static final String PRO_TAG_NAME = "pro";
    private static final String ADVANCED_TAG_NAME = "advanced";
    private static List<Tag> allTagsList;
    private static Tag freeTag;
    private static final String CREATED_TAG_NAME = "createdTag";
    private static Tag createdTag;

    @Resource
    private TagJdbcDao tagJdbcDao;

    @BeforeAll
    public static void init() {
        allTagsList = Arrays.asList(new Tag(FREE_TAG_ID, FREE_TAG_NAME),
                new Tag(PRO_TAG_ID, PRO_TAG_NAME), new Tag(ADVANCED_TAG_ID, ADVANCED_TAG_NAME));

        freeTag = new Tag(FREE_TAG_ID, FREE_TAG_NAME);

        createdTag = new Tag(CREATED_TAG_NAME);
    }

    @Test
    public void getAll_success() {
        List<Tag> actual = tagJdbcDao.getAll();
        assertEquals(allTagsList, actual);
    }

    @Test
    public void getById_success() {
        Tag actual = tagJdbcDao.getById(FREE_TAG_ID).get();
        assertEquals(freeTag, actual);
    }

    @Test
    public void getById_fail() {
        Tag actual = tagJdbcDao.getById(PRO_TAG_ID).get();
        assertNotEquals(freeTag, actual);
    }

    @Test
    @Rollback
    public void deleteById_success() {
        tagJdbcDao.deleteById(PRO_TAG_ID);
        Optional<Tag> tagOptional = tagJdbcDao.getById(PRO_TAG_ID);
        assertFalse(tagOptional.isPresent());
    }

    @Test
    @Rollback
    public void create_success() {
        long tagId = tagJdbcDao.create(createdTag);
        Optional<Tag> actual = tagJdbcDao.getById(tagId);
        assertTrue(actual.isPresent());
        Tag expected = new Tag(tagId, CREATED_TAG_NAME);
        assertEquals(expected, actual.get());
    }

    @Test
    @Rollback
    public void getByName_success() {
        Optional<Tag> tagOptional = tagJdbcDao.getByName(FREE_TAG_NAME);
        assertTrue(tagOptional.isPresent());
        assertEquals(freeTag, tagOptional.get());
    }

}