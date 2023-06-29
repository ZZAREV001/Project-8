package org.gpsutil.service;

import org.gpsutil.model.VisitedLocation;
import org.gpsutil.model.Location;
import org.gpsutil.model.Attraction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilServiceImpl implements GpsUtilService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private final int DEFAULT_PROXIMITY_BUFFER = 10;
    private final int proximityBuffer = DEFAULT_PROXIMITY_BUFFER;
    private final int ATTRACTION_PROXIMITY_RANGE = 200;

    // This method should get the user's location (in reality, we have to get user location and use a GPS API)
    @Override
    public VisitedLocation getUserLocation(String userId) {
        // Generating random latitude and longitude for testing
        double latitude = Math.random() * 180 - 90; // values between -90 and 90
        double longitude = Math.random() * 360 - 180; // values between -180 and 180

        // Creating a location object
        Location location = new Location(latitude, longitude);

        // Creating a visited location object with the current time
        VisitedLocation visitedLocation = new VisitedLocation(UUID.fromString(userId), location, new Date());

        return visitedLocation;
    }

    // This method should get a list of all attractions
    @Override
    public List<Attraction> getAttractions() {
        List<Attraction> attractions = new ArrayList<>();

        // Adding some hardcoded attractions (real app: retrieve from a database or an external API)
        attractions.add(new Attraction("Statue of Liberty", "New York City", "NY",
                new Location(40.6892534, -74.0466891)));
        attractions.add(new Attraction("Eiffel Tower", "Paris", "France",
                new Location(48.8588443, 2.2943506)));
        attractions.add(new Attraction("The Colosseum", "Rome", "Italy",
                new Location(41.8902102, 12.4922309)));

        // Return the list of attractions
        return attractions;
    }

    // This method should check if a location is near an attraction
    @Override
    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return !(getDistance(attraction.getLocation(), location) > ATTRACTION_PROXIMITY_RANGE);
    }

    // This method calculates the distance between two locations
    @Override
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

