package com.journme.domain.repository;

import com.journme.domain.JourneyBase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * <h1>DAO repository class</h1>
 * DAO repository to access MongoDB data store of Journey base via JPA/Querydsl.
 *
 * @author PHT
 * @version 1.0
 * @since 24.10.2015
 */
public interface JourneyBaseRepository extends MongoRepository<JourneyBase, String>, QueryDslPredicateExecutor<JourneyBase> {

    @Query("{$group: {_id: {}, count: {$sum: {$size: { '$ifNull': [ '$linkedFromJourneys', [] ] }}}}}")
    List<Integer> allLinksCount();
}
