package com.journme.rest.state.resource;

import com.journme.domain.State;
import com.journme.domain.repository.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
@Component
@Singleton
public class StateResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateResource.class);

    @Autowired
    private StateRepository stateRepository;

    @GET
    public List<State> retrieveState() {
        LOGGER.debug("Incoming request to retrieve all states");
        return stateRepository.findAll();
    }
}
