package com.journme.rest.journey.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.AliasDetail;
import com.journme.domain.JourneyBase;
import com.journme.domain.JourneyDetails;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.common.AbstractResource;
import com.journme.rest.common.filter.ProtectedByAuthToken;
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
        LOGGER.info("Incoming request to create a journey with name {}", journey.getName());

        AliasBase aliasBase = returnAliasFromContext(aliasId);
        AliasDetail alias = aliasService.getAliasDetail(aliasId);
        journey.setAlias(aliasBase);
        journey.setId(null); //ensures that new Journey is created in the collection
        journeyService.save(journey);

        alias.getJourneys().add(journey);
        aliasService.save(alias);

        return journey;
    }

    @POST
    @Path("/{journeyId}")
    @ProtectedByAuthToken
    public JourneyBase updateJourney(
            @NotBlank @PathParam("journeyId") String journeyId,
            @NotNull @Valid JourneyBase changedJourney) {
        LOGGER.info("Incoming request to update journey {}", journeyId);

        JourneyBase existingJourney = journeyService.getJourneyBase(journeyId);
        returnAliasFromContext(existingJourney.getAlias().getId());
        existingJourney.copy(changedJourney);

        return journeyService.save(existingJourney);
    }

    @POST
    @Path("/{journeyId}/follow/{aliasId}")
    @ProtectedByAuthToken
    public void followJourney(
            @PathParam("journeyId") String journeyId,
            @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to follow journey {} with alias {}", journeyId, aliasId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);

        journey.getFollowers().add(aliasDetail);
        journeyService.save(journey);
        aliasDetail.getFollowedJourneys().add(journey);
    }

    @POST
    @Path("/{journeyId}/unfollow/{aliasId}")
    @ProtectedByAuthToken
    public void unfollowJourney(
            @PathParam("journeyId") String journeyId,
            @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to unfollow journey {} with alias {}", journeyId, aliasId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);

        journey.getFollowers().remove(aliasDetail);
        journeyService.save(journey);
        aliasDetail.getFollowedJourneys().remove(journey);
    }

    @POST
    @Path("/{journeyId}/link/{linkedJourneyId}")
    @ProtectedByAuthToken
    public void linkJourney(
            @PathParam("journeyId") String journeyId,
            @PathParam("linkedJourneyId") String linkedJourneyId) {
        LOGGER.info("Incoming request to link journey {} from journey {}", journeyId, linkedJourneyId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        JourneyDetails linkedJourney = journeyService.getJourneyDetail(linkedJourneyId);

        journey.getLinkedFromJourneys().add(linkedJourney);
        journeyService.save(journey);
        linkedJourney.getLinkedToJourneys().add(journey);
        journeyService.save(linkedJourney);
    }

    @POST
    @Path("/{journeyId}/unlink/{linkedJourneyId}")
    @ProtectedByAuthToken
    public JourneyDetails unlinkJourney(
            @PathParam("journeyId") String journeyId,
            @PathParam("linkedJourneyId") String linkedJourneyId) {
        LOGGER.info("Incoming request to unlink journey {} from journey {}", journeyId, linkedJourneyId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        JourneyDetails linkedJourney = journeyService.getJourneyDetail(linkedJourneyId);

        journey.getLinkedFromJourneys().remove(linkedJourney);
        journeyService.save(journey);
        linkedJourney.getLinkedToJourneys().remove(journey);
        journeyService.save(linkedJourney);

        return journey;
    }

    @POST
    @Path("/{journeyId}/requestJoin/{aliasId}")
    @ProtectedByAuthToken
    public void requestJoinJourney(
            @PathParam("journeyId") String journeyId,
            @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to request joining journey {} with alias {}", journeyId, aliasId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);

        journey.getJoinRequests().add(aliasDetail);
        journeyService.save(journey);
    }

    @POST
    @Path("/{journeyId}/acceptJoin/{aliasId}")
    @ProtectedByAuthToken
    public void acceptJoinJourney(
            @PathParam("journeyId") String journeyId,
            @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to accept joining journey {} with alias {}", journeyId, aliasId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);

        journey.getJoinRequests().remove(aliasDetail);
        journey.getJoinedAliases().add(aliasDetail);
        journeyService.save(journey);
        aliasDetail.getJoinedJourneys().add(journey);
        aliasService.save(aliasDetail);
    }

    @POST
    @Path("/{journeyId}/removeJoin/{aliasId}")
    @ProtectedByAuthToken
    public void removeJoinJourney(
            @PathParam("journeyId") String journeyId,
            @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to unjoin journey {} with alias {}", journeyId, aliasId);

        JourneyDetails journey = journeyService.getJourneyDetail(journeyId);
        AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);

        journey.getJoinedAliases().remove(aliasDetail);
        journeyService.save(journey);
        aliasDetail.getJoinedJourneys().remove(journey);
        aliasService.save(aliasDetail);
    }
}
