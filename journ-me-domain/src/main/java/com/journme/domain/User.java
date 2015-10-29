package com.journme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Domain class/persistence entity</h1>
 * The class representing a user in the persistent store
 *
 * @author mary_fisher
 * @version 1.0
 * @since 22.10.2015
 */
@Document(collection = "user")
public class User extends BaseEntity {

    @Indexed(unique = true)
    private String email;

    @DBRef(lazy = true)
    private List<Alias> aliases = new ArrayList<Alias>();

    @DBRef(lazy = true)
    private Alias currentAlias;

    @JsonIgnore
    private String passwordHash;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(List<Alias> aliases) {
        this.aliases = aliases;
    }

    public Alias getCurrentAlias() {
        return currentAlias;
    }

    public void setCurrentAlias(Alias currentAlias) {
        this.currentAlias = currentAlias;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
