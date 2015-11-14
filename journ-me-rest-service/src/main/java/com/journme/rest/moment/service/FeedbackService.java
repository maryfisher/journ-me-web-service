package com.journme.rest.moment.service;

import com.journme.domain.Feedback;
import com.journme.rest.moment.repository.FeedbackRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 13.11.2015
 */
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getFeedbackByDate(String aliasId, Date from, Date to) {
        return feedbackRepository.findRecent(new ObjectId(aliasId), from);
    }
}
