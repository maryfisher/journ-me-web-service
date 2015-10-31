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

    @Indexed
    private String email;

    @DBRef
    private List<AliasBase> aliases = new ArrayList<>();

    @DBRef
    private AliasBase currentAlias;

    @JsonIgnore
    private String passwordHash;

    @JsonIgnore
    private byte[] passwordHashSalt;

    @JsonIgnore
    private int passwordHashIterations;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AliasBase> getAliases() {
        return aliases;
    }

    public void setAliases(List<AliasBase> aliases) {
        this.aliases = aliases;
    }

    public AliasBase getCurrentAlias() {
        return currentAlias;
    }

    public void setCurrentAlias(AliasBase currentAlias) {
        this.currentAlias = currentAlias;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordHashSalt() {
        return passwordHashSalt;
    }

    public void setPasswordHashSalt(byte[] passwordHashSalt) {
        this.passwordHashSalt = passwordHashSalt;
    }

    public int getPasswordHashIterations() {
        return passwordHashIterations;
    }

    public void setPasswordHashIterations(int passwordHashIterations) {
        this.passwordHashIterations = passwordHashIterations;
    }
}
