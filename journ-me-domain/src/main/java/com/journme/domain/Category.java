package com.journme.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "category")
public class Category extends AbstractIdEntity {

    @Indexed
    private String code;

    private String name;

    private String descript;

    private Boolean isActive = Boolean.TRUE;

    @Override
    public String getId() {
        // Jackson uses getter during serialization - make FrontEnd work with category code as the ID
        return code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
