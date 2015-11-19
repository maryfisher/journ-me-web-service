package com.journme.domain.repository;

import com.journme.domain.AliasDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public interface AliasDetailRepository extends MongoRepository<AliasDetail, String>, QueryDslPredicateExecutor<AliasDetail> {
}
