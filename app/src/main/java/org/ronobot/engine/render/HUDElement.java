package org.ronobot.engine.render;

import org.ronobot.engine.core.Game;

/**
 * HUDElement represents a UI element for the game HUD.
 * <p>
 * HUD elements display game information such as health, ammo, score,
 * and weapon status. Each element has a position, dimensions, and
 * a rendering callback for custom display logic.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class HUDElement {

    /**
     * HUD element type enum.
     */
    public enum Type {
        HEALTH_BAR,
        AMMO_DISPLAY,
        WEAPON_ICON,
        SCORE_DISPLAY,
        LEVEL_INDICATOR,
        DEBUG_INFO,
        MENUS,
        MESSAGE_BOX,
        INVENTORY_SCREEN
    }

    /**
     * Gets all type values.
     *
     * @return Array of all type values
     */
    public static Type[] values() {
        return Type.values();
    }

    /**
     * The element type.
     */
    private final Type type;

    /**
     * X position in pixels.
     */
    private int x = 0;

    /**
     * Y position in pixels.
     */
    private int y = 0;

    /**
     * Width in pixels.
     */
    private int width = 0;

    /**
     * Height in pixels.
     */
    private int height = 0;

    /**
     * Whether this element is visible.
     */
    private boolean visible = true;

    /**
     * Callback for rendering the element.
     */
    private RenderCallback renderCallback;

    /**
     * Creates a new HUDElement with the specified type.
     *
     * @param type The element type
     */
    public HUDElement(Type type) {
        this.type = type;
    }

    /**
     * Creates a new HUDElement with the specified type string.
     * Accepts lowercase string names for convenience.
     *
     * @param typeName The element type name (case-insensitive)
     */
    public HUDElement(String typeName) {
        if (typeName == null || typeName.trim().isEmpty()) {
            throw new IllegalArgumentException("typeName cannot be null or empty");
        }
        this.type = Type.valueOf(typeName.toUpperCase());
    }

    /**
     * Creates a new HUDElement with custom properties.
     *
     * @param type      The element type
     * @param x         X position
     * @param y         Y position
     * @param width     Width in pixels
     * @param height    Height in pixels
     */
    public HUDElement(Type type, int x, int y, int width, int height) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the element type.
     *
     * @return The element type
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the X position.
     *
     * @return The X position in pixels
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the X position.
     *
     * @param x The X position in pixels
     * @return This element (for chaining)
     */
    public HUDElement setX(int x) {
        this.x = x;
        return this;
    }

    /**
     * Gets the Y position.
     *
     * @return The Y position in pixels
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the Y position.
     *
     * @param y The Y position in pixels
     * @return This element (for chaining)
     */
    public HUDElement setY(int y) {
        this.y = y;
        return this;
    }

    /**
     * Gets the width.
     *
     * @return The width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width.
     *
     * @param width The width in pixels
     * @return This element (for chaining)
     */
    public HUDElement setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Gets the height.
     *
     * @return The height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height.
     *
     * @param height The height in pixels
     * @return This element (for chaining)
     */
    public HUDElement setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * Gets whether the element is visible.
     *
     * @return true if visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets visibility.
     *
     * @param visible true if visible
     * @return This element (for chaining)
     */
    public HUDElement setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    /**
     * Gets the render callback.
     *
     * @return The render callback, or null if not set
     */
    public RenderCallback getRenderCallback() {
        return renderCallback;
    }

    /**
     * Sets the render callback for custom rendering.
     *
     * @param callback The render callback
     * @return This element (for chaining)
     */
    public HUDElement setRenderCallback(RenderCallback callback) {
        this.renderCallback = callback;
        return this;
    }

    /**
     * Renders this element using its callback or default rendering.
     *
     * @param game The game context
     * @return true if rendered successfully
     */
    public boolean render(Game game) {
        if (!visible) {
            return false;
        }

        if (renderCallback != null) {
            return renderCallback.render(game, this);
        }

        // Default stub rendering
        System.out.println(String.format("HUD: Rendering %s at (%d,%d) %dx%d",
                type, x, y, width, height));
        return true;
    }

    /**
     * Default render callback for generic HUD elements.
     */
    public static class DefaultRenderCallback implements RenderCallback {
        @Override
        public boolean render(Game game, HUDElement element) {
            return element.render(game);
        }
    }

    /**
     * Renders this element to a buffer (stub method).
     *
     * @param game The game context
     * @return true if rendered successfully
     */
    public boolean renderToBuffer(Game game) {
        if (!visible) {
            return false;
        }
        // Stub implementation
        return true;
    }

    @Override
    public String toString() {
        return "HUDElement{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", visible=" + visible +
                ", callback=" + renderCallback +
                '}';
    }

    /**
     * Creates a health bar element.
     *
     * @param x X position
     * @param y Y position
     * @return A configured health bar HUDElement
     */
    public static HUDElement healthBar(int x, int y) {
        HUDElement element = new HUDElement(Type.HEALTH_BAR)
                .setX(x)
                .setY(y)
                .setWidth(200)
                .setHeight(16)
                .setRenderCallback(new DefaultRenderCallback());
        return element;
    }

    /**
     * Creates an ammo display element.
     *
     * @param x X position
     * @param y Y position
     * @return A configured ammo display HUDElement
     */
    public static HUDElement ammoDisplay(int x, int y) {
        HUDElement element = new HUDElement(Type.AMMO_DISPLAY)
                .setX(x)
                .setY(y)
                .setWidth(120)
                .setHeight(20)
                .setRenderCallback(new DefaultRenderCallback());
        return element;
    }

    /**
     * Creates a score display element.
     *
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     * @return A configured score display HUDElement
     */
    public static HUDElement scoreDisplay(int x, int y, int width, int height) {
        HUDElement element = new HUDElement(Type.SCORE_DISPLAY)
                .setX(x)
                .setY(y)
                .setWidth(width)
                .setHeight(height)
                .setRenderCallback(new DefaultRenderCallback());
        return element;
    }

    /**
     * Creates a score display element.
     *
     * @param x X position
     * @param y Y position
     * @return A configured score display HUDElement
     */
    public static HUDElement scoreDisplay(int x, int y) {
        return scoreDisplay(x, y, 120, 20);
    }

    /**
     * Creates a score display element with only x position.
     *
     * @param x X position
     * @return A configured score display HUDElement
     */
    public static HUDElement scoreDisplay(int x) {
        return scoreDisplay(x, 24);
    }

    /**
     * Creates a score display element at default position.
     *
     * @return A configured score display HUDElement
     */
    public static HUDElement scoreDisplay() {
        return scoreDisplay(500, 24);
    }

    /**
     * Creates a level indicator element.
     *
     * @param x X position (default: 540)
     * @param y Y position (default: 24)
     * @param width Width (default: 100)
     * @param height Height (default: 20)
     * @return A configured level indicator HUDElement
     */
    public static HUDElement levelIndicator(int x, int y, int width, int height) {
        HUDElement element = new HUDElement(Type.LEVEL_INDICATOR)
                .setX(x)
                .setY(y)
                .setWidth(width)
                .setHeight(height)
                .setRenderCallback(new DefaultRenderCallback());
        return element;
    }

    /**
     * Creates a level indicator element.
     *
     * @param x X position (default: 540)
     * @param y Y position (default: 24)
     * @param width Width (default: 100)
     * @param height Height (default: 20)
     * @return A configured level indicator HUDElement
     */
    public static HUDElement levelIndicator(int x, int y) {
        return levelIndicator(x, y, 100, 20);
    }

    /**
     * Creates a level indicator element at default position.
     *
     * @return A configured level indicator HUDElement
     */
    public static HUDElement levelIndicator() {
        return levelIndicator(540, 24);
    }

    /**
     * Creates a debug info element.
     *
     * @param x X position (default: 400)
     * @param y Y position (default: 432)
     * @param width Width (default: 240)
     * @param height Height (default: 48)
     * @return A configured debug info HUDElement
     */
    public static HUDElement debugInfo(int x, int y, int width, int height) {
        HUDElement element = new HUDElement(Type.DEBUG_INFO)
                .setX(x)
                .setY(y)
                .setWidth(width)
                .setHeight(height)
                .setRenderCallback(new DefaultRenderCallback());
        return element;
    }

    /**
     * Creates a debug info element at default position.
     *
     * @return A configured debug info HUDElement
     */
    public static HUDElement debugInfo() {
        return debugInfo(400, 432, 240, 48);
    }

    /**
     * Creates a menu element.
     *
     * @param x X position (default: 320)
     * @param y Y position (default: 240)
     * @param width Width (default: 400)
     * @param height Height (default: 300)
     * @return A configured menu HUDElement
     */
    public static HUDElement menu(int x, int y, int width, int height) {
        HUDElement element = new HUDElement(Type.MENUS)
                .setX(x)
                .setY(y)
                .setWidth(width)
                .setHeight(height)
                .setRenderCallback(new DefaultRenderCallback());
        return element;
    }

    /**
     * Creates a menu element at default position.
     *
     * @return A configured menu HUDElement
     */
    public static HUDElement menu() {
        return menu(320, 240, 400, 300);
    }

    /**
     * Creates a message box element.
     *
     * @param x X position (default: 40)
     * @param y Y position (default: 240)
     * @param width Width (default: 600)
     * @param height Height (default: 200)
     * @return A configured message box HUDElement
     */
    public static HUDElement messageBox(int x, int y, int width, int height) {
        HUDElement element = new HUDElement(Type.MESSAGE_BOX)
                .setX(x)
                .setY(y)
                .setWidth(width)
                .setHeight(height)
                .setRenderCallback(new DefaultRenderCallback());
        return element;
    }

    /**
     * Creates a message box element at default position.
     *
     * @return A configured message box HUDElement
     */
    public static HUDElement messageBox() {
        return messageBox(40, 240, 600, 200);
    }

    /**
     * Render callback interface for HUD elements.
     */
    @FunctionalInterface
    public interface RenderCallback {
        /**
         * Renders the HUD element.
         *
         * @param game The game context
         * @param element The element to render
         * @return true if rendered successfully
         */
        boolean render(Game game, HUDElement element);

        /**
         * Default implementation that handles case-insensitive type lookup.
         * @param game The game context
         * @param element The element to render
         * @return true if rendered successfully
         */
        default boolean renderDefault(Game game, HUDElement element) {
            return element.render(game);
        }
    }

    /**
     * HUD constant for screen width.
     */
    public static final int SCREEN_WIDTH = 640;

    /**
     * HUD constant for screen height.
     */
    public static final int SCREEN_HEIGHT = 480;
}
