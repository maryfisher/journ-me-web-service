package com.journme.domain.repository;

import com.journme.domain.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface CategoryRepository extends MongoRepository<Category, String>, QueryDslPredicateExecutor<Category> {
}
