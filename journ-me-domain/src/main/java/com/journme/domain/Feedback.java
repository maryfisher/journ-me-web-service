package com.journme.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.journme.domain.converter.EntityToIdSerializer;
import com.journme.domain.converter.NullDeserializer;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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

    @DBRef
    @JsonSerialize(using = EntityToIdSerializer.class)
    @JsonDeserialize(using= NullDeserializer.class)
    private MomentBase moment;

    @NotBlank
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
