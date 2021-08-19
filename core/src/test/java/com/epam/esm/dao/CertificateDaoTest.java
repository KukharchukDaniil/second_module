package com.epam.esm.dao;

import com.epam.esm.entities.Certificate;
import com.epam.esm.spring.SpringConfiguration;
import com.epam.esm.spring.SpringTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;

@ContextConfiguration(loader= AnnotationConfigContextLoader.class ,classes = SpringTestConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
class CertificateDaoTest {
    @Resource
    private CertificateDao certificateDao;
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringTestConfiguration.class
        );
        certificateDao =  context.getBean(CertificateDao.class);
    } //fix

    @Test
    @Transactional
    @Rollback(true)
    public void testCreate(){
        Certificate certificate = new Certificate("name","description",
                1,1,"10.02.2002","10.02.2002");
        certificateDao.create(certificate);
        System.out.println(certificateDao.getAll());
    }

}
//service
//controller
//пофиксить метод
//написать тесты