package com.epam.esm.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class for proper Servlet mapping
 */
@EnableWebMvc
@Configuration
@ComponentScan("com.epam.esm")
public class SpringMvcConfig implements WebMvcConfigurer {

}
