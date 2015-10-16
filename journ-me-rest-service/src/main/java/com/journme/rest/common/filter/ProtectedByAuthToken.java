package com.journme.rest.common.filter;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>Auth token filter annotation</h1>
 *
 * @author PHT
 * @version 1.0
 * @since 16.10.2015
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtectedByAuthToken {

    /**
     * String identifier for Authorization Token authentication. Value "AUTHORIZATION_TOKEN"
     */
    String AUTHORIZATION_TOKEN = "AUTHORIZATION_TOKEN";

}
