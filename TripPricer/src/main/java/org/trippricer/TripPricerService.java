package org.trippricer;

import java.util.List;

public interface TripPricerService {
    List<Provider> getPrice(String apiKey, String attractionId, String adults, String children, String nightsStay, String rewardsPoints);
}
