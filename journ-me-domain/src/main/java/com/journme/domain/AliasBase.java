package com.journme.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.journme.domain.converter.EntityToIdSerializer;
import com.journme.domain.converter.NullDeserializer;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 22.10.2015
 */
@Document(collection = "alias")
public class AliasBase extends AbstractEntity {

    @NotBlank
    private String name;

    @DBRef
    @JsonSerialize(using = EntityToIdSerializer.class)
    @JsonDeserialize(using = NullDeserializer.class)
    private AliasImage image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AliasImage getImage() {
        return image;
    }

    public void setImage(AliasImage image) {
        this.image = image;
    }

    public AliasBase copy(AliasBase other) {
        if (other.name != null) {
            this.name = other.name;
        }
        if (other.image != null) {
            this.image = other.image;
        }
        return this;
    }

    public AliasBase copyAll(AliasBase other) {
        super.copyAll(other);
        copy(other);
        return this;
    }

}
