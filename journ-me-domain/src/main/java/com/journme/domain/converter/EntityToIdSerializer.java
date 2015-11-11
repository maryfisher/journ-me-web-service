package com.journme.domain.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.journme.domain.AbstractEntity;

import java.io.IOException;

public class EntityToIdSerializer extends StdSerializer<AbstractEntity> {

    public EntityToIdSerializer() {
        super(AbstractEntity.class);
    }

    @Override
    public void serialize(AbstractEntity entity, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(entity.getId());
    }
}