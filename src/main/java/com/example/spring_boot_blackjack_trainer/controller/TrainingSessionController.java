package com.example.spring_boot_blackjack_trainer.controller;

import com.example.spring_boot_blackjack_trainer.model.TrainingSession;
import com.example.spring_boot_blackjack_trainer.service.TrainingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class TrainingSessionController {

    private final TrainingSessionService sessionService;

    @Autowired
    public TrainingSessionController(TrainingSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public TrainingSession createSession(@RequestParam Long userId) {
        return sessionService.createSession(userId);
    }

    @GetMapping("/user/{userId}")
    public List<TrainingSession> getSessionsByUser(@PathVariable Long userId) {
        return sessionService.getSessionsByUser(userId);
    }

    @GetMapping("/{sessionId}")
    public TrainingSession getSession(@PathVariable Long sessionId) {
        return sessionService.getSessionById(sessionId);
    }

    @DeleteMapping("/{sessionId}")
    public void deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
    }
}
