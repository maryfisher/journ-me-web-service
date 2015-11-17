package com.journme.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "topic")
public class Topic extends AbstractEntity {

    @Indexed
    private String tag;

    private Map<String, Double> categoryWeight = new HashMap<>();

    public Topic() {
    }

    public Topic(String tag) {
        this.tag = tag;
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
}
