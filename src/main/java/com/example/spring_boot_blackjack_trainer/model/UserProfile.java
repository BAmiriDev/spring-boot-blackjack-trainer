package com.example.spring_boot_blackjack_trainer.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int totalHandsPlayed;
    private int correctMoves;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingSession> trainingSessions;

    public UserProfile() {}

    public UserProfile(String username) {
        this.username = username;
        this.totalHandsPlayed = 0;
        this.correctMoves = 0;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalHandsPlayed() {
        return totalHandsPlayed;
    }

    public void setTotalHandsPlayed(int totalHandsPlayed) {
        this.totalHandsPlayed = totalHandsPlayed;
    }

    public int getCorrectMoves() {
        return correctMoves;
    }

    public void setCorrectMoves(int correctMoves) {
        this.correctMoves = correctMoves;
    }

    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }
}

