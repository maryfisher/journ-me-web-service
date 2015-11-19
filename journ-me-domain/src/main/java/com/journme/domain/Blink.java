package com.journme.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.journme.domain.converter.EmptyArrayConverter;
import com.journme.domain.converter.EntityToIdSerializer;
import com.journme.domain.converter.NullConverter;
import com.journme.domain.converter.StateListConverter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Document(collection = "blink")
public class Blink extends AbstractEntity {

    public enum BlinkFormat {
        RIGHT_IMAGE,
        LEFT_IMAGE,
        DOUBLE_TEXT,
        SINGLE_TEXT,
        VIDEO,
        SINGLE_IMAGE,
        DOUBLE_IMAGE
    }

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private BlinkFormat format = BlinkFormat.RIGHT_IMAGE;

    @DBRef(lazy = true)
    @JsonSerialize(contentUsing = EntityToIdSerializer.class)
    @JsonDeserialize(converter = EmptyArrayConverter.class)
    private List<BlinkImage> images = new ArrayList<>();

    private List<String> texts = new ArrayList<>();

    private Integer index;

    @DBRef(lazy = true)
    @JsonSerialize(using = EntityToIdSerializer.class)
    @JsonDeserialize(converter = NullConverter.class)
    private MomentBase moment;

    private Integer ratio = 48;

    @DBRef(lazy = true)
    @JsonSerialize(contentUsing = EntityToIdSerializer.class)
    @JsonDeserialize(converter = StateListConverter.class)
    private List<State> states = new ArrayList<>();

    public BlinkFormat getFormat() {
        return format;
    }

    public void setFormat(BlinkFormat format) {
        this.format = format;
    }

    public List<BlinkImage> getImages() {
        return images;
    }

    public void setImages(List<BlinkImage> images) {
        this.images = images;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public MomentBase getMoment() {
        return moment;
    }

    public void setMoment(MomentBase moment) {
        this.moment = moment;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public Blink copy(Blink other) {
        if (other.format != null) {
            this.format = other.format;
        }
        // Note: cannot copy over images, else losing them
//        if (other.images != null) {
//            this.images = other.images;
//        }
        if (other.texts != null) {
            this.texts = other.texts;
        }
        if (other.index != null) {
            this.index = other.index;
        }
//        if (other.moment != null) {
//            this.moment = other.moment;
//        }
        if (other.ratio != null) {
            this.ratio = other.ratio;
        }
        if (other.states != null) {
            this.states = other.states;
        }

        return this;
    }
}
