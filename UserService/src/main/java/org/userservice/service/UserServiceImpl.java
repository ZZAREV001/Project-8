package org.userservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.userservice.exceptions.UserNotFoundException;
import org.userservice.model.User;
import org.userservice.model.UserReward;
import org.userservice.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "user-rewards-updates", groupId = "userservice")
    public void updateUserRewards(UserReward userReward) {
        // Fetch the user
        Optional<User> optionalUser = userRepository.findById(userReward.getUserId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Create a new UserReward object that is managed by Hibernate
            UserReward managedUserReward = new UserReward();
            managedUserReward.setVisitedLocationId(userReward.getVisitedLocationId());
            managedUserReward.setAttractionId(userReward.getAttractionId());
            managedUserReward.setRewardPoints(userReward.getRewardPoints());

            // Add the UserReward to the user's list of rewards
            user.getUserRewards().add(managedUserReward);

            // Save the user
            userRepository.save(user);
        } else {
            // Handle the case where the user does not exist
            throw new UserNotFoundException("User with id " + userReward.getUserId() + " not found.");
        }
    }

}
