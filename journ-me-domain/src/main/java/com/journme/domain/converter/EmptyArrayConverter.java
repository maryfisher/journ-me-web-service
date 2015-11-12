package com.journme.domain.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.journme.domain.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class EmptyArrayConverter extends StdConverter<List<String>, List<AbstractEntity>> {
    @Override
    public List<AbstractEntity> convert(List<String> value) {
        return new ArrayList<>();
    }
}
