package com.journme.rest.moment.resource;

import com.journme.domain.Blink;
import com.journme.domain.BlinkImage;
import com.journme.domain.MomentDetail;
import com.journme.domain.MomentImage;
import com.journme.domain.repository.BlinkImageRepository;
import com.journme.domain.repository.BlinkRepository;
import com.journme.domain.repository.MomentImageRepository;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.common.util.Constants;
import com.journme.rest.contract.JournMeExceptionDto;
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
    private MomentImageRepository momentImageRepository;

    @Autowired
    private BlinkRepository blinkRepository;

    private BlinkImageRepository blinkImageRepository;

    @Autowired
    public BlinkResource(BlinkImageRepository blinkImageRepository) {
        super(blinkImageRepository);
        this.blinkImageRepository = blinkImageRepository;
    }

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
            int mediaFileCount = Math.min(imageParts.size(), blink.getFormat().getMediaFileCount());
            for (int i = 0; i < mediaFileCount; i++) {
                FormDataBodyPart imagePart = imageParts.get(i);
                MediaType mediaType = toSupportedMediaType(imagePart);
                byte[] image = toByteArray(imagePart, mediaType);

                if (image != null && image.length > 0) {
                    String imageName = imagePart.getContentDisposition().getFileName();
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

        int changedMediaFileCount = 0;
        int existingMediaFileCount = existingBlink.getImages().size();
        if (imageParts != null && !imageParts.isEmpty()) {
            changedMediaFileCount = Math.min(imageParts.size(), changedBlink.getFormat().getMediaFileCount());
            for (int k = 0; k < changedMediaFileCount; k++) {
                FormDataBodyPart imagePart = imageParts.get(k);
                MediaType mediaType = toSupportedMediaType(imagePart);
                byte[] image = toByteArray(imagePart, mediaType);

                if (image != null && image.length > 0) {
                    String imageName = imagePart.getContentDisposition().getFileName();
                    BlinkImage blinkImage = k < existingBlink.getImages().size() ? existingBlink.getImages().get(k) : null;
                    if (blinkImage != null) {
                        // cannot reuse same image entity in DB for new image binary, because browser cached image according to ID
                        blinkImageRepository.delete(blinkImage.getId());
                    }
                    blinkImage = new BlinkImage(imageName, mediaType.toString(), image);

                    blinkImage = blinkImageRepository.save(blinkImage);
                    if (k < existingBlink.getImages().size()) {
                        existingBlink.getImages().set(k, blinkImage);
                    } else {
                        existingBlink.getImages().add(blinkImage);
                    }
                }
            }
        }

        // When changed blink has a format with fewer media/image files than existing blink, delete the "orphaned" media files
        for (int j = existingMediaFileCount; j > changedMediaFileCount; j--) {
            List<BlinkImage> existingImages = existingBlink.getImages();
            blinkImageRepository.delete(existingImages.get(j - 1).getId());
            existingImages.remove(j - 1);
        }

        existingBlink.copy(changedBlink);
        return blinkRepository.save(existingBlink);
    }
}
