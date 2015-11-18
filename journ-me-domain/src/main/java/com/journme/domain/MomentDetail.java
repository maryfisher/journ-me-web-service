package com.journme.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.journme.domain.converter.EmptyArrayConverter;
import com.journme.domain.converter.EntityToIdSerializer;
import com.journme.domain.converter.StateListConverter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public class MomentDetail extends MomentBase {

    @DBRef(lazy = true)
    @JsonSerialize(contentUsing = EntityToIdSerializer.class)
    @JsonDeserialize(converter = StateListConverter.class)
    private List<State> states = new ArrayList<>();

    @DBRef
    private List<Feedback> feedback = new ArrayList<>();

    @DBRef(lazy = true)
    @JsonSerialize(contentUsing = EntityToIdSerializer.class)
    @JsonDeserialize(converter = EmptyArrayConverter.class)
    private List<Blink> blinks = new ArrayList<>();

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

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
