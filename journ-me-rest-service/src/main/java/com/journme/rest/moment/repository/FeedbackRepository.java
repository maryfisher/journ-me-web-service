package com.journme.rest.moment.repository;

import com.journme.domain.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
}
