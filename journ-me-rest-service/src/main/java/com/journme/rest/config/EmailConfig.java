package com.journme.rest.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Value("${sendGrid.username:}")
    private String sendGridUsername;

    @Value("${sendGrid.password:}")
    private String sendGridPassword;

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(sendGridUsername, sendGridPassword);
    }

}
