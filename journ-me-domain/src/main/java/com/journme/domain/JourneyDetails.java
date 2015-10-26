package com.journme.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Domain class/persistence entity</h1>
 * The class representing a journey details properties in the persistent store.
 *
 * @author PHT
 * @version 1.0
 * @since 24.10.2015
 */
public class JourneyDetails extends JourneyBase {

    @DBRef(lazy = true)
    private List<Moment> moments = new ArrayList<Moment>();

    @DBRef(lazy = true)
    private List<Alias> followers = new ArrayList<Alias>();

    @DBRef(lazy = true)
    private List<JourneyBase> linkedToJourneys = new ArrayList<JourneyBase>();

    @DBRef(lazy = true)
    private List<JourneyBase> linkedFromJourneys = new ArrayList<JourneyBase>();

    @DBRef(lazy = true)
    private List<Alias> joinedAliases = new ArrayList<Alias>();

    @DBRef(lazy = true)
    private List<Alias> joinRequests = new ArrayList<Alias>();

    public List<Moment> getMoments() {
        return moments;
    }

    public void setMoments(List<Moment> moments) {
        this.moments = moments;
    }

    public List<Alias> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Alias> followers) {
        this.followers = followers;
    }

    public List<JourneyBase> getLinkedToJourneys() {
        return linkedToJourneys;
    }

    public void setLinkedToJourneys(List<JourneyBase> linkedToJourneys) {
        this.linkedToJourneys = linkedToJourneys;
    }

    public List<JourneyBase> getLinkedFromJourneys() {
        return linkedFromJourneys;
    }

    public void setLinkedFromJourneys(List<JourneyBase> linkedFromJourneys) {
        this.linkedFromJourneys = linkedFromJourneys;
    }

    public List<Alias> getJoinedAliases() {
        return joinedAliases;
    }

    public void setJoinedAliases(List<Alias> joinedAliases) {
        this.joinedAliases = joinedAliases;
    }

    public List<Alias> getJoinRequests() {
        return joinRequests;
    }

    public void setJoinRequests(List<Alias> joinRequests) {
        this.joinRequests = joinRequests;
    }
}
