package com.journme.rest.config;

import com.journme.rest.alias.service.AliasService;
import com.journme.rest.journey.service.JourneyService;
import com.journme.rest.moment.service.FeedbackService;
import com.journme.rest.moment.service.MomentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 31.10.2015
 */
@Configuration
public class ServiceConfig {

    @Bean
    public AliasService aliasService() {
        return new AliasService();
    }

    @Bean
    public JourneyService journeyService() {
        return new JourneyService();
    }

    @Bean
    public MomentService momentService() {
        return new MomentService();
    }

    @Bean
    public FeedbackService feedbackService() {
        return new FeedbackService();
    }
}
