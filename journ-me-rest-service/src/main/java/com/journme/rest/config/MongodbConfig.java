package com.journme.rest.config;

import com.journme.domain.AbstractEntity;
import com.journme.domain.AliasBase;
import com.journme.domain.AliasDetail;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

/**
 * <h1>MongoDB configuration</h1>
 * This class contains all MongoDB related configurations
 *
 * @author PHT
 * @version 1.0
 * @since 20.10.2015
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = {com.journme.rest.Application.class})
public class MongodbConfig extends AbstractMongoConfiguration {

    @Value("${mongodb.host:localhost}")
    private String mongodbHost;

    @Value("${mongodb.port:27017}")
    private int mongodbPort;

    @Value("${mongodb.name:journmedb}")
    private String mongodbName;

    @Override
    protected String getDatabaseName() {
        return mongodbName;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(mongodbHost, mongodbPort);
    }

    @Override
    protected String getMappingBasePackage() {
        return AbstractEntity.class.getPackage().getName();
    }

    @Bean
    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        MappingMongoConverter converter = super.mappingMongoConverter();
        // DefaultMongoTypeMapper(null) suppresses the _class to be persisted
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    @Bean
    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(Collections.singletonList(new AliasConverter()));
    }

    private class AliasConverter implements Converter<AliasDetail, AliasBase> {

        public AliasBase convert(AliasDetail source) {
            AliasBase dest = new AliasBase();
            dest.clone(source);
            return dest;
        }
    }
}
