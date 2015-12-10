package com.journme.rest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <h1>Spring Boot Application main configuration</h1>
 * This class entails the main configuration for Spring Boot.
 * <p>
 * The Application is designed to run as a JAR with an embedded Tomcat server.
 *
 * @author PHT
 * @version 1.0
 * @since 15.10.2015
 */
@SpringBootApplication
@EnableAsync
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

}