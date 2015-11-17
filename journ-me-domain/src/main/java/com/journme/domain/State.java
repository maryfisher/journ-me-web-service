package com.journme.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Document(collection = "state")
public class State extends AbstractIdEntity {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private StateType type;

    private String name;

    public StateType getType() {
        return type;
    }

    public void setType(StateType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum StateType {
        FEELING,
        NEUTRAL_FEELING,
        BAD_FEELING,
        NEED,
        OPEN_NEED
    }
}
