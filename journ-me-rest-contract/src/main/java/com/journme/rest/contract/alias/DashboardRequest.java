package com.journme.rest.contract.alias;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 14.11.2015
 */
public class DashboardRequest {
    @NotBlank
    private String aliasId;

    public String getAliasId() {
        return aliasId;
    }

    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }
}
