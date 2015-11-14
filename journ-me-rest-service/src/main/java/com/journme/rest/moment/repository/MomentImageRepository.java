package com.journme.rest.moment.repository;

import com.journme.domain.MomentImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface MomentImageRepository extends MongoRepository<MomentImage, String>, QueryDslPredicateExecutor<MomentImage> {
}
