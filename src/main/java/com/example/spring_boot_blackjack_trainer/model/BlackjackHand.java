package com.example.spring_boot_blackjack_trainer.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class BlackjackHand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> playerCards = new ArrayList<>();

    @ElementCollection
    private List<String> dealerCards = new ArrayList<>();

    private String playerMove;
    private String correctMove;
    private boolean isCorrect;
    private boolean ongoing = true;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private TrainingSession session;

    public BlackjackHand() {}

    public BlackjackHand(TrainingSession session) {
        this.session = session;
    }

    // Convenience methods
    public void addPlayerCard(String card) {
        this.playerCards.add(card);
    }

    public void addDealerCard(String card) {
        this.dealerCards.add(card);
    }

    public Long getId() {
        return id;
    }

    public List<String> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(List<String> playerCards) {
        this.playerCards = playerCards;
    }

    public List<String> getDealerCards() {
        return dealerCards;
    }

    public void setDealerCards(List<String> dealerCards) {
        this.dealerCards = dealerCards;
    }

    public String getPlayerMove() {
        return playerMove;
    }

    public void setPlayerMove(String playerMove) {
        this.playerMove = playerMove;
    }

    public String getCorrectMove() {
        return correctMove;
    }

    public void setCorrectMove(String correctMove) {
        this.correctMove = correctMove;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public TrainingSession getSession() {
        return session;
    }

    public void setSession(TrainingSession session) {
        this.session = session;
    }
}
