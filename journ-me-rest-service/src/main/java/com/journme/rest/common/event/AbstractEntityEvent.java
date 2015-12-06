package com.journme.rest.common.event;

/**
 * Base class for all DB events
 * <p>
 * Created by PHT on 21.02.2016.
 */
public abstract class AbstractEntityEvent implements Event {

    public enum Type {
        NEW,
        CHANGED,
        REMOVED
    }

    public AbstractEntityEvent(Type type, String userId) {
        this.type = type;
        this.userId = userId;
    }

    private Type type;

    private String userId;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
