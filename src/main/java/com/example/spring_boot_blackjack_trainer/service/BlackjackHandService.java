package com.example.spring_boot_blackjack_trainer.service;

import com.example.spring_boot_blackjack_trainer.model.BlackjackHand;


import java.util.List;

public interface BlackjackHandService {
    BlackjackHand saveHand(Long sessionId, String playerHand, String dealerCard, String playerMove);
    List<BlackjackHand> getHandsBySession(Long sessionId);
    void deleteHand(Long handId);
    List<BlackjackHand> saveSplitHands(Long sessionId, List<String> playerCards, String dealerCardStr);
    List<String> playMoveOnHand(List<String> playerHand, List<String> dealerCards, String playerMove);


}
