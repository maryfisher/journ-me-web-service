package com.journme.rest.alias.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.AliasDetail;
import com.journme.domain.AliasImage;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.common.AbstractResource;
import com.journme.rest.common.filter.ProtectedByAuthToken;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
public class AliasResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliasResource.class);

    @Autowired
    private AliasService aliasService;

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
            @NotNull @Valid @FormDataParam("alias") AliasBase changedAlias) throws IOException {
        LOGGER.info("Incoming request to update alias {}", aliasId);
        AliasBase existingAlias = assertAliasInContext(aliasId);

        byte[] image = toByteArray(imagePart);
        if (image != null && image.length > 0) {
            String imageName = imagePart.getContentDisposition().getFileName();
            String mimeType = imagePart.getMediaType().toString();
            AliasImage aliasImage = existingAlias.getImage() != null ? existingAlias.getImage() : new AliasImage();
            aliasImage.setName(imageName);
            aliasImage.setMediaType(mimeType);
            aliasImage.setImage(image);

            //TODO: generate small thumbnail from original image

            aliasImage = aliasService.save(aliasImage);
            changedAlias.setImage(aliasImage);
        }

        existingAlias.copy(changedAlias);
        return aliasService.save(changedAlias);
    }

    @GET
    @Path("/image/{aliasImageId}/{imageClassifier}")
    public Response retrieveImage(
            @NotBlank @PathParam("aliasImageId") String aliasImageId,
            @PathParam("imageClassifier") ImageClassifier imageClassifier) {
        LOGGER.info("Incoming request to retrieve alias image {}", aliasImageId);
        AliasImage image = aliasService.getImage(aliasImageId);
        if (image != null) {
            byte[] imageData = (imageClassifier == ImageClassifier.thumbnail && image.getThumbnail() != null) ?
                    image.getThumbnail() : image.getImage();
            return Response.
                    status(Response.Status.OK).
                    entity(imageData).
                    type(image.getMediaType()).
                    build();
        } else {
            return Response.noContent().build();
        }
    }
}
