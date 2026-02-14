package com.cognizant.userservice.config;

import org.springdoc.core.providers.RepositoryRestResourceProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class SwaggerConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration
            (RepositoryRestConfiguration config, CorsRegistry cors) {
        config.setExposeRepositoryMethodsByDefault(false);
    }
}