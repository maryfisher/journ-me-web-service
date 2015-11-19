package com.journme.rest.common.searchfilter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.journme.domain.JourneyBase;

import java.util.Set;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public class JourneySearchFilter extends SearchFilter {

    String text;

    SearchFilter.TextMatch textMatcher;

    JourneyBase.JoinType join;

    SearchFilter.ElementMatch joinMatcher;

    Set<String> categories;

    SearchFilter.CollectionMatch categoriesMatcher;

    Set<String> topics;

    SearchFilter.CollectionMatch topicsMatcher;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextMatch getTextMatcher() {
        return textMatcher;
    }

    public void setTextMatcher(TextMatch textMatcher) {
        this.textMatcher = textMatcher;
    }

    public JourneyBase.JoinType getJoin() {
        return join;
    }

    public void setJoin(JourneyBase.JoinType join) {
        this.join = join;
    }

    public ElementMatch getJoinMatcher() {
        return joinMatcher;
    }

    public void setJoinMatcher(ElementMatch joinMatcher) {
        this.joinMatcher = joinMatcher;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public CollectionMatch getCategoriesMatcher() {
        return categoriesMatcher;
    }

    public void setCategoriesMatcher(CollectionMatch categoriesMatcher) {
        this.categoriesMatcher = categoriesMatcher;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }

    public CollectionMatch getTopicsMatcher() {
        return topicsMatcher;
    }

    public void setTopicsMatcher(CollectionMatch topicsMatcher) {
        this.topicsMatcher = topicsMatcher;
    }
}
