package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 22.10.2015
 */
@Document(collection = "alias")
public class AliasBase extends BaseEntity {

    private String name;

    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void copy(AliasBase other) {
        if (other.name != null) {
            this.name = other.name;
        }
        if (other.image != null) {
            this.image = other.image;
        }
    }

    @Override
    public AliasBase clone(BaseEntity other) {
        super.clone(other);
        copy((AliasBase) other);
        return this;
    }

}
