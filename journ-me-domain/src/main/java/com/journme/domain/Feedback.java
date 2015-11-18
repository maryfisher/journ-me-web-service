package com.journme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.journme.domain.converter.EntityToIdSerializer;
import com.journme.domain.converter.NullConverter;
import com.journme.domain.converter.StateListConverter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Document(collection = "feedback")
public class Feedback extends AbstractEntity {

    @DBRef
    private AliasBase alias;

    @JsonIgnore
    @DBRef(lazy = true)
    private AliasBase momentAlias;

    @DBRef(lazy = true)
    @JsonSerialize(using = EntityToIdSerializer.class)
    @JsonDeserialize(converter = NullConverter.class)
    private JourneyBase journey;

    @DBRef(lazy = true)
    @JsonSerialize(using = EntityToIdSerializer.class)
    @JsonDeserialize(converter = NullConverter.class)
    private MomentBase moment;

    @NotBlank
    private String body;

    @DBRef(lazy = true)
    @JsonSerialize(contentUsing = EntityToIdSerializer.class)
    @JsonDeserialize(converter = StateListConverter.class)
    private List<State> states = new ArrayList<>();

    public AliasBase getAlias() {
        return alias;
    }

    public void setAlias(AliasBase alias) {
        this.alias = alias;
    }

    public AliasBase getMomentAlias() {
        return momentAlias;
    }

    public void setMomentAlias(AliasBase momentAlias) {
        this.momentAlias = momentAlias;
    }

    public JourneyBase getJourney() {
        return journey;
    }

    public void setJourney(JourneyBase journey) {
        this.journey = journey;
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
