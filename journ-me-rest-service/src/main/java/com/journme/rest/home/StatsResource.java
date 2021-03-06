package com.journme.rest.home;

import com.journme.domain.repository.FeedbackRepository;
import com.journme.rest.contract.stats.StatsResponse;
import com.journme.rest.journey.service.JourneyService;
import com.journme.rest.moment.service.MomentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 10.11.2015
 */
@Component
@Singleton
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class StatsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsResource.class);

    @Autowired
    private MomentService momentService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private JourneyService journeyService;

    @GET
    public StatsResponse retrieveStats() {
        LOGGER.info("Incoming request to retrieve stats");
        StatsResponse response = new StatsResponse();
        response.setAllJourneys(journeyService.countAll());
        response.setAllMoments(momentService.countAll());
        response.setAllFeedbacks(feedbackRepository.count());

        return response;
    }
}
