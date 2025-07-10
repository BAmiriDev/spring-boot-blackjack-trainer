package com.example.spring_boot_blackjack_trainer.controller.web;

import com.example.spring_boot_blackjack_trainer.model.UserProfile;
import com.example.spring_boot_blackjack_trainer.model.TrainingSession;
import com.example.spring_boot_blackjack_trainer.service.UserProfileService;
import com.example.spring_boot_blackjack_trainer.service.TrainingSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web")
public class WebController {

    private final UserProfileService userService;
    private final TrainingSessionService sessionService;

    public WebController(UserProfileService userService, TrainingSessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String home() {
        return "home";
    }

    @PostMapping("/start")
    public String createUserAndStart(@RequestParam String username) {
        UserProfile user = userService.createUser(username);
        TrainingSession session = sessionService.createSession(user.getId());
        return "redirect:/web/session/" + session.getId();
    }

    @GetMapping("/session/{id}")
    public String showSession(@PathVariable Long id) {
        return "session";
    }
}
