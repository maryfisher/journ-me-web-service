package com.journme.rest.common.event;

import com.journme.domain.AliasBase;
import com.journme.domain.MomentBase;

/**
 * Concrete event classs for moments
 * <p>
 * Created by PHT on 21.02.2016.
 */
public class MomentEvent extends AbstractEntityEvent<MomentBase> implements PersonalEvent<AliasBase> {

    private final AliasBase alias;

    public MomentEvent(Type type, MomentBase moment, AliasBase alias) {
        super(type, moment);
        this.alias = alias;
    }

    @Override
    public AliasBase getAffectedPerson() {
        return alias;
    }
}
