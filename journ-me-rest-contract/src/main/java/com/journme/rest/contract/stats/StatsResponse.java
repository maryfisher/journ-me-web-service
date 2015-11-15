package com.journme.rest.contract.stats;

import com.journme.domain.JourneyBase;
import com.journme.domain.MomentDetail;

import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 10.11.2015
 */
public class StatsResponse {
    private Long allJourneys;
    private Long allMoments;
    private Long allLinks;
    private Long allJoins;
    private Long allFollowers;
    private Long allFeedbacks;

    private List<MomentDetail> recentMoments;
    private List<JourneyBase> recentJourneys;

    public Long getAllJourneys() {
        return allJourneys;
    }

    public void setAllJourneys(Long allJourneys) {
        this.allJourneys = allJourneys;
    }

    public Long getAllMoments() {
        return allMoments;
    }

    public void setAllMoments(Long allMoments) {
        this.allMoments = allMoments;
    }

    public Long getAllLinks() {
        return allLinks;
    }

    public void setAllLinks(Long allLinks) {
        this.allLinks = allLinks;
    }

    public Long getAllJoins() {
        return allJoins;
    }

    public void setAllJoins(Long allJoins) {
        this.allJoins = allJoins;
    }

    public Long getAllFollowers() {
        return allFollowers;
    }

    public void setAllFollowers(Long allFollowers) {
        this.allFollowers = allFollowers;
    }

    public Long getAllFeedbacks() {
        return allFeedbacks;
    }

    public void setAllFeedbacks(Long allFeedbacks) {
        this.allFeedbacks = allFeedbacks;
    }

    public List<MomentDetail> getRecentMoments() {
        return recentMoments;
    }

    public void setRecentMoments(List<MomentDetail> recentMoments) {
        this.recentMoments = recentMoments;
    }

    public List<JourneyBase> getRecentJourneys() {
        return recentJourneys;
    }

    public void setRecentJourneys(List<JourneyBase> recentJourneys) {
        this.recentJourneys = recentJourneys;
    }
}
