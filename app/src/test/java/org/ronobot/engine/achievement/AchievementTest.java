package org.ronobot.engine.achievement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Achievement class.
 */
class AchievementTest {

    private final Achievement achievement;

    public AchievementTest() {
        this.achievement = new Achievement("FirstKill", "Kill your first enemy", 10);
    }

    @Test
    @DisplayName("Achievement is created with given name and description")
    void testCreateWithNameAndDescription() {
        assertNotNull(achievement.getName());
        assertEquals("FirstKill", achievement.getName());
        assertNotNull(achievement.getDescription());
        assertEquals("Kill your first enemy", achievement.getDescription());
    }

    @Test
    @DisplayName("Achievement is created with points")
    void testCreateWithPoints() {
        Achievement scored = new Achievement("HighScore", "Reach high score", 50);
        assertEquals(50, scored.getPoints());
    }

    @Test
    @DisplayName("Achievement getter returns correct values")
    void testGetters() {
        assertEquals("FirstKill", achievement.getName());
        assertEquals("Kill your first enemy", achievement.getDescription());
        assertEquals(10, achievement.getPoints());
        assertFalse(achievement.isUnlocked());
        assertFalse(achievement.isCompleted());
        assertFalse(achievement.isFullyCompleted());
    }

    @Test
    @DisplayName("Achievement unlock returns true on first unlock")
    void testUnlockFirst() {
        assertTrue(achievement.unlock());
        assertTrue(achievement.isUnlocked());
    }

    @Test
    @DisplayName("Achievement unlock returns false after already completed")
    void testUnlockAfterComplete() {
        achievement.unlock();
        achievement.complete();
        assertFalse(achievement.unlock());
    }

    @Test
    @DisplayName("Achievement complete sets completed flag")
    void testComplete() {
        achievement.unlock();
        achievement.complete();
        assertTrue(achievement.isCompleted());
        assertTrue(achievement.isFullyCompleted());
    }

    @Test
    @DisplayName("Achievement reset reverts to not unlocked and not completed")
    void testReset() {
        achievement.unlock();
        achievement.complete();
        achievement.reset();
        assertFalse(achievement.isUnlocked());
        assertFalse(achievement.isCompleted());
        assertFalse(achievement.isFullyCompleted());
    }

    @Test
    @DisplayName("Achievement toString returns expected format")
    void testToString() {
        achievement.unlock();
        String actual = achievement.toString();
        assertTrue(actual.contains("name='FirstKill'"), "Should contain name");
        assertTrue(actual.contains("Kill your first enemy"), "Should contain description");
        assertTrue(actual.contains("unlocked=true"), "Should show unlocked");
        assertTrue(actual.contains("completed=false"), "Should not show completed");
        assertTrue(actual.contains("points=10"), "Should contain points");
    }

    @Test
    @DisplayName("Achievement setPoints updates point value")
    void testSetPoints() {
        Achievement scored = new Achievement("Test", "Test desc", 10);
        scored.setPoints(50);
        assertEquals(50, scored.getPoints());
    }

    @Test
    @DisplayName("New achievement with default values")
    void testNewAchievement() {
        Achievement newAchievement = new Achievement("New", "Description");
        assertNotNull(newAchievement.getName());
        assertNotNull(newAchievement.getDescription());
        assertEquals(0, newAchievement.getPoints());
        assertFalse(newAchievement.isUnlocked());
        assertFalse(newAchievement.isCompleted());
    }
}
