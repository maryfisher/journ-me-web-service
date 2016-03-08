package com.journme.rest.journey.service;

import com.journme.domain.JourneyBase;
import com.journme.domain.JourneyDetail;
import com.journme.domain.QJourneyBase;
import com.journme.domain.QJourneyDetail;
import com.journme.domain.repository.JourneyBaseRepository;
import com.journme.domain.repository.JourneyDetailsRepository;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.searchfilter.JourneySearchFilter;
import com.journme.rest.common.searchfilter.PredicateBuilder;
import com.journme.rest.contract.JournMeExceptionDto;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.DateTimePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    public JourneyDetail save(JourneyDetail journey) {
        return journeyDetailsRepository.save(journey);
    }

    public JourneyDetail getJourneyDetail(String journeyId) {
        JourneyDetail journey = journeyDetailsRepository.findOne(journeyId);
        if (journey == null) {
            throwJourneyExc(journeyId);
        }

        return journey;
    }

    public JourneyBase getJourneyBase(String journeyId) {
        JourneyBase journey = journeyBaseRepository.findOne(journeyId);
        if (journey == null) {
            throwJourneyExc(journeyId);
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

        return journeyBaseRepository.findAll(criteria, new PageRequest(0, 10, Direction.DESC, qCreated.getMetadata().getName()));
    }

    /*public Page<JourneyBase> searchJourneys(PageRequest pageRequest, JourneySearchFilter searchFilter) {
        // Can use QJourneyDetails predicates, even if querying against journeyBaseRepository
        QJourneyDetails qJourney = QJourneyDetails.journeyDetails;
        BooleanExpression predicate = qJourney.isPublic.isTrue();
        if (searchFilter != null) {
            predicate = predicate.and(PredicateBuilder.fromSearchFilter(searchFilter));
        }
        return journeyBaseRepository.findAll(predicate, pageRequest);
    }*/

    public Page<JourneyBase> searchJourneys(PageRequest pageRequest, JourneySearchFilter sf) {
        // Can use QJourneyDetails predicates, even if querying against journeyBaseRepository
        QJourneyDetail qJourney = QJourneyDetail.journeyDetail;
        BooleanBuilder predicate = new BooleanBuilder();
        //TODO not a good idea if in the future we let users search their own journeys
        predicate = predicate.and(qJourney.isPublic.isTrue());
        if (sf != null) {
            if (!StringUtils.isEmpty(sf.getText()) && sf.getTextMatcher() != null) {
                predicate.and(
                        PredicateBuilder.matchTextFieldAgainstText(sf.getTextMatcher(), qJourney.name, sf.getText())
                                .or(PredicateBuilder.matchTextFieldAgainstText(sf.getTextMatcher(), qJourney.descript, sf.getText()))
                );
            }
            if (sf.getJoin() != null && sf.getJoinMatcher() != null) {
                predicate.and(PredicateBuilder.matchFieldAgainstValue(sf.getJoinMatcher(), qJourney.join, sf.getJoin()));
            }
            if (!CollectionUtils.isEmpty(sf.getCategories()) && sf.getCategoriesMatcher() != null) {
                predicate.and(PredicateBuilder.matchFieldAgainstValues(sf.getCategoriesMatcher(), qJourney.categoryWeights.any().category, sf.getCategories()));
            }
            if (!CollectionUtils.isEmpty(sf.getTopics()) && sf.getTopicsMatcher() != null) {
                predicate.and(PredicateBuilder.matchCollectionFieldAgainstValues(sf.getTopicsMatcher(), qJourney.topics, sf.getTopics()));
            }
        }
        return journeyBaseRepository.findAll(predicate, pageRequest);
    }

    private void throwJourneyExc(String journeyId) {
        throw new JournMeException("No JourneyDetail found for given journey ID " + journeyId,
                Response.Status.BAD_REQUEST,
                JournMeExceptionDto.ExceptionCode.JOURNEY_NONEXISTENT);
    }
}
