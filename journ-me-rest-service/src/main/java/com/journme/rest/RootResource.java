package com.journme.rest;

import com.journme.rest.journey.resource.JourneyResource;
import com.journme.rest.user.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("/api")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class RootResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootResource.class);

    @GET
    @Path("/internal/monitoring/healthchecks")
    public String getHealthcheck() {
        LOGGER.debug("Incoming healthcheck call");
        return "{'status':'OK','message':'OK!'}";
    }

    @Path("/journey")
    public Class<JourneyResource> getJourneyResource() {
        return JourneyResource.class;
    }

    @Path("/user")
    public Class<UserResource> getUserResource() {
        return UserResource.class;
    }

}
