package com.journme.rest.common.resource;

import com.journme.domain.AbstractEntity;
import com.journme.domain.AliasBase;
import com.journme.domain.User;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.AuthTokenFilter;
import com.journme.rest.contract.ImageClassifier;
import com.journme.rest.contract.JournMeExceptionDto;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.springframework.http.MediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 31.10.2015
 */
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public abstract class AbstractResource {

    //Runtime will inject request-scoped proxy into singleton resource class field
    @Context
    private SecurityContext securityContext;

    protected User returnUserFromContext() {
        return ((AuthTokenFilter.UserPrincipal) securityContext.getUserPrincipal()).getUser();
    }

    protected AliasBase assertAliasInContext(String... aliasIds) {
        List<AliasBase> userAliases = ((AuthTokenFilter.UserPrincipal) securityContext.getUserPrincipal()).getUser().getAliases();

        for (String aliasId : aliasIds) {
            for (AliasBase userAlias : userAliases) {
                if (userAlias.equalsId(aliasId)) {
                    return userAlias;
                }
            }
        }

        throw new JournMeException("User does not own alias with any of the alias IDs "
                + String.join(",", Arrays.asList(aliasIds)),
                Response.Status.BAD_REQUEST,
                JournMeExceptionDto.ExceptionCode.ALIAS_NONEXISTENT);
    }

    protected byte[] toByteArray(FormDataBodyPart imagePart) throws IOException {
        return imagePart != null ? IOUtils.toByteArray(imagePart.getValueAs(InputStream.class)) : null;
    }

    protected BufferedImage createResizedCopy(Image originalImage,
                                              int scaledWidth, int scaledHeight,
                                              boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public static abstract class AbstractImageResource extends AbstractResource {

        protected Response sendImageResponse(AbstractEntity.AbstractImageEntity image, ImageClassifier imageClassifier) {
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

}
