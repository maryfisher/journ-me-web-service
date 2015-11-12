package com.journme.rest.moment.repository;

import com.journme.domain.BlinkImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface BlinkImageRepository extends MongoRepository<BlinkImage, String>, QueryDslPredicateExecutor<BlinkImage> {
}
