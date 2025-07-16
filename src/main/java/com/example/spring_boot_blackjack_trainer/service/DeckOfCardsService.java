package com.example.spring_boot_blackjack_trainer.service;

public interface DeckOfCardsService {
    String createNewDeck();
    String drawCard(String deckId);
}
