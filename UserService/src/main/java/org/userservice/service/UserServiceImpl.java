package org.userservice.service;

public class UserServiceImpl {
    private UserRepository userRepository;

    @KafkaListener(topics = "user-rewards-updates", groupId = "userservice")
    public void updateUserRewards(UserReward userReward) {
        // Fetch the user
        User user = userRepository.findUserById(userReward.getUserId());

        // Add the UserReward to the user's list of rewards
        user.getUserRewards().add(userReward);

        // Save the user
        userRepository.save(user);
    }
}
