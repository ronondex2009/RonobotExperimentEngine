package org.ronobot.engine.collision;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CollisionManager.
 * <p>
 * Tests collision detection between various entity types.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
 */
@DisplayName("CollisionManager Tests")
class CollisionManagerTest {

    @Nested
    @DisplayName("Entity registration")
    class Registration {

        @DisplayName("Entity can be registered")
        @Test
        void testRegisterEntity() {
            Entity entity = new Entity(1, 0f, 0f, 32, 32, "entity1");
            CollisionManager manager = new CollisionManager();
            manager.registerEntity(entity);

            assertEquals(1, manager.getEntityCount());
            assertTrue(manager.isEntityRegistered(entity));
        }

        @DisplayName("Entity can be unregistered")
        @Test
        void testUnregisterEntity() {
            Entity entity = new Entity(1, 0f, 0f, 32, 32, "entity1");
            CollisionManager manager = new CollisionManager();
            manager.registerEntity(entity);
            manager.unregisterEntity(entity);

            assertEquals(0, manager.getEntityCount());
            assertFalse(manager.isEntityRegistered(entity));
        }

        @DisplayName("Manager can find collisions")
        @Test
        void testFindCollisions() {
            Entity entity1 = new Entity(1, 0f, 0f, 32, 32, "entity1");
            Entity entity2 = new Entity(2, 30f, 30f, 32, 32, "entity2");
            CollisionManager manager = new CollisionManager();
            manager.registerEntity(entity1);
            manager.registerEntity(entity2);

            List<CollisionResult> collisions = manager.findCollisions();
            assertNotNull(collisions);
            assertEquals(1, collisions.size());
        }
    }

    @Nested
    @DisplayName("Collision detection")
    class CollisionDetection {

        @DisplayName("Colliding entities return collision result")
        @Test
        void testCollidingEntities() {
            Entity entity1 = new Entity(1, 0f, 0f, 32, 32, "entity1");
            Entity entity2 = new Entity(2, 30f, 30f, 32, 32, "entity2");
            CollisionManager manager = new CollisionManager();
            manager.registerEntity(entity1);
            manager.registerEntity(entity2);

            List<CollisionResult> collisions = manager.findCollisions();

            assertNotNull(collisions);
            assertNotNull(collisions.get(0));
        }

        @DisplayName("Non-colliding entities return no collision")
        @Test
        void testNonCollidingEntities() {
            Entity entity1 = new Entity(1, 0f, 0f, 32, 32, "entity1");
            Entity entity2 = new Entity(2, 100f, 0f, 32, 32, "entity2");
            CollisionManager manager = new CollisionManager();
            manager.registerEntity(entity1);
            manager.registerEntity(entity2);

            List<CollisionResult> collisions = manager.findCollisions();

            assertNotNull(collisions);
            assertEquals(0, collisions.size());
        }
    }

    @Nested
    @DisplayName("Manager lifecycle")
    class Lifecycle {

        @DisplayName("Manager can clear all entities")
        @Test
        void testClear() {
            Entity entity = new Entity(1, 0f, 0f, 32, 32, "entity1");
            CollisionManager manager = new CollisionManager();
            manager.registerEntity(entity);
            manager.clear();

            assertEquals(0, manager.getEntityCount());
        }

        @DisplayName("Empty manager returns no collisions")
        @Test
        void testEmptyManager() {
            CollisionManager manager = new CollisionManager();

            List<CollisionResult> collisions = manager.findCollisions();

            assertNotNull(collisions);
            assertEquals(0, collisions.size());
        }
    }

    @Nested
    @DisplayName("Dead entity handling")
    class DeadEntities {

        @DisplayName("Dead entities are not checked for collision")
        @Test
        void testDeadEntity() {
            Entity entity1 = new Entity(1, 0f, 0f, 32, 32, "entity1");
            Entity entity2 = new Entity(2, 30f, 30f, 32, 32, "entity2");

            // Mark entity1 as dead
            entity1.setHealth(0);

            CollisionManager manager = new CollisionManager();
            manager.registerEntity(entity1);
            manager.registerEntity(entity2);

            List<CollisionResult> collisions = manager.findCollisions();

            // Should not find collision since entity1 is dead
            assertEquals(0, collisions.size());
        }
    }

    @Nested
    @DisplayName("All collisions including duplicates")
    class AllCollisions {

        @DisplayName("Manager can find all collisions including duplicates")
        @Test
        void testAllCollisions() {
            Entity entity1 = new Entity(1, 0f, 0f, 32, 32, "entity1");
            Entity entity2 = new Entity(2, 30f, 30f, 32, 32, "entity2");
            CollisionManager manager = new CollisionManager();
            manager.registerEntity(entity1);
            manager.registerEntity(entity2);

            List<CollisionResult> collisions = manager.findAllCollisionsIncludingDuplicates();

            assertNotNull(collisions);
            // Should have at least one collision
            assertTrue(collisions.size() >= 1);
        }
    }

    @Nested
    @DisplayName("Registered entities")
    class RegisteredEntities {

        @DisplayName("Manager can get all registered entities")
        @Test
        void testGetRegisteredEntities() {
            Entity entity1 = new Entity(1, 0f, 0f, 32, 32, "entity1");
            Entity entity2 = new Entity(2, 30f, 30f, 32, 32, "entity2");
            CollisionManager manager = new CollisionManager();
            manager.registerEntity(entity1);
            manager.registerEntity(entity2);

            List<Entity> entities = manager.getRegisteredEntities();

            assertNotNull(entities);
            assertEquals(2, entities.size());
        }
    }
}
