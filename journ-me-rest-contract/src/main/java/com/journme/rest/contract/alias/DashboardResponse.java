package com.journme.rest.contract.alias;

import com.journme.domain.Feedback;

import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 14.11.2015
 */
public class DashboardResponse {
    private List<Feedback> recentFeedback;

    public List<Feedback> getRecentFeedback() {
        return recentFeedback;
    }

    public void setRecentFeedback(List<Feedback> recentFeedback) {
        this.recentFeedback = recentFeedback;
    }
}
