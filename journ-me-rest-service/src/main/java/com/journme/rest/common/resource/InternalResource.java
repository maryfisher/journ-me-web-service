package com.journme.rest.common.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.journme.domain.Category;
import com.journme.domain.State;
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
import java.util.Arrays;
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
    private ObjectMapperWrapper objectMapperWrapper;

    @GET
    @Path("/monitoring/healthchecks")
    public String getHealthcheck() {
        LOGGER.debug("Incoming healthcheck call");
        return "{\"status\":\"OK\",\"message\":\"OK!\"}";
    }

    @GET
    @Path("/config/jm-config.js")
    @Produces("application/javascript")
    public String getConfig(@Context Providers providers) throws IOException {
        ObjectMapper objectMapper = objectMapperWrapper.getObjectMapper();

        URL url = Resources.getResource(Constants.Templates.JM_CONFIG_FILE);
        String templateText = Resources.toString(url, Charsets.UTF_8);

        Category category1 = new Category();
        category1.setId("abc");
        category1.setCode("SPORT");
        category1.setName("Sport");
        category1.setDescript("Sports related");
        List<Category> categories = Arrays.asList(category1);

        List<State> states = stateRepository.findAll();

        String categoriesJson = objectMapper.writeValueAsString(categories);
        String statesJson = objectMapper.writeValueAsString(states);

        templateText = templateText.replace("${categoriesObject}", categoriesJson);
        templateText = templateText.replace("${statesObject}", statesJson);

        return templateText;
    }

}
