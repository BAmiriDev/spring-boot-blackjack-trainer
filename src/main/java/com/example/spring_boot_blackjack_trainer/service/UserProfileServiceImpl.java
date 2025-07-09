package com.example.spring_boot_blackjack_trainer.service;

import com.example.spring_boot_blackjack_trainer.model.UserProfile;
import com.example.spring_boot_blackjack_trainer.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfile createUser(String username) {
        UserProfile user = new UserProfile(username);
        return userProfileRepository.save(user);
    }

    @Override
    public List<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }

    @Override
    public UserProfile getUserById(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userProfileRepository.deleteById(id);
    }
    @Override
    public void saveUser(UserProfile user) {
        userProfileRepository.save(user);
    }

}
