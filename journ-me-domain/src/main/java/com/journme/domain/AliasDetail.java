package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 28.10.2015
 */
public class AliasDetail extends AliasBase {

    @DBRef
    private List<JourneyBase> journeys = new ArrayList<>();

    @DBRef
    private List<JourneyBase> followedJourneys = new ArrayList<>();

    @DBRef
    private List<JourneyBase> joinedJourneys = new ArrayList<>();

    public List<JourneyBase> getJoinedJourneys() {
        return joinedJourneys;
    }

    public void setJoinedJourneys(List<JourneyBase> joinedJourneys) {
        this.joinedJourneys = joinedJourneys;
    }

    public List<JourneyBase> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<JourneyBase> journeys) {
        this.journeys = journeys;
    }

    public List<JourneyBase> getFollowedJourneys() {
        return followedJourneys;
    }

    public void setFollowedJourneys(List<JourneyBase> followedJourneys) {
        this.followedJourneys = followedJourneys;
    }
}
