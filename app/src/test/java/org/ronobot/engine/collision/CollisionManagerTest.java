package org.ronobot.engine.collision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CollisionManager class.
 * <p>
 * Tests collision detection, entity registration, and collision resolution.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
@DisplayName("CollisionManager Tests")
class CollisionManagerTest {

    private CollisionManager manager;

    @BeforeEach
    void setUp() {
        this.manager = new CollisionManager();
    }

    @Test
    @DisplayName("Collision detection works between overlapping entities")
    void testCollisionDetection() {
        Entity entity1 = createEntity("entity1", 0f, 0f, 10f, 10f);
        Entity entity2 = createEntity("entity2", 5f, 5f, 10f, 10f);

        assertTrue(manager.registerEntity(entity1));
        assertTrue(manager.registerEntity(entity2));

        List<CollisionResult> collisions = manager.findCollisions();

        assertTrue(collisions.size() >= 1, "Should detect collision between overlapping entities");
    }

    @Test
    @DisplayName("No collision detected for non-overlapping entities")
    void testNoCollision() {
        Entity entity1 = createEntity("entity1", 0f, 0f, 10f, 10f);
        Entity entity2 = createEntity("entity2", 100f, 100f, 10f, 10f);

        assertTrue(manager.registerEntity(entity1));
        assertTrue(manager.registerEntity(entity2));

        List<CollisionResult> collisions = manager.findCollisions();

        assertTrue(collisions.isEmpty(), "Should not detect collision for non-overlapping entities");
    }

    @Test
    @DisplayName("Entity registration returns false for null entity")
    void testRegisterNullEntity() {
        assertFalse(manager.registerEntity(null));
    }

    @Test
    @DisplayName("Entity registration returns false for duplicate name")
    void testRegisterDuplicateEntity() {
        Entity entity = createEntity("test", 0f, 0f, 10f, 10f);
        assertTrue(manager.registerEntity(entity));
        
        Entity duplicate = createEntity("test", 0f, 0f, 10f, 10f);
        assertFalse(manager.registerEntity(duplicate), "Should not register duplicate entity");
    }

    @Test
    @DisplayName("Entity count matches registered entities")
    void testEntityCount() {
        Entity entity1 = createEntity("e1", 0f, 0f, 10f, 10f);
        Entity entity2 = createEntity("e2", 0f, 0f, 10f, 10f);
        Entity entity3 = createEntity("e3", 0f, 0f, 10f, 10f);

        assertTrue(manager.registerEntity(entity1));
        assertTrue(manager.registerEntity(entity2));
        
        assertEquals(2, manager.getEntityCount());
        
        assertTrue(manager.registerEntity(entity3));
        assertEquals(3, manager.getEntityCount());
    }

    @Test
    @DisplayName("Unregister removes entity from manager")
    void testUnregisterEntity() {
        Entity entity = createEntity("test", 0f, 0f, 10f, 10f);
        assertTrue(manager.registerEntity(entity));
        assertEquals(1, manager.getEntityCount());
        
        assertTrue(manager.unregisterEntity(entity));
        assertEquals(0, manager.getEntityCount());
    }

    @Test
    @DisplayName("Unregister returns false for unregistered entity")
    void testUnregisterUnregistered() {
        Entity entity = createEntity("test", 0f, 0f, 10f, 10f);
        assertFalse(manager.unregisterEntity(entity));
    }

    @Test
    @DisplayName("findCollisions returns empty list for empty manager")
    void testFindCollisionsEmpty() {
        List<CollisionResult> collisions = manager.findCollisions();
        assertTrue(collisions.isEmpty());
    }

    @Test
    @DisplayName("findAndResolveCollisions returns collisions list")
    void testFindAndResolveCollisions() {
        Entity entity1 = createEntity("e1", 0f, 0f, 10f, 10f);
        Entity entity2 = createEntity("e2", 5f, 5f, 10f, 10f);

        assertTrue(manager.registerEntity(entity1));
        assertTrue(manager.registerEntity(entity2));

        List<CollisionResult> collisions = manager.findAndResolveCollisions(0.1f);

        assertTrue(collisions.size() >= 1, "Should find collisions");
    }

    @Test
    @DisplayName("clear removes all entities and notifications")
    void testClear() {
        Entity entity1 = createEntity("e1", 0f, 0f, 10f, 10f);
        Entity entity2 = createEntity("e2", 5f, 5f, 10f, 10f);

        assertTrue(manager.registerEntity(entity1));
        assertTrue(manager.registerEntity(entity2));
        
        manager.findCollisions();
        
        assertEquals(2, manager.getEntityCount());
        
        manager.clear();
        
        assertEquals(0, manager.getEntityCount());
    }

    @Test
    @DisplayName("toString returns expected format")
    void testToString() {
        Entity entity1 = createEntity("e1", 0f, 0f, 10f, 10f);
        Entity entity2 = createEntity("e2", 5f, 5f, 10f, 10f);

        assertTrue(manager.registerEntity(entity1));
        assertTrue(manager.registerEntity(entity2));
        
        String result = manager.toString();
        
        assertTrue(result.contains("entityCount"));
        assertTrue(result.contains("notificationCount"));
    }

    @Test
    @DisplayName("isEntityRegistered returns correct status")
    void testIsEntityRegistered() {
        Entity entity1 = createEntity("e1", 0f, 0f, 10f, 10f);
        Entity entity2 = createEntity("e2", 5f, 5f, 10f, 10f);

        assertTrue(manager.registerEntity(entity1));
        assertTrue(manager.registerEntity(entity2));

        assertTrue(manager.isEntityRegistered(entity1));
        assertTrue(manager.isEntityRegistered(entity2));
    }

    @Test
    @DisplayName("isEntityRegistered returns false for null entity")
    void testIsEntityRegisteredNull() {
        assertFalse(manager.isEntityRegistered(null));
    }

    @Test
    @DisplayName("findCollisions returns empty list for null entity query")
    void testFindCollisionsNull() {
        List<CollisionResult> collisions = manager.findCollisions();
        assertTrue(collisions.isEmpty());
    }

    private Entity createEntity(String name, float x, float y, float width, float height) {
        Entity entity = new Entity(0, x, y, (int)width, (int)height, name);
        return entity;
    }
}
