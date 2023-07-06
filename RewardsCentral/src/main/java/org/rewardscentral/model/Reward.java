package org.rewardscentral.model;

import java.util.UUID;

public class Reward {
    private UUID userId;
    private String attractionName;
    private int rewardPoints;
    private double userLatitude;
    private double userLongitude;
    private double attractionLatitude;
    private double attractionLongitude;

    public Reward(UUID userId, String attractionName, int rewardPoints,
                  double userLatitude, double userLongitude,
                  double attractionLatitude, double attractionLongitude) {
        this.userId = userId;
        this.attractionName = attractionName;
        this.rewardPoints = rewardPoints;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        this.attractionLatitude = attractionLatitude;
        this.attractionLongitude = attractionLongitude;
    }

    // Getters and Setters

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public double getAttractionLatitude() {
        return attractionLatitude;
    }

    public void setAttractionLatitude(double attractionLatitude) {
        this.attractionLatitude = attractionLatitude;
    }

    public double getAttractionLongitude() {
        return attractionLongitude;
    }

    public void setAttractionLongitude(double attractionLongitude) {
        this.attractionLongitude = attractionLongitude;
    }
}
