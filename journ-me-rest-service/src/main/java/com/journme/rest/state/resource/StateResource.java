package com.journme.rest.state.resource;

import com.journme.domain.State;
import com.journme.rest.state.repository.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public class StateResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateResource.class);

    @Autowired
    private StateRepository stateRepository;

    @GET
    @Path("/")
    public List<State> retrieveState() {
        LOGGER.info("Incoming request to retrieve all states");
        return stateRepository.findAll();
    }
}
