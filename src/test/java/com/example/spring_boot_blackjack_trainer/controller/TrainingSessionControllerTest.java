package com.example.spring_boot_blackjack_trainer.controller;

import com.example.spring_boot_blackjack_trainer.model.TrainingSession;
import com.example.spring_boot_blackjack_trainer.service.TrainingSessionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingSessionController.class)
@Import(TrainingSessionControllerTest.MockConfig.class)
class TrainingSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainingSessionService sessionService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public TrainingSessionService sessionService() {
            return Mockito.mock(TrainingSessionService.class);
        }
    }

    @Test
    void testCreateSession() throws Exception {
        TrainingSession session = new TrainingSession();
        when(sessionService.createSession(anyLong())).thenReturn(session);

        mockMvc.perform(post("/api/sessions")
                        .param("userId", "1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).createSession(1L);
    }

    @Test
    void testGetSessionsByUser() throws Exception {
        when(sessionService.getSessionsByUser(anyLong())).thenReturn(List.of(new TrainingSession()));

        mockMvc.perform(get("/api/sessions/user/1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).getSessionsByUser(1L);
    }

    @Test
    void testGetSession() throws Exception {
        TrainingSession session = new TrainingSession();
        when(sessionService.getSessionById(anyLong())).thenReturn(session);

        mockMvc.perform(get("/api/sessions/1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).getSessionById(1L);
    }

    @Test
    void testDeleteSession() throws Exception {
        mockMvc.perform(delete("/api/sessions/1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).deleteSession(1L);
    }
}
