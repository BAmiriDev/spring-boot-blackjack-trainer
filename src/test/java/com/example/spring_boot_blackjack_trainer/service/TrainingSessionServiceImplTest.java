package com.example.spring_boot_blackjack_trainer.service;

import com.example.spring_boot_blackjack_trainer.model.TrainingSession;
import com.example.spring_boot_blackjack_trainer.model.UserProfile;
import com.example.spring_boot_blackjack_trainer.repository.TrainingSessionRepository;
import com.example.spring_boot_blackjack_trainer.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingSessionServiceImplTest {

    @Mock
    private TrainingSessionRepository sessionRepo;

    @Mock
    private UserProfileRepository userRepo;

    @InjectMocks
    private TrainingSessionServiceImpl sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSession() {
        UserProfile user = new UserProfile("testUser");
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        TrainingSession session = new TrainingSession(LocalDate.now(), user);
        when(sessionRepo.save(any(TrainingSession.class))).thenReturn(session);

        TrainingSession created = sessionService.createSession(1L);

        assertNotNull(created);
        assertEquals(user, created.getUserProfile());
        verify(userRepo, times(1)).findById(1L);
        verify(sessionRepo, times(1)).save(any(TrainingSession.class));
    }

    @Test
    void testCreateSession_UserNotFound() {
        when(userRepo.findById(2L)).thenReturn(Optional.empty());

        TrainingSession created = sessionService.createSession(2L);

        assertNull(created);
        verify(userRepo, times(1)).findById(2L);
        verify(sessionRepo, times(0)).save(any());
    }

    @Test
    void testGetSessionsByUser() {
        UserProfile user = new UserProfile("multiSessions");
        TrainingSession session1 = new TrainingSession(LocalDate.now(), user);
        TrainingSession session2 = new TrainingSession(LocalDate.now(), user);

        when(sessionRepo.findByUserProfileId(1L)).thenReturn(Arrays.asList(session1, session2));

        List<TrainingSession> sessions = sessionService.getSessionsByUser(1L);

        assertEquals(2, sessions.size());
        verify(sessionRepo, times(1)).findByUserProfileId(1L);
    }

    @Test
    void testGetSessionById() {
        UserProfile user = new UserProfile("findSession");
        TrainingSession session = new TrainingSession(LocalDate.now(), user);

        when(sessionRepo.findById(1L)).thenReturn(Optional.of(session));

        TrainingSession found = sessionService.getSessionById(1L);

        assertNotNull(found);
        assertEquals(session, found);
        verify(sessionRepo, times(1)).findById(1L);
    }

    @Test
    void testDeleteSession() {
        sessionService.deleteSession(1L);
        verify(sessionRepo, times(1)).deleteById(1L);
    }

    @Test
    void testSaveSession() {
        TrainingSession session = mock(TrainingSession.class);
        sessionService.saveSession(session);

        verify(sessionRepo, times(1)).save(session);
    }
}
