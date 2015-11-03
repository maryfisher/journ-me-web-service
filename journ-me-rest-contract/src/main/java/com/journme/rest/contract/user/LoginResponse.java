package com.journme.rest.contract.user;

import com.journme.domain.AliasBase;
import com.journme.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private List<String> aliases = new ArrayList<>();

    private String currentAlias;

    private Map<String, Object> headerItems;

    public LoginResponse(User user) {
        headerItems = new HashMap<>();
        email = user.getEmail();
        aliases = new ArrayList<>(user.getAliases().size());
        aliases.addAll(user.getAliases().stream().map(AliasBase::getId).collect(Collectors.toList()));
        currentAlias = user.getCurrentAlias().getId();
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

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getCurrentAlias() {
        return currentAlias;
    }

    public void setCurrentAlias(String currentAlias) {
        this.currentAlias = currentAlias;
    }
}
