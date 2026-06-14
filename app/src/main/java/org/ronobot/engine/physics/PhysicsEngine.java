package org.ronobot.engine.physics;

import org.ronobot.engine.collision.CollisionManager;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Velocity;
import org.ronobot.engine.math.Position;

import java.util.List;

/**
 * Physics engine for entity interactions, collision response, and movement.
 *
 * @author ronobot
 */
public class PhysicsEngine {

    /**
     * The collision manager for detection.
     */
    private final CollisionManager collisionManager;

    /**
     * Creates a new PhysicsEngine with the given collision manager.
     *
     * @param collisionManager the collision manager
     */
    public PhysicsEngine(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    /**
     * Creates a PhysicsEngine without a collision manager (for direct entity management).
     */
    public PhysicsEngine() {
        this.collisionManager = null;
    }

    /**
     * Updates physics for all entities.
     *
     * @param deltaTime time in milliseconds
     */
    public void update(double deltaTime) {
        if (collisionManager == null) {
            return;
        }

        // Update physics - collisionManager handles entity movement
        // Convert ms to seconds for collision manager
        float deltaTimeSeconds = (float) (deltaTime / 1000.0);
        collisionManager.findAndResolveCollisions(deltaTimeSeconds);
    }

    /**
     * Applies impulse to entity (e.g., from explosion or powerup).
     *
     * @param entity the entity
     * @param impulseX impulse in X direction
     * @param impulseY impulse in Y direction
     */
    public void applyImpulse(Entity entity, double impulseX, double impulseY) {
        Velocity vel = entity.getVelocity();
        if (vel != null) {
            vel.applyForce(impulseX, impulseY);
        }
    }

    /**
     * Stops entity movement (for deaths, pickups, etc.).
     *
     * @param entity the entity
     */
    public void stopMovement(Entity entity) {
        Velocity vel = entity.getVelocity();
        if (vel != null) {
            vel.setX(0f);
            vel.setY(0f);
        }
    }

    /**
     * Gets the collision manager.
     *
     * @return the collision manager, or null if not set
     */
    public CollisionManager getCollisionManager() {
        return collisionManager;
    }
}
