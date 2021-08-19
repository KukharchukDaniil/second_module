package com.epam.esm.spring;

import com.epam.esm.dao.CertificateDao;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.epam.esm")
public class SpringTestConfiguration {

    private final Environment environment;

    public static final String DATA_SOURCE_PROPERTIES_PATH = "spring_config";
    public static final String DRIVER_CLASS_NAME = "jdbc.driverClassName";
    public static final String URL = "jdbc.url";
    public static final String USERNAME = "jdbc.username";
    public static final String PASSWORD = "jdbc.password";

    @Autowired
    public SpringTestConfiguration(Environment environment) {
        this.environment = environment;
    }


    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty(DRIVER_CLASS_NAME));
        dataSource.setUrl(environment.getProperty(URL));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    @Bean
    public CertificateDao certificateDao(){
        return new CertificateDao(jdbcTemplate());
    }
}
