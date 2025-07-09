package com.example.spring_boot_blackjack_trainer.controller;

import com.example.spring_boot_blackjack_trainer.model.BlackjackHand;
import com.example.spring_boot_blackjack_trainer.service.BlackjackHandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hands")
public class BlackjackHandController {

    private final BlackjackHandService handService;

    @Autowired
    public BlackjackHandController(BlackjackHandService handService) {
        this.handService = handService;
    }

    @PostMapping
    public BlackjackHand saveHand(@RequestParam Long sessionId,
                                  @RequestParam String playerHand,
                                  @RequestParam String dealerCard,
                                  @RequestParam String playerMove) {
        return handService.saveHand(sessionId, playerHand, dealerCard, playerMove);
    }

    @GetMapping("/session.html/{sessionId}")
    public List<BlackjackHand> getHandsBySession(@PathVariable Long sessionId) {
        return handService.getHandsBySession(sessionId);
    }

    @DeleteMapping("/{handId}")
    public void deleteHand(@PathVariable Long handId) {
        handService.deleteHand(handId);
    }
}
