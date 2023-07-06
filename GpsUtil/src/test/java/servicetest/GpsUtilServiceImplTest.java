package servicetest;

import org.gpsutil.exceptions.GpsUtilException;
import org.gpsutil.model.Attraction;
import org.gpsutil.model.Location;
import org.gpsutil.model.VisitedLocation;
import org.gpsutil.service.GpsUtilServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class GpsUtilServiceImplTest {

    private GpsUtilServiceImpl gpsUtilService = new GpsUtilServiceImpl();

    @BeforeEach
    public void setUp() {
        gpsUtilService = new GpsUtilServiceImpl();
    }

    @Test
    public void getUserLocation_ShouldReturnValidLocation() {
        // Generate a random UUID for testing
        String userId = UUID.randomUUID().toString();

        // Call the method to be tested
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(userId);

        // Assert that the returned VisitedLocation object is not null
        assertThat(visitedLocation).isNotNull();

        // Assert that the userId in the returned VisitedLocation matches the input userId
        assertThat(visitedLocation.getUserId().toString()).isEqualTo(userId);

        // Assert that the latitude and longitude are within valid ranges
        assertThat(visitedLocation.getLocation().getLatitude()).isBetween(-90.0, 90.0);
        assertThat(visitedLocation.getLocation().getLongitude()).isBetween(-180.0, 180.0);
    }

    @Test
    public void getUserLocation_ShouldThrowExceptionForInvalidUserId() {
        assertThatThrownBy(() -> gpsUtilService.getUserLocation("invalidUserId"))
                .isInstanceOf(GpsUtilException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getAttractions_ShouldReturnListOfAttractions() {
        // Act
        List<Attraction> attractions = gpsUtilService.getAttractions();

        // Assert
        assertThat(attractions).isNotNull();
        assertThat(attractions).hasSize(3);

        // Asserting the properties of the first attraction
        Attraction firstAttraction = attractions.get(0);
        assertThat(firstAttraction.getName()).isEqualTo("Statue of Liberty");
        assertThat(firstAttraction.getCity()).isEqualTo("New York City");
        assertThat(firstAttraction.getState()).isEqualTo("NY");
        assertThat(firstAttraction.getLocation().getLatitude()).isEqualTo(40.6892534);
        assertThat(firstAttraction.getLocation().getLongitude()).isEqualTo(-74.0466891);
    }

    @Test
    public void isWithinAttractionProximity_ShouldReturnTrueWhenWithinProximity() {
        // Arrange
        Attraction attraction = new Attraction("Test Attraction", "Test City", "Test State",
                new Location(40.6892534, -74.0466891));
        Location locationNearby = new Location(40.6892534, -74.0466891); // Same location as attraction

        // Act
        boolean isWithinProximity = gpsUtilService.isWithinAttractionProximity(attraction, locationNearby);

        // Assert
        assertThat(isWithinProximity).isTrue();
    }

    @Test
    public void isWithinAttractionProximity_ShouldReturnFalseWhenOutsideProximity() {
        // Arrange
        Attraction attraction = new Attraction("Test Attraction", "Test City", "Test State",
                new Location(40.6892534, -74.0466891));
        Location locationFarAway = new Location(50.0, 0.0); // A location far away from the attraction

        // Act
        boolean isWithinProximity = gpsUtilService.isWithinAttractionProximity(attraction, locationFarAway);

        // Assert
        assertThat(isWithinProximity).isFalse();
    }

    @Test
    public void getDistance_ShouldReturnZeroForSameLocations() {
        // Arrange
        Location location = new Location(40.6892534, -74.0466891);

        // Act
        double distance = gpsUtilService.getDistance(location, location);

        // Assert
        assertThat(distance).isZero();
    }

    @Test
    public void getDistance_ShouldReturnPositiveForDifferentLocations() {
        // Arrange
        Location location1 = new Location(40.6892534, -74.0466891); // Statue of Liberty
        Location location2 = new Location(48.8588443, 2.2943506); // Eiffel Tower

        // Act
        double distance = gpsUtilService.getDistance(location1, location2);

        // Assert
        assertThat(distance).isPositive();
    }

    @Test
    public void getDistance_ShouldReturnCorrectDistanceForKnownLocations() {
        // Arrange
        Location newYork = new Location(40.7128, -74.0060);
        Location losAngeles = new Location(34.0522, -118.2437);
        double expectedDistance = 2451; // Approximate distance in miles between New York and Los Angeles

        // Act
        double distance = gpsUtilService.getDistance(newYork, losAngeles);

        // Assert
        assertThat(distance).isCloseTo(expectedDistance, within(50.0)); // Allowing a margin of error
    }
}

