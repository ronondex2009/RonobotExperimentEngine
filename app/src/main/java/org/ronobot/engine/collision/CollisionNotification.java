package org.ronobot.engine.collision;

/**
 * CollisionNotification represents a collision event notification
 * for the DOOM-like engine.
 * <p>
 * This class provides a simple notification system for collision events,
 * allowing entities to receive callbacks when collisions occur.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class CollisionNotification {

    /**
     * Type of collision event.
     */
    public enum EventType {
        /**
         * Player collided with wall.
         */
        WALL_COLLISION,

        /**
         * Player collided with another entity.
         */
        ENTITY_COLLISION,

        /**
         * Projectile hit target.
         */
        PROJECTILE_HIT,

        /**
         * Map decoration interaction.
         */
        MAP_DECORATION,

        /**
         * Boundary reached.
         */
        BOUNDARY_REACHED,

        /**
         * Door opened/closed.
         */
        DOOR_INTERACTION
    }

    /**
     * Collision type identifier.
     */
    private final EventType type;

    /**
     * Primary entity involved in collision.
     */
    private final int primaryId;

    /**
     * Secondary entity involved in collision (if any).
     */
    private final int secondaryId;

    /**
     * Collision position.
     */
    private final double x;

    /**
     * Collision y position.
     */
    private final double y;

    /**
     * Timestamp of collision (frame number).
     */
    private final int frame;

    /**
     * Collision notification constructor.
     *
     * @param type Type of collision event
     * @param primaryId Primary entity ID
     * @param secondaryId Secondary entity ID (0 if none)
     * @param x Collision x position
     * @param y Collision y position
     * @param frame Frame number when collision occurred
     */
    public CollisionNotification(EventType type, int primaryId, int secondaryId, double x, double y, int frame) {
        this.type = type;
        this.primaryId = primaryId;
        this.secondaryId = secondaryId;
        this.x = x;
        this.y = y;
        this.frame = frame;
    }

    /**
     * Gets the collision event type.
     *
     * @return The event type
     */
    public EventType getType() {
        return type;
    }

    /**
     * Gets the primary entity ID.
     *
     * @return Primary entity ID
     */
    public int getPrimaryId() {
        return primaryId;
    }

    /**
     * Gets the secondary entity ID (0 if none).
     *
     * @return Secondary entity ID
     */
    public int getSecondaryId() {
        return secondaryId;
    }

    /**
     * Gets the collision x position.
     *
     * @return Collision x position
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the collision y position.
     *
     * @return Collision y position
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the frame number when collision occurred.
     *
     * @return Frame number
     */
    public int getFrame() {
        return frame;
    }

    /**
     * Checks if this notification is for a wall collision.
     *
     * @return true if wall collision
     */
    public boolean isWallCollision() {
        return type == EventType.WALL_COLLISION;
    }

    /**
     * Checks if this notification is for an entity collision.
     *
     * @return true if entity collision
     */
    public boolean isEntityCollision() {
        return type == EventType.ENTITY_COLLISION;
    }

    /**
     * Checks if this notification is for a projectile hit.
     *
     * @return true if projectile hit
     */
    public boolean isProjectileHit() {
        return type == EventType.PROJECTILE_HIT;
    }

    /**
     * Checks if this notification is for a map decoration interaction.
     *
     * @return true if map decoration
     */
    public boolean isMapDecoration() {
        return type == EventType.MAP_DECORATION;
    }

    /**
     * Checks if this notification is for a boundary reached event.
     *
     * @return true if boundary reached
     */
    public boolean isBoundaryReached() {
        return type == EventType.BOUNDARY_REACHED;
    }

    /**
     * Checks if this notification is for a door interaction.
     *
     * @return true if door interaction
     */
    public boolean isDoorInteraction() {
        return type == EventType.DOOR_INTERACTION;
    }

    /**
     * Gets a string representation of the notification.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "CollisionNotification{" +
                "type=" + type +
                ", primaryId=" + primaryId +
                ", secondaryId=" + secondaryId +
                ", x=" + String.format("%.1f", x) +
                ", y=" + String.format("%.1f", y) +
                ", frame=" + frame +
                '}';
    }
}
