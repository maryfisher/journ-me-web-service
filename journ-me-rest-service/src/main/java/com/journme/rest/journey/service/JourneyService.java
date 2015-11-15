package com.journme.rest.journey.service;

import com.journme.domain.JourneyBase;
import com.journme.domain.JourneyDetails;
import com.journme.domain.QJourneyBase;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.journey.repository.JourneyBaseRepository;
import com.journme.rest.journey.repository.JourneyDetailsRepository;
import com.mysema.query.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 31.10.2015
 */
public class JourneyService {

    @Autowired
    private JourneyDetailsRepository journeyDetailsRepository;

    @Autowired
    private JourneyBaseRepository journeyBaseRepository;

    public JourneyBase save(JourneyBase journey) {
        return journeyBaseRepository.save(journey);
    }

    public JourneyDetails save(JourneyDetails journey) {
        return journeyDetailsRepository.save(journey);
    }

    public JourneyDetails getJourneyDetail(String journeyId) {
        JourneyDetails journey = journeyDetailsRepository.findOne(journeyId);
        if (journey == null) {
            thowJourneyExc(journeyId);
        }

        return journey;
    }

    public JourneyBase getJourneyBase(String journeyId) {
        JourneyBase journey = journeyBaseRepository.findOne(journeyId);
        if (journey == null) {
            thowJourneyExc(journeyId);
        }

        return journey;
    }

    public long countAll() {
        return journeyBaseRepository.count();
    }

    public List<JourneyBase> getJourneysByDate(Date from, Date to) {

        QJourneyBase journey = QJourneyBase.journeyBase;

        BooleanBuilder criteria = new BooleanBuilder();
        if (from != null) {
            criteria.and(journey.created.after(from));
        }
        if (to != null) {
            criteria.and(journey.created.before(to));
        }

        return (List<JourneyBase>) journeyBaseRepository.findAll(criteria, journey.created.desc());
    }

    private void thowJourneyExc(String journeyId) {
        throw new JournMeException("No JourneyDetail found for given journey ID " + journeyId,
                Response.Status.BAD_REQUEST,
                JournMeExceptionDto.ExceptionCode.JOURNEY_NONEXISTENT);
    }
}
