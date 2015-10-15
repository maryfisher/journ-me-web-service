package com.journme.rest.contract.journey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <h1>POJO for REST serialisation</h1>
 * This class represents the basic data properties of a Journey domain object on REST exchanges.
 *
 * @author PHT
 * @version 1.0
 * @since 15.10.2015
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JourneyBaseDto {

    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
