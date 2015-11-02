package com.journme.rest.moment.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.Feedback;
import com.journme.domain.MomentBase;
import com.journme.domain.MomentDetail;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.moment.repository.FeedbackRepository;
import com.journme.rest.moment.service.MomentService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class FeedbackResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackResource.class);

    @Autowired
    private MomentService momentService;

    @Autowired
    private AliasService aliasService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @POST
    @ProtectedByAuthToken
    public Feedback createFeedback(
            @NotBlank @QueryParam("aliasId") String aliasId,
            @NotBlank @QueryParam("momentId") String momentId,
            Feedback feedback) {
        LOGGER.info("Incoming request to create a new moment");

        MomentDetail moment = momentService.getMomentDetail(momentId);
        AliasBase alias = aliasService.getAliasBase(aliasId);

        feedback.setAlias(alias);
        feedback.setMoment(new MomentBase().clone(moment));
        feedback.setId(null); //ensures that new Moment is created in the collection
        feedbackRepository.save(feedback);

        moment.getFeedback().add(feedback);
        momentService.save(moment);

        return feedback;
    }
}
