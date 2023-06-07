package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.user.User;

public interface RewardsService {

    void calculateRewards(User user);

    boolean isWithinAttractionProximity(Attraction attraction, Location location);

    double getDistance(Location loc1, Location loc2);
}