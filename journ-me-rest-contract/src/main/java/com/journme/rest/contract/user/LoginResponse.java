package com.journme.rest.contract.user;

import java.util.HashMap;
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

    private Map<String, Object> headerItems;

    public LoginResponse() {
        headerItems = new HashMap<>();
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
}
