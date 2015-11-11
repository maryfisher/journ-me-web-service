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
    private List<MomentBase> moments = new ArrayList<>();

    @DBRef(lazy = true)
    private List<AliasBase> followers = new ArrayList<>();

    @DBRef(lazy = true)
    private List<JourneyBase> linkedToJourneys = new ArrayList<>();

    @DBRef(lazy = true)
    private List<JourneyBase> linkedFromJourneys = new ArrayList<>();

    @DBRef(lazy = true)
    private List<AliasBase> joinedAliases = new ArrayList<>();

    @DBRef(lazy = true)
    private List<AliasBase> joinRequests = new ArrayList<>();

    public List<MomentBase> getMoments() {
        return moments;
    }

    public void setMoments(List<MomentBase> moments) {
        this.moments = moments;
    }

    public List<AliasBase> getFollowers() {
        return followers;
    }

    public void setFollowers(List<AliasBase> followers) {
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

    public List<AliasBase> getJoinedAliases() {
        return joinedAliases;
    }

    public void setJoinedAliases(List<AliasBase> joinedAliases) {
        this.joinedAliases = joinedAliases;
    }

    public List<AliasBase> getJoinRequests() {
        return joinRequests;
    }

    public void setJoinRequests(List<AliasBase> joinRequests) {
        this.joinRequests = joinRequests;
    }
}
