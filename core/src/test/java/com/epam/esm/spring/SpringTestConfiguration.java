package com.epam.esm.spring;

import com.epam.esm.dao.CertificateJdbcDao;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:spring_config.properties")
@EnableTransactionManagement
public class SpringTestConfiguration extends SpringConfiguration {

    public static final String DRIVER_CLASS_NAME = "jdbc.driverClassName";
    public static final String URL = "jdbc.url";
    public static final String USERNAME = "jdbc.username";
    public static final String PASSWORD = "jdbc.password";

    @Autowired
    public SpringTestConfiguration(Environment environment) {
        super(environment);
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty(DRIVER_CLASS_NAME));
        dataSource.setUrl(environment.getProperty(URL));
        dataSource.setUsername(environment.getProperty(USERNAME));
        dataSource.setPassword(environment.getProperty(PASSWORD));
        return dataSource;

    }

    @Bean
    public DataSourceTransactionManager jdbcTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public CertificateJdbcDao certificateDao() {
        return new CertificateJdbcDao(jdbcTemplate());
    }

}
