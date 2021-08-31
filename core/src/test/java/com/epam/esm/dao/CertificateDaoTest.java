package com.epam.esm.dao;


import com.epam.esm.comparators.CertificateComparator;
import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.dao.DaoException;
import com.epam.esm.spring.SpringTestConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})
@Transactional
public class CertificateDaoTest {

    public static final LocalDateTime DATE = LocalDateTime.parse("2021-08-27T01:06:56.817");
    public static final String CERTIFICATE_ONE_NAME = "certificate_one";
    public static final String CERTIFICATE_ONE_DESCRIPTION = "b_description";
    public static final int CERTIFICATE_ONE_PRICE = 1;
    public static final int CERTIFICATE_ONE_DURATION = 1;
    public static final int CERTIFICATE_ONE_ID = 1;
    public static final Tag FREE_TAG = new Tag(1, "free");
    public static final Tag PRO_TAG = new Tag(2, "pro");
    public static final Tag ADVANCED_TAG = new Tag(3, "advanced");
    private static final int ALL_EXPECTED_LIST_SIZE = 3;
    private static final List<Tag> CERTIFICATE_ONE_TAGS = new ArrayList<Tag>(Arrays.asList(FREE_TAG, PRO_TAG));
    public static final Certificate CREATE_EXPECTED = new Certificate("name", "description",
            1, 1, LocalDateTime.now(), LocalDateTime.now());
    public static final Certificate CERTIFICATE_ONE = new Certificate(CERTIFICATE_ONE_NAME, CERTIFICATE_ONE_DESCRIPTION,
            CERTIFICATE_ONE_PRICE, CERTIFICATE_ONE_DURATION, DATE, DATE);
    private static final long CREATE_EXPECTED_ID = 4;
    private static final Integer CERTIFICATE_ONE_CHANGED_DURATION = 222;
    private static final int GET_BY_TAG_NAME_EXPECTED = 2;
    public static final String VALID_NAME_PART = "cer";
    private static final int EXPECTED_GET_BY_NAME_PART = 3;
    private static final long INVALID_ID = 132;
    private static final String INVALID_NAME = "DDDDDDDDDDD";

    @Autowired
    private CertificateDao certificateDao;

    private static Certificate GET_BY_ID_EXPECTED_CERTIFICATE;

    @BeforeAll
    public static void init() {
        GET_BY_ID_EXPECTED_CERTIFICATE = new Certificate(CERTIFICATE_ONE_NAME, CERTIFICATE_ONE_DESCRIPTION,
                CERTIFICATE_ONE_PRICE, CERTIFICATE_ONE_DURATION, DATE, DATE);
        GET_BY_ID_EXPECTED_CERTIFICATE.setId(CERTIFICATE_ONE_ID);
        List<Tag> tags = Arrays.asList(FREE_TAG, PRO_TAG);
        GET_BY_ID_EXPECTED_CERTIFICATE.setTagList(tags);

    }

    @BeforeEach
    public void setCertificateOneDuration() {
        CERTIFICATE_ONE.setDuration(CERTIFICATE_ONE_DURATION);
    }

    @Test
    public void getById_validId_success() {
        Certificate actual = certificateDao.getById(CERTIFICATE_ONE_ID).get();
        assertEquals(GET_BY_ID_EXPECTED_CERTIFICATE, actual);
        Assertions.assertThat(actual).usingComparator(new CertificateComparator()).isEqualTo(GET_BY_ID_EXPECTED_CERTIFICATE);
        assertEquals(0, compareTagLists(GET_BY_ID_EXPECTED_CERTIFICATE.getTagList(), actual.getTagList()));
    }

    @Test
    public void getById_noEntityWithId_fail() {
        assertThrows(NoSuchElementException.class, () -> {
            Certificate actual = certificateDao.getById(INVALID_ID).get();
        });
    }

    @Test
    public void getAll_success() {
        List<Certificate> actual = certificateDao.getAll();

        assertEquals(ALL_EXPECTED_LIST_SIZE, actual.size());
    }


    @Test
    @Rollback
    public void create_success() throws DaoException {

        Certificate expected = CREATE_EXPECTED;
        expected.setId(CREATE_EXPECTED_ID);
        expected.setTagList(new ArrayList<>());
        certificateDao.create(expected);
        Optional<Certificate> certificateOptional = certificateDao.getById(CREATE_EXPECTED_ID);

        Certificate actual = certificateOptional.get();
        Assertions.assertThat(actual).usingComparator(new CertificateComparator()).isEqualTo(expected);
        assertEquals(0, compareTagLists(expected.getTagList(), actual.getTagList()));
    }

    @Test
    @Rollback
    public void update_success() {
        Certificate expected = CERTIFICATE_ONE;
        expected.setDuration(CERTIFICATE_ONE_CHANGED_DURATION);
        expected.setTagList(CERTIFICATE_ONE_TAGS);
        expected.setId(CERTIFICATE_ONE_ID);
        certificateDao.update(expected);
        Certificate actual = certificateDao.getById(CERTIFICATE_ONE_ID).get();
        Assertions.assertThat(actual).usingComparator(new CertificateComparator()).isEqualTo(expected);
        assertEquals(0, compareTagLists(expected.getTagList(), actual.getTagList()));

    }

    @Test
    public void getByTagName_validName_success() {
        List<Certificate> byTagName = certificateDao.getByTagName(PRO_TAG.getName());
        int actual = byTagName.size();
        assertEquals(GET_BY_TAG_NAME_EXPECTED, actual);
    }

    @Test
    public void getByTagName_invalidName_fail() {
        List<Certificate> byTagName = certificateDao.getByTagName(INVALID_NAME);
        int actual = byTagName.size();
        assertNotEquals(GET_BY_TAG_NAME_EXPECTED, actual);
    }


    @Test
    public void getByNamePart_success() {
        List<Certificate> byTagName = certificateDao.getByNamePart(VALID_NAME_PART);
        int actual = byTagName.size();
        assertEquals(EXPECTED_GET_BY_NAME_PART, actual);
    }


    @Test
    public void isAttachedToTag_success() {
        boolean actual = certificateDao.isAttachedToTag(1L, 1L);
        assertTrue(actual);
    }

    @Test
    public void isAttachedToTag_fail() {
        boolean actual = certificateDao.isAttachedToTag(1L, 13L);
        assertFalse(actual);
    }

    @Test
    @Rollback
    public void attachToTag_success() {
        Certificate expected = CERTIFICATE_ONE;
        expected.setTagList(CERTIFICATE_ONE_TAGS);
        expected.setId(CERTIFICATE_ONE_ID);
        List<Tag> tagList = expected.getTagList();
        tagList.add(ADVANCED_TAG);

        certificateDao.attachCertificateToTag(CERTIFICATE_ONE_ID, ADVANCED_TAG.getId());
        Certificate actual = certificateDao.getById(CERTIFICATE_ONE_ID).get();

        Assertions.assertThat(actual).usingComparator(new CertificateComparator()).isEqualTo(expected);
        assertEquals(0, compareTagLists(expected.getTagList(), actual.getTagList()));
    }

    @BeforeTestMethod()
    @Test
    @Rollback
    public void deleteById_success() {
        certificateDao.deleteById(CERTIFICATE_ONE_ID);
        Optional<Certificate> certificateOptional = certificateDao.getById(CERTIFICATE_ONE_ID);
        assertFalse(certificateOptional.isPresent());
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
