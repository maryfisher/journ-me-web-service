package com.journme.rest.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.journme.domain.BaseEntity;
import com.journme.rest.RootResource;
import com.journme.rest.common.errorhandling.JerseyExceptionMapper;
import com.journme.rest.common.filter.AuthTokenFilter;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.mongodb.DBRef;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;

/**
 * <h1>Jersey configuration</h1> This class contains all Jersey REST related configurations
 *
 * @author PHT
 * @version 1.0
 * @since 15.10.2015
 */
@Configuration
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
    }

    @Provider
    static class CustomObjectMapperProvider implements ContextResolver<ObjectMapper> {

        final ObjectMapper defaultObjectMapper;

        public CustomObjectMapperProvider() {
            defaultObjectMapper = new ObjectMapper();
            defaultObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            final SimpleModule module = new SimpleModule("MongoDBRefSerializer");
            module.addSerializer(LazyLoadingProxy.class, new JsonSerializer<LazyLoadingProxy>() {
                @Override
                public void serialize(LazyLoadingProxy value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                    DBRef dbRef = value.toDBRef();
                    if (dbRef != null) {
                        jgen.writeString(dbRef.getId().toString());
                    } else {
                        //TODO: this is hacky, find a more elegant solution
                        List<? extends BaseEntity> proxyList = (List<? extends BaseEntity>) value.getTarget();
                        jgen.writeStartArray();
                        int size = proxyList.size();
                        for (int i = 0; i < size; i++) {
                            jgen.writeString(proxyList.get(i).getId());
                        }
                        jgen.writeEndArray();
                    }
                }
            });
            defaultObjectMapper.registerModule(module);
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return defaultObjectMapper;
        }
    }

}
