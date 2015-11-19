package com.journme.domain.repository;

import com.journme.domain.JourneyDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * <h1>DAO repository class</h1>
 * DAO repository to access MongoDB data store of Journey details via JPA/Querydsl.
 *
 * @author PHT
 * @version 1.0
 * @since 20.10.2015
 */
public interface JourneyDetailsRepository extends MongoRepository<JourneyDetails, String>, QueryDslPredicateExecutor<JourneyDetails> {
}
