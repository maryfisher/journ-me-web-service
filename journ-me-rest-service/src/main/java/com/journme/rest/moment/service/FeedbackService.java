package com.journme.rest.moment.service;

import com.journme.domain.Feedback;
import com.journme.domain.QFeedback;
import com.journme.rest.moment.repository.FeedbackRepository;
import com.mysema.query.types.path.DateTimePath;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import java.util.Date;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 13.11.2015
 */
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public Page<Feedback> getFeedbackByDate(String aliasId, Date from) {
        DateTimePath<Date> qCreated = QFeedback.feedback.created;
        return feedbackRepository.findRecent(new ObjectId(aliasId), from, new PageRequest(0, 10, Direction.DESC, qCreated.getMetadata().getName()));
    }
}
