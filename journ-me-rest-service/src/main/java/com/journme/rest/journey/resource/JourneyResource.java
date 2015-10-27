package com.journme.rest.journey.resource;

import com.journme.domain.Alias;
import com.journme.domain.JourneyBase;
import com.journme.domain.JourneyDetails;
import com.journme.rest.alias.repository.AliasRepository;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.contract.JournMeExceptionDto.ExceptionCode;
import com.journme.rest.contract.journey.CreateJourneyRequest;
import com.journme.rest.journey.repository.JourneyBaseRepository;
import com.journme.rest.journey.repository.JourneyDetailsRepository;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(JourneyResource.class);

    @Autowired
    private JourneyDetailsRepository journeyDetailsRepository;

    @Autowired
    private JourneyBaseRepository journeyBaseRepository;

    @Autowired
    private AliasRepository aliasRepository;

    @GET
    @Path("/{journeyId}")
    public JourneyDetails retrieveJourney(@NotBlank @PathParam("journeyId") String journeyId) {
        LOGGER.info("Incoming request to retrieve journey {}", journeyId);
        return journeyDetailsRepository.findOne(journeyId);
    }

    @POST
    @ProtectedByAuthToken
    public JourneyBase createJourney(@NotNull @Valid CreateJourneyRequest createRequest) {
        JourneyBase journey = createRequest.getJourney();
        LOGGER.info("Incoming request to create a journey with name {}", journey.getName());

        Alias alias = aliasRepository.findOne(createRequest.getAliasId());
        if (alias != null) {
            journey.setAlias(alias);
            journey.setId(null); //ensures that new Journey is created in the collection
            return journeyBaseRepository.save(journey);
        } else {
            throw new JournMeException("No alias found for given alias ID " + createRequest.getAliasId(),
                    Response.Status.BAD_REQUEST,
                    ExceptionCode.ALIAS_NONEXISTENT);
        }
    }

    @POST
    @Path("/{journeyId}")
    @ProtectedByAuthToken
    public JourneyBase updateJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotNull @Valid JourneyBase changedJourney) {
        LOGGER.info("Incoming request to update journey {}", journeyId);
        JourneyBase existingJourney = journeyBaseRepository.findOne(journeyId);
        if (existingJourney != null) {
            existingJourney.copy(changedJourney);
            return journeyBaseRepository.save(existingJourney);
        } else {
            throw new JournMeException("No journey found for given journey ID " + journeyId,
                    Response.Status.BAD_REQUEST,
                    ExceptionCode.JOURNEY_NONEXISTENT);
        }
    }

}
