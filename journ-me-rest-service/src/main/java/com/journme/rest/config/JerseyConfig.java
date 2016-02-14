package com.journme.rest.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.Converter;
import com.journme.domain.converter.StateListConverter;
import com.journme.rest.RootResource;
import com.journme.rest.common.errorhandling.JerseyExceptionMapper;
import com.journme.rest.common.filter.AuthTokenFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.SpringHandlerInstantiator;

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

    // Injecting Jackson ObjectMapper that was instantiated via Spring - this ensures the OM has
    // SpringHandlerInstantiator set, which allows OM feature objects (serializers, deserializers,...)
    // to include Spring managed injected fields (e.g. Repositories)
    @Autowired
    private ObjectMapper springObjectMapper;

    @Autowired
    private ApplicationContext springApplicationContext;

    public JerseyConfig() {
        //Jersey REST endpoints
        register(RootResource.class);

        //Jersey filters
        register(AuthTokenFilter.class);

        //Jersey features
        register(MultiPartFeature.class);

        //Jackson features
        register(JacksonFeature.class);
        register(JerseyObjectMapperProvider.class);

        //Jersey exception mappings
        register(JerseyExceptionMapper.class);
        register(JerseyExceptionMapper.JacksonMappingExceptionMapper.class);
        register(JerseyExceptionMapper.JacksonParseExceptionMapper.class);
    }

    @Bean
    public StateListConverter stateListConverter() {
        // Register Jackson Converter as Spring Bean to support injected Spring JPA Repository
        return new StateListConverter();
    }

    @Bean
    public ObjectMapperWrapper objectMapperWrapper() {
        return new ObjectMapperWrapper(springObjectMapper, springApplicationContext);
    }

    // Instruct Jersey to use configured object mapper
    @Provider
    private static class JerseyObjectMapperProvider implements ContextResolver<ObjectMapper> {

        @Autowired
        private ObjectMapperWrapper objectMapperWrapper;

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return objectMapperWrapper.getObjectMapper();
        }
    }

    public static class ObjectMapperWrapper {
        private final ObjectMapper objectMapper;

        public ObjectMapperWrapper(ObjectMapper om, ApplicationContext ac) {
            om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            om.configure(MapperFeature.USE_STATIC_TYPING, true);

            // Exchange Handler Instantiator so that Jackson converters are also handled
            om.setHandlerInstantiator(new EnhancedSpringHandlerInstantiator(ac.getAutowireCapableBeanFactory()));
            this.objectMapper = om;
        }

        public ObjectMapper getObjectMapper() {
            return objectMapper;
        }
    }

    private static class EnhancedSpringHandlerInstantiator extends SpringHandlerInstantiator {

        private final AutowireCapableBeanFactory springBeanFactory;

        public EnhancedSpringHandlerInstantiator(AutowireCapableBeanFactory beanFactory) {
            super(beanFactory);
            this.springBeanFactory = beanFactory;
        }

        @Override
        public Converter<?, ?> converterInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass) {
            return (Converter<?, ?>) this.springBeanFactory.createBean(implClass);
        }

    }

}
