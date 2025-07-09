package com.example.spring_boot_blackjack_trainer.repository;

import com.example.spring_boot_blackjack_trainer.model.TrainingSession;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    List<TrainingSession> findByUserProfileId(Long userId);
}
