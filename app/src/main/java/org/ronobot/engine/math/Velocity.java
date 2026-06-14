package org.ronobot.engine.math;

/**
 * Represents velocity in 2D space.
 */
public class Velocity {

    /**
     * Zero velocity constant.
     */
    public static final Velocity ZERO = new Velocity();

    private float x;
    private float y;

    /**
     * Creates a new Velocity.
     *
     * @param x The x velocity component
     * @param y The y velocity component
     */
    public Velocity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new zero velocity.
     */
    public Velocity() {
        this(0f, 0f);
    }

    /**
     * Gets the x velocity component.
     *
     * @return The x velocity
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x velocity component.
     *
     * @param x The x velocity
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets the y velocity component.
     *
     * @return The y velocity
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the y velocity component.
     *
     * @param y The y velocity
     */
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Velocity{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
