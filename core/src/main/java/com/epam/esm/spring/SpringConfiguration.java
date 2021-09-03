package com.epam.esm.spring;

import com.epam.esm.mapping.TagRowMapper;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Provides configuration for Spring container
 */
@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:spring_config.properties")
public class SpringConfiguration {

    private static final String EXTRACTOR_TAG_ROW_MAPPER_ID = "extractor.tagRowMapperIdColumn";
    private static final String EXTRACTOR_TAG_ROW_MAPPER_NAME = "extractor.tagRowMapperNameColumn";
    private static final String DAO_TAG_ROW_MAPPER_ID_COLUMN = "dao.tagRowMapperIdColumn";
    private static final String DAO_TAG_ROW_MAPPER_NAME_COLUMN = "dao.tagRowMapperNameColumn";
    private static final String RESOURCE_BUNDLE_BASE_NAME = "resourceBundle.baseName";
    protected final Environment environment;

    private static final String DRIVER_CLASS_NAME = "jdbc.driverClassName";
    private static final String URL = "jdbc.url";
    private static final String USERNAME = "jdbc.username";
    private static final String PASSWORD = "jdbc.password";

    @Autowired
    public SpringConfiguration(Environment environment) {
        this.environment = environment;
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
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSourceTransactionManager jdbcTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "extractorTagRowMapper")
    public TagRowMapper extractorTagRowMapper() {
        return new TagRowMapper(environment.getProperty(EXTRACTOR_TAG_ROW_MAPPER_ID),
                environment.getProperty(EXTRACTOR_TAG_ROW_MAPPER_NAME));
    }

    @Bean(name = "daoTagRowMapper")
    public TagRowMapper daoTagRowMapper() {
        return new TagRowMapper(environment.getProperty(DAO_TAG_ROW_MAPPER_ID_COLUMN),
                environment.getProperty(DAO_TAG_ROW_MAPPER_NAME_COLUMN));
    }


}