package com.journme.rest.journey.resource;

import com.journme.domain.Journey;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.journey.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.*;

/**
 * <h1>Journey endpoints</h1>
 *
 * @author PHT
 * @version 1.0
 * @since 16.10.2015
 */
@Component
@Singleton
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class JourneyResource {

    @Autowired
    JourneyRepository journeyRepository;

    @GET
    @Path("/{journeyId}")
    public Journey retrieveJourney(@PathParam("journeyId") String journeyId) {
        return journeyRepository.findOne(journeyId);
    }

    @POST
    @ProtectedByAuthToken
    public String createJourney(Journey newJourney) {
        newJourney.setId(null); //ensures that new Journey is created in the collection
        Journey savedJourney = journeyRepository.save(newJourney);
        return savedJourney.getId();
    }

    @POST
    @Path("/{journeyId}")
    @ProtectedByAuthToken
    public boolean updateJourney(
            @PathParam("journeyId") String journeyId,
            Journey changedJourney) {
        Journey existingJourney = journeyRepository.findOne(journeyId);
        if (existingJourney != null) {
            changedJourney.setId(existingJourney.getId());
            journeyRepository.save(changedJourney);
            return true;
        } else {
            return false;
        }
    }

}
