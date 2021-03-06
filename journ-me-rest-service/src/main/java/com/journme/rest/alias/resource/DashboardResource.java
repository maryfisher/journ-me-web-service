package com.journme.rest.alias.resource;

import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.common.util.Constants;
import com.journme.rest.contract.alias.DashboardRequest;
import com.journme.rest.contract.alias.DashboardResponse;
import com.journme.rest.moment.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.POST;
import java.util.Date;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 13.11.2015
 */
@Component
@Singleton
public class DashboardResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardResource.class);

    @Autowired
    private FeedbackService feedbackService;

    //TODO: move this to FeedbackResource as GET endpoint supporting pagination, sorting & search filter
    @POST
    @ProtectedByAuthToken
    public DashboardResponse retrieveRecentFeedback(DashboardRequest request) {
        LOGGER.info("Incoming request to retrieve recent dashboard with params {}", request);
        assertAliasInContext(request.getAliasId());
        DashboardResponse response = new DashboardResponse();
        response.setRecentFeedback(
                feedbackService.getFeedbackByDate(request.getAliasId(),
                        new Date(new Date().getTime() - Constants.TWO_WEEKS_MILLISEC))
                        .getContent());
        return response;
    }
}
