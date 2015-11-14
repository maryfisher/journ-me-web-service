package com.journme.rest.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.journme.rest.RootResource;
import com.journme.rest.common.errorhandling.JerseyExceptionMapper;
import com.journme.rest.common.filter.AuthTokenFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * <h1>Jersey configuration</h1> This class contains all Jersey REST related configurations
 *
 * @author PHT
 * @version 1.0
 * @since 15.10.2015
 */
@Configuration
@ApplicationPath("/api") //Restrict Jersey to /api endpoints to allow Spring Boot to serve static content
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        //Jersey REST endpoints
        register(RootResource.class);

        //Jersey filters
        register(AuthTokenFilter.class);

        //Jersey features
        register(MultiPartFeature.class);

        //Jackson features
        register(JacksonFeature.class);
        register(CustomObjectMapperProvider.class);

        //Jersey exception mappings
        register(JerseyExceptionMapper.class);
        register(JerseyExceptionMapper.JacksonMappingExceptionMapper.class);
        register(JerseyExceptionMapper.JacksonParseExceptionMapper.class);
    }

    @Provider
    static class CustomObjectMapperProvider implements ContextResolver<ObjectMapper> {

        final ObjectMapper defaultObjectMapper;

        public CustomObjectMapperProvider() {
            defaultObjectMapper = new ObjectMapper();
            defaultObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            defaultObjectMapper.configure(MapperFeature.USE_STATIC_TYPING, true);
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return defaultObjectMapper;
        }
    }

}
