package org.ronobot.engine.collision;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Entity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CollisionResult.
 * <p>
 * Tests verify collision result structure and entity references.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
 */
@DisplayName("CollisionResult Tests")
class CollisionResultTest {

    @Nested
    @DisplayName("Creation")
    class Creation {

        @DisplayName("CollisionResult is created")
        @Test
        void testCreation() {
            Entity entityA = new Entity(1, 100f, 100f, 32, 64, "entityA");
            Entity entityB = new Entity(2, 150f, 150f, 32, 64, "entityB");
            CollisionResult result = new CollisionResult(entityA, entityB);

            assertNotNull(result);
            assertNotNull(result.getEntityA());
            assertNotNull(result.getEntityB());
        }

        @DisplayName("CollisionResult can be created without explicit names")
        @Test
        void testCreationAnonymous() {
            Entity entityA = new Entity(1, 100f, 100f, 32, 64, "entityA");
            Entity entityB = new Entity(2, 150f, 150f, 32, 64, "entityB");
            CollisionResult result = new CollisionResult(entityA, entityB);

            assertNotNull(result);
            assertNotNull(result.getEntityA());
            assertNotNull(result.getEntityB());
        }
    }

    @Nested
    @DisplayName("Entity A getter")
    class EntityAGetter {

        @DisplayName("Entity A getter returns correct entity")
        @Test
        void testGetEntityA() {
            Entity entityA = new Entity(1, 100f, 100f, 32, 64, "entityA");
            Entity entityB = new Entity(2, 150f, 150f, 32, 64, "entityB");
            CollisionResult result = new CollisionResult(entityA, entityB);

            assertEquals(entityA, result.getEntityA());
        }
    }

    @Nested
    @DisplayName("Entity B getter")
    class EntityBGetter {

        @DisplayName("Entity B getter returns correct entity")
        @Test
        void testGetEntityB() {
            Entity entityA = new Entity(1, 100f, 100f, 32, 64, "entityA");
            Entity entityB = new Entity(2, 150f, 150f, 32, 64, "entityB");
            CollisionResult result = new CollisionResult(entityA, entityB);

            assertEquals(entityB, result.getEntityB());
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToString {

        @DisplayName("CollisionResult toString includes entity names")
        @Test
        void testToString() {
            Entity entityA = new Entity(1, 100f, 100f, 32, 64, "entityA");
            Entity entityB = new Entity(2, 150f, 150f, 32, 64, "entityB");
            CollisionResult result = new CollisionResult(entityA, entityB);

            String actual = result.toString();

            assertTrue(actual.contains("entityA"));
            assertTrue(actual.contains("entityB"));
        }
    }
}
