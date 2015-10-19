package com.journme.rest;

import com.journme.rest.journey.resource.JourneyResource;
import com.journme.rest.journey.service.JourneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    JourneyService journeyService;

    @GET
    @Path("/internal/monitoring/healthchecks")
    public String getHealthcheck() {
        LOGGER.debug("Internal healthcheck call");
        return "{'status':'OK','message':'OK!'}";
    }

    @Path("/journey")
    public Class<JourneyResource> getJourneyPublicResource() {
        return JourneyResource.class;
    }

}
