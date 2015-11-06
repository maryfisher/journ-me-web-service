package com.journme.rest.common.resource;

import com.journme.domain.AbstractEntity;
import com.journme.rest.contract.ImageClassifier;

import javax.ws.rs.core.Response;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 06.11.2015
 */
public class ImageResource extends AbstractResource {

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
