package com.journme.rest.alias.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.AliasDetail;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.*;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class AliasResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliasResource.class);

    @Autowired
    private AliasService aliasService;

    @GET
    @Path("/{aliasId}")
    public AliasDetail retrieveAlias(@PathParam("aliasId") String aliasId) {
        LOGGER.info("Incoming request to retrieve alias {}", aliasId);
        return aliasService.getAliasDetail(aliasId);
    }

    @POST
    @Path("/{aliasId}")
    @ProtectedByAuthToken
    public AliasBase updateAlias(
            @PathParam("aliasId") String aliasId,
            AliasBase changedAlias) {
        LOGGER.info("Incoming request to update alias {}", aliasId);
        AliasBase existingAlias = aliasService.getAliasDetail(aliasId);
        existingAlias.copy(changedAlias);
        return aliasService.save(changedAlias);
    }
}
