package com.journme.rest.common.event;

import com.journme.domain.MomentBase;

/**
 * Concrete event classs for moments
 * <p>
 * Created by PHT on 21.02.2016.
 */
public class MomentEvent extends AbstractEntityEvent {

    private MomentBase moment;

    public MomentEvent(Type type, String userId, MomentBase moment) {
        super(type, userId);
        this.moment = moment;
    }

}
