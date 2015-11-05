package com.journme.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class StaticResourceConfig extends WebMvcConfigurerAdapter {

    @Value("${staticResource.locations:}")
    private String[] locations;

    @Value("${staticResource.cachePeriod:3600}")
    private int cachePeriod;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (locations != null && locations.length > 0) {
            registry.addResourceHandler("/**")
                    .addResourceLocations(locations)
                    .setCachePeriod(cachePeriod);
        }
    }

}
