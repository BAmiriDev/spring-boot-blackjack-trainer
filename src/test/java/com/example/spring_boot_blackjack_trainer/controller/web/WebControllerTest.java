package com.example.spring_boot_blackjack_trainer.controller.web;

import com.example.spring_boot_blackjack_trainer.model.TrainingSession;
import com.example.spring_boot_blackjack_trainer.model.UserProfile;
import com.example.spring_boot_blackjack_trainer.service.BlackjackHandService;
import com.example.spring_boot_blackjack_trainer.service.TrainingSessionService;
import com.example.spring_boot_blackjack_trainer.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserProfileService userService;

    @Autowired
    private TrainingSessionService sessionService;

    @Autowired
    private BlackjackHandService handService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserProfileService userProfileService() {
            return Mockito.mock(UserProfileService.class);
        }

        @Bean
        TrainingSessionService trainingSessionService() {
            return Mockito.mock(TrainingSessionService.class);
        }

        @Bean
        BlackjackHandService blackjackHandService() {
            return Mockito.mock(BlackjackHandService.class);
        }
    }

    @Test
    void testHome() throws Exception {
        mockMvc.perform(get("/web"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    void testCreateUserAndStart() throws Exception {
        UserProfile user = new UserProfile("testuser");
        TrainingSession session = new TrainingSession(LocalDate.now(), user);

        when(userService.createUser("testuser")).thenReturn(user);
        when(sessionService.createSession(user.getId())).thenReturn(session);

        mockMvc.perform(post("/web/start").param("username", "testuser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/web/session/*"));
    }

    @Test
    void testShowSummary() throws Exception {
        UserProfile user = new UserProfile("testuser");
        TrainingSession session = new TrainingSession(LocalDate.now(), user);

        session.setTotalHands(10);
        session.setCorrectMoves(7);

        when(sessionService.getSessionById(1L)).thenReturn(session);
        when(userService.getUserById(Mockito.any())).thenReturn(user);

        mockMvc.perform(get("/web/session/1/summary"))
                .andExpect(status().isOk())
                .andExpect(view().name("summary"))
                .andExpect(model().attributeExists("username", "correct", "wrong", "accuracy", "total"))
                .andExpect(model().attribute("username", "testuser"))
                .andExpect(model().attribute("correct", 7))
                .andExpect(model().attribute("wrong", 3))
                .andExpect(model().attribute("accuracy", "70.00"))
                .andExpect(model().attribute("total", 10));
    }
}
