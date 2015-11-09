package com.journme.domain.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.journme.domain.AbstractEntity;

import java.io.IOException;

public class NullDeserializer extends StdDeserializer<AbstractEntity> {

    public NullDeserializer() {
        super(AbstractEntity.class);
    }

    @Override
    public AbstractEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return null;
    }


}