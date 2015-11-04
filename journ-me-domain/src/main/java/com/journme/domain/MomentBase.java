package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 22.10.2015
 */
@Document(collection = "moment")
public class MomentBase extends AbstractEntity {

    private Boolean isPublic;

    @DBRef(lazy = true)
    private AliasBase alias;

    @DBRef(lazy = true)
    private JourneyBase journey;

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public AliasBase getAlias() {
        return alias;
    }

    public void setAlias(AliasBase alias) {
        this.alias = alias;
    }

    public JourneyBase getJourney() {
        return journey;
    }

    public void setJourney(JourneyBase journey) {
        this.journey = journey;
    }

    public void copy(MomentBase other) {
        if (other.isPublic != null) {
            this.isPublic = other.isPublic;
        }
    }

    @Override
    public MomentBase clone(AbstractEntity other) {
        super.clone(other);
        copy((MomentBase) other);
        return this;
    }
}
