package com.example.spring_boot_blackjack_trainer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HandGenerator {

    private static final List<String> cards = Arrays.asList(
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"
    );

    private static final List<String> suits = Arrays.asList("clubs", "diamonds", "hearts", "spades");

    private static final Random rand = new Random();

    public static String dealCard() {
        String value = cards.get(rand.nextInt(cards.size()));
        String suit = suits.get(rand.nextInt(suits.size()));

        switch (value) {
            case "A": return "ace_of_" + suit;
            case "K": return "king_of_" + suit;
            case "Q": return "queen_of_" + suit;
            case "J": return "jack_of_" + suit;
            default: return value + "_of_" + suit;
        }
    }

    public static List<String> dealPlayerHand() {
        List<String> hand = new ArrayList<>();
        hand.add(dealCard());
        hand.add(dealCard());
        return hand;
    }

    public static List<String> dealDealerHand() {
        List<String> hand = new ArrayList<>();
        hand.add(dealCard());
        return hand;
    }

    public static int calculateTotal(List<String> hand) {
        int total = 0;
        int aces = 0;

        for (String card : hand) {
            if (card.startsWith("ace")) {
                total += 11;
                aces++;
            } else if (card.startsWith("king") || card.startsWith("queen") || card.startsWith("jack")) {
                total += 10;
            } else {
                total += Integer.parseInt(card.split("_", 2)[0]);
            }
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }
}