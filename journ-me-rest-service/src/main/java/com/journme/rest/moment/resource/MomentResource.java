package com.journme.rest.moment.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.JourneyDetails;
import com.journme.domain.MomentBase;
import com.journme.domain.MomentDetail;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.journey.service.JourneyService;
import com.journme.rest.moment.service.MomentService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
public class MomentResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MomentResource.class);

    @Autowired
    private MomentService momentService;

    @Autowired
    private JourneyService journeyService;

    @GET
    @Path("/{momentId}")
    public MomentDetail retrieveMoment(@NotBlank @PathParam("momentId") String momentId) {
        LOGGER.info("Incoming request to retrieve moment {}", momentId);
        return momentService.getMomentDetail(momentId);
    }

    @POST
    @ProtectedByAuthToken
    public MomentBase createMoment(
            @NotBlank @QueryParam("journeyId") String journeyId,
            @NotBlank @QueryParam("aliasId") String aliasId,
            @NotNull @Valid MomentBase moment) {
        LOGGER.info("Incoming request to create a new moment under journy {} for alias {}", journeyId, aliasId);

        AliasBase aliasBase = assertAliasInContext(aliasId);
        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        if (journey.getAlias().equals(aliasBase) || journey.getJoinedAliases().contains(aliasBase)) {
            moment.setJourney(journey);
            moment.setAlias(aliasBase);

            moment.setId(null); //ensures that new Moment is created in the collection
            moment = momentService.save(moment);

            journey.getMoments().add(moment);
            journeyService.save(journey);

            return moment;
        } else {
            throw new JournMeException("Is not journey owner nor among joined alias IDs " + aliasId,
                    Response.Status.BAD_REQUEST,
                    JournMeExceptionDto.ExceptionCode.ALIAS_NONEXISTENT);
        }
    }

    @POST
    @Path("/{momentId}")
    @ProtectedByAuthToken
    public MomentBase updateMoment(
            @NotBlank @PathParam("momentId") String momentId,
            @NotNull @Valid MomentBase changedMoment) {
        LOGGER.info("Incoming request to update moment {}", momentId);
        MomentDetail existingMoment = momentService.getMomentDetail(momentId);
        assertAliasInContext(existingMoment.getAlias().getId());
        existingMoment.copy(changedMoment);
        existingMoment = momentService.save(existingMoment);
        return changedMoment.copyAll(existingMoment);

    }
}
