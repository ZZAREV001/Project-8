package org.rewardscentral.service;

import org.rewardscentral.model.Reward;
import org.rewardscentral.model.VisitedLocation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RewardsCentral {

    int calculateUserRewardPoints(UUID userId, List<VisitedLocation> visitedLocations);

    Map<UUID, Integer> calculateRewardsForAllUsers(Map<UUID, List<VisitedLocation>> userVisitedLocations);

    int getAttractionRewardPoints(UUID attractionId);

    List<Reward> getUserRewards(UUID userId);
}
