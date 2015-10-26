package com.journme.rest.contract.journey;

import com.journme.domain.JourneyBase;

/**
 * <h1>POJO for REST serialisation</h1>
 * This class represents the create new journey request REST data transfer object
 *
 * @author PHT
 * @version 1.0
 * @since 26.10.2015
 */
public class CreateJourneyRequest {

    private String aliasId;

    private JourneyBase journey;

    public String getAliasId() {
        return aliasId;
    }

    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }

    public JourneyBase getJourney() {
        return journey;
    }

    public void setJourney(JourneyBase journey) {
        this.journey = journey;
    }
}
