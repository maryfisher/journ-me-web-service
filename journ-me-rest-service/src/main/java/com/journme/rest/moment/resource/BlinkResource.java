package com.journme.rest.moment.resource;

import com.journme.domain.Blink;
import com.journme.domain.BlinkImage;
import com.journme.domain.MomentBase;
import com.journme.domain.MomentDetail;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.ImageResource;
import com.journme.rest.contract.ImageClassifier;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.moment.repository.BlinkImageRepository;
import com.journme.rest.moment.repository.BlinkRepository;
import com.journme.rest.moment.service.MomentService;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
public class BlinkResource extends ImageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlinkResource.class);

    @Autowired
    private MomentService momentService;

    @Autowired
    private BlinkRepository blinkRepository;

    @Autowired
    private BlinkImageRepository blinkImageRepository;

    @GET
    @Path("/{blinkId}/")
    public Blink retrieveBlink(@NotBlank @PathParam("blinkId") String blinkId) {
        LOGGER.info("Incoming request to retrieve blink {}", blinkId);
        return blinkRepository.findOne(blinkId);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    @ProtectedByAuthToken
    public Blink createBlink(
            @NotBlank @QueryParam("momentId") String momentId,
            @FormDataParam("file") List<FormDataBodyPart> imageParts,
            @FormDataParam("blink") FormDataBodyPart blinkPart) throws IOException {
        LOGGER.info("Incoming request to create a new blink under moment {}", momentId);

        blinkPart.setMediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE);
        Blink blink = blinkPart.getValueAs(Blink.class);

        MomentDetail moment = momentService.getMomentDetail(momentId);
        assertAliasInContext(moment.getAlias().getId());

        if (imageParts != null && !imageParts.isEmpty()) {
            for (FormDataBodyPart imagePart : imageParts) {
                String imageName = imagePart.getContentDisposition().getFileName();
                String mimeType = imagePart.getMediaType().toString();
                byte[] image = toByteArray(imagePart);

                BlinkImage blinkImage = new BlinkImage(imageName, mimeType, image);

                //TODO: generate small thumbnail from original image

                blinkImage = blinkImageRepository.save(blinkImage);
                blink.getImages().add(blinkImage);
            }
        }

        blink.setIndex(moment.getBlinks().size());
        blink.setMoment(new MomentBase().clone(moment));
        blink.setId(null); //ensures that new Moment is created in the collection
        blink = blinkRepository.save(blink);

        moment.getBlinks().add(blink);
        momentService.save(moment);

        return blink;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    @Path("/{blinkId}")
    @ProtectedByAuthToken
    public Blink updateBlink(
            @NotBlank @PathParam("blinkId") String blinkId,
            @FormDataParam("file") List<FormDataBodyPart> imageParts,
            @FormDataParam("blink") FormDataBodyPart blinkPart) throws IOException {
        LOGGER.info("Incoming request to update blink {}", blinkId);
        blinkPart.setMediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE);
        Blink changedBlink = blinkPart.getValueAs(Blink.class);

        Blink existingBlink = blinkRepository.findOne(blinkId);
        if (existingBlink == null) {
            throw new JournMeException("No Blink found for given ID " + blinkId,
                    Response.Status.BAD_REQUEST,
                    JournMeExceptionDto.ExceptionCode.BLINK_NONEXISTENT);
        }

        //TODO: how should update blink affect already stored images?

        assertAliasInContext(existingBlink.getMoment().getAlias().getId());
        existingBlink.copy(changedBlink);
        return blinkRepository.save(existingBlink);

    }

    @GET
    @Path("/image/{blinkImageId}/{imageClassifier}")
    public Response retrieveImage(
            @NotBlank @PathParam("blinkImageId") String blinkImageId,
            @PathParam("imageClassifier") ImageClassifier imageClassifier) {
        LOGGER.info("Incoming request to retrieve blink image {}", blinkImageId);
        BlinkImage image = blinkImageRepository.findOne(blinkImageId);
        return sendImageResponse(image, imageClassifier);
    }
}
