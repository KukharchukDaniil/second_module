package com.epam.esm.dao;

import com.epam.esm.comparators.TagComparator;
import com.epam.esm.entities.Tag;
import com.epam.esm.spring.SpringTestConfiguration;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})
@Transactional
public class TagDaoTest {
    public static final int FREE_TAG_ID = 1;
    public static final int PRO_TAG_ID = 2;
    public static final int ADVANCED_TAG_ID = 3;
    public static final String FREE_TAG_NAME = "free";
    public static final String PRO_TAG_NAME = "pro";
    public static final String ADVANCED_TAG_NAME = "advanced";
    private static final List<Tag> ALL_TAGS_LIST = Arrays.asList(new Tag(FREE_TAG_ID, FREE_TAG_NAME),
            new Tag(PRO_TAG_ID, PRO_TAG_NAME), new Tag(ADVANCED_TAG_ID, ADVANCED_TAG_NAME));
    private static final Tag TAG_BY_ID = new Tag(FREE_TAG_ID, FREE_TAG_NAME);
    private static final String CREATED_TAG_NAME = "createdTag";
    private static final Tag CREATED_TAG = new Tag(CREATED_TAG_NAME);

    @Resource
    private TagDao tagDao;


    @Test
    public void getAll_success() {
        List<Tag> actual = tagDao.getAll();
        assertEquals(0, compareTagLists(ALL_TAGS_LIST, actual));
    }

    @Test
    public void getById_success() {
        Tag actual = tagDao.getById(FREE_TAG_ID).get();
        assertThat(actual).usingComparator(new TagComparator()).isEqualTo(TAG_BY_ID);
    }

    @Test
    public void getById_fail() {
        Tag actual = tagDao.getById(PRO_TAG_ID).get();
        assertNotEquals(TAG_BY_ID, actual);
    }

    @Test
    @Rollback
    public void deleteById_success() {
        tagDao.deleteById(PRO_TAG_ID);
        Optional<Tag> tagOptional = tagDao.getById(PRO_TAG_ID);
        assertFalse(tagOptional.isPresent());
    }

    @Test
    @Rollback
    public void create_success() {
        long tagId = tagDao.create(CREATED_TAG);
        Optional<Tag> actual = tagDao.getById(tagId);
        Tag expected = new Tag(tagId, CREATED_TAG_NAME);
        assertThat(actual.get()).usingComparator(new TagComparator()).isEqualTo(expected);
    }

    @Test
    @Rollback
    public void getByName_success() {
        Optional<Tag> tagOptional = tagDao.getByName(FREE_TAG_NAME);

    }

    private int compareTagLists(List<Tag> o1, List<Tag> o2) {
        int result = 0;
        if (o1 == null || o2 == null) {
            if (o1 == o2) {
                result = 0;
            } else {
                result = 1;
            }
        } else {
            if (o1.size() == o2.size()) {
                for (int i = 0; i < o1.size(); i++) {
                    Tag o1tag = o1.get(i);
                    Tag o2Tag = o2.get(i);
                    result += o1tag.getName().equals(o2Tag.getName())
                            && o1tag.getId() == o2Tag.getId() ? 0 : 1;
                }
            }
        }
        return result;
    }

}