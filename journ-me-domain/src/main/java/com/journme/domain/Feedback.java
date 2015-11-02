package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public class Feedback extends BaseEntity {

    @DBRef
    private AliasBase alias;

    @DBRef(lazy = true)
    private MomentBase moment;

    private String body;

    @DBRef
    private List<State> states;

    public AliasBase getAlias() {
        return alias;
    }

    public void setAlias(AliasBase alias) {
        this.alias = alias;
    }

    public MomentBase getMoment() {
        return moment;
    }

    public void setMoment(MomentBase moment) {
        this.moment = moment;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
}
