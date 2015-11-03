package com.journme.rest.contract.user;

import com.journme.domain.AliasBase;
import com.journme.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>POJO for REST serialisation</h1>
 * This class represents the login response REST data transfer object
 *
 * @author PHT
 * @version 1.0
 * @since 26.10.2015
 */
public class LoginResponse {

    public static final String AUTH_TOKEN_HEADER_KEY = "x-jm-auth-token";

    private String email;

    private List<AliasBase> aliases = new ArrayList<>();

    private AliasBase currentAlias;

    private Map<String, Object> headerItems;

    public LoginResponse(User user) {
        headerItems = new HashMap<>();
        email = user.getEmail();
        aliases = user.getAliases();
        currentAlias = user.getCurrentAlias();
    }

    public Map<String, Object> getHeaderItems() {
        return headerItems;
    }

    public void setHeaderItems(Map<String, Object> headerItems) {
        this.headerItems = headerItems;
    }

    public void put(String key, Object value) {
        this.headerItems.put(key, value);
    }

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
}
