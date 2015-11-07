package com.journme.rest.moment.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.Feedback;
import com.journme.domain.MomentBase;
import com.journme.domain.MomentDetail;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.moment.repository.FeedbackRepository;
import com.journme.rest.moment.service.MomentService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
public class FeedbackResource extends AbstractResource {

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
            @NotNull @Valid Feedback feedback) {
        LOGGER.info("Incoming request to create a new feedback under moment {} for alias {}", momentId, aliasId);

        AliasBase alias = assertAliasInContext(aliasId);
        MomentDetail moment = momentService.getMomentDetail(momentId);

        feedback.setAlias(alias);
        feedback.setMoment(new MomentBase().clone(moment));
        feedback.setId(null); //ensures that new Feedback is created in the collection
        feedback = feedbackRepository.save(feedback);

        moment.getFeedback().add(feedback);
        momentService.save(moment);

        return feedback;
    }
}
