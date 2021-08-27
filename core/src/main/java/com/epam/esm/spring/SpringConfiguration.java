package com.epam.esm.spring;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:spring_config.properties")
public class SpringConfiguration {

    protected final Environment environment;

    public static final String DATA_SOURCE_PROPERTIES_PATH = "spring_config";
    public static final String DRIVER_CLASS_NAME = "jdbc.driverClassName";
    public static final String URL = "jdbc.url";
    public static final String USERNAME = "jdbc.username";
    public static final String PASSWORD = "jdbc.password";

    @Autowired
    public SpringConfiguration(Environment environment) {
        this.environment = environment;
    }


    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty( DRIVER_CLASS_NAME));
        dataSource.setUrl(environment.getProperty(URL));
        dataSource.setUsername(environment.getProperty(USERNAME));
        dataSource.setPassword(environment.getProperty(PASSWORD));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
}