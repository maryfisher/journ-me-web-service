package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "aliasImage")
public class AliasImage extends AbstractEntity.AbstractImageEntity {

    public AliasImage() {
    }

    public AliasImage(String name, String mediaType, byte[] image) {
        super(name, mediaType, image);
    }

}
