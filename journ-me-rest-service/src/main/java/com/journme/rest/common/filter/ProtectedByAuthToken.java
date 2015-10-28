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
     * String identifier for Authorization Token authentication. Value "JM_TOKEN_AUTH"
     */
    String TOKEN_AUTH = "JM_TOKEN_AUTH";

}
