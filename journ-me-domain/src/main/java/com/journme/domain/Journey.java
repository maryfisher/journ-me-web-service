package com.journme.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <h1>Domain class/persistence entity</h1>
 * The class representing a journey in the persistent store.
 *
 * @author PHT
 * @version 1.0
 * @since 20.10.2015
 */
@Document(collection = "Journey")
public class Journey {

    @Id
    private String id;

    private String name;

    private String descript;

    private String alias;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
