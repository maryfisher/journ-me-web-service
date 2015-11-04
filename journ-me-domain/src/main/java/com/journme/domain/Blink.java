package com.journme.domain;

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

    private Integer format = 0;

    @DBRef(lazy = true)
    private List<BlinkImage> images = new ArrayList<>();

    private List<String> texts = new ArrayList<>();

    private Integer index;

    @DBRef(lazy = true)
    private MomentBase moment;

    private Integer ratio = 48;

    //TODO: render states from DB into HTML as Angular module configuration
    @DBRef
    private List<State> states = new ArrayList<>();

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
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

    public void copy(Blink other) {
        //TODO
    }
}
