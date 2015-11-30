package com.journme.rest.common.resource;

import com.journme.domain.AbstractEntity;
import com.journme.domain.AliasBase;
import com.journme.domain.User;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.AuthTokenFilter;
import com.journme.rest.common.util.Constants;
import com.journme.rest.contract.ImageClassifier;
import com.journme.rest.contract.JournMeExceptionDto.ExceptionCode;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

        throw new JournMeException("User does not own alias with any of the alias IDs " + String.join(",", Arrays.asList(aliasIds)),
                Response.Status.BAD_REQUEST,
                ExceptionCode.ALIAS_NONEXISTENT);
    }

    public static abstract class AbstractImageResource extends AbstractResource {

        protected Response sendImageResponse(AbstractEntity.AbstractImageEntity image, ImageClassifier imageClassifier) {
            if (image != null) {
                byte[] imageData = image.getImage() != null ? image.getImage() : image.getThumbnail();
                byte[] thumbnailData = image.getThumbnail() != null ? image.getThumbnail() : image.getImage();

                return Response.
                        status(Response.Status.OK).
                        entity(imageClassifier == ImageClassifier.thumbnail ? thumbnailData : imageData).
                        type(image.getMediaType()).
                        build();
            } else {
                return Response.noContent().build();
            }
        }

        protected byte[] toByteArray(FormDataBodyPart imagePart, MediaType mediaType) {
            try {
                return (mediaType != null && imagePart != null) ? IOUtils.toByteArray(imagePart.getValueAs(InputStream.class)) : null;
            } catch (IOException ex) {
                throw new JournMeException("Exception during reading of image byte data",
                        Response.Status.BAD_REQUEST,
                        ExceptionCode.FILE_TYPE_CORRUPTED_INVALID,
                        ex);
            }
        }

        protected byte[] createResizedCopy(byte[] image, MediaType mediaType, int scaledWidth, int scaledHeight, boolean preserveRatio, boolean preserveAlpha) {
            try {
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image));
                if (preserveRatio) {
                    int originalWidth = originalImage.getWidth();
                    int originalHeight = originalImage.getHeight();
                    double originalAspectRatio = (double) originalWidth / originalHeight;
                    double scaledAspectRatio = (double) scaledWidth / scaledHeight;

                    if (originalAspectRatio > scaledAspectRatio) {
                        double multiplier = (double) scaledWidth / originalWidth;
                        scaledHeight = (int) (originalHeight * multiplier);
                    } else if (originalAspectRatio < scaledAspectRatio) {
                        double multiplier = (double) scaledHeight / originalHeight;
                        scaledWidth = (int) (originalWidth * multiplier);
                    }
                }

                int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
                BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, imageType);
                Graphics2D g = scaledImage.createGraphics();
                if (preserveAlpha) {
                    g.setComposite(AlphaComposite.Src);
                }
                g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
                g.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(scaledImage, mediaType.getSubtype(), baos);
                baos.flush();
                return baos.toByteArray();
            } catch (Exception ex) {
                throw new JournMeException("Exception during image resizing",
                        Response.Status.BAD_REQUEST,
                        ExceptionCode.FILE_TYPE_CORRUPTED_INVALID,
                        ex);
            }
        }

        protected MediaType toSupportedMediaType(FormDataBodyPart imagePart) {
            if (imagePart == null || StringUtils.isEmpty(imagePart.getMediaType())) {
                return null;
            }
            String mediaTypeStr = imagePart.getMediaType().toString();
            try {
                MediaType mediaType = MediaType.parseMediaType(mediaTypeStr);
                if (Constants.SUPPORTED_MEDIA_TYPE.contains(mediaType)) {
                    return mediaType;
                } else if (MediaType.TEXT_PLAIN.equals(mediaType)) {
                    // "null" was send in by ng-file-upload for unchanged/invalid file
                    return null;
                } else {
                    throw new JournMeException("Not among supported media types " + mediaTypeStr,
                            Response.Status.BAD_REQUEST,
                            ExceptionCode.FILE_TYPE_CORRUPTED_INVALID);
                }
            } catch (InvalidMediaTypeException ex) {
                throw new JournMeException("Cannot parse media type string " + mediaTypeStr,
                        Response.Status.BAD_REQUEST,
                        ExceptionCode.FILE_TYPE_CORRUPTED_INVALID,
                        ex);
            }
        }
    }

}
