package com.journme.domain.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.journme.domain.AbstractIdEntity;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;

import java.io.IOException;

public class EntityToIdSerializer extends StdSerializer<AbstractIdEntity> {

    public EntityToIdSerializer() {
        super(AbstractIdEntity.class);
    }

    @Override
    public void serialize(AbstractIdEntity entity, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (entity instanceof LazyLoadingProxy) {
            jgen.writeString(((LazyLoadingProxy) entity).toDBRef().getId().toString());
        } else {
            jgen.writeString(entity.getId());
        }
    }
}