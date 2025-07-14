package com.example.spring_boot_blackjack_trainer.service;

import com.example.spring_boot_blackjack_trainer.model.BlackjackHand;
import com.example.spring_boot_blackjack_trainer.model.TrainingSession;
import com.example.spring_boot_blackjack_trainer.repository.BlackjackHandRepository;
import com.example.spring_boot_blackjack_trainer.repository.TrainingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlackjackHandServiceImplTest {

    @Mock
    private BlackjackHandRepository handRepo;

    @Mock
    private TrainingSessionRepository sessionRepo;

    @InjectMocks
    private BlackjackHandServiceImpl handService;

    private TrainingSession session;

    @BeforeEach
    void setUp() {
        session = new TrainingSession(LocalDate.now(), null);
        session.setHands(Collections.emptyList());
        session.setPlayerHands(Collections.singletonList(Arrays.asList("5", "5")));

        when(sessionRepo.findById(1L)).thenReturn(Optional.of(session));
        when(handRepo.save(any(BlackjackHand.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testSaveHand_HitMove() {
        BlackjackHand result = handService.saveHand(1L, "5,5", "6", "HIT");
        assertNotNull(result);
        assertEquals("HIT", result.getPlayerMove());
        assertTrue(result.isOngoing() || !result.isOngoing());
        assertNotNull(result.getPlayerCards());
    }

    @Test
    void testSaveHand_StandMove() {
        BlackjackHand result = handService.saveHand(1L, "9,8", "7", "STAND");
        assertNotNull(result);
        assertEquals("STAND", result.getPlayerMove());
        assertFalse(result.isOngoing());
    }

    @Test
    void testSaveHand_DoubleMove() {
        BlackjackHand result = handService.saveHand(1L, "4,4", "5", "DOUBLE");
        assertNotNull(result);
        assertEquals("DOUBLE", result.getPlayerMove());
        assertFalse(result.isOngoing());
        assertTrue(result.getPlayerCards().size() >= 3);
    }

    @Test
    void testSaveHand_SplitMove() {
        BlackjackHand result = handService.saveHand(1L, "8,8", "6", "SPLIT");
        assertNotNull(result);
        assertEquals("SPLIT", result.getPlayerMove());
        assertFalse(result.isOngoing());
    }

    @Test
    void testSaveSplitHands_ValidSplit() {
        List<BlackjackHand> splitHands = handService.saveSplitHands(1L, Arrays.asList("8", "8"), "6");
        assertNotNull(splitHands);
        assertEquals(2, splitHands.size());
        assertEquals("Split Hand 1", splitHands.get(0).getPlayerMove());
        assertEquals("Split Hand 2", splitHands.get(1).getPlayerMove());
        assertTrue(splitHands.get(0).isOngoing());
        assertTrue(splitHands.get(1).isOngoing());
    }
}
