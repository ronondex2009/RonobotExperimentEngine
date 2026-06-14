package org.ronobot.engine.collision;

import org.ronobot.engine.core.Entity;

/**
 * Represents a collision detection result between two entities.
 * <p>
 * This class holds the pair of entities that are detected to be colliding
 * along with metadata about the collision.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class CollisionResult {

    /**
     * First entity involved in the collision.
     */
    private final Entity entityA;

    /**
     * Second entity involved in the collision.
     */
    private final Entity entityB;

    /**
     * Creates a new collision result with the given entities.
     *
     * @param entityA The first entity.
     * @param entityB The second entity.
     */
    public CollisionResult(Entity entityA, Entity entityB) {
        this.entityA = entityA;
        this.entityB = entityB;
    }

    /**
     * Returns the first entity involved in the collision.
     *
     * @return The first entity.
     */
    public Entity getEntityA() {
        return entityA;
    }

    /**
     * Returns the second entity involved in the collision.
     *
     * @return The second entity.
     */
    public Entity getEntityB() {
        return entityB;
    }

    /**
     * Returns a string representation of the collision result.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        return String.format("CollisionResult(entityA=%s, entityB=%s)", entityA.getName(), entityB.getName());
    }
}
