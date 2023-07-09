package org.userservice.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class User {

    private final UUID userId;
    private final String userName;
    private String phoneNumber;
    private String emailAddress;

    private List<UserReward> userRewards;

    public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.userRewards = new ArrayList<>();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public List<UserReward> getUserRewards() {
        return userRewards;
    }
}
