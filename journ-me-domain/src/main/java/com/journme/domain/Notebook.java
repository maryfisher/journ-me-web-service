package com.journme.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 18.02.2016
 */
@Document(collection = "notebook")
public class Notebook extends AbstractEntity {
    @NotBlank
    private String name;

    private String descript;

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
}
