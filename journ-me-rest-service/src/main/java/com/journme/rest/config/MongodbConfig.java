package com.journme.rest.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * <h1>MongoDB configuration</h1>
 * This class contains all MongoDB related configurations
 *
 * @author PHT
 * @version 1.0
 * @since 20.10.2015
 */
@Configuration
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

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(mongodbHost, mongodbPort);
    }
}
