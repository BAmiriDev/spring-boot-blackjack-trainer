package com.example.spring_boot_blackjack_trainer.service;

import com.example.spring_boot_blackjack_trainer.model.TrainingSession;


import java.util.List;

public interface TrainingSessionService {
    TrainingSession createSession(Long userId);
    List<TrainingSession> getSessionsByUser(Long userId);
    TrainingSession getSessionById(Long sessionId);
    void deleteSession(Long sessionId);

    void saveSession(TrainingSession session);
}
