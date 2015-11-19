package com.journme.rest.journey.service;

import com.journme.domain.JourneyBase;
import com.journme.domain.JourneyDetails;
import com.journme.domain.QJourneyBase;
import com.journme.domain.QJourneyDetails;
import com.journme.domain.repository.JourneyBaseRepository;
import com.journme.domain.repository.JourneyDetailsRepository;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.searchfilter.JourneySearchFilter;
import com.journme.rest.common.searchfilter.PredicateBuilder;
import com.journme.rest.contract.JournMeExceptionDto;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.DateTimePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import javax.ws.rs.core.Response;
import java.util.Date;

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

    public Page<JourneyBase> getJourneysByDate(Date from, Date to) {
        DateTimePath<Date> qCreated = QJourneyBase.journeyBase.created;

        BooleanExpression criteria = QJourneyBase.journeyBase.isPublic.isTrue();
        if (from != null) {
            criteria.and(qCreated.after(from));
        }
        if (to != null) {
            criteria.and(qCreated.before(to));
        }

        return journeyBaseRepository.findAll(criteria, new PageRequest(0, 20, Direction.DESC, qCreated.getMetadata().getName()));
    }

    public Page<JourneyBase> searchJourneys(PageRequest pageRequest, JourneySearchFilter searchFilter) {
        // Can use QJourneyDetails predicates, even if querying against journeyBaseRepository
        QJourneyDetails qJourney = QJourneyDetails.journeyDetails;
        BooleanExpression predicate = qJourney.isPublic.isTrue();
        predicate = predicate.and(PredicateBuilder.fromSearchFilter(searchFilter));
        return journeyBaseRepository.findAll(predicate, pageRequest);
    }

    private void thowJourneyExc(String journeyId) {
        throw new JournMeException("No JourneyDetail found for given journey ID " + journeyId,
                Response.Status.BAD_REQUEST,
                JournMeExceptionDto.ExceptionCode.JOURNEY_NONEXISTENT);
    }
}
