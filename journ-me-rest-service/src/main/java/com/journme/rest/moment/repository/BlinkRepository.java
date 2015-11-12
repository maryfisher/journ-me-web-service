package com.journme.rest.moment.repository;

import com.journme.domain.Blink;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public interface BlinkRepository extends MongoRepository<Blink, String>, QueryDslPredicateExecutor<Blink> {
}
