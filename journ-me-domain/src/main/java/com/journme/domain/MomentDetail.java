package com.journme.domain;

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
    private List<State> states = new ArrayList<>();

    @DBRef
    private List<Feedback> feedback = new ArrayList<>();

    @DBRef(lazy = true)
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
