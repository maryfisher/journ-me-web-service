package com.journme.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;
import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

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

    @NotBlank
    @TextIndexed
    private String name;

    @TextIndexed
    private String descript;

    @DBRef(lazy = true)
    private AliasBase alias;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private JoinType join = JoinType.NONE;

    private Boolean isPublic = Boolean.TRUE;

    @NotNull
    @Valid
    private Set<CategoryWeight> categoryWeights = new HashSet<>();

    @Indexed
    @NotNull
    private Set<String> topics = new HashSet<>();

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

    public Set<CategoryWeight> getCategoryWeights() {
        return categoryWeights;
    }

    public void setCategoryWeights(Set<CategoryWeight> categoryWeights) {
        this.categoryWeights = categoryWeights;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
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
        if (other.categoryWeights != null) {
            this.categoryWeights = other.categoryWeights;
        }
        if (other.topics != null) {
            this.topics = other.topics;
        }
        return this;
    }

    public JourneyBase copyAll(JourneyBase other) {
        super.copyAll(other);
        copy(other);
        return this;
    }

    public enum JoinType {
        ALL,
        SELECTED,
        NONE
    }

    @Entity
    public static class CategoryWeight {
        @NotBlank
        private String category;

        @NotNull
        @Min(0)
        @Max(100)
        private Double weight;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || category != null && other instanceof CategoryWeight && category.equals(((CategoryWeight) other).category);
        }

        @Override
        public int hashCode() {
            return category != null ? category.hashCode() : 0;
        }

        @Override
        public String toString() {
            return category;
        }
    }
}
