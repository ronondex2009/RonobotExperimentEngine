package org.ronobot.engine.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Entity base class.
 * <p>
 * Tests cover entity lifecycle, collision box management,
 * and basic behavior verification.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
 */
@DisplayName("Entity Tests")
class EntityTest {

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @DisplayName("Entity is created successfully")
        @Test
        void testCreation() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50), "testEntity");
            assertNotNull(entity);
            assertEquals("testEntity", entity.getName());
        }

        @DisplayName("Entity name defaults to class name")
        @Test
        void testDefaultName() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            assertEquals("Entity", entity.getName());
        }

        @DisplayName("Entity position is set correctly")
        @Test
        void testPosition() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            assertEquals(100.0f, entity.getX(), 0.01f);
            assertEquals(100.0f, entity.getY(), 0.01f);
        }

        @DisplayName("Entity size is set correctly")
        @Test
        void testSize() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            assertEquals(50, entity.getWidth());
            assertEquals(50, entity.getHeight());
        }
    }

    @Nested
    @DisplayName("Position")
    class PositionTests {

        @DisplayName("Position update updates entity")
        @Test
        void testPositionUpdate() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            entity.setPosition(200.0f, 200.0f);

            assertEquals(200.0f, entity.getX(), 0.01f);
            assertEquals(200.0f, entity.getY(), 0.01f);
        }
    }

    @Nested
    @DisplayName("Size")
    class SizeTests {

        @DisplayName("Size update updates entity")
        @Test
        void testSizeUpdate() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            entity.setSize(100, 100);

            assertEquals(100, entity.getWidth());
            assertEquals(100, entity.getHeight());
        }
    }

    @Nested
    @DisplayName("Collision box")
    class CollisionBoxTests {

        @DisplayName("Get collision box width")
        @Test
        void testGetCollisionBoxWidth() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            assertNotNull(entity.getCollisionBox());
            assertEquals(50, entity.getCollisionBox().getWidth());
        }

        @DisplayName("Get collision box height")
        @Test
        void testGetCollisionBoxHeight() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            assertEquals(50, entity.getCollisionBox().getHeight());
        }

        @DisplayName("Get collision box center X")
        @Test
        void testCollisionBoxCenterX() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            double expectedCenterX = entity.getX() + entity.getWidth() / 2.0;
            assertEquals(expectedCenterX, entity.getCollisionBox().getCenterX(), 0.01f);
        }

        @DisplayName("Get collision box center Y")
        @Test
        void testCollisionBoxCenterY() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            double expectedCenterY = entity.getY() + entity.getHeight() / 2.0;
            assertEquals(expectedCenterY, entity.getCollisionBox().getCenterY(), 0.01f);
        }
    }

    @Nested
    @DisplayName("Serialization")
    class SerializationTests {

        @DisplayName("Entity toString includes name and position")
        @Test
        void testToString() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50), "testEntity");
            String actual = entity.toString();
            assertTrue(actual.contains("testEntity"));
            assertTrue(actual.contains("100"));
        }

        @DisplayName("Entity hashCode is consistent")
        @Test
        void testHashCode() {
            Entity entity = new Entity(1, new Position(100, 100), new Size(50, 50));
            int hash1 = entity.hashCode();
            int hash2 = entity.hashCode();
            assertEquals(hash1, hash2);
        }
    }

    @Nested
    @DisplayName("Lifecycle")
    class LifecycleTests {

        @DisplayName("Entity can be created and destroyed")
        @Test
        void testLifecycle() {
            Entity disposable = new Entity(1, new Position(0, 0), new Size(0, 0));
            assertNotNull(disposable);
            disposable.setSize(0, 0);
        }
    }
}
