package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 18.02.2016
 */
@Document(collection = "aliasImage")
public class NoteImage extends AbstractEntity.AbstractImageEntity {

    public NoteImage() {
    }

    public NoteImage(String name, String mediaType, byte[] image) {
        super(name, mediaType, image);
    }
}
