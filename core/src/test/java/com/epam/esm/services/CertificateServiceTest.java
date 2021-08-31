package com.epam.esm.services;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entities.Certificate;
import com.epam.esm.enums.CertificateSortingOrder;
import com.epam.esm.exceptions.service.CertificateNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {
    public static final int ID = 0;
    public static final Certificate FIRST_CERTIFICATE = new Certificate(
            "a_name", "description", 1, 1, LocalDateTime.now(), LocalDateTime.now()
    );
    public static final Certificate SECOND_CERTIFICATE = new Certificate(
            "b_name", "description", 1, 1, LocalDateTime.now(), LocalDateTime.now()
    );
    public static final Certificate THIRD_CERTIFICATE = new Certificate(
            "c_name", "description", 1, 1, LocalDateTime.now(), LocalDateTime.now()
    );
    public static final List<Certificate> CERTIFICATE_LIST =
            Arrays.asList(
                    FIRST_CERTIFICATE,
                    SECOND_CERTIFICATE,
                    THIRD_CERTIFICATE
            );
    public static final String NAME = "name";

    private static CertificateDao certificateDao;
    private static TagDao tagDao;

    private static CertificateService certificateService;


    @BeforeEach
    public void init() {
        certificateDao = mock(CertificateDao.class);
        tagDao = mock(TagDao.class);
        certificateService = new CertificateService(certificateDao, tagDao);
    }

    @Test
    void getById_whenIdIsValid_success() {
        when(certificateDao.getById(ID)).thenReturn(Optional.of(FIRST_CERTIFICATE));
        Certificate actual = certificateService.getById(ID);
        Assertions.assertEquals(FIRST_CERTIFICATE, actual);

    }

    @Test
    void getById_whenIdIsInvalid_throwsCertificateNotFoundException() {
        when(certificateDao.getById(ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(CertificateNotFoundException.class, () -> {
            certificateService.getById(ID);
        });

    }

    @Test
    void getAll_success() {
        List<Certificate> expected = Arrays.asList(FIRST_CERTIFICATE);
        when(certificateDao.getAll()).thenReturn(expected);
        List<Certificate> actual = certificateService.getAll(CertificateSortingOrder.NONE);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByTagName_success() {
        List<Certificate> expected = Arrays.asList(FIRST_CERTIFICATE);
        when(certificateDao.getByTagName(NAME)).thenReturn(expected);
        List<Certificate> actual = certificateService.getByTagName(NAME);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByNamePartSorted_whenSortingOrderIsAsc_success() {
        List<Certificate> expected = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE, THIRD_CERTIFICATE);
        when(certificateDao.getByNamePart(NAME)).thenReturn(CERTIFICATE_LIST);
        List<Certificate> actual = certificateService.getByNamePartSorted(CertificateSortingOrder.ASC, NAME);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByNamePartSorted_whenSortingOrderIsDesc_success() {
        List<Certificate> expected = Arrays.asList(THIRD_CERTIFICATE, SECOND_CERTIFICATE, FIRST_CERTIFICATE);
        when(certificateDao.getByNamePart(NAME)).thenReturn(CERTIFICATE_LIST);
        List<Certificate> actual = certificateService.getByNamePartSorted(CertificateSortingOrder.DESC, NAME);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByNamePartSorted_whenSortingOrderIsNone_success() {
        when(certificateDao.getByNamePart(NAME)).thenReturn(CERTIFICATE_LIST);
        List<Certificate> actual = certificateService.getByNamePartSorted(CertificateSortingOrder.NONE, NAME);
        Assertions.assertEquals(CERTIFICATE_LIST, actual);
    }

    @Test
    void update_whenIdIsValid_success() {
        Assertions.assertDoesNotThrow(() -> {
            when(certificateDao.getById(ID)).thenReturn(Optional.of(FIRST_CERTIFICATE));
            certificateService.update(FIRST_CERTIFICATE);
        });
    }

    @Test
    void update_whenIdIsInvalid_throwsCertificateNotFoundException() {
        Assertions.assertThrows(CertificateNotFoundException.class, () -> {
            when(certificateDao.getById(ID)).thenReturn(Optional.empty());
            certificateService.update(FIRST_CERTIFICATE);
        });
    }

    @Test
    void delete_whenIdIsValid_success() {
        Assertions.assertDoesNotThrow(() -> {
            when(certificateDao.getById(ID)).thenReturn(Optional.of(new Certificate()));
            certificateService.delete(ID);
        });
    }

    @Test
    void delete_whenIdIsInvalid_throwsCertificateNotFoundException() {
        Assertions.assertThrows(CertificateNotFoundException.class, () -> {
            when(certificateDao.getById(ID)).thenReturn(Optional.empty());
            certificateService.delete(ID);
        });
    }
}