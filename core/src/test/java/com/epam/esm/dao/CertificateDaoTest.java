package com.epam.esm.dao;


import com.epam.esm.entities.Certificate;
import com.epam.esm.exceptions.dao.DaoException;
import com.epam.esm.spring.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class CertificateDaoTest {

    private CertificateDao certificateDao;

    @Test
    public void testGetCertificateById() {
        List<Certificate> all = certificateDao.getAll();
//        System.out.println(all);
        assertTrue(true);
    }

    @Test
    @Rollback
    public void testCreateCertificate() throws DaoException {
        Certificate certificate = new Certificate("name", "description",
                1, 1, LocalDateTime.now(), LocalDateTime.now());
        long certificateId = certificateDao.create(certificate);
        certificate.setId(certificateId);
        Optional<Certificate> certificateOptional = certificateDao.getById(certificateId);
        Certificate actual = certificateOptional.get();
        assertEquals(certificate, actual);
    }

}
