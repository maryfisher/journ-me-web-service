package com.journme.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 22.10.2015
 */
@Document(collection = "alias")
public abstract class AbstractAlias extends AbstractEntity {

    @NotBlank
    private String name;

    @DBRef(lazy = true)
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

    public void copy(AbstractAlias other) {
        if (other.name != null) {
            this.name = other.name;
        }
        if (other.image != null) {
            this.image = other.image;
        }
    }

    @Override
    public AbstractAlias clone(AbstractEntity other) {
        super.clone(other);
        copy((AbstractAlias) other);
        return this;
    }

}