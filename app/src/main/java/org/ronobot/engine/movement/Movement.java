package org.ronobot.engine.movement;

import org.ronobot.engine.math.Velocity;

/**
 * Simple movement handler that stores velocity for an entity.
 *
 * @author ronobot
 */
public class Movement {

    /**
     * The current velocity of this entity.
     */
    private final Velocity velocity;

    /**
     * Creates a Movement with default settings.
     */
    public Movement() {
        this.velocity = Velocity.ZERO;
    }

    /**
     * Creates a Movement with specified velocity.
     *
     * @param velocity the velocity
     */
    public Movement(Velocity velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets the current velocity.
     *
     * @return the current velocity
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * Applies a force to change velocity (impulse-based movement).
     *
     * @param forceX force in X direction
     * @param forceY force in Y direction
     */
    public void applyForce(double forceX, double forceY) {
        // Simple impulse: force/mass = acceleration, mass = 10
        velocity.setX(velocity.getX() + (float) (forceX / 10.0));
        velocity.setY(velocity.getY() + (float) (forceY / 10.0));
    }

    /**
     * Applies a velocity directly.
     *
     * @param vx velocity X
     * @param vy velocity Y
     */
    public void setVelocity(double vx, double vy) {
        velocity.setX((float) vx);
        velocity.setY((float) vy);
    }

    /**
     * Applies a velocity target (smooth movement).
     *
     * @param targetVx target X velocity
     * @param targetVy target Y velocity
     * @param deltaTime time in milliseconds
     */
    public void setVelocityTarget(double targetVx, double targetVy, double deltaTime) {
        double deltaSeconds = deltaTime / 1000.0;
        double speedX = (targetVx - velocity.getX()) * 50.0 * deltaSeconds;
        double speedY = (targetVy - velocity.getY()) * 50.0 * deltaSeconds;

        velocity.setX((float) (velocity.getX() + speedX));
        velocity.setY((float) (velocity.getY() + speedY));
    }

    /**
     * Resets velocity to zero.
     */
    public void reset() {
        velocity.setX(0f);
        velocity.setY(0f);
    }
}
