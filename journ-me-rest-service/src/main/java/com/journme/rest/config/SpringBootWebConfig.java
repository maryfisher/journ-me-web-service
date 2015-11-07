package com.journme.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpringBootWebConfig {

    @Value("${staticResource.locations:file:./lib/journ-me-client-dist/}")
    private String[] locations;

    @Value("${staticResource.cachePeriod:3600}")
    private int cachePeriod;

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                if (locations != null && locations.length > 0) {
                    registry.addResourceHandler("/**")
                            .addResourceLocations(locations)
                            .setCachePeriod(cachePeriod);
                }
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("forward:/index.html");
            }
        };
    }

}
