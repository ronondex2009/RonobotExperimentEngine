package org.ronobot.engine.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Projectile entity.
 * <p>
 * Tests verify projectile-specific behavior including damage,
 * lifetime management, and basic entity functionality.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
 */
@DisplayName("Projectile Tests")
class ProjectileTest {

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @DisplayName("Projectile is created successfully")
        @Test
        void testCreation() {
            Projectile projectile = new Projectile(new Position(100, 100), new Size(16, 16), 10, "projectile");
            assertNotNull(projectile);
            assertEquals("projectile", projectile.getName());
            assertTrue(projectile instanceof Entity);
        }

        @DisplayName("Projectile without name uses default")
        @Test
        void testNoName() {
            Projectile projectile = new Projectile(new Position(100, 100), new Size(16, 16), 10);
            assertEquals("Projectile", projectile.getName());
            assertEquals(10, projectile.getDamage());
        }

        @DisplayName("Projectile damage is non-negative")
        @Test
        void testDamageNonNegative() {
            Projectile projectile = new Projectile(new Position(100, 100), new Size(16, 16), -10);
            assertEquals(0, projectile.getDamage());
        }

        @DisplayName("Projectile has default lifetime")
        @Test
        void testDefaultLifetime() {
            Projectile projectile = new Projectile(new Position(100, 100), new Size(16, 16), 10);
            assertEquals(60, projectile.getLifetime());
        }
    }

    @Nested
    @DisplayName("Damage")
    class DamageTests {

        @DisplayName("Projectile damage can be set")
        @Test
        void testSetDamage() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 10);
            projectile.setDamage(50);
            assertEquals(50, projectile.getDamage());
        }

        @DisplayName("Projectile damage cannot be negative")
        @Test
        void testNonNegativeDamage() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 10);
            projectile.setDamage(-10);
            assertEquals(0, projectile.getDamage());
        }

        @DisplayName("Projectile damage with constructor")
        @Test
        void testConstructorDamage() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 20);
            assertEquals(20, projectile.getDamage());
        }
    }

    @Nested
    @DisplayName("Lifetime")
    class LifetimeTests {

        @DisplayName("Projectile lifetime decreases each frame")
        @Test
        void testLifetimeDecreases() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 100);
            projectile.setLifetime(10);
            projectile.update();
            assertEquals(9, projectile.getLifetime());
        }

        @DisplayName("Projectile dies when lifetime reaches zero")
        @Test
        void testProjectileDies() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 10);
            projectile.setLifetime(5);
            for (int i = 0; i < 5; i++) {
                projectile.update();
            }
            // After 5 updates, lifetime should be 0
            assertEquals(0, projectile.getLifetime());
        }

        @DisplayName("Projectile survives with positive lifetime")
        @Test
        void testProjectileSurvives() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 100);
            projectile.setLifetime(100);
            projectile.update();
            assertFalse(projectile.isDead());
            assertEquals(99, projectile.getLifetime());
        }

        @DisplayName("Projectile with zero lifetime is already dead")
        @Test
        void testZeroLifetimeDies() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 10);
            projectile.setLifetime(0);
            assertTrue(projectile.isDead());
        }
    }

    @Nested
    @DisplayName("Position")
    class PositionTests {

        @DisplayName("Projectile has initial position")
        @Test
        void testInitialPosition() {
            Projectile projectile = new Projectile(new Position(100, 100), new Size(10, 10), 10, "p");
            assertEquals(100.0f, projectile.getX(), 0.01f);
            assertEquals(100.0f, projectile.getY(), 0.01f);
        }
    }

    @Nested
    @DisplayName("Size")
    class SizeTests {

        @DisplayName("Projectile has initial size")
        @Test
        void testInitialSize() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(16, 16), 10);
            assertEquals(16, projectile.getWidth());
            assertEquals(16, projectile.getHeight());
        }
    }

    @Nested
    @DisplayName("Inheritance")
    class InheritanceTests {

        @DisplayName("Projectile extends Entity")
        @Test
        void testExtendsEntity() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 10);
            assertTrue(projectile instanceof Entity);
        }

        @DisplayName("Projectile has entity health")
        @Test
        void testHasHealth() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 10);
            assertEquals(100, projectile.getHealth());
        }
    }

    @Nested
    @DisplayName("Serialization")
    class SerializationTests {

        @DisplayName("Projectile toString includes key fields")
        @Test
        void testToString() {
            Projectile projectile = new Projectile(new Position(0, 0), new Size(10, 10), 10);
            String actual = projectile.toString();
            assertTrue(actual.contains("damage"));
            assertTrue(actual.contains("lifetime"));
        }
    }
}
