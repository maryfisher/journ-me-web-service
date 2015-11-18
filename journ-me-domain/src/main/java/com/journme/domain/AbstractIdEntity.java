package com.journme.domain;

import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.annotation.Id;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 18.11.2015
 */
@Entity
public abstract class AbstractIdEntity {

    @Id
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
