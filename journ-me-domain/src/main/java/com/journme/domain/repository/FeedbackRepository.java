package com.journme.domain.repository;

import com.journme.domain.Feedback;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Date;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public interface FeedbackRepository extends MongoRepository<Feedback, String>, QueryDslPredicateExecutor<Feedback> {

    @Query(value = "{'momentAlias.$id': ?0, 'alias.$id': {'$ne': ?0}, 'created': {'$gt': ?1}}")
    Page<Feedback> findRecent(ObjectId aliasId, Date date, Pageable page);

}
