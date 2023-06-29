package org.gpsutil.service;

import org.gpsutil.model.Attraction;
import org.gpsutil.model.Location;
import org.gpsutil.model.VisitedLocation;

import java.util.List;

public interface GpsUtilService {

    VisitedLocation getUserLocation(String userId);

    List<Attraction> getAttractions();

    boolean isWithinAttractionProximity(Attraction attraction, Location location);

    // This method calculates the distance between two locations
    double getDistance(Location loc1, Location loc2);
}
