package servicetest;

import org.gpsutil.exceptions.GpsUtilException;
import org.gpsutil.model.VisitedLocation;
import org.gpsutil.service.GpsUtilServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GpsUtilServiceImplTest {

    private GpsUtilServiceImpl gpsUtilService = new GpsUtilServiceImpl();

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
}

