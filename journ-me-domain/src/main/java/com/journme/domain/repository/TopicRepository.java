package com.journme.domain.repository;

import com.journme.domain.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface TopicRepository extends MongoRepository<Topic, String>, QueryDslPredicateExecutor<Topic> {

    Topic findByTag(String tag);

}
