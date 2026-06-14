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
     * Gets the x coordinate.
     *
     * @return The x coordinate
     */
    public float x() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return The y coordinate
     */
    public float y() {
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

    /**
     * Creates a copy of this position.
     *
     * @return A new Position with same values
     */
    public Position copy() {
        return new Position(x, y);
    }

    /**
     * Sets the x coordinate.
     *
     * @param x the new x value
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y the new y value
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets both x and y coordinates.
     *
     * @param x the new x value
     * @param y the new y value
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the width (alias for x).
     *
     * @return the x value
     */
    public float width() {
        return x;
    }

    /**
     * Gets the height (alias for y).
     *
     * @return the y value
     */
    public float height() {
        return y;
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
