package com.journme.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "topic")
public class Topic extends AbstractEntity {

    @TextIndexed
    private String tag;

    @Indexed
    private Integer count = 0;

    // Need to add sparse index to collection manually via MongoDB
    private Map<String, Double> categoryWeight = new HashMap<>();

    public Topic() {
    }

    public Topic(String tag) {
        this.tag = tag;
    }

    @Override
    public String getId() {
        // Jackson uses getter during serialization - make FrontEnd work with topic tag as the ID
        return tag;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Double> getCategoryWeight() {
        return categoryWeight;
    }

    public void setCategoryWeight(Map<String, Double> categoryWeight) {
        this.categoryWeight = categoryWeight;
    }

    public Integer incrementCount() {
        return ++count;
    }
}
