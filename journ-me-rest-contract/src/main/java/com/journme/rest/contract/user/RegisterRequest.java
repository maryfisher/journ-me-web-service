package com.journme.rest.contract.user;

/**
 * <h1>POJO for REST serialisation</h1>
 * This class represents the register request REST data transfer object
 *
 * @author PHT
 * @version 1.0
 * @since 26.10.2015
 */
public class RegisterRequest extends LoginRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
