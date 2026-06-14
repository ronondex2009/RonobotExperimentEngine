package org.ronobot.engine.math;

import java.util.Objects;

/**
 * Represents an axis-aligned rectangle (AABB) for collision detection.
 * Used for entity bounding boxes and collision management.
 *
 * @author ronobot
 * @since 1.0
 */
public class Rectangle {

    /**
     * Top-left position.
     */
    private final Position position;

    /**
     * Width of the rectangle.
     */
    private final int width;

    /**
     * Height of the rectangle.
     */
    private final int height;

    /**
     * Creates a new Rectangle from the given position and size.
     *
     * @param position The top-left position
     * @param size The size of the rectangle
     */
    public Rectangle(Position position, Size size) {
        this.position = Objects.requireNonNull(position);
        this.width = Math.max(size.getWidth(), 0);
        this.height = Math.max(size.getHeight(), 0);
    }

    /**
     * Creates a new Rectangle from the given position, width, and height.
     *
     * @param position The top-left position
     * @param width The width
     * @param height The height
     */
    public Rectangle(Position position, int width, int height) {
        this.position = Objects.requireNonNull(position);
        this.width = Math.max(width, 0);
        this.height = Math.max(height, 0);
    }

    /**
     * Creates a unit rectangle at the origin.
     *
     * @return A new Rectangle at (0, 0) with size 1x1
     */
    public static Rectangle unit() {
        return new Rectangle(Position.origin(), 1, 1);
    }

    /**
     * Creates a rectangle from the given position, width, and height.
     *
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     * @return A new Rectangle instance
     */
    public static Rectangle of(float x, float y, int width, int height) {
        return new Rectangle(Position.of(x, y), width, height);
    }

    /**
     * Creates a rectangle from the given position and size.
     *
     * @param position The position
     * @param width The width
     * @param height The height
     * @return A new Rectangle instance
     */
    public static Rectangle of(Position position, int width, int height) {
        return new Rectangle(position, width, height);
    }

    /**
     * Creates a rectangle from the given position and size.
     *
     * @param position The position
     * @param size The size
     * @return A new Rectangle instance
     */
    public static Rectangle of(Position position, Size size) {
        return new Rectangle(position, size);
    }

    /**
     * Gets the position.
     *
     * @return The position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets the x position.
     *
     * @return The x position
     */
    public float getX() {
        return position.getX();
    }

    /**
     * Gets the y position.
     *
     * @return The y position
     */
    public float getY() {
        return position.getY();
    }

    /**
     * Gets the width.
     *
     * @return The width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height.
     *
     * @return The height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the center X coordinate.
     *
     * @return The center X
     */
    public float getCenterX() {
        return getX() + width / 2.0f;
    }

    /**
     * Gets the center Y coordinate.
     *
     * @return The center Y
     */
    public float getCenterY() {
        return getY() + height / 2.0f;
    }

    /**
     * Checks if this rectangle intersects with another rectangle.
     *
     * @param other The other rectangle to check against
     * @return true if rectangles intersect
     */
    public boolean intersects(Rectangle other) {
        if (other == null || other.width <= 0 || other.height <= 0) {
            return false;
        }
        return getX() < other.getX() + other.width &&
                getX() + width > other.getX() &&
                getY() < other.getY() + other.height &&
                getY() + height > other.getY();
    }

    /**
     * Checks if another rectangle is completely contained within this rectangle.
     *
     * @param other The rectangle to check for containment
     * @return true if other is fully contained within this rectangle
     */
    public boolean contains(Rectangle other) {
        if (other == null) {
            return false;
        }
        return getX() <= other.getX() &&
                getY() <= other.getY() &&
                getX() + width >= other.getX() + other.width &&
                getY() + height >= other.getY() + other.height;
    }

    /**
     * Checks if a point lies on the boundary of this rectangle.
     *
     * @param x The x coordinate to check
     * @return true if x is on a vertical boundary edge
     */
    public boolean isAtBoundaryX(float x) {
        return x == getX() || x == getX() + width;
    }

    /**
     * Checks if a point lies on the boundary of this rectangle.
     *
     * @param y The y coordinate to check
     * @return true if y is on a horizontal boundary edge
     */
    public boolean isAtBoundaryY(float y) {
        return y == getY() || y == getY() + height;
    }

    /**
     * Checks if a point lies on any boundary of this rectangle.
     *
     * @param x The x coordinate to check
     * @param y The y coordinate to check
     * @return true if (x, y) lies on any boundary edge
     */
    public boolean isAtBoundary(float x, float y) {
        return isAtBoundaryX(x) || isAtBoundaryY(y);
    }

    /**
     * Expands or contracts this rectangle by the given delta.
     *
     * @param delta The amount to expand (positive) or contract (negative)
     * @return A new Rectangle with expanded/contracted boundaries
     */
    public Rectangle expand(int delta) {
        if (delta == 0) {
            return this;
        }
        int newWidth = Math.max(0, width + 2 * delta);
        int newHeight = Math.max(0, height + 2 * delta);
        return new Rectangle(position, newWidth, newHeight);
    }

    /**
     * Creates a rectangle with the given x position.
     *
     * @param x The x position
     * @return A new Rectangle with the given x position
     */
    public Rectangle withX(float x) {
        return new Rectangle(Position.of(x, getY()), width, height);
    }

    /**
     * Creates a rectangle with the given y position.
     *
     * @param y The y position
     * @return A new Rectangle with the given y position
     */
    public Rectangle withY(float y) {
        return new Rectangle(Position.of(getX(), y), width, height);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "position=" + position +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rectangle)) return false;
        Rectangle rectangle = (Rectangle) o;
        return width == rectangle.width && height == rectangle.height &&
                Float.compare(position.getX(), rectangle.getX()) == 0 &&
                Float.compare(position.getY(), rectangle.getY()) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Integer.hashCode(width) + Integer.hashCode(height) +
                31 * Long.hashCode(Float.floatToIntBits(position.getX())) +
                31 * Long.hashCode(Float.floatToIntBits(position.getY()));
    }
}
