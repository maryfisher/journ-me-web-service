package com.journme.rest.common.event;

import com.journme.domain.AbstractEntity;

/**
 * Base class for all DB events
 * <p>
 * Created by PHT on 21.02.2016.
 */
public abstract class AbstractEntityEvent<T extends AbstractEntity> implements Event {

    public enum Type {
        INSERT,
        UPDATE,
        DELETE
    }

    private final Type type;

    private final T entity;

    public AbstractEntityEvent(Type type, T entity) {
        this.type = type;
        this.entity = entity;
    }

    @Override
    public T getPayLoad() {
        return entity;
    }

    public Type getType() {
        return type;
    }
}
