package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public class Blink extends BaseEntity {

    private Integer format = 0;
    private List<String> images = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
    private Integer index;
    @DBRef(lazy = true)
    private MomentBase moment;
    private Integer ratio = 48;
    @DBRef
    private List<State> states = new ArrayList<>();

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
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

    public void copy(Blink other) {
        //TODO
    }
}
