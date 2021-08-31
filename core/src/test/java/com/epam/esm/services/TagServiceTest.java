package com.epam.esm.services;

import com.epam.esm.comparators.TagComparator;
import com.epam.esm.dao.TagJdbcDao;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.service.TagNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private static TagJdbcDao tagJdbcDao;
    public static final String NAME = "name";
    public static final long ID = 1;
    private static final Tag TAG = new Tag(NAME);


    private static TagService tagService;


    @BeforeEach
    public void init() {
        tagJdbcDao = mock(TagJdbcDao.class);
        tagService = new TagService(tagJdbcDao);
    }

    @Test
    void getAll_success() {
        when(tagJdbcDao.getAll()).thenReturn(Arrays.asList(TAG));
        List<Tag> actual = tagService.getAll();
        Assertions.assertThat(actual.get(0)).usingComparator(new TagComparator()).isEqualTo(TAG);
    }

    @Test
    void getById_whenIdIsValid_success() {
        when(tagJdbcDao.getById(ID)).thenReturn(Optional.of(TAG));
        Tag actual = tagService.getById(ID);
        Assertions.assertThat(actual).usingComparator(new TagComparator()).isEqualTo(TAG);
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
            when(tagJdbcDao.getById(ID)).thenReturn(Optional.of(TAG));
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
        when(tagJdbcDao.getByName(NAME)).thenReturn(Optional.of(TAG));
        Tag actual = tagService.getByName(NAME);
        Assertions.assertThat(actual).usingComparator(new TagComparator()).isEqualTo(TAG);
    }

    @Test
    void getByName_whenNameIsInvalid_throwsTagNotFoundException() {
        assertThrows(TagNotFoundException.class, () -> {
            when(tagJdbcDao.getByName(NAME)).thenReturn(Optional.empty());
            tagService.getByName(NAME);
        });
    }
}