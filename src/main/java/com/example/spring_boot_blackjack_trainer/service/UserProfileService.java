package com.example.spring_boot_blackjack_trainer.service;

import com.example.spring_boot_blackjack_trainer.model.UserProfile;


import java.util.List;

public interface UserProfileService {
    UserProfile createUser(String username);
    List<UserProfile> getAllUsers();
    UserProfile getUserById(Long id);
    void deleteUser(Long id);
    void saveUser(UserProfile user);

}
