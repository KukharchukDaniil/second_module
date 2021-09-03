package com.epam.esm.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;

/**
 * Class for proper Servlet mapping
 */
@EnableWebMvc
@Configuration
@ComponentScan("com.epam.esm")
public class SpringMvcConfig implements WebMvcConfigurer {

    private static final String BASE_NAMES = "exceptions";

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename(BASE_NAMES);
        resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return resourceBundleMessageSource;
    }
}
