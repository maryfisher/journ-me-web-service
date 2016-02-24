package com.journme.rest.common.event;

/**
 * Interface for events that are relevant to users
 * Created by PHT on 21.02.2016.
 */
public interface PersonalEvent<X> {

    X getAffectedPerson();

}
