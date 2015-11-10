package com.journme.domain.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.journme.domain.AbstractEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmptyArrayDeserializer extends StdDeserializer<List<? extends AbstractEntity>> {

    public EmptyArrayDeserializer() {
        super(List.class);
    }

    @Override
    public List<? extends AbstractEntity> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new ArrayList<>();
    }


}