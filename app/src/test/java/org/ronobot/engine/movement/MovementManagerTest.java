package org.ronobot.engine.movement;

import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entity.EnemyEntity;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MovementManager class.
 *
 * @author ronobot
 */
class MovementManagerTest {

    private MovementManager manager;

    @BeforeEach
    void setUp() {
        manager = new MovementManager();
    }

    @Test
    void testMovementManagerCreation() {
        assertNotNull(manager);
        assertEquals(0, manager.size());
    }

    @Test
    void testAddEntity() {
        Entity entity = new Entity(1, 0f, 0f, 16, 16);
        manager.addEntity(entity);

        assertTrue(manager.contains(entity));
        assertEquals(1, manager.size());
        assertNotNull(manager.getVelocity(entity));
    }

    @Test
    void testRemoveEntity() {
        Entity entity = new Entity(1, 0f, 0f, 16, 16);
        manager.addEntity(entity);

        assertEquals(1, manager.size());
        manager.removeEntity(entity);
        assertEquals(0, manager.size());
        assertFalse(manager.contains(entity));
    }

    @Test
    void testClear() {
        Entity entity1 = new Entity(1, 0f, 0f, 16, 16);
        Entity entity2 = new Entity(2, 10f, 10f, 16, 16);
        manager.addEntity(entity1);
        manager.addEntity(entity2);

        assertEquals(2, manager.size());
        manager.clear();
        assertEquals(0, manager.size());
        assertFalse(manager.contains(entity1));
        assertFalse(manager.contains(entity2));
    }

    @Test
    void testApplyForce() {
        Entity entity = new Entity(1, 0f, 0f, 16, 16);
        manager.addEntity(entity);

        Velocity velocity = manager.getVelocity(entity);
        velocity.applyForce(100.0, 0.0);

        assertEquals(10.0, velocity.getX(), 0.001);
        assertEquals(0.0, velocity.getY(), 0.001);
    }

    @Test
    void testSetVelocity() {
        Entity entity = new Entity(1, 0f, 0f, 16, 16);
        manager.addEntity(entity);

        manager.setVelocity(entity, 50.0, 50.0);

        assertEquals(50.0, manager.getVelocity(entity).getX(), 0.001);
        assertEquals(50.0, manager.getVelocity(entity).getY(), 0.001);
    }

    @Test
    void testClearAllVelocity() {
        Entity entity1 = new Entity(1, 0f, 0f, 16, 16);
        Entity entity2 = new Entity(2, 10f, 10f, 16, 16);
        manager.addEntity(entity1);
        manager.addEntity(entity2);

        // Give them some velocity
        manager.setVelocity(entity1, 100.0, 0.0);
        manager.setVelocity(entity2, 0.0, 50.0);

        manager.clearAllVelocity();

        assertEquals(0.0, manager.getVelocity(entity1).getX(), 0.001);
        assertEquals(0.0, manager.getVelocity(entity1).getY(), 0.001);
        assertEquals(0.0, manager.getVelocity(entity2).getX(), 0.001);
        assertEquals(0.0, manager.getVelocity(entity2).getY(), 0.001);
    }
}
