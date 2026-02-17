package com.example.fieldsync_inventory_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Enable trailing slash matching with PathPatternParser (default in Spring Boot 3.x)
        configurer.setPatternParser(new PathPatternParser());
        // Prevent trailing slash responding with error
        configurer.setUseTrailingSlashMatch(true); // deprecated
    }
}