package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 14.11.2015
 */
@Document(collection = "momentImage")
public class MomentImage extends AbstractEntity.AbstractImageEntity {
    public MomentImage() {
    }

    public MomentImage(String name, String mediaType, byte[] image) {
        super(name, mediaType, image);
    }
}
