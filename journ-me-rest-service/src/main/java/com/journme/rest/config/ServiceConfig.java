package com.journme.rest.config;

import com.google.common.eventbus.EventBus;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.journey.service.CategoryTopicService;
import com.journme.rest.journey.service.JourneyService;
import com.journme.rest.moment.service.FeedbackService;
import com.journme.rest.moment.service.MomentService;
import com.journme.rest.user.service.NotificationService;
import com.journme.rest.user.service.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 31.10.2015
 */
@Configuration
public class ServiceConfig {

    @Autowired
    private EventBus eventBus;

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

    @Bean
    public CategoryTopicService categoryTopicService() {
        return new CategoryTopicService();
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    @Bean
    public NotificationService notificationService() {
        NotificationService notificationService = new NotificationServiceImpl();
        eventBus.register(notificationService);
        return notificationService;
    }
}
