package org.ronobot.engine.math;

/**
 * Represents a 2D position with x and y coordinates.
 * <p>
 * Provides a semantic alias for Point with clearer intent for position data.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class Position {

    /**
     * X coordinate.
     */
    public float x;

    /**
     * Y coordinate.
     */
    public float y;

    /**
     * Creates a new Position with the specified coordinates.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a position from the given x and y coordinates.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return A new Position instance
     */
    public static Position of(float x, float y) {
        return new Position(x, y);
    }

    /**
     * Creates a position from a Point.
     *
     * @param point The point to convert
     * @return A new Position instance
     */
    public static Position from(Point point) {
        return new Position(point.getX(), point.getY());
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

    /**
     * Creates a zero position at origin (0, 0).
     *
     * @return A new Position at origin
     */
    public static Position origin() {
        return of(0f, 0f);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return Float.compare(x, position.x) == 0 && Float.compare(y, position.y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Float.floatToRawIntBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Float.floatToRawIntBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
