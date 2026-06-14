package org.ronobot.engine.physics;

import org.ronobot.engine.collision.CollisionManager;
import org.ronobot.engine.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PhysicsEngine class.
 *
 * @author ronobot
 */
class PhysicsEngineTest {

    private PhysicsEngine physicsEngine;
    private CollisionManager collisionManager;

    @BeforeEach
    void setUp() {
        collisionManager = new CollisionManager();
        physicsEngine = new PhysicsEngine(collisionManager);
    }

    @Test
    void testPhysicsEngineCreationWithCollisionManager() {
        assertNotNull(physicsEngine);
        assertNotNull(physicsEngine.getCollisionManager());
        assertEquals(collisionManager, physicsEngine.getCollisionManager());
    }

    @Test
    void testPhysicsEngineCreationWithoutCollisionManager() {
        PhysicsEngine noMgrEngine = new PhysicsEngine();
        assertNull(noMgrEngine.getCollisionManager());
    }

    @Test
    void testUpdateWithNullCollisionManager() {
        PhysicsEngine noMgrEngine = new PhysicsEngine();
        
        // Should not throw exception
        noMgrEngine.update(16.0);
    }

    @Test
    void testUpdateCallsCollisionDetection() {
        // Create entities
        Entity entity1 = new Entity(1, 0f, 0f, 32, 32);
        Entity entity2 = new Entity(2, 30f, 30f, 32, 32); // Overlaps with entity1
        
        collisionManager.registerEntity(entity1);
        collisionManager.registerEntity(entity2);
        
        physicsEngine.update(16.0);
        
        // Should complete without errors
    }

    @Test
    void testUpdateWithEmptyEntities() {
        collisionManager.clear();
        physicsEngine.update(16.0);
    }

    @Test
    void testPhysicsEngineIsolation() {
        PhysicsEngine engine1 = new PhysicsEngine(null);
        PhysicsEngine engine2 = new PhysicsEngine(new CollisionManager());
        
        // Each engine should be independent
        engine2.update(16.0);
        engine1.update(16.0);
    }
}
