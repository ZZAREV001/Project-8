package org.gpsutil;

import org.gpsutil.model.Attraction;
import org.gpsutil.model.VisitedLocation;
import org.gpsutil.service.GpsUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gps")
public class GpsUtilController {

    private final GpsUtilService gpsUtilService;

    @Autowired
    public GpsUtilController(GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
    }

    @GetMapping("/userLocation/{userId}")
    public VisitedLocation getUserLocation(@PathVariable String userId) {
        return gpsUtilService.getUserLocation(userId);
    }

    @GetMapping("/attractions")
    public List<Attraction> getAttractions() {
        return gpsUtilService.getAttractions();
    }
}

