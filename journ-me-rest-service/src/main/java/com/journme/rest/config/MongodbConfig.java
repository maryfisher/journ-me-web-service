package com.journme.rest.config;

import com.journme.domain.BaseEntity;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

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
        return BaseEntity.class.getPackage().getName();
    }

    @Bean
    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        MappingMongoConverter converter = super.mappingMongoConverter();
        // DefaultMongoTypeMapper(null) suppresses the _class to be persisted
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }
}
