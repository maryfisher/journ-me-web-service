package com.journme.rest.moment.resource;

import com.journme.domain.Blink;
import com.journme.domain.BlinkImage;
import com.journme.domain.MomentDetail;
import com.journme.domain.MomentImage;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.common.util.Constants;
import com.journme.rest.contract.ImageClassifier;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.moment.repository.BlinkImageRepository;
import com.journme.rest.moment.repository.BlinkRepository;
import com.journme.rest.moment.repository.MomentImageRepository;
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
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
public class BlinkResource extends AbstractResource.AbstractImageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlinkResource.class);

    @Autowired
    private MomentService momentService;

    @Autowired
    private BlinkRepository blinkRepository;

    @Autowired
    private BlinkImageRepository blinkImageRepository;

    @Autowired
    private MomentImageRepository momentImageRepository;

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
            @FormDataParam("blink") FormDataBodyPart blinkPart) {
        LOGGER.info("Incoming request to create a new blink under moment {}", momentId);

        blinkPart.setMediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE);
        Blink blink = blinkPart.getValueAs(Blink.class);

        MomentDetail moment = momentService.getMomentDetail(momentId);
        assertAliasInContext(moment.getAlias().getId());

        if (imageParts != null && !imageParts.isEmpty()) {
            for (FormDataBodyPart imagePart : imageParts) {
                String imageName = imagePart.getContentDisposition().getFileName();
                MediaType mediaType = toSupportedMediaType(imagePart.getMediaType().toString());

                byte[] image = toByteArray(imagePart);

                if (moment.getThumb() == null) {
                    byte[] thumbnail = createResizedCopy(image, mediaType, Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE, true, true);
                    MomentImage momentImage = new MomentImage(imageName, mediaType.toString(), null);
                    momentImage.setThumbnail(thumbnail);
                    momentImage = momentImageRepository.save(momentImage);
                    moment.setThumb(momentImage);
                }

                BlinkImage blinkImage = new BlinkImage(imageName, mediaType.toString(), image);
                blinkImage = blinkImageRepository.save(blinkImage);
                blink.getImages().add(blinkImage);
            }
        }

        blink.setIndex(moment.getBlinks().size());
        blink.setMoment(moment);
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
            @FormDataParam("blink") FormDataBodyPart blinkPart) {
        LOGGER.info("Incoming request to update blink {}", blinkId);
        blinkPart.setMediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE);
        Blink changedBlink = blinkPart.getValueAs(Blink.class);

        Blink existingBlink = blinkRepository.findOne(blinkId);
        if (existingBlink == null) {
            throw new JournMeException("No Blink found for given ID " + blinkId,
                    Response.Status.BAD_REQUEST,
                    JournMeExceptionDto.ExceptionCode.BLINK_NONEXISTENT);
        }
        assertAliasInContext(existingBlink.getMoment().getAlias().getId());

        int indexCount = 0;
        if (imageParts != null && !imageParts.isEmpty()) {
            for (FormDataBodyPart imagePart : imageParts) {
                if (imagePart != null) {
                    String imageName = imagePart.getContentDisposition().getFileName();
                    MediaType mediaType = toSupportedMediaType(imagePart.getMediaType().toString());
                    byte[] image = toByteArray(imagePart);

                    BlinkImage blinkImage = existingBlink.getImages().size() > indexCount ? existingBlink.getImages().get(indexCount) : null;
                    if (blinkImage != null) {
                        // cannot reuse same image entity in DB for new image binary, because browser cached image according to ID
                        blinkRepository.delete(blinkImage.getId());
                    }
                    blinkImage = new BlinkImage(imageName, mediaType.toString(), image);

                    blinkImage = blinkImageRepository.save(blinkImage);
                    if (indexCount >= existingBlink.getImages().size()) {
                        existingBlink.getImages().add(blinkImage);
                    } else {
                        existingBlink.getImages().set(indexCount, blinkImage);
                    }
                }
                indexCount++;
            }
        }
        //If changing from 2 image format to 1 image format, must avoid keeping the orphaned image in the DB
        if (existingBlink.getFormat() == Blink.BlinkFormat.DOUBLE_IMAGE && changedBlink.getFormat() != Blink.BlinkFormat.DOUBLE_IMAGE) {
            List<BlinkImage> existingImages = existingBlink.getImages();
            blinkImageRepository.delete(existingImages.get(1).getId());
            existingImages.remove(1);
        }

        existingBlink.copy(changedBlink);
        return blinkRepository.save(existingBlink);

    }

    @GET
    @Path("/image/{blinkImageId}/{imageClassifier:\\w*}")
    public Response retrieveImageWithClassifier(
            @NotBlank @PathParam("blinkImageId") String blinkImageId,
            @DefaultValue("original") @PathParam("imageClassifier") ImageClassifier imageClassifier) {
        LOGGER.info("Incoming request to retrieve {} blink image {}", imageClassifier, blinkImageId);
        BlinkImage image = blinkImageRepository.findOne(blinkImageId);
        return sendImageResponse(image, imageClassifier);
    }

    @GET
    @Path("/image/{blinkImageId}")
    public Response retrieveImage(@NotBlank @PathParam("blinkImageId") String blinkImageId) {
        return retrieveImageWithClassifier(blinkImageId, ImageClassifier.original);
    }
}
