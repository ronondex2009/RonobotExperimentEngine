package org.ronobot.engine.physics;

import org.ronobot.engine.collision.CollisionManager;

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

        // Update physics stub - collisionManager handles entity movement
        collisionManager.findAndResolveCollisions((float) deltaTime / 1000.0);
    }

    /**
     * Applies impulse to entity (e.g., from explosion or powerup).
     *
     * @param entity the entity
     * @param impulseX impulse in X direction
     * @param impulseY impulse in Y direction
     */
    public void applyImpulse(Object entity, double impulseX, double impulseY) {
        // For now, this is a stub
    }

    /**
     * Stops entity movement (for deaths, pickups, etc.).
     *
     * @param entity the entity
     */
    public void stopMovement(Object entity) {
        // For now, just a stub
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
