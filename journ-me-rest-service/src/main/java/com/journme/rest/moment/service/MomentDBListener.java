package com.journme.rest.moment.service;

import com.google.common.eventbus.EventBus;
import com.journme.domain.AliasBase;
import com.journme.domain.JourneyDetails;
import com.journme.domain.MomentBase;
import com.journme.domain.repository.JourneyDetailsRepository;
import com.journme.rest.common.event.AbstractEntityEvent.Type;
import com.journme.rest.common.event.MomentEvent;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

@Component
public class MomentDBListener extends AbstractMongoEventListener<MomentBase> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MomentDBListener.class);

    private static final Long ZERO = 0L;

    @Autowired
    private JourneyDetailsRepository journeyDetailsRepository;

    @Autowired
    private EventBus eventBus;

    @Override
    public void onAfterSave(MomentBase moment, DBObject dbo) {
        if (ZERO.equals(moment.getVersion())) {
            LOGGER.info("Successful moment creation {} triggers event", moment.getId());

            JourneyDetails journey;
            if ((moment.getJourney() instanceof JourneyDetails)) {
                journey = (JourneyDetails) moment.getJourney();
            } else {
                journey = journeyDetailsRepository.findOne(moment.getJourney().getId());
            }

            for (AliasBase followerAlias : journey.getFollowers()) {
                eventBus.post(new MomentEvent(Type.INSERT, moment, followerAlias));
            }
        }
    }
}
