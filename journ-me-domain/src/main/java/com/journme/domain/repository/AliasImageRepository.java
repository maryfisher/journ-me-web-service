package com.journme.domain.repository;

import com.journme.domain.AliasImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface AliasImageRepository extends MongoRepository<AliasImage, String>, QueryDslPredicateExecutor<AliasImage> {
}
