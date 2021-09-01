package com.epam.esm.services;

import com.epam.esm.dao.TagJdbcDao;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.service.TagNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private static TagJdbcDao tagJdbcDao;
    private static final String NAME = "name";
    private static final long ID = 1;
    private static Tag tag;

    private static TagService tagService;

    @BeforeEach
    public void init() {
        tagJdbcDao = mock(TagJdbcDao.class);
        tagService = new TagService(tagJdbcDao);
        tag = new Tag(NAME);
    }

    @Test
    void getAll_success() {
        List<Tag> tagList = Arrays.asList(tag);
        when(tagJdbcDao.getAll()).thenReturn(tagList);
        List<Tag> actual = tagService.getAll();
        assertEquals(tagList, actual);
    }

    @Test
    void getById_whenIdIsValid_success() {
        when(tagJdbcDao.getById(ID)).thenReturn(Optional.of(tag));
        Tag actual = tagService.getById(ID);
        assertEquals(tag, actual);
    }

    @Test
    void getById_whenIdIsInvalid_throwsTagNotFoundException() {
        assertThrows(TagNotFoundException.class, () -> {
            when(tagJdbcDao.getById(ID)).thenReturn(Optional.empty());
            tagService.getById(ID);
        });
    }

    @Test
    void deleteById_whenIdIsValid_success() {
        assertDoesNotThrow(() -> {
            when(tagJdbcDao.getById(ID)).thenReturn(Optional.of(tag));
            tagService.deleteById(ID);
        });
    }

    @Test
    void deleteById_whenIdIsInvalid_throwsTagNotFoundException() {
        assertThrows(TagNotFoundException.class, () -> {
            when(tagJdbcDao.getById(ID)).thenReturn(Optional.empty());
            tagService.deleteById(ID);
        });
    }

    @Test
    void getByName_whenNameIsValid_success() {
        when(tagJdbcDao.getByName(NAME)).thenReturn(Optional.of(tag));
        Tag actual = tagService.getByName(NAME);
        assertEquals(tag, actual);
    }

    @Test
    void getByName_whenNameIsInvalid_throwsTagNotFoundException() {
        assertThrows(TagNotFoundException.class, () -> {
            when(tagJdbcDao.getByName(NAME)).thenReturn(Optional.empty());
            tagService.getByName(NAME);
        });
    }
}