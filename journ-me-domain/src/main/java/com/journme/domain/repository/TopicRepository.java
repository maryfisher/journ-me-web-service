package com.journme.domain.repository;

import com.journme.domain.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Collection;
import java.util.List;

public interface TopicRepository extends MongoRepository<Topic, String>, QueryDslPredicateExecutor<Topic> {

    Topic findByTag(String tag);

    List<Topic> findByTagIn(Collection<String> tags);

}
