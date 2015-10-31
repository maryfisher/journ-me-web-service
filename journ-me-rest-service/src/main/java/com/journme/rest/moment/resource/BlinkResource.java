package com.journme.rest.moment.resource;

import com.journme.domain.Blink;
import com.journme.domain.MomentBase;
import com.journme.domain.MomentDetail;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.moment.repository.BlinkRepository;
import com.journme.rest.moment.service.MomentService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class BlinkResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlinkResource.class);

    @Autowired
    private MomentService momentService;

    @Autowired
    private BlinkRepository blinkRepository;

    @GET
    @Path("/{blinkId}/")
    public Blink retrieveBlink(@PathParam("blinkId") String blinkId) {
        LOGGER.info("Incoming request to retrieve blink {}", blinkId);
        return blinkRepository.findOne(blinkId);
    }

    @POST
    @ProtectedByAuthToken
    public Blink createBlink(
            @NotBlank @QueryParam("momentId") String momentId,
            Blink blink) {
        LOGGER.info("Incoming request to create a new blink");

        MomentDetail moment = momentService.getMomentDetail(momentId);

        blink.setIndex(moment.getBlinks().size());
        blink.setMoment(new MomentBase().clone(moment));
        blink.setId(null); //ensures that new Moment is created in the collection
        blinkRepository.save(blink);

        moment.getBlinks().add(blink);
        momentService.save(moment);

        return blink;
    }

    @POST
    @Path("/{blinkId}")
    @ProtectedByAuthToken
    public Blink updateMoment(
            @PathParam("blinkId") String blinkId,
            Blink changedBlink) {
        LOGGER.info("Incoming request to update blink {}", blinkId);
        Blink existingBlink = blinkRepository.findOne(blinkId);
        if (existingBlink == null) {
            throw new JournMeException("No Blink found for given ID " + blinkId,
                    Response.Status.BAD_REQUEST,
                    JournMeExceptionDto.ExceptionCode.BLINK_NONEXISTENT);
        }
        existingBlink.copy(changedBlink);
        return blinkRepository.save(existingBlink);

    }
}
