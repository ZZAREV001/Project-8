package org.gpsutil;

import org.gpsutil.VisitedLocation;
import org.gpsutil.Location;
import org.gpsutil.Attraction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GpsUtilService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private final int defaultProximityBuffer = 10;
    private final int proximityBuffer = defaultProximityBuffer;
    private final int attractionProximityRange = 200;

    // This method should get the user's location
    public VisitedLocation getUserLocation(String userId) {
        // Implementation here...
    }

    // This method should get a list of all attractions
    public List<Attraction> getAttractions() {
        // Implementation here...
    }

    // This method should check if a location is near an attraction
    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return getDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    // This method calculates the distance between two locations
    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lon1 = Math.toRadians(loc1.getLongitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double lon2 = Math.toRadians(loc2.getLongitude());

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }

}

