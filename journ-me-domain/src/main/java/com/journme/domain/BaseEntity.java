package com.journme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
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
public abstract class BaseEntity {

    @Id
    private String id;

    @Version
    @JsonIgnore
    private Long version;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date lastModified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
        return this == other || id != null && other instanceof BaseEntity && id.equals(((BaseEntity) other).getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public BaseEntity clone(BaseEntity other) {
        if (other.id != null) {
            this.id = other.id;
        }
        return this;
    }

    public static abstract class BaseImageEntity extends BaseEntity {

        private byte[] image;
        private byte[] thumbnail;
        private String name;
        private String mediaType;

        public BaseImageEntity() {
        }

        public BaseImageEntity(String name, String mediaType, byte[] image) {
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
