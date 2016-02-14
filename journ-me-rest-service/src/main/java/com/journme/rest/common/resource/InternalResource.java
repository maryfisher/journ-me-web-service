package com.journme.rest.common.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.journme.domain.Category;
import com.journme.domain.State;
import com.journme.domain.repository.CategoryRepository;
import com.journme.domain.repository.StateRepository;
import com.journme.rest.common.util.Constants;
import com.journme.rest.config.JerseyConfig.ObjectMapperWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
@Singleton
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class InternalResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalResource.class);

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapperWrapper objectMapperWrapper;

    @GET
    @Path("/monitoring/healthchecks")
    public String getHealthcheck() {
        LOGGER.debug("Incoming healthcheck call");
        //TODO: healthcheck to return software version (PROD) or commit hash (DEV)
        return "{\"status\":\"OK\",\"message\":\"OK!\"}";
    }

    @GET
    @Path("/config/jm-config.js")
    @Produces("application/javascript")
    public String getConfig(@Context Providers providers) throws IOException {
        LOGGER.info("Incoming call to retrieve client app configuration JS");
        ObjectMapper objectMapper = objectMapperWrapper.getObjectMapper();

        // TODO: potential to cache the generated JS for a certain amount of time
        URL fileLocation = Resources.getResource(Constants.Templates.JM_CONFIG_FILE);
        String templateText = Resources.toString(fileLocation, Charsets.UTF_8);

        List<Category> categories = categoryRepository.findAll();
        List<State> states = stateRepository.findAll();

        String categoriesJson = objectMapper.writeValueAsString(categories);
        String statesJson = objectMapper.writeValueAsString(states);

        templateText = templateText.replace("${categoriesObject}", categoriesJson);
        templateText = templateText.replace("${statesObject}", statesJson);

        return templateText;
    }

}
