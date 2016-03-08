package com.journme.domain.repository;

import com.journme.domain.Notebook;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 18.02.2016
 */
public interface NotebookRepository extends MongoRepository<Notebook, String>, QueryDslPredicateExecutor<Notebook> {
}
