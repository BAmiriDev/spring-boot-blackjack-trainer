package com.example.spring_boot_blackjack_trainer.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicStrategyTest {

    @Test
    void testRecommendedMove_HardTotal() {
        String move = BasicStrategy.getRecommendedMove("10", "7");
        assertEquals("Hit", move);

        move = BasicStrategy.getRecommendedMove("8", "10");
        assertEquals("Hit", move);

        move = BasicStrategy.getRecommendedMove("12", "3");
        assertEquals("Hit", move);

        move = BasicStrategy.getRecommendedMove("13", "6");
        assertEquals("Stand", move);
    }

    @Test
    void testRecommendedMove_SoftTotal() {
        String move = BasicStrategy.getRecommendedMove("ace,7", "3");
        assertEquals("Double", move);

        move = BasicStrategy.getRecommendedMove("ace,7", "9");
        assertEquals("Hit", move);

        move = BasicStrategy.getRecommendedMove("ace,6", "2");
        assertEquals("Hit", move);
    }

    @Test
    void testRecommendedMove_PairSplitting() {
        String move = BasicStrategy.getRecommendedMove("8,8", "9");
        assertEquals("Split", move);

        move = BasicStrategy.getRecommendedMove("9,9", "9");
        assertEquals("Split", move);

        move = BasicStrategy.getRecommendedMove("9,9", "7");
        assertEquals("Stand", move);

        move = BasicStrategy.getRecommendedMove("9,9", "10");
        assertEquals("Stand", move);

        move = BasicStrategy.getRecommendedMove("2,2", "3");
        assertEquals("Split", move);

        move = BasicStrategy.getRecommendedMove("2,2", "8");
        assertEquals("Hit", move);

        move = BasicStrategy.getRecommendedMove("5,5", "6");
        assertEquals("Double", move);

        move = BasicStrategy.getRecommendedMove("10,10", "5");
        assertEquals("Stand", move);
    }
}
