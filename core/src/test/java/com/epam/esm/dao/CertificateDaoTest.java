package com.epam.esm.dao;


import com.epam.esm.entities.Certificate;
import com.epam.esm.spring.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class CertificateDaoTest {
    //TODO:
    // 1. create tests for Dao
    // 2. finish TagController
    // 3. test TagController using Postman
    // 4. check rest api uri naming
    // 5. fix date formats in Certificate entity
    // 6. refactor CertificateResultSetExtractor
    // 7. check theory
    @Autowired
    private CertificateDao certificateDao;

    @Test
    public void testCreate(){
        Certificate certificate = new Certificate("name","description",
                1,1, Date.valueOf("2015-03-31").toString(),Date.valueOf("2015-03-31").toString());
        certificateDao.create(certificate);
        System.out.println(certificateDao.getAll());
        assertEquals("a","a");
    }

}
//service
//controller
//пофиксить метод
//написать тесты