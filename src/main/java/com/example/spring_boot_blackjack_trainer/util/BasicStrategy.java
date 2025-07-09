package com.example.spring_boot_blackjack_trainer.util;

import java.util.Arrays;
import java.util.List;

public class BasicStrategy {

    private static final List<String> tens = Arrays.asList("10", "J", "Q", "K");

    public static String getRecommendedMove(String playerHand, String dealerCard) {
        String[] cards = playerHand.split(",");
        int dealer = parseCardValue(dealerCard);
        int total = calculateTotal(cards);
        boolean isSoft = isSoft(cards);
        boolean isPair = isPair(cards);

        // Blackjack detection
        if (cards.length == 2 && hasAce(cards) && hasTenValue(cards)) {
            return "Stand";
        }

        // Pair Splitting
        if (cards.length == 2 && isPair) {
            String rank = getRank(cards[0]);
            if (rank.equals("A")) return "Split";
            if (tens.contains(rank)) {
                // Never split 10s, play as hard 20
                return getHardTotalMove(total, dealer, cards.length);
            }
            if (rank.equals("9")) return (dealer >= 2 && dealer <= 6) || dealer == 8 || dealer == 9 ? "Split" : getHardTotalMove(total, dealer, cards.length);
            if (rank.equals("8")) return "Split";
            if (rank.equals("7")) return dealer >= 2 && dealer <= 7 ? "Split" : getHardTotalMove(total, dealer, cards.length);
            if (rank.equals("6")) return dealer >= 2 && dealer <= 6 ? "Split" : getHardTotalMove(total, dealer, cards.length);
            if (rank.equals("4")) return dealer == 5 || dealer == 6 ? "Split" : getHardTotalMove(total, dealer, cards.length);
            if (rank.equals("3") || rank.equals("2")) return dealer >= 2 && dealer <= 7 ? "Split" : getHardTotalMove(total, dealer, cards.length);
            if (rank.equals("5")) return getHardTotalMove(total, dealer, cards.length);
        }

        // Soft Totals
        if (isSoft) {
            return getSoftTotalMove(total, dealer, cards.length);
        }

        // Hard Totals
        return getHardTotalMove(total, dealer, cards.length);
    }

    private static String getHardTotalMove(int total, int dealer, int handSize) {
        if (total >= 17) return "Stand";
        if (total >= 13 && dealer >= 2 && dealer <= 6) return "Stand";
        if (total == 12 && dealer >= 4 && dealer <= 6) return "Stand";
        if (handSize == 2) {
            if (total == 11) return "Double";
            if (total == 10 && dealer >= 2 && dealer <= 9) return "Double";
            if (total == 9 && dealer >= 3 && dealer <= 6) return "Double";
        }
        return "Hit";
    }

    private static String getSoftTotalMove(int total, int dealer, int handSize) {
        switch (total) {
            case 20:
            case 19:
                return "Stand";
            case 18:
                if (handSize == 2 && dealer >= 3 && dealer <= 6) return "Double";
                if (dealer == 2 || dealer == 7 || dealer == 8) return "Stand";
                return "Hit";
            case 17:
            case 16:
            case 15:
                if (handSize == 2 && dealer >= 4 && dealer <= 6) return "Double";
                return "Hit";
            case 14:
            case 13:
                if (handSize == 2 && dealer >= 5 && dealer <= 6) return "Double";
                return "Hit";
            default:
                return "Hit";
        }
    }

    private static int calculateTotal(String[] cards) {
        int total = 0;
        int aces = 0;
        for (String card : cards) {
            if (card.startsWith("ace")) {
                aces++;
                total += 11;
            } else if (card.startsWith("king") || card.startsWith("queen") || card.startsWith("jack") || card.startsWith("10")) {
                total += 10;
            } else {
                int value = Integer.parseInt(card.split("_", 2)[0]);
                total += value;
            }
        }
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }
        return total;
    }

    private static boolean isSoft(String[] cards) {
        int aces = 0;
        int total = 0;
        for (String card : cards) {
            if (card.startsWith("ace")) {
                aces++;
                total += 11;
            } else if (card.startsWith("king") || card.startsWith("queen") || card.startsWith("jack") || card.startsWith("10")) {
                total += 10;
            } else {
                int value = Integer.parseInt(card.split("_", 2)[0]);
                total += value;
            }
        }
        return aces > 0 && total <= 21;
    }

    private static boolean isPair(String[] cards) {
        if (cards.length != 2) return false;
        String rank1 = getRank(cards[0]);
        String rank2 = getRank(cards[1]);
        return rank1.equals(rank2) || (tens.contains(rank1) && tens.contains(rank2));
    }

    private static boolean hasAce(String[] cards) {
        for (String card : cards) {
            if (card.startsWith("ace")) return true;
        }
        return false;
    }

    private static boolean hasTenValue(String[] cards) {
        for (String card : cards) {
            if (card.startsWith("10") || card.startsWith("jack") || card.startsWith("queen") || card.startsWith("king")) {
                return true;
            }
        }
        return false;
    }

    private static String getRank(String card) {
        String cleaned = card.trim().toUpperCase();
        if (cleaned.startsWith("ACE")) return "A";
        if (cleaned.startsWith("KING")) return "K";
        if (cleaned.startsWith("QUEEN")) return "Q";
        if (cleaned.startsWith("JACK")) return "J";
        if (cleaned.startsWith("10")) return "10";
        return cleaned.split("_", 2)[0];
    }

    private static int parseCardValue(String card) {
        card = card.trim().toLowerCase();
        if (card.startsWith("ace")) return 11;
        if (card.startsWith("king") || card.startsWith("queen") || card.startsWith("jack") || card.startsWith("10")) return 10;
        return Integer.parseInt(card.split("_", 2)[0]);
    }
}
