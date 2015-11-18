package com.journme.domain.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.journme.domain.State;
import com.journme.domain.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 18.11.2015
 */
public class StateListConverter extends StdConverter<List<String>, List<State>> {

    @Autowired
    private StateRepository stateRepository;

    @Override
    public List<State> convert(List<String> value) {
        return (List<State>) stateRepository.findAll(value);
    }
}
