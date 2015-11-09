package com.journme.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class JourneyBase extends AbstractEntity {

    public enum JoinType {
        ALL,
        SELECTED,
        NONE
    }

    @NotBlank
    private String name;

    private String descript;

    @DBRef
    private AliasBase alias;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private JoinType join = JoinType.NONE;

    private Boolean isPublic = Boolean.TRUE;

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

    public AliasBase getAlias() {
        return alias;
    }

    public void setAlias(AliasBase alias) {
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

    public JourneyBase copy(JourneyBase other) {
        if (other.name != null) {
            this.name = other.name;
        }
        if (other.descript != null) {
            this.descript = other.descript;
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
        return this;
    }

    public JourneyBase copyAll(JourneyBase other) {
        super.copyAll(other);
        copy(other);
        return this;
    }
}
