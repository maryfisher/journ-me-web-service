package com.journme.domain.repository;

import com.journme.domain.AliasBase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * <h1>DAO repository class</h1>
 * DAO repository to access MongoDB data store of AliasBase via JPA/Querydsl.
 *
 * @author PHT
 * @version 1.0
 * @since 26.10.2015
 */
public interface AliasBaseRepository extends MongoRepository<AliasBase, String>, QueryDslPredicateExecutor<AliasBase> {
}
