package com.journme.rest.common.event;

/**
 * Tagging interface for events
 * Created by PHT on 21.02.2016.
 */
public interface Event<T> {

    T getPayLoad();

}
