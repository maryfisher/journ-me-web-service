package com.journme.rest.contract.stats;

import com.journme.domain.JourneyBase;
import com.journme.domain.MomentBase;

import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 11.11.2015
 */
public class RecentResponse {
    private List<MomentBase> recentMoments;
    private List<JourneyBase> recentJourneys;

    public List<MomentBase> getRecentMoments() {
        return recentMoments;
    }

    public void setRecentMoments(List<MomentBase> recentMoments) {
        this.recentMoments = recentMoments;
    }

    public List<JourneyBase> getRecentJourneys() {
        return recentJourneys;
    }

    public void setRecentJourneys(List<JourneyBase> recentJourneys) {
        this.recentJourneys = recentJourneys;
    }
}
