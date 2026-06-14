package org.ronobot.engine.collision;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Rectangle;

/**
 * Represents a collision detection result between two entities.
 * <p>
 * This class holds the pair of entities that are detected to be colliding
 * along with metadata about the collision including position data and
 * resolution information for visualization.
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
     * Collision position x.
     */
    private float positionX;

    /**
     * Collision position y.
     */
    private float positionY;

    /**
     * Normal vector x component.
     */
    private float normalX;

    /**
     * Normal vector y component.
     */
    private float normalY;

    /**
     * Whether the collision was resolved.
     */
    private boolean resolved = false;

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
     * Sets the collision position.
     *
     * @param position The collision position
     */
    public void setPosition(float position) {
        this.positionX = position;
        this.positionY = 0;
    }

    /**
     * Sets the collision position x and y.
     *
     * @param x The x position
     * @param y The y position
     */
    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
    }

    /**
     * Sets the normal vector.
     *
     * @param normalX The normal x component
     * @param normalY The normal y component
     */
    public void setNormal(float normalX, float normalY) {
        this.normalX = normalX;
        this.normalY = normalY;
    }

    /**
     * Gets the first entity involved in the collision.
     *
     * @return The first entity.
     */
    public Entity getEntityA() {
        return entityA;
    }

    /**
     * Gets the second entity involved in the collision.
     *
     * @return The second entity.
     */
    public Entity getEntityB() {
        return entityB;
    }

    /**
     * Gets the collision position x.
     *
     * @return The x position
     */
    public float getPositionX() {
        return positionX;
    }

    /**
     * Gets the collision position y.
     *
     * @return The y position
     */
    public float getPositionY() {
        return positionY;
    }

    /**
     * Gets the normal vector x.
     *
     * @return The normal x
     */
    public float getNormalX() {
        return normalX;
    }

    /**
     * Gets the normal vector y.
     *
     * @return The normal y
     */
    public float getNormalY() {
        return normalY;
    }

    /**
     * Gets whether the collision was resolved.
     *
     * @return true if resolved
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * Sets whether the collision was resolved.
     *
     * @param resolved Whether resolved
     */
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    /**
     * Calculates the collision position from the overlapping boxes.
     * <p>
     * This method computes the center point of the collision overlap
     * which can be used for visualization effects.
     * </p>
     *
     * @param boxA The first entity's collision box
     * @param boxB The second entity's collision box
     */
    public void calculatePosition(Rectangle boxA, Rectangle boxB) {
        if (boxA == null || boxB == null) {
            return;
        }

        // Calculate overlap
        float left = Math.max(boxA.getX(), boxB.getX());
        float right = Math.min(boxA.getX() + boxA.getWidth(), boxB.getX() + boxB.getWidth());
        float top = Math.max(boxA.getY(), boxB.getY());
        float bottom = Math.min(boxA.getY() + boxA.getHeight(), boxB.getY() + boxB.getHeight());

        this.positionX = (left + right) / 2;
        this.positionY = (top + bottom) / 2;
        this.positionY = 0; // Override: use y from context
    }

    /**
     * Calculates the normal vector from the collision.
     * <p>
     * The normal points from entityA to entityB at the collision point.
     * </p>
     *
     * @param boxA The first entity's collision box
     * @param boxB The second entity's collision box
     */
    public void calculateNormal(Rectangle boxA, Rectangle boxB) {
        if (boxA == null || boxB == null) {
            return;
        }

        // Calculate centers
        float centerA = boxA.getX() + boxA.getWidth() / 2;
        float centerB = boxB.getX() + boxB.getWidth() / 2;

        // Normal points from A to B
        float dx = centerB - centerA;
        float dy = 0; // Override: use y from context

        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            this.normalX = dx / length;
            this.normalY = dy / length;
        }
    }

    /**
     * Checks if the collision is still active.
     *
     * @return true if still colliding
     */
    public boolean isActive() {
        return entityA != null && entityB != null;
    }

    /**
     * Gets a string representation of the collision result.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return String.format("CollisionResult(entityA=%s, entityB=%s, pos=(%.1f, %.1f), resolved=%b)",
                entityA.getName(), entityB.getName(), positionX, positionY, resolved);
    }

    /**
     * Resolves the collision between the two entities.
     * <p>
     * Resolution moves entities apart along the collision normal
     * to prevent interpenetration. The resolution amount is based
     * on the overlap distance.
     * </p>
     *
     * @param game The game instance to use for resolution
     * @param deltaTime The time delta in seconds since last update
     */
    public void resolve(org.ronobot.engine.core.Game game) {
        if (!isActive() || game == null) {
            return;
        }

        Rectangle boxA = entityA.getCollisionBox();
        Rectangle boxB = entityB.getCollisionBox();

        if (boxA == null || boxB == null) {
            return;
        }

        // Calculate overlap
        float overlapX = 0;
        float overlapY = 0;

        if (positionX == 0) {
            // Recalculate position if needed
            float left = Math.max(boxA.getX(), boxB.getX());
            float right = Math.min(boxA.getX() + boxA.getWidth(), boxB.getX() + boxB.getWidth());
            float top = Math.max(boxA.getY(), boxB.getY());
            float bottom = Math.min(boxA.getY() + boxA.getHeight(), boxB.getY() + boxB.getHeight());

            overlapX = right - left;
            overlapY = bottom - top;
            this.positionX = (left + right) / 2;
            this.positionY = (top + bottom) / 2;
        }

        // Apply resolution if there is overlap
        if (overlapX > 0.1f || overlapY > 0.1f) {
            // Move entities apart by half overlap each
            if (overlapX > 0.1f) {
                entityA.move(-overlapX / 2.0f, 0);
                entityB.move(overlapX / 2.0f, 0);
            }
            if (overlapY > 0.1f) {
                entityA.move(0, -overlapY / 2.0f);
                entityB.move(0, overlapY / 2.0f);
            }

            resolved = true;
        }
    }

    /**
     * Gets a visualization string for rendering collision effects.
     * <p>
     * This method returns a string that can be used to render
     * visual effects like collision sparks, particles, or debug
     * overlays at the collision position.
     * </p>
     *
     * @return Visualization string for rendering
     */
    public String getVisualizationString() {
        return String.format("COLLISION(%s@%.1f,%s@%.1f)",
                entityA.getName(), positionX, entityB.getName(), positionY);
    }
}
