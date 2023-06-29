package org.gpsutil.model;

public class Attraction {

    private String name;
    private String city;
    private String state;
    private Location location;

    public Attraction(String name, String city, String state, Location location) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
