package org.ronobot.engine.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PlayerEntity subclass.
 * <p>
 * Tests verify player-specific behavior including health management,
 * armor, and basic entity functionality.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
 */
@DisplayName("PlayerEntity Tests")
class PlayerEntityTest {

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @DisplayName("PlayerEntity is created successfully")
        @Test
        void testCreation() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            assertNotNull(player);
            assertEquals("Player", player.getName());
            assertTrue(player instanceof Entity);
        }

        @DisplayName("PlayerEntity has default health")
        @Test
        void testDefaultHealth() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            assertEquals(100, player.getHealth());
        }

        @DisplayName("PlayerEntity has default max health")
        @Test
        void testDefaultMaxHealth() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            assertEquals(100, player.getMaxHealth());
        }

        @DisplayName("PlayerEntity has default armor")
        @Test
        void testDefaultArmor() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            assertEquals(0, player.getArmor());
        }

        @DisplayName("PlayerEntity can be created with custom size")
        @Test
        void testCustomSize() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f, 64, 128);
            assertEquals(64, player.getWidth());
            assertEquals(128, player.getHeight());
        }
    }

    @Nested
    @DisplayName("Damage")
    class DamageTests {

        @DisplayName("PlayerEntity can take damage")
        @Test
        void testTakeDamage() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            int initialHealth = player.getHealth();
            int damage = 25;
            player.takeDamage(damage);
            assertEquals(initialHealth - damage, player.getHealth());
        }

        @DisplayName("PlayerEntity cannot go below zero health")
        @Test
        void testHealthFloor() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            player.setHealth(10);
            player.takeDamage(100);
            assertEquals(0, player.getHealth());
        }

        @DisplayName("Healing increases health")
        @Test
        void testHeal() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            player.setHealth(50);
            player.heal(30);
            assertEquals(80, player.getHealth());
        }

        @DisplayName("PlayerEntity cannot exceed max health")
        @Test
        void testHealthCeiling() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            player.setHealth(80);
            player.heal(50);
            assertEquals(100, player.getHealth());
        }

        @DisplayName("PlayerEntity dies at zero health")
        @Test
        void testDeath() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            player.setHealth(0);
            assertTrue(player.isDead());
        }

        @DisplayName("PlayerEntity resurrected at max health")
        @Test
        void testResurrection() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            player.setHealth(0);
            player.resurrect();
            assertEquals(100, player.getHealth());
            assertFalse(player.isDead());
        }
    }

    @Nested
    @DisplayName("State")
    class StateTests {

        @DisplayName("Player transitions from alive to dead")
        @Test
        void testAliveToDead() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            player.setHealth(90);
            assertFalse(player.isDead());
            player.takeDamage(100);
            assertTrue(player.isDead());
        }

        @DisplayName("Player transitions from dead to alive")
        @Test
        void testDeadToAlive() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            player.setHealth(0);
            assertTrue(player.isDead());
            player.resurrect();
            assertFalse(player.isDead());
        }

        @DisplayName("Player cannot die twice")
        @Test
        void testAlreadyDead() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            player.setHealth(0);
            assertTrue(player.isDead());
            player.takeDamage(100);
            assertTrue(player.isDead());
        }
    }

    @Nested
    @DisplayName("Inheritance")
    class InheritanceTests {

        @DisplayName("PlayerEntity extends Entity")
        @Test
        void testExtendsEntity() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            assertTrue(player instanceof Entity);
        }

        @DisplayName("PlayerEntity has entity position")
        @Test
        void testHasPosition() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            assertEquals(100.0f, player.getX(), 0.01f);
            assertEquals(100.0f, player.getY(), 0.01f);
        }

        @DisplayName("PlayerEntity has entity size")
        @Test
        void testHasSize() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            assertEquals(32, player.getWidth());
            assertEquals(32, player.getHeight());
        }
    }

    @Nested
    @DisplayName("Serialization")
    class SerializationTests {

        @DisplayName("PlayerEntity has toString with health")
        @Test
        void testToString() {
            PlayerEntity player = new PlayerEntity(1, 100f, 100f);
            String actual = player.toString();
            assertTrue(actual.contains("Player"));
            assertTrue(actual.contains("health"));
            assertTrue(actual.contains("weapon"));
        }
    }
}
