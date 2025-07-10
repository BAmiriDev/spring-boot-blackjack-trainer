package com.example.spring_boot_blackjack_trainer.controller.web;

import com.example.spring_boot_blackjack_trainer.model.TrainingSession;
import com.example.spring_boot_blackjack_trainer.model.UserProfile;
import com.example.spring_boot_blackjack_trainer.service.BlackjackHandService;
import com.example.spring_boot_blackjack_trainer.service.TrainingSessionService;
import com.example.spring_boot_blackjack_trainer.service.UserProfileService;
import com.example.spring_boot_blackjack_trainer.util.HandGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/web")
public class WebController {

    @Autowired
    private UserProfileService userService;

    @Autowired
    private TrainingSessionService sessionService;

    @Autowired
    private BlackjackHandService handService;

    @GetMapping
    public String home() {
        return "home";
    }

    @PostMapping("/start")
    public String createUserAndStart(@RequestParam String username) {
        UserProfile user = userService.createUser(username);
        TrainingSession session = sessionService.createSession(user.getId());
        sessionService.saveSession(session);
        return "redirect:/web/session/" + session.getId();
    }

    @GetMapping("/session/{id}")
    public String showSession(@PathVariable Long id, Model model) {
        TrainingSession session = sessionService.getSessionById(id);
        List<String> playerHand = HandGenerator.dealPlayerHand();
        List<String> dealerHand = HandGenerator.dealDealerHand();

        List<List<String>> playerHands = new ArrayList<>();
        playerHands.add(new ArrayList<>(playerHand));
        session.setPlayerHands(playerHands);
        session.setCurrentHandIndex(0);
        session.setRoundOngoing(true);
        sessionService.saveSession(session);

        boolean canSplit = canSplit(playerHand.get(0), playerHand.get(1));

        model.addAttribute("sessionId", id);
        model.addAttribute("dealerCards", dealerHand);
        model.addAttribute("playerCards1", playerHand);
        model.addAttribute("playerCards2", null);
        model.addAttribute("activeHand", 1);
        model.addAttribute("feedback", null);
        model.addAttribute("ongoing", true);
        model.addAttribute("canSplit", canSplit);

        return "session";
    }

    @PostMapping("/session/{id}/play")
    public String play(@PathVariable Long id,
                       @RequestParam List<String> dealerCards,
                       @RequestParam String playerMove,
                       Model model) {

        TrainingSession session = sessionService.getSessionById(id);
        int currentHandIndex = session.getCurrentHandIndex();
        List<List<String>> playerHands = session.getPlayerHands();
        List<String> currentHand = new ArrayList<>(playerHands.get(currentHandIndex));
        String feedback = "";

        if (!session.isRoundOngoing()) {
            feedback = "⛔ Round is over. Please deal a new hand.";
        } else if (playerMove.equalsIgnoreCase("Split")) {
            if (currentHand.size() == 2 && canSplit(currentHand.get(0), currentHand.get(1)) && playerHands.size() == 1) {
                List<String> splitHand1 = new ArrayList<>(Arrays.asList(currentHand.get(0), HandGenerator.dealCard()));
                List<String> splitHand2 = new ArrayList<>(Arrays.asList(currentHand.get(1), HandGenerator.dealCard()));
                playerHands.set(0, splitHand1);
                playerHands.add(splitHand2);
                session.setPlayerHands(playerHands);
                session.setCurrentHandIndex(0);
                session.setRoundOngoing(true);
                feedback = "✂️ Split complete. Playing first hand.";
            } else {
                feedback = "❌ Cannot split. Must be a pair or 10/J/Q/K cards.";
            }
        } else if (Arrays.asList("Hit", "Stand", "Double").contains(playerMove)) {

            if (playerMove.equalsIgnoreCase("Double") && currentHand.size() != 2) {
                feedback = "❌ Cannot double after hitting. Correct: Hit or Stand.";
            } else {
                String correctMove = getCorrectMove(currentHand, dealerCards);
                boolean isCorrect = playerMove.equalsIgnoreCase(correctMove);
                feedback = isCorrect ? "✅ Correct!" : "❌ Incorrect. Correct: " + correctMove;

                session.setTotalHands(session.getTotalHands() + 1);
                if (isCorrect) {
                    session.setCorrectMoves(session.getCorrectMoves() + 1);
                }

                currentHand = handService.playMoveOnHand(currentHand, dealerCards, playerMove);
                playerHands.set(currentHandIndex, currentHand);

                boolean handFinished = playerMove.equalsIgnoreCase("Stand")
                        || HandGenerator.calculateTotal(currentHand) > 21
                        || playerMove.equalsIgnoreCase("Double");

                if (handFinished) {
                    if (playerHands.size() > currentHandIndex + 1) {
                        session.setCurrentHandIndex(currentHandIndex + 1);
                    } else {
                        while (HandGenerator.calculateTotal(dealerCards) < 17) {
                            dealerCards.add(HandGenerator.dealCard());
                        }
                        session.setRoundOngoing(false);
                    }
                }
            }
        } else {
            feedback = "Invalid move.";
        }

        sessionService.saveSession(session);

        model.addAttribute("sessionId", id);
        model.addAttribute("dealerCards", dealerCards);
        model.addAttribute("playerCards1", playerHands.get(0));
        model.addAttribute("playerCards2", playerHands.size() > 1 ? playerHands.get(1) : null);
        model.addAttribute("activeHand", session.getCurrentHandIndex() + 1);
        model.addAttribute("feedback", feedback);
        model.addAttribute("ongoing", session.isRoundOngoing());
        model.addAttribute("canSplit", playerHands.size() == 1 && canSplit(playerHands.get(0).get(0), playerHands.get(0).get(1)));

        return "session";
    }

    @GetMapping("/session/{id}/deal")
    public String dealNewHand(@PathVariable Long id) {
        return "redirect:/web/session/" + id;
    }

    @GetMapping("/session/{id}/summary")
    public String showSummary(@PathVariable Long id, Model model) {
        TrainingSession session = sessionService.getSessionById(id);
        UserProfile user = userService.getUserById(session.getUserProfile().getId());

        int totalHands = session.getTotalHands();
        int correctMoves = session.getCorrectMoves();
        int wrongMoves = totalHands - correctMoves;
        double accuracy = totalHands > 0 ? (correctMoves * 100.0 / totalHands) : 0.0;

        model.addAttribute("username", user.getUsername());
        model.addAttribute("correct", correctMoves);
        model.addAttribute("wrong", wrongMoves);
        model.addAttribute("accuracy", String.format("%.2f", accuracy));
        model.addAttribute("total", totalHands);

        return "summary";
    }

    private boolean canSplit(String card1, String card2) {
        List<String> tens = Arrays.asList("10", "J", "Q", "K");
        String rank1 = getRank(card1);
        String rank2 = getRank(card2);
        return rank1.equals(rank2) || (tens.contains(rank1) && tens.contains(rank2));
    }

    private String getRank(String card) {
        String cleaned = card.trim().toUpperCase();
        if (cleaned.startsWith("ACE")) return "A";
        if (cleaned.startsWith("KING")) return "K";
        if (cleaned.startsWith("QUEEN")) return "Q";
        if (cleaned.startsWith("JACK")) return "J";
        if (cleaned.startsWith("10")) return "10";
        return cleaned.split("_", 2)[0];
    }

    private String getCorrectMove(List<String> playerHand, List<String> dealerCards) {
        String player = String.join(",", playerHand);
        String dealer = dealerCards.get(0);
        return com.example.spring_boot_blackjack_trainer.util.BasicStrategy.getRecommendedMove(player, dealer);
    }
}