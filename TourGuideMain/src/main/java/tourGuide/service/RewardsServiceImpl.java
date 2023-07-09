package tourGuide.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.index.strtree.STRtree;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@Service
@Slf4j
public class RewardsServiceImpl implements RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private final int DEFAULT_PROXIMITY_BUFFER = 10;
    private int PROXIMITY_BUFFER = DEFAULT_PROXIMITY_BUFFER;
    private final int ATTRACTION_PROXIMITY_RANGE = 200;
    private final GpsUtil gpsUtil;
    private final RewardCentral rewardsCentral;

    public RewardsServiceImpl(GpsUtil gpsUtil, RewardCentral rewardCentral) {
        this.gpsUtil = gpsUtil;
        this.rewardsCentral = rewardCentral;
    }

    public void setPROXIMITY_BUFFER(int PROXIMITY_BUFFER) {
        this.PROXIMITY_BUFFER = PROXIMITY_BUFFER;
    }

    public void setDefaultProximityBuffer() {
        PROXIMITY_BUFFER = DEFAULT_PROXIMITY_BUFFER;
    }

    /** Method for calculating rewards
     * @param user
     * Building the spatial index (STRtree) has a time complexity of O(m log m), where m is the number of attractions.
     * In the worst case scenario, every attraction could be nearby, so the time complexity of this part of the method could be O(n*m) in the worst case
     * where m is the number of attractions and n is the number of visited locations.
     */
    @Override
    public void calculateRewards(User user) {
        log.info("Entering calculateRewards method for user: {}", user.getUserId());

        // Create a new STRtree (a type of R-tree with good performance characteristics)
        STRtree spatialIndex = new STRtree();

        // Get the list of attractions from gpsUtil
        List<Attraction> attractions = gpsUtil.getAttractions();
        log.info("Number of attractions: {}", attractions.size());

        // Add each attraction to the index
        for (Attraction attraction : attractions) {
            // Create a point for the attraction's location
            Coordinate coord = new Coordinate(attraction.longitude, attraction.latitude);
            Point point = new GeometryFactory().createPoint(coord);
            // Insert the point into the spatial index, associating it with the attraction
            spatialIndex.insert(point.getEnvelopeInternal(), attraction);
        }

        // Get the list of visited locations from the user
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        log.info("Number of visited locations for user: {}", userLocations.size());

        // Now we can quickly find attractions near a visited location
        for (VisitedLocation visitedLocation : userLocations) {
            log.info("Processing visited location: {}", visitedLocation.location);

            // Create a point for the visited location
            Coordinate coord = new Coordinate(visitedLocation.location.longitude, visitedLocation.location.latitude);
            Point point = new GeometryFactory().createPoint(coord);

            // Find attractions within 10 miles of the visited location
            double milesToDegrees = 10 / 69; // Rough conversion from miles to degrees
            Envelope env = new Envelope(coord);
            env.expandBy(milesToDegrees);

            // Query the spatial index for attractions within the envelope
            List nearbyAttractions = spatialIndex.query(env);
            log.info("Nearby attractions: {}", nearbyAttractions.size());

            // loop over nearbyAttractions instead of all attractions
            for (Object nearbyAttraction : nearbyAttractions) {
                Attraction attraction = (Attraction) nearbyAttraction;
                log.info("Processing nearby attraction: {}", attraction.attractionName);

                // Calculate the reward points
                int rewardPoints = getRewardPoints(attraction, user);
                log.info("Reward points: {}", rewardPoints);

                // Create a UserReward object
                UserReward userReward = new UserReward(visitedLocation, attraction, rewardPoints);

                // Add the UserReward to the user's list of rewards
                user.addUserReward(userReward);
            }
        }

        log.info("Total rewards for user: {}", user.getUserRewards().size());
    }

    @Override
    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return getDistance(attraction, location) > ATTRACTION_PROXIMITY_RANGE ? false : true;
    }

    // Using this method is for brute force approach. No need in calculateRewards() when R-tree data structure is used.
    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > PROXIMITY_BUFFER ? false : true;
    }

    private int getRewardPoints(Attraction attraction, User user) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

    @Override
    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}
