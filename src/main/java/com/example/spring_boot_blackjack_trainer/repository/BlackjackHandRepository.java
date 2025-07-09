package com.example.spring_boot_blackjack_trainer.repository;

import com.example.spring_boot_blackjack_trainer.model.BlackjackHand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlackjackHandRepository extends JpaRepository<BlackjackHand, Long> {
    List<BlackjackHand> findBySessionId(Long sessionId);
}
