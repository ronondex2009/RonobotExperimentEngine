package org.ronobot.engine.math;

/**
 * Alias for Rectangle - Axis-Aligned Bounding Box.
 * <p>
 * This class is deprecated in favor of Rectangle.
 * Kept for backwards compatibility.
 * </p>
 *
 * @author ronobot
 * @deprecated Use Rectangle instead
 * @since 1.0
 */
@Deprecated
public class AxisAlignedBox extends Rectangle {

    /**
     * Creates a new AxisAlignedBox.
     *
     * @deprecated Use Rectangle instead
     * @param position The position
     * @param size The size
     */
    @Deprecated
    public AxisAlignedBox(Position position, Size size) {
        super(position, size);
    }

    /**
     * Creates a new AxisAlignedBox.
     *
     * @deprecated Use Rectangle instead
     * @param position The position
     * @param width The width
     * @param height The height
     */
    @Deprecated
    public AxisAlignedBox(Position position, int width, int height) {
        super(position, width, height);
    }
}
