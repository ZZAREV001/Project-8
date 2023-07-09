package org.rewardscentral.service;

import org.rewardscentral.model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RewardsCentralServiceImpl {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private final int ATTRACTION_PROXIMITY_RANGE = 10; // in miles
    private final KafkaTemplate<String, UserReward> kafkaTemplate;
    private List<Attraction> attractions;
    private final KafkaTemplate<String, Attraction> kafkaTemplate1;
    private final Map<UUID, List<VisitedLocation>> userVisitedLocations = new HashMap<>();

    public RewardsCentralServiceImpl(KafkaTemplate<String, UserReward> kafkaTemplate, KafkaTemplate<String, Attraction> kafkaTemplate1) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate1 = kafkaTemplate1;
    }

    @KafkaListener(topics = "attractions-updates", groupId = "rewardscentral")
    public void updateAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    @KafkaListener(topics = "attractions", groupId = "rewardscentral")
    public void consumeAttractions(Attraction attraction) {
        // Add the attraction to your local list of attractions
        attractions.add(attraction);
    }

    // We have ever a method to check if a location is within proximity of an attraction
    // This method is triggered whenever a message is published to the "user-location-updates" topic
    @KafkaListener(topics = "user-location-updates", groupId = "rewardscentral")
    public void calculateRewardPointsForUser(UUID userId, List<VisitedLocation> visitedLocations) {
        // Initialize totalRewardPoints
        int totalRewardPoints = 0;

        // Iterate through each attraction
        for (Attraction attraction : attractions) {
            // Check if the visited location is within proximity of the attraction
            if (isWithinAttractionProximity(attraction, visitedLocation.getLocation())) {
                // Calculate reward points for this attraction
                int rewardPoints = calculateRewardPointsForAttraction(attraction);

                // Add reward points to the total
                totalRewardPoints += rewardPoints;

                // Create a UserReward object
                UserReward userReward = new UserReward(visitedLocation, attraction, rewardPoints);

                // Publish the UserReward to Kafka topic for the UserService to consume
                kafkaTemplate.send("user-rewards-updates", userReward);
            }
        }
    }

    private int calculateRewardPointsForAttraction(Attraction attraction) {
        // Implementation to calculate reward points for an attraction.
        // As no rules are specified, we simply generate a number between 1 and 100
        Random random = new Random();
        return random.nextInt(100) + 1;
    }

    private boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        // Calculate the distance between the attraction and the location
        double distance = getDistance(attraction.getLocation(), location);

        // Return true if the distance is less than or equal to the proximity range
        return distance <= ATTRACTION_PROXIMITY_RANGE;
    }

    private double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lon1 = Math.toRadians(loc1.getLongitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double lon2 = Math.toRadians(loc2.getLongitude());

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }

    // Calculate rewards for a batch of users
    public Map<UUID, Integer> calculateRewardsForAllUsers(Map<UUID, List<VisitedLocation>> userVisitedLocations) {
        Map<UUID, Integer> userRewardPoints = new HashMap<>();
        for (Map.Entry<UUID, List<VisitedLocation>> entry : userVisitedLocations.entrySet()) {
            UUID userId = entry.getKey();
            List<VisitedLocation> visitedLocations = entry.getValue();
            int totalRewardPoints = calculateRewardPointsForUser(userId, visitedLocations);
            userRewardPoints.put(userId, totalRewardPoints);
        }
        return userRewardPoints;
    }

    @KafkaListener(topics = "user-location-updates", groupId = "rewardscentral")
    public int calculateUserRewardPoints(VisitedLocation visitedLocation) {
        // Initialize totalRewardPoints
        int totalRewardPoints = 0;

        // Get the list of visited locations for the user
        List<VisitedLocation> visitedLocations = getVisitedLocationsForUser(visitedLocation.getUserId());

        // Iterate through each visited location
        for (VisitedLocation userVisitedLocation : visitedLocations) { // renamed variable
            // Iterate through each attraction
            for (Attraction attraction : attractions) { // directly use the attractions field
                // Check if the visited location is within proximity of the attraction
                if (isWithinAttractionProximity(attraction, userVisitedLocation.getLocation())) { // use renamed variable
                    // Calculate reward points for this attraction
                    int rewardPoints = calculateRewardPointsForAttraction(attraction);

                    // Add reward points to the total
                    totalRewardPoints += rewardPoints;
                }
            }
        }

        return totalRewardPoints;
    }

    @KafkaListener(topics = "user-visited-locations", groupId = "rewardscentral")
    public void consumeUserVisitedLocations(VisitedLocation visitedLocation) {
        // Get the list of visited locations for the user
        List<VisitedLocation> visitedLocations = userVisitedLocations.getOrDefault(visitedLocation.getUserId(), new ArrayList<>());

        // Add the new visited location
        visitedLocations.add(visitedLocation);

        // Update the list of visited locations for the user
        userVisitedLocations.put(visitedLocation.getUserId(), visitedLocations);
    }

    public List<VisitedLocation> getVisitedLocationsForUser(UUID userId) {
        return userVisitedLocations.getOrDefault(userId, new ArrayList<>());
    }

    // Get a list of rewards that a user has earned
    public List<Reward> getUserRewards(UUID userId) {
        // Implementation here...
    }
}
