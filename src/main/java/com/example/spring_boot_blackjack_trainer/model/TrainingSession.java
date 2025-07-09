package com.example.spring_boot_blackjack_trainer.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate sessionDate;
    private int totalHands;
    private int correctMoves;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlackjackHand> hands;

    @ElementCollection
    private List<List<String>> playerHands = new ArrayList<>();

    private int currentHandIndex = 0;

    private boolean roundOngoing = true;


    public TrainingSession() {}

    public TrainingSession(LocalDate sessionDate, UserProfile userProfile) {
        this.sessionDate = sessionDate;
        this.userProfile = userProfile;
        this.totalHands = 0;
        this.correctMoves = 0;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public int getTotalHands() {
        return totalHands;
    }

    public void setTotalHands(int totalHands) {
        this.totalHands = totalHands;
    }

    public int getCorrectMoves() {
        return correctMoves;
    }

    public void setCorrectMoves(int correctMoves) {
        this.correctMoves = correctMoves;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<BlackjackHand> getHands() {
        return hands;
    }

    public void setHands(List<BlackjackHand> hands) {
        this.hands = hands;
    }

    public List<List<String>> getPlayerHands() {
        return playerHands;
    }

    public void setPlayerHands(List<List<String>> playerHands) {
        this.playerHands = playerHands;
    }

    public int getCurrentHandIndex() {
        return currentHandIndex;
    }

    public void setCurrentHandIndex(int currentHandIndex) {
        this.currentHandIndex = currentHandIndex;
    }

    public boolean isRoundOngoing() {
        return roundOngoing;
    }

    public void setRoundOngoing(boolean roundOngoing) {
        this.roundOngoing = roundOngoing;
    }


}
