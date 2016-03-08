package com.journme.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.journme.domain.converter.EntityToIdSerializer;
import com.journme.domain.converter.NullConverter;
import org.hibernate.validator.constraints.NotBlank;
import org.mongodb.morphia.annotations.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 18.02.2016
 */
@Document(collection = "note")
public class Note extends AbstractEntity {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private NoteType type;

    @NotBlank
    private String text;

    @Indexed
    private String notebookId;

    @DBRef(lazy = true)
    @JsonSerialize(using = EntityToIdSerializer.class)
    @JsonDeserialize(converter = NullConverter.class)
    private NoteImage image;

    public NoteType getType() {
        return type;
    }

    public void setType(NoteType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NoteImage getImage() {
        return image;
    }

    public void setImage(NoteImage image) {
        this.image = image;
    }

    public String getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(String notebookId) {
        this.notebookId = notebookId;
    }

    public Note copy(Note other) {
        if (other.text != null) {
            this.text = other.text;
        }
        if (other.type != null) {
            this.type = other.type;
        }
        if (other.notebookId != null) {
            this.notebookId = other.notebookId;
        }

        return this;
    }

    public enum NoteType {
        IMAGE,
        TEXT
    }
}
