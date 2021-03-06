package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blinkImage")
public class BlinkImage extends AbstractEntity.AbstractImageEntity {

    public BlinkImage() {
    }

    public BlinkImage(String name, String mediaType, byte[] image) {
        super(name, mediaType, image);
    }

}
