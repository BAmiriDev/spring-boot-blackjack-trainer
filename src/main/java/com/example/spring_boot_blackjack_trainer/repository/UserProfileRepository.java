package com.example.spring_boot_blackjack_trainer.repository;

import com.example.spring_boot_blackjack_trainer.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUsername(String username);
}
