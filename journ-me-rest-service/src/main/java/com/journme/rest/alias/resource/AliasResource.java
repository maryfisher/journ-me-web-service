package com.journme.rest.alias.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.AliasDetail;
import com.journme.domain.AliasImage;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.common.util.Constants;
import com.journme.rest.contract.ImageClassifier;
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

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
public class AliasResource extends AbstractResource.AbstractImageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliasResource.class);

    @Autowired
    private AliasService aliasService;

    @Path("/dashboard")
    public Class<DashboardResource> getStatResource() {
        return DashboardResource.class;
    }

    @GET
    @Path("/{aliasId}")
    public AliasDetail retrieveAlias(@NotBlank @PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to retrieve alias {}", aliasId);
        return aliasService.getAliasDetail(aliasId);
    }

    @POST
    @Path("/{aliasId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    @ProtectedByAuthToken
    public AliasBase updateAlias(
            @NotBlank @PathParam("aliasId") String aliasId,
            @FormDataParam("file") FormDataBodyPart imagePart,
            @FormDataParam("alias") FormDataBodyPart aliasPart) {
        LOGGER.info("Incoming request to update alias {}", aliasId);
        AliasBase aliasBaseInContext = assertAliasInContext(aliasId);

        aliasPart.setMediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE);
        AliasBase changedAlias = aliasPart.getValueAs(AliasBase.class);

        AliasDetail existingAlias = aliasService.getAliasDetail(aliasId);
        MediaType mediaType = toSupportedMediaType(imagePart);
        byte[] image = toByteArray(imagePart, mediaType);
        if (image != null && image.length > 0) {
            String imageName = imagePart.getContentDisposition().getFileName();

            AliasImage aliasImage = existingAlias.getImage();
            if (aliasImage != null) {
                // cannot reuse same image entity in DB for new image binary, because browser cached image according to ID
                aliasService.deleteImage(aliasImage.getId());
            }
            aliasImage = new AliasImage(imageName, mediaType.toString(), image);
            byte[] thumbnail = createResizedCopy(image, mediaType, Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE, true, true);
            aliasImage.setThumbnail(thumbnail);

            aliasImage = aliasService.save(aliasImage);
            changedAlias.setImage(aliasImage);
        }

        existingAlias.copy(changedAlias);
        existingAlias = aliasService.save(existingAlias);
        return aliasBaseInContext.copyAll(existingAlias);
    }

    @GET
    @Path("/image/{aliasImageId}/{imageClassifier:\\w*}")
    public Response retrieveImageWithClassifier(
            @NotBlank @PathParam("aliasImageId") String aliasImageId,
            @DefaultValue("original") @PathParam("imageClassifier") ImageClassifier imageClassifier) {
        LOGGER.info("Incoming request to retrieve {} alias image {}", imageClassifier, aliasImageId);
        AliasImage image = aliasService.getImage(aliasImageId);
        return sendImageResponse(image, imageClassifier);
    }

    @GET
    @Path("/image/{aliasImageId}")
    public Response retrieveImage(@NotBlank @PathParam("aliasImageId") String aliasImageId) {
        return retrieveImageWithClassifier(aliasImageId, ImageClassifier.original);
    }
}
