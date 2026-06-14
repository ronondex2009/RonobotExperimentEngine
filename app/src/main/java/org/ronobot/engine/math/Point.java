package org.ronobot.engine.math;

/**
 * A simple point in 2D space.
 * <p>
 * Used for various math operations in the game engine.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
 */
public class Point {

    private float x;
    private float y;

    /**
     * Creates a new Point.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new Point at the origin.
     */
    public Point() {
        this(0f, 0f);
    }

    /**
     * Gets the x coordinate.
     *
     * @return The x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return The y coordinate
     */
    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
