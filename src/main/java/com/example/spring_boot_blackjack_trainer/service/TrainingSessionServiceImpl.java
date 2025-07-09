package com.example.spring_boot_blackjack_trainer.service;

import com.example.spring_boot_blackjack_trainer.model.TrainingSession;
import com.example.spring_boot_blackjack_trainer.model.UserProfile;
import com.example.spring_boot_blackjack_trainer.repository.TrainingSessionRepository;
import com.example.spring_boot_blackjack_trainer.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingSessionServiceImpl implements TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public TrainingSessionServiceImpl(TrainingSessionRepository trainingSessionRepository,
                                      UserProfileRepository userProfileRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public TrainingSession createSession(Long userId) {
        UserProfile user = userProfileRepository.findById(userId).orElse(null);
        if (user == null) return null;

        TrainingSession session = new TrainingSession(LocalDate.now(), user);
        return trainingSessionRepository.save(session);
    }

    @Override
    public List<TrainingSession> getSessionsByUser(Long userId) {
        return trainingSessionRepository.findByUserProfileId(userId);
    }

    @Override
    public TrainingSession getSessionById(Long sessionId) {
        return trainingSessionRepository.findById(sessionId).orElse(null);
    }

    @Override
    public void deleteSession(Long sessionId) {
        trainingSessionRepository.deleteById(sessionId);
    }
    @Override
    public void saveSession(TrainingSession session) {
        trainingSessionRepository.save(session);
    }

}
