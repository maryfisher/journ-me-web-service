package com.journme.domain.repository;

import com.journme.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface NotificationRepository extends MongoRepository<Notification, String>, QueryDslPredicateExecutor<Notification> {

    Page<Notification> findByUserId(String userId, Pageable page);

}
