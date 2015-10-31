package com.journme.rest.moment.resource;

import com.journme.domain.*;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.journey.service.JourneyService;
import com.journme.rest.moment.service.MomentService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class MomentResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MomentResource.class);

    @Autowired
    private MomentService momentService;

    @Autowired
    private JourneyService journeyService;

    @Autowired
    private AliasService aliasService;

    @GET
    @Path("/{momentId}")
    public MomentDetail retrieveMoment(@PathParam("momentId") String momentId) {
        LOGGER.info("Incoming request to retrieve moment {}", momentId);
        return momentService.getMomentDetail(momentId);
    }

    @POST
    @ProtectedByAuthToken
    public MomentBase createMoment(
            @NotBlank @QueryParam("journeyId") String journeyId,
            @NotBlank @QueryParam("aliasId") String aliasId,
            @NotNull @Valid MomentBase moment) {
        LOGGER.info("Incoming request to create a new moment");

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        moment.setJourney(new JourneyBase().clone(journey));

        AliasBase aliasBase = aliasService.getAliasBase(aliasId);
        moment.setAlias(aliasBase);

        moment.setId(null); //ensures that new Moment is created in the collection
        momentService.save(moment);

        journey.getMoments().add(moment);
        journeyService.save(journey);

        return moment;
    }

    @POST
    @Path("/{momentId}")
    @ProtectedByAuthToken
    public MomentBase updateMoment(
            @PathParam("momentId") String momentId,
            MomentBase changedMoment) {
        LOGGER.info("Incoming request to update moment {}", momentId);
        MomentBase existingMoment = momentService.getMomentBase(momentId);
        existingMoment.copy(changedMoment);
        return momentService.save(existingMoment);

    }
}
