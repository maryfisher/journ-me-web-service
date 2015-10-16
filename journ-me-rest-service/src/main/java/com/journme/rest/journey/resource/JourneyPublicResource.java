package com.journme.rest.journey.resource;

import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * <h1>Journey endpoints that can be accessed without authentication</h1>
 *
 * @author PHT
 * @version 1.0
 * @since 16.10.2015
 */
@Component
@Singleton
public class JourneyPublicResource {

    @GET
    @Path("/{journeyId}")
    public String getJourney(@PathParam("journeyId") int journeyId) {
        return "{'returned':" + journeyId + "}";
    }

}
