package com.epam.esm.dao;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.dao.DaoException;
import com.epam.esm.spring.SpringTestConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class CertificateJdbcDaoTest {

    private static LocalDateTime date;
    private static final String CERTIFICATE_ONE_NAME = "certificate_one";
    private static final String CERTIFICATE_ONE_DESCRIPTION = "b_description";
    private static final int CERTIFICATE_ONE_PRICE = 1;
    private static final int CERTIFICATE_ONE_DURATION = 1;
    private static final int CERTIFICATE_ONE_ID = 1;
    private static final String FREE_TAG_NAME = "free";
    public static final long FREE_TAG_ID = 1;
    public static final long PRO_TAG_ID = 2;
    public static final long ADVANCED_TAG_ID = 3;
    public static final String PRO_TAG_NAME = "pro";
    public static final String ADVANCED_TAG_NAME = "advanced";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    private static Tag freeTag;
    private static Tag proTag;
    private static Tag advancedTag;
    private static final int ALL_EXPECTED_LIST_SIZE = 3;
    private static List<Tag> certificateOneTags;
    private static Certificate createExpected;
    private static Certificate certificate_one;
    private static Certificate detachTagCertificate;
    private static final long CREATE_EXPECTED_ID = 4;
    private static final Integer CERTIFICATE_ONE_CHANGED_DURATION = 222;
    private static final int GET_BY_TAG_NAME_EXPECTED = 2;
    private static final String VALID_NAME_PART = "cer";
    private static final int EXPECTED_GET_BY_NAME_PART = 3;
    private static final long INVALID_ID = 132;
    private static final String INVALID_NAME = "DDDDDDDDDDD";

    @Autowired
    private CertificateJdbcDao certificateJdbcDao;

    private static Certificate getByIdExpectedCertificate;

    @BeforeAll
    public static void init() {
        date = LocalDateTime.parse("2021-08-27T01:06:56.817");

        freeTag = new Tag(FREE_TAG_ID, FREE_TAG_NAME);
        proTag = new Tag(PRO_TAG_ID, PRO_TAG_NAME);
        advancedTag = new Tag(ADVANCED_TAG_ID, ADVANCED_TAG_NAME);

        getByIdExpectedCertificate = new Certificate(CERTIFICATE_ONE_NAME, CERTIFICATE_ONE_DESCRIPTION,
                CERTIFICATE_ONE_PRICE, CERTIFICATE_ONE_DURATION, date, date);

        detachTagCertificate = new Certificate(CERTIFICATE_ONE_NAME, CERTIFICATE_ONE_DESCRIPTION,
                CERTIFICATE_ONE_PRICE, CERTIFICATE_ONE_DURATION, date, date);
        detachTagCertificate.setTagList(new ArrayList<>());

        getByIdExpectedCertificate.setId(CERTIFICATE_ONE_ID);
        List<Tag> tags = Arrays.asList(freeTag, proTag);
        getByIdExpectedCertificate.setTagList(tags);

        certificate_one = new Certificate(CERTIFICATE_ONE_NAME, CERTIFICATE_ONE_DESCRIPTION,
                CERTIFICATE_ONE_PRICE, CERTIFICATE_ONE_DURATION, date, date);
        certificateOneTags = new ArrayList<Tag>(Arrays.asList(freeTag, proTag));

        createExpected = new Certificate(NAME, DESCRIPTION,
                CERTIFICATE_ONE_PRICE, CERTIFICATE_ONE_DURATION, date, date);
    }

    @BeforeEach
    public void setCertificateOneDuration() {
        certificate_one.setDuration(CERTIFICATE_ONE_DURATION);
    }

    @Test
    public void getById_success() {
        Optional<Certificate> actualOptional = certificateJdbcDao.getById(CERTIFICATE_ONE_ID);

        assertTrue(actualOptional.isPresent());

        Certificate actual = actualOptional.get();
        assertEquals(getByIdExpectedCertificate, actual);
    }

    @Test
    public void getById_noEntityWithId_fail() {
        Optional<Certificate> certificateOptional = certificateJdbcDao.getById(INVALID_ID);
        assertFalse(certificateOptional.isPresent());
    }

    @Test
    public void getAll_success() {
        List<Certificate> actual = certificateJdbcDao.getAll();
        assertEquals(ALL_EXPECTED_LIST_SIZE, actual.size());
    }

    @Test
    @Rollback
    public void create_success() throws DaoException {

        Certificate expected = createExpected;
        expected.setId(CREATE_EXPECTED_ID);
        expected.setTagList(new ArrayList<>());
        certificateJdbcDao.create(expected);
        Optional<Certificate> certificateOptional = certificateJdbcDao.getById(CREATE_EXPECTED_ID);

        assertTrue(certificateOptional.isPresent());
        Certificate actual = certificateOptional.get();
        assertEquals(expected, actual);
    }

    @Test
    @Rollback
    public void update_success() {
        Certificate expected = certificate_one;
        expected.setDuration(CERTIFICATE_ONE_CHANGED_DURATION);
        expected.setTagList(certificateOneTags);
        expected.setId(CERTIFICATE_ONE_ID);
        certificateJdbcDao.update(expected);

        Optional<Certificate> certificateOptional = certificateJdbcDao.getById(CERTIFICATE_ONE_ID);
        assertTrue(certificateOptional.isPresent());
        Certificate actual = certificateOptional.get();
        assertEquals(expected, actual);

    }

    @Test
    public void getByTagName_validName_success() {
        List<Certificate> byTagName = certificateJdbcDao.getByTagName(proTag.getName());
        int actual = byTagName.size();
        assertEquals(GET_BY_TAG_NAME_EXPECTED, actual);
    }

    @Test
    public void getByTagName_invalidName_fail() {
        List<Certificate> byTagName = certificateJdbcDao.getByTagName(INVALID_NAME);
        int actual = byTagName.size();
        assertNotEquals(GET_BY_TAG_NAME_EXPECTED, actual);
    }

    @Test
    public void getByNamePart_success() {
        List<Certificate> byTagName = certificateJdbcDao.getByNamePart(VALID_NAME_PART);
        int actual = byTagName.size();
        assertEquals(EXPECTED_GET_BY_NAME_PART, actual);
    }

    @Test
    public void isAttachedToTag_success() {
        boolean actual = certificateJdbcDao.isAttachedToTag(1L, 1L);
        assertTrue(actual);
    }

    @Test
    public void isAttachedToTag_fail() {
        boolean actual = certificateJdbcDao.isAttachedToTag(1L, 13L);
        assertFalse(actual);
    }

    @Test
    @Rollback
    public void attachToTag_success() {
        Certificate expected = certificate_one;
        expected.setTagList(certificateOneTags);
        expected.setId(CERTIFICATE_ONE_ID);
        List<Tag> tagList = expected.getTagList();
        tagList.add(advancedTag);

        certificateJdbcDao.attachCertificateToTag(CERTIFICATE_ONE_ID, advancedTag.getId());
        Optional<Certificate> certificateOptional = certificateJdbcDao.getById(CERTIFICATE_ONE_ID);
        assertTrue(certificateOptional.isPresent());
        Certificate actual = certificateOptional.get();

        assertEquals(expected, actual);
    }

    @Test
    @Rollback
    public void detachTag_success() {
        certificateJdbcDao.detachTagsFromCertificateExceptPresented(CERTIFICATE_ONE_ID, new Long[]{FREE_TAG_ID, PRO_TAG_ID});
        Optional<Certificate> certificateOptional = certificateJdbcDao.getById(CERTIFICATE_ONE_ID);
        assertTrue(certificateOptional.isPresent());
        assertEquals(detachTagCertificate.getTagList(), certificateOptional.get().getTagList());
    }

    @Test
    @Rollback
    public void deleteById_success() {
        certificateJdbcDao.deleteById(CERTIFICATE_ONE_ID);
        Optional<Certificate> certificateOptional = certificateJdbcDao.getById(CERTIFICATE_ONE_ID);
        assertFalse(certificateOptional.isPresent());
    }
}
