package com.journme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.util.Date;

/**
 * <h1>Base class for persistance entities</h1>
 *
 * @author PHT
 * @version 1.0
 * @since 24.10.2015
 */
public abstract class AbstractEntity extends AbstractIdEntity {

    @Version
    @JsonIgnore
    private Long version;

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date lastModified;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean equalsId(String otherId) {
        return id != null && id.equals(otherId);
    }

    @Override
    public boolean equals(Object other) {
        // A new entity not yet saved against the DB cannot equal another entity
        return this == other || id != null && other instanceof AbstractEntity && id.equals(((AbstractEntity) other).getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public AbstractEntity copyAll(AbstractEntity other) {
        if (other.id != null) {
            this.id = other.id;
        }
        if (other.version != null) {
            this.version = other.version;
        }
        if (other.created != null) {
            this.created = other.created;
        }
        if (other.lastModified != null) {
            this.lastModified = other.lastModified;
        }
        return this;
    }

    public static abstract class AbstractImageEntity extends AbstractEntity {

        private byte[] image;
        private byte[] thumbnail;
        private String name;
        private String mediaType;

        public AbstractImageEntity() {
        }

        public AbstractImageEntity(String name, String mediaType, byte[] image) {
            this();
            this.name = name;
            this.mediaType = mediaType;
            this.image = image;
        }

        public byte[] getImage() {
            return image;
        }

        public void setImage(byte[] image) {
            this.image = image;
        }

        public byte[] getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(byte[] thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }
    }
}
