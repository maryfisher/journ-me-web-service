package com.journme.rest;

import com.journme.rest.alias.resource.AliasResource;
import com.journme.rest.common.resource.InternalResource;
import com.journme.rest.home.StatsResource;
import com.journme.rest.journey.resource.JourneyResource;
import com.journme.rest.moment.resource.BlinkResource;
import com.journme.rest.moment.resource.FeedbackResource;
import com.journme.rest.moment.resource.MomentResource;
import com.journme.rest.state.resource.StateResource;
import com.journme.rest.user.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * <h1>Jersey root resource</h1>
 * This class represents the Jersey REST entry resource.
 * Subresources are invoked according to the matched URL path endpoints.
 *
 * @author PHT
 * @version 1.0
 * @since 15.10.2015
 */
@Component
@Singleton
@Path("/") //JerseyConfig already defines "/api" as endpoint start
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class RootResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootResource.class);

    @Path("/internal")
    public Class<InternalResource> getInternalResource() {
        return InternalResource.class;
    }

    @Path("/journey")
    public Class<JourneyResource> getJourneyResource() {
        return JourneyResource.class;
    }

    @Path("/user")
    public Class<UserResource> getUserResource() {
        return UserResource.class;
    }

    @Path("/alias")
    public Class<AliasResource> getAliasResource() {
        return AliasResource.class;
    }

    @Path("/moment")
    public Class<MomentResource> getMomentResource() {
        return MomentResource.class;
    }

    @Path("/state")
    public Class<StateResource> getStateResource() {
        return StateResource.class;
    }

    @Path("/feedback")
    public Class<FeedbackResource> getFeedbackResource() {
        return FeedbackResource.class;
    }

    @Path("/blink")
    public Class<BlinkResource> getBlinkResource() {
        return BlinkResource.class;
    }

    @Path("/stats")
    public Class<StatsResource> getStatResource() {
        return StatsResource.class;
    }

}
