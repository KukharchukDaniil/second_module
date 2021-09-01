package com.epam.esm.services;

import com.epam.esm.dao.CertificateJdbcDao;
import com.epam.esm.dao.TagJdbcDao;
import com.epam.esm.entities.Certificate;
import com.epam.esm.enums.CertificateSortingOrder;
import com.epam.esm.exceptions.service.CertificateNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    private static LocalDateTime date;
    private static final int ID = 0;
    private static final String C_NAME = "c_name";
    private static final String DESCRIPTION = "description";
    private static final String B_NAME = "b_name";
    private static final String A_NAME = "a_name";
    private static final int PRICE = 1;
    private static final int DURATION = 1;
    public static Certificate firstCertificate;
    public static Certificate secondCertificate;
    public static Certificate thirdCertificate;
    public static List<Certificate> certificateList;
    private static final String NAME = "name";

    private static CertificateJdbcDao certificateJdbcDao;
    private static TagJdbcDao tagJdbcDao;

    private static CertificateService certificateService;

    @BeforeAll
    public static void initClass() {
        date = LocalDateTime.now();

        thirdCertificate = new Certificate(
                C_NAME, DESCRIPTION, PRICE, DURATION, date, date
        );

        secondCertificate = new Certificate(
                B_NAME, DESCRIPTION, PRICE, DURATION, date, date
        );

        firstCertificate = new Certificate(
                A_NAME, DESCRIPTION, PRICE, DURATION, date, date
        );

        certificateList =
                Arrays.asList(
                        firstCertificate,
                        secondCertificate,
                        thirdCertificate
                );
    }

    @BeforeEach
    public void initMethod() {
        certificateJdbcDao = mock(CertificateJdbcDao.class);
        tagJdbcDao = mock(TagJdbcDao.class);
        certificateService = new CertificateService(certificateJdbcDao, tagJdbcDao);
    }

    @Test
    void getById_whenIdIsValid_success() {
        when(certificateJdbcDao.getById(ID)).thenReturn(Optional.of(firstCertificate));
        Certificate actual = certificateService.getById(ID);
        assertEquals(firstCertificate, actual);

    }

    @Test
    void getById_whenIdIsInvalid_throwsCertificateNotFoundException() {
        when(certificateJdbcDao.getById(ID)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> {
            certificateService.getById(ID);
        });

    }

    @Test
    void getAll_success() {
        List<Certificate> expected = Arrays.asList(firstCertificate);
        when(certificateJdbcDao.getAll()).thenReturn(expected);
        List<Certificate> actual = certificateService.getAll(CertificateSortingOrder.NONE);
        assertEquals(expected, actual);
    }

    @Test
    void getAllSorted_whenListIsNull_success() {
        when(certificateJdbcDao.getAll()).thenReturn(null);
        List<Certificate> actual = certificateService.getAll(CertificateSortingOrder.ASC);
        assertEquals(null, actual);
    }

    @Test
    void getByTagName_success() {
        List<Certificate> expected = Arrays.asList(firstCertificate);
        when(certificateJdbcDao.getByTagName(NAME)).thenReturn(expected);
        List<Certificate> actual = certificateService.getByTagName(NAME);
        assertEquals(expected, actual);
    }

    @Test
    void getByNamePartSorted_whenSortingOrderIsAsc_success() {
        List<Certificate> expected = Arrays.asList(firstCertificate, secondCertificate, thirdCertificate);
        when(certificateJdbcDao.getByNamePart(NAME)).thenReturn(certificateList);
        List<Certificate> actual = certificateService.getByNamePartSorted(CertificateSortingOrder.ASC, NAME);
        assertEquals(expected, actual);
    }

    @Test
    void getByNamePartSorted_whenSortingOrderIsDesc_success() {
        List<Certificate> expected = Arrays.asList(thirdCertificate, secondCertificate, firstCertificate);
        when(certificateJdbcDao.getByNamePart(NAME)).thenReturn(certificateList);
        List<Certificate> actual = certificateService.getByNamePartSorted(CertificateSortingOrder.DESC, NAME);
        assertEquals(expected, actual);
    }

    @Test
    void getByNamePartSorted_whenSortingOrderIsNone_success() {
        when(certificateJdbcDao.getByNamePart(NAME)).thenReturn(certificateList);
        List<Certificate> actual = certificateService.getByNamePartSorted(CertificateSortingOrder.NONE, NAME);
        assertEquals(certificateList, actual);
    }

    @Test
    void update_whenIdIsValid_success() {
        assertDoesNotThrow(() -> {
            when(certificateJdbcDao.getById(ID)).thenReturn(Optional.of(firstCertificate));
            certificateService.update(firstCertificate);
        });
    }

    @Test
    void update_whenIdIsInvalid_throwsCertificateNotFoundException() {
        assertThrows(CertificateNotFoundException.class, () -> {
            when(certificateJdbcDao.getById(ID)).thenReturn(Optional.empty());
            certificateService.update(firstCertificate);
        });
    }

    @Test
    void delete_whenIdIsValid_success() {
        assertDoesNotThrow(() -> {
            when(certificateJdbcDao.getById(ID)).thenReturn(Optional.of(new Certificate()));
            certificateService.delete(ID);
        });
    }

    @Test
    void delete_whenIdIsInvalid_throwsCertificateNotFoundException() {
        assertThrows(CertificateNotFoundException.class, () -> {
            when(certificateJdbcDao.getById(ID)).thenReturn(Optional.empty());
            certificateService.delete(ID);
        });
    }
}