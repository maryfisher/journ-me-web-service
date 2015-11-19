package com.journme.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.journme.domain.converter.EmptyArrayConverter;
import com.journme.domain.converter.EntityToIdSerializer;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public class MomentDetail extends MomentBase {

    @DBRef
    private List<Feedback> feedback = new ArrayList<>();

    @DBRef(lazy = true)
    @JsonSerialize(contentUsing = EntityToIdSerializer.class)
    @JsonDeserialize(converter = EmptyArrayConverter.class)
    private List<Blink> blinks = new ArrayList<>();

    public List<Feedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }

    public List<Blink> getBlinks() {
        return blinks;
    }

    public void setBlinks(List<Blink> blinks) {
        this.blinks = blinks;
    }
}
