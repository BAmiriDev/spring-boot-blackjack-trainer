package com.example.spring_boot_blackjack_trainer.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandGeneratorTest {

    @Test
    void testDealPlayerHand() {
        List<String> playerHand = HandGenerator.dealPlayerHand();
        assertEquals(2, playerHand.size(), "Player hand should have 2 cards");
    }

    @Test
    void testDealDealerHand() {
        List<String> dealerHand = HandGenerator.dealDealerHand();
        assertEquals(1, dealerHand.size(), "Dealer hand should have 1 card");
    }

    @Test
    void testDealCard() {
        String card = HandGenerator.dealCard();
        assertNotNull(card, "Dealt card should not be null");
        assertFalse(card.isEmpty(), "Dealt card should not be empty");
    }

    @Test
    void testCalculateTotal_NoAces() {
        List<String> hand = List.of("2_clubs", "3_hearts", "4_spades");
        int total = HandGenerator.calculateTotal(hand);
        assertEquals(9, total, "Total should be 9");
    }

    @Test
    void testCalculateTotal_WithAceBelow21() {
        List<String> hand = List.of("ace_spades", "7_clubs");
        int total = HandGenerator.calculateTotal(hand);
        assertEquals(18, total, "Total should be 18 (soft)");
    }

    @Test
    void testCalculateTotal_WithAceAbove21() {
        List<String> hand = List.of("ace_spades", "king_clubs", "queen_hearts");
        int total = HandGenerator.calculateTotal(hand);
        assertEquals(21, total, "Total should be 21 after adjusting Ace to 1");
    }

    @Test
    void testCalculateTotal_MultipleAces() {
        List<String> hand = List.of("ace_spades", "ace_hearts", "8_clubs");
        int total = HandGenerator.calculateTotal(hand);
        assertEquals(20, total, "Total should correctly adjust Aces to prevent busting");
    }
}
