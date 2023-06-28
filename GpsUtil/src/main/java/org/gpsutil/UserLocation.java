package org.gpsutil;

import java.util.List;
import java.util.UUID;

public class UserLocation {

    private UUID userId;
    private List<VisitedLocation> visitedLocations;

    public UserLocation(UUID userId, List<VisitedLocation> visitedLocations) {
        this.userId = userId;
        this.visitedLocations = visitedLocations;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<VisitedLocation> getVisitedLocations() {
        return visitedLocations;
    }

    public void setVisitedLocations(List<VisitedLocation> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }
}

