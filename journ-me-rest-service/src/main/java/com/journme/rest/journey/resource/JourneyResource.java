package com.journme.rest.journey.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.AliasDetail;
import com.journme.domain.JourneyBase;
import com.journme.domain.JourneyDetails;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.journey.service.JourneyService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
public class JourneyResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JourneyResource.class);

    @Autowired
    private JourneyService journeyService;

    @Autowired
    private AliasService aliasService;

    @GET
    @Path("/{journeyId}")
    public JourneyDetails retrieveJourney(@NotBlank @PathParam("journeyId") String journeyId) {
        LOGGER.info("Incoming request to retrieve journey {}", journeyId);
        return journeyService.getJourneyDetail(journeyId);
    }

    @POST
    @ProtectedByAuthToken
    public JourneyBase createJourney(
            @NotBlank @QueryParam("aliasId") String aliasId,
            @NotNull @Valid JourneyBase journey) {
        LOGGER.info("Incoming request to create a journey with under alias {}", aliasId);

        AliasBase aliasBase = assertAliasInContext(aliasId);
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);
        journey.setAlias(aliasBase);
        journey.setId(null); //ensures that new Journey is created in the collection
        journey = journeyService.save(journey);

        aliasDetail.getJourneys().add(journey);
        aliasService.save(aliasDetail);

        return journey;
    }

    @POST
    @Path("/{journeyId}")
    @ProtectedByAuthToken
    public JourneyBase updateJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotNull @Valid JourneyBase changedJourney) {
        LOGGER.info("Incoming request to update journey {}", journeyId);

        JourneyDetails existingJourney = journeyService.getJourneyDetail(journeyId);
        assertAliasInContext(existingJourney.getAlias().getId());
        existingJourney.copy(changedJourney);

        existingJourney = journeyService.save(existingJourney);
        return changedJourney.copyAll(existingJourney);
    }

    @POST
    @Path("/{journeyId}/follow/{aliasId}")
    @ProtectedByAuthToken
    public void followJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotBlank @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to follow journey {} with alias {}", journeyId, aliasId);

        AliasBase aliasBase = assertAliasInContext(aliasId);
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);
        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);

        journey.getFollowers().add(aliasBase);
        journey = journeyService.save(journey);

        aliasDetail.getFollowedJourneys().add(journey);
        aliasService.save(aliasDetail);
    }

    @POST
    @Path("/{journeyId}/unfollow/{aliasId}")
    @ProtectedByAuthToken
    public void unfollowJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotBlank @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to unfollow journey {} with alias {}", journeyId, aliasId);

        AliasBase aliasBase = assertAliasInContext(aliasId);
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);
        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);

        journey.getFollowers().remove(aliasBase);
        journey = journeyService.save(journey);

        aliasDetail.getFollowedJourneys().remove(journey);
        aliasService.save(aliasDetail);
    }

    @POST
    @Path("/{journeyId}/link/{linkedJourneyId}")
    @ProtectedByAuthToken
    public void linkJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotBlank @PathParam("linkedJourneyId") String linkedJourneyId) {
        LOGGER.info("Incoming request to link journey {} from journey {}", journeyId, linkedJourneyId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        JourneyDetails linkedJourney = journeyService.getJourneyDetail(linkedJourneyId);

        assertAliasInContext(linkedJourney.getAlias().getId());

        journey.getLinkedFromJourneys().add(linkedJourney);
        journey = journeyService.save(journey);
        linkedJourney.getLinkedToJourneys().add(journey);
        journeyService.save(linkedJourney);
    }

    @POST
    @Path("/{journeyId}/unlink/{linkedJourneyId}")
    @ProtectedByAuthToken
    public JourneyDetails unlinkJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotBlank @PathParam("linkedJourneyId") String linkedJourneyId) {
        LOGGER.info("Incoming request to unlink journey {} from journey {}", journeyId, linkedJourneyId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        JourneyDetails linkedJourney = journeyService.getJourneyDetail(linkedJourneyId);

        assertAliasInContext(linkedJourney.getAlias().getId());

        journey.getLinkedFromJourneys().remove(linkedJourney);
        journey = journeyService.save(journey);
        linkedJourney.getLinkedToJourneys().remove(journey);
        journeyService.save(linkedJourney);

        return journey;
    }

    @POST
    @Path("/{journeyId}/requestJoin/{aliasId}")
    @ProtectedByAuthToken
    public void requestJoinJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotBlank @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to request joining journey {} with alias {}", journeyId, aliasId);

        AliasBase aliasBase = assertAliasInContext(aliasId);
        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);

        journey.getJoinRequests().add(aliasBase);
        journeyService.save(journey);
    }

    @POST
    @Path("/{journeyId}/acceptJoin/{aliasId}")
    @ProtectedByAuthToken
    public void acceptJoinJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotBlank @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to accept joining journey {} with alias {}", journeyId, aliasId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        assertAliasInContext(journey.getAlias().getId());
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);

        journey.getJoinRequests().remove(aliasDetail);
        journey.getJoinedAliases().add(aliasDetail);
        journey = journeyService.save(journey);

        aliasDetail.getJoinedJourneys().add(journey);
        aliasService.save(aliasDetail);
    }

    @POST
    @Path("/{journeyId}/removeJoin/{aliasId}")
    @ProtectedByAuthToken
    public void removeJoinJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotBlank @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to unjoin journey {} with alias {}", journeyId, aliasId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        assertAliasInContext(aliasId, journey.getAlias().getId());
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);

        journey.getJoinedAliases().remove(aliasDetail);
        journey = journeyService.save(journey);

        aliasDetail.getJoinedJourneys().remove(journey);
        aliasService.save(aliasDetail);
    }
}
