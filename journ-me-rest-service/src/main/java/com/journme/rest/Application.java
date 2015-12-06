package com.journme.rest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}