package com.journme.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <h1>Domain class/persistence entity</h1>
 * The class representing a journey base properties in the persistent store.
 *
 * @author PHT
 * @version 1.0
 * @since 20.10.2015
 */
@Document(collection = "journey")
public class JourneyBase extends BaseEntity {

    public enum JoinType {
        ALL,
        SELECTED,
        NONE
    }

    @NotBlank
    private String name;

    private String description;

    @DBRef(lazy = true)
    private Alias alias;

    private JoinType join = JoinType.NONE;

    private Boolean isPublic = Boolean.TRUE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public JoinType getJoin() {
        return join;
    }

    public void setJoin(JoinType join) {
        this.join = join;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void copy(JourneyBase other) {
        if (other.name != null) {
            this.name = other.name;
        }
        if (other.description != null) {
            this.description = other.description;
        }
        if (other.alias != null) {
            this.alias = other.alias;
        }
        if (other.join != null) {
            this.join = other.join;
        }
        if (other.isPublic != null) {
            this.isPublic = other.isPublic;
        }
    }
}
