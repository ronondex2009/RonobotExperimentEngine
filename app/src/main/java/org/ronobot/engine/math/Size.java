package org.ronobot.engine.math;

/**
 * Represents a 2D size with width and height dimensions.
 * Used for entity dimensions and collision box sizing.
 *
 * @author ronobot
 * @since 1.0
 */
public final class Size {

    /**
     * Width in pixels.
     */
    private final int width;

    /**
     * Height in pixels.
     */
    private final int height;

    /**
     * Creates a new Size with the specified dimensions.
     *
     * @param width The width
     * @param height The height
     */
    public Size(int width, int height) {
        this.width = Math.max(width, 0);
        this.height = Math.max(height, 0);
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
     * Creates a size from the given width and height dimensions.
     *
     * @param width The width
     * @param height The height
     * @return A new Size instance
     */
    public static Size of(int width, int height) {
        return new Size(width, height);
    }

    /**
     * Creates a unit size of 1x1.
     *
     * @return A new Size at unit dimensions
     */
    public static Size unit() {
        return of(1, 1);
    }

    @Override
    public String toString() {
        return "Size{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Size)) return false;
        Size size = (Size) o;
        return width == size.width && height == size.height;
    }

    @Override
    public int hashCode() {
        return 31 * Integer.hashCode(width) + Integer.hashCode(height);
    }
}
