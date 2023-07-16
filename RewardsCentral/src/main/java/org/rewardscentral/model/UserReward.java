package org.rewardscentral.model;

import java.util.UUID;

public class UserReward {

    private UUID userId;
    private VisitedLocation visitedLocation;
    private Attraction attraction;
    private int rewardPoints;

    public UserReward(UUID userId, VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
        this.userId = userId;
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public VisitedLocation getVisitedLocation() {
        return visitedLocation;
    }

    public void setVisitedLocation(VisitedLocation visitedLocation) {
        this.visitedLocation = visitedLocation;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
