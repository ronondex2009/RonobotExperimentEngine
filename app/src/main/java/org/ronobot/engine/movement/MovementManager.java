package org.ronobot.engine.movement;

import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entity.EnemyEntity;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.entity.Entity;
import org.ronobot.engine.math.Velocity;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages movement for multiple entities.
 *
 * @author ronobot
 */
public class MovementManager {

    /**
     * Maps entities to their velocity controllers.
     */
    private final Map<Entity, Velocity> velocityMap = new HashMap<>();

    /**
     * Creates a new MovementManager.
     */
    public MovementManager() {
    }

    /**
     * Adds an entity to the movement manager.
     *
     * @param entity the entity to manage
     */
    public void addEntity(Entity entity) {
        if (!velocityMap.containsKey(entity)) {
            velocityMap.put(entity, Velocity.ZERO);
        }
    }

    /**
     * Removes an entity from movement management.
     *
     * @param entity the entity to remove
     */
    public void removeEntity(Entity entity) {
        velocityMap.remove(entity);
    }

    /**
     * Removes all entities from movement management.
     */
    public void clear() {
        velocityMap.clear();
    }

    /**
     * Gets the Velocity for an entity.
     *
     * @param entity the entity
     * @return the Velocity, or null if not managed
     */
    public Velocity getVelocity(Entity entity) {
        return velocityMap.get(entity);
    }

    /**
     * Applies a force to an entity's velocity.
     *
     * @param entity the entity to apply force to
     * @param forceX force in X direction
     * @param forceY force in Y direction
     */
    public void applyForce(Entity entity, double forceX, double forceY) {
        Velocity velocity = velocityMap.get(entity);
        if (velocity != null) {
            velocity.applyForce(forceX, forceY);
        }
    }

    /**
     * Sets the velocity of an entity.
     *
     * @param entity the entity to set velocity for
     * @param vx velocity X
     * @param vy velocity Y
     */
    public void setVelocity(Entity entity, double vx, double vy) {
        Velocity velocity = velocityMap.get(entity);
        if (velocity != null) {
            velocity.setX((float) vx);
            velocity.setY((float) vy);
        }
    }

    /**
     * Gets the number of entities being managed.
     *
     * @return the count
     */
    public int size() {
        return velocityMap.size();
    }

    /**
     * Checks if an entity is being managed.
     *
     * @param entity the entity
     * @return true if managed
     */
    public boolean contains(Entity entity) {
        return velocityMap.containsKey(entity);
    }

    /**
     * Clears velocity for all entities.
     */
    public void clearAllVelocity() {
        for (Velocity velocity : velocityMap.values()) {
            velocity.setX(0f);
            velocity.setY(0f);
        }
    }
}
