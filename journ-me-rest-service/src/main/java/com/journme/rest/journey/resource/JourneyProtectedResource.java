package com.journme.rest.journey.resource;

import com.journme.rest.common.filter.ProtectedByAuthToken;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * <h1>Journey endpoints that are protected by authentication</h1>
 *
 * @author PHT
 * @version 1.0
 * @since 16.10.2015
 */
@Component
@Singleton
@ProtectedByAuthToken
public class JourneyProtectedResource {

    @POST
    @Path("/{journeyId}")
    public String updateJourney(@PathParam("journeyId") int journeyId) {
        return "{'updated':" + journeyId + "}";
    }

    @POST
    public String createJourney() {
        return "{'created':123}";
    }

}
