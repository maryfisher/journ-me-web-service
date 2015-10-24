package com.journme.rest.journey.resource;

import com.journme.domain.JourneyBase;
import com.journme.domain.JourneyDetails;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.journey.repository.JourneyBaseRepository;
import com.journme.rest.journey.repository.JourneyDetailsRepository;
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
    JourneyDetailsRepository journeyDetailsRepository;

    @Autowired
    JourneyBaseRepository journeyBaseRepository;

    @GET
    @Path("/{journeyId}")
    public JourneyDetails retrieveJourney(@PathParam("journeyId") String journeyId) {
        JourneyDetails journey = journeyDetailsRepository.findOne(journeyId);
        return journey;
    }

    @POST
    @ProtectedByAuthToken
    public JourneyBase createJourney(JourneyBase newJourney) {
        newJourney.setId(null); //ensures that new Journey is created in the collection
        return journeyBaseRepository.save(newJourney);
    }

    @POST
    @Path("/{journeyId}")
    @ProtectedByAuthToken
    public JourneyBase updateJourney(
            @PathParam("journeyId") String journeyId,
            JourneyBase changedJourney) {
        JourneyBase existingJourney = journeyBaseRepository.findOne(journeyId);
        if (existingJourney != null) {
            existingJourney.copy(changedJourney);
            return journeyBaseRepository.save(existingJourney);
        } else {
            return null;
        }
    }

}
