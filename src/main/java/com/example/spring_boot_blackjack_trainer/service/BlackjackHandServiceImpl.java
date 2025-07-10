package com.example.spring_boot_blackjack_trainer.service;

import com.example.spring_boot_blackjack_trainer.model.BlackjackHand;
import com.example.spring_boot_blackjack_trainer.model.TrainingSession;
import com.example.spring_boot_blackjack_trainer.repository.BlackjackHandRepository;
import com.example.spring_boot_blackjack_trainer.repository.TrainingSessionRepository;
import com.example.spring_boot_blackjack_trainer.util.BasicStrategy;
import com.example.spring_boot_blackjack_trainer.util.HandGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BlackjackHandServiceImpl implements BlackjackHandService {

    private final BlackjackHandRepository handRepository;
    private final TrainingSessionRepository sessionRepository;

    @Autowired
    public BlackjackHandServiceImpl(BlackjackHandRepository handRepository,
                                    TrainingSessionRepository sessionRepository) {
        this.handRepository = handRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public BlackjackHand saveHand(Long sessionId, String playerHandStr, String dealerCardStr, String playerMove) {
        TrainingSession session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) return null;

        List<String> playerCards = new ArrayList<>(Arrays.asList(playerHandStr.split(",")));
        List<String> dealerCards = new ArrayList<>(Arrays.asList(dealerCardStr.split(",")));

        BlackjackHand hand = new BlackjackHand(session);
        hand.setPlayerCards(new ArrayList<>(playerCards));
        hand.setDealerCards(new ArrayList<>(dealerCards));
        hand.setPlayerMove(playerMove.toUpperCase());

        switch (playerMove.toUpperCase()) {
            case "HIT":
                String hitCard = HandGenerator.dealCard();
                playerCards.add(hitCard);
                hand.setPlayerCards(playerCards);

                if (HandGenerator.calculateTotal(playerCards) > 21) {
                    hand.setOngoing(false);
                    // dealer does not draw mid-split
                    if (session.getPlayerHands().size() == 1) {
                        revealDealer(dealerCards);
                        playDealer(dealerCards);
                        hand.setDealerCards(dealerCards);
                    }
                    resolveHand(hand, session);
                } else {
                    hand.setOngoing(true);
                }
                break;

            case "STAND":
                hand.setOngoing(false);
                // dealer only draws if this is the last hand
                if (session.getPlayerHands().size() == 1 || session.getCurrentHandIndex() == session.getPlayerHands().size() - 1) {
                    playDealer(dealerCards);
                    hand.setDealerCards(dealerCards);
                }
                resolveHand(hand, session);
                break;

            case "DOUBLE":
                if (playerCards.size() == 2) {
                    String doubleCard = HandGenerator.dealCard();
                    playerCards.add(doubleCard);
                    hand.setPlayerCards(playerCards);
                    hand.setOngoing(false);

                    // dealer only draws if this is the last hand
                    if (session.getPlayerHands().size() == 1 || session.getCurrentHandIndex() == session.getPlayerHands().size() - 1) {
                        playDealer(dealerCards);
                        hand.setDealerCards(dealerCards);
                    }
                    resolveHand(hand, session);
                } else {
                    hand.setOngoing(false);
                    hand.setCorrect(false);
                    hand.setCorrectMove("Invalid double (only allowed on 2 cards)");
                }
                break;

            case "SPLIT":
                hand.setOngoing(false);
                hand.setCorrect(false);
                hand.setCorrectMove("Split move handled elsewhere");
                break;

            default:
                hand.setOngoing(false);
                hand.setCorrect(false);
                hand.setCorrectMove("Unknown move");
        }

        return handRepository.save(hand);
    }

    @Override
    public List<BlackjackHand> saveSplitHands(Long sessionId, List<String> playerCards, String dealerCardStr) {
        List<BlackjackHand> splitHands = new ArrayList<>();
        TrainingSession session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) return splitHands;

        if (playerCards.size() == 2 && playerCards.get(0).equals(playerCards.get(1))) {
            String card1 = playerCards.get(0);
            String card2 = playerCards.get(1);
            List<String> dealerCards = new ArrayList<>(Arrays.asList(dealerCardStr.split(",")));

            List<String> hand1Cards = new ArrayList<>(Arrays.asList(card1, HandGenerator.dealCard()));
            BlackjackHand hand1 = new BlackjackHand(session);
            hand1.setPlayerCards(hand1Cards);
            hand1.setDealerCards(dealerCards);
            hand1.setPlayerMove("Split Hand 1");
            hand1.setOngoing(true);
            handRepository.save(hand1);
            splitHands.add(hand1);

            List<String> hand2Cards = new ArrayList<>(Arrays.asList(card2, HandGenerator.dealCard()));
            BlackjackHand hand2 = new BlackjackHand(session);
            hand2.setPlayerCards(hand2Cards);
            hand2.setDealerCards(dealerCards);
            hand2.setPlayerMove("Split Hand 2");
            hand2.setOngoing(true);
            handRepository.save(hand2);
            splitHands.add(hand2);
        }

        return splitHands;
    }

    @Override
    public List<BlackjackHand> getHandsBySession(Long sessionId) {
        return handRepository.findBySessionId(sessionId);
    }

    @Override
    public void deleteHand(Long handId) {
        handRepository.deleteById(handId);
    }

    @Override
    public List<String> playMoveOnHand(List<String> playerHand, List<String> dealerCards, String playerMove) {
        List<String> updatedHand = new ArrayList<>(playerHand);

        switch (playerMove.toUpperCase()) {
            case "HIT":
                updatedHand.add(HandGenerator.dealCard());
                break;

            case "DOUBLE":
                if (updatedHand.size() == 2) {
                    updatedHand.add(HandGenerator.dealCard());
                }
                break;

            case "STAND":
                // No card drawn for player
                break;
        }

        // Do NOT draw dealer cards here; it is handled after both split hands are played
        return updatedHand;
    }

    private void playDealer(List<String> dealerCards) {
        while (HandGenerator.calculateTotal(dealerCards) < 17) {
            dealerCards.add(HandGenerator.dealCard());
        }
    }

    private void revealDealer(List<String> dealerCards) {
        if (dealerCards.size() == 1) {
            dealerCards.add(HandGenerator.dealCard());
        }
    }

    private void resolveHand(BlackjackHand hand, TrainingSession session) {
        String correctMove = BasicStrategy.getRecommendedMove(
                String.join(",", hand.getPlayerCards()),
                hand.getDealerCards().get(0)
        );
        hand.setCorrectMove(correctMove);
        hand.setCorrect(hand.getPlayerMove().equalsIgnoreCase(correctMove));

        session.setTotalHands(session.getTotalHands() + 1);
        if (hand.isCorrect()) {
            session.setCorrectMoves(session.getCorrectMoves() + 1);
        }
        sessionRepository.save(session);
    }
}
