package com.journme.rest;

import com.journme.rest.journey.resource.JourneyProtectedResource;
import com.journme.rest.journey.resource.JourneyPublicResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
public class RootResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootResource.class);

    @GET
    @Path("/internal/monitoring/healthchecks")
    public String getHealthcheck() {
        LOGGER.debug("Internal healthcheck call");
        return "{'status':'OK','message':'OK!'}";
    }

    //TODO: Why can't two subresources have the same path?
    @Path("/journey0")
    public Class<JourneyPublicResource> getJourneyPublicResource() {
        return JourneyPublicResource.class;
    }

    @Path("/journey1")
    public Class<JourneyProtectedResource> getJourneyProtectedResource() {
        return JourneyProtectedResource.class;
    }
}
