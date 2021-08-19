package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entities.Certificate;
import com.epam.esm.spring.SpringConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringConfiguration.class
        );
        CertificateDao dao =  context.getBean(CertificateDao.class);
        List<Certificate> aNew = dao.getAll();
        System.out.println(aNew);
    }
}
