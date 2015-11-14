package com.journme.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.journme.domain.converter.EntityToIdSerializer;
import com.journme.domain.converter.NullConverter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 22.10.2015
 */
@Document(collection = "moment")
public class MomentBase extends AbstractEntity {

    private Date date;

    private Boolean isPublic;

    private String title;

    @DBRef(lazy = true)
    @JsonSerialize(using = EntityToIdSerializer.class)
    @JsonDeserialize(converter = NullConverter.class)
    private MomentImage thumb;

    @DBRef(lazy = true)
    @JsonSerialize(using = EntityToIdSerializer.class)
    @JsonDeserialize(converter = NullConverter.class)
    private AliasBase alias;

    @DBRef(lazy = true)
    @JsonSerialize(using = EntityToIdSerializer.class)
    @JsonDeserialize(converter = NullConverter.class)
    private JourneyBase journey;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MomentImage getThumb() {
        return thumb;
    }

    public void setThumb(MomentImage thumb) {
        this.thumb = thumb;
    }

    public AliasBase getAlias() {
        return alias;
    }

    public void setAlias(AliasBase alias) {
        this.alias = alias;
    }

    public JourneyBase getJourney() {
        return journey;
    }

    public void setJourney(JourneyBase journey) {
        this.journey = journey;
    }

    public MomentBase copy(MomentBase other) {
        if (other.isPublic != null) {
            this.isPublic = other.isPublic;
        }
        if (other.alias != null) {
            this.alias = other.alias;
        }
        if (other.journey != null) {
            this.journey = other.journey;
        }
        if (other.date != null) {
            this.date = other.date;
        }
        return this;
    }

    public MomentBase copyAll(MomentBase other) {
        super.copyAll(other);
        copy(other);
        return this;
    }
}
