package org.gpsutil.model;

import java.util.Date;
import java.util.UUID;

public class VisitedLocation {
    private UUID userId;
    private Location location;
    private Date timestamp;

    public VisitedLocation(UUID userId, Location location, Date timestamp) {
        this.userId = userId;
        this.location = location;
        this.timestamp = timestamp;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
