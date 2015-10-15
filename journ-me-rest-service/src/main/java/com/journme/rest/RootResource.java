package com.journme.rest;

import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * <h1>Jersey root resource</h1>
 * This class represents the Jersey REST entry resource.
 * Subresources are invoked according to the matched URL path endpoints.
 *
 * @author PHT
 * @version 1.0
 * @since 15.10.2015
 */
@Component
@Singleton
@Path(RootResource.BASE_RESOURCE_PATH)
public class RootResource {

    public static final String BASE_RESOURCE_PATH = "/api";

    public static final String HEALTHCHECK_RESOURCE_PATH = "/internal/monitoring/healthchecks";

    @GET
    @Path(HEALTHCHECK_RESOURCE_PATH)
    public String getHealthcheck() {
        return "{\"status\":\"OK\",\"message\":\"OK!\"}";
    }
}
