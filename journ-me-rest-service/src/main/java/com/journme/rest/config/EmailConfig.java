package com.journme.rest.config;

import com.journme.rest.user.service.EmailService;
import com.journme.rest.user.service.EmailServiceImpl;
import com.journme.rest.user.service.EmailServiceMock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class EmailConfig {

    @Value("${sendGrid.username:}")
    private String sendGridUsername;

    @Value("${sendGrid.password:}")
    private String sendGridPassword;

    @Bean
    public EmailService emailService() {
        if (StringUtils.isEmpty(sendGridUsername) || StringUtils.isEmpty(sendGridPassword)) {
            return new EmailServiceMock();
        } else {
            return new EmailServiceImpl(sendGridUsername, sendGridPassword);
        }
    }
}
