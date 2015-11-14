package com.journme.domain.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.journme.domain.AbstractEntity;

public class NullConverter extends StdConverter<String, AbstractEntity> {
    @Override
    public AbstractEntity convert(String value) {
        return null;
    }
}
