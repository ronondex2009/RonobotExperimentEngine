/**
 * SpriteRenderer - A renderer for sprite-based debug/HUD overlays.
 *
 * Provides functionality for:
 * - Rendering sprites for entities
 * - Displaying health bars
 * - Showing ammunition counts
 * - Rendering debug information
 *
 * @author ronobot
 * @since 1.0-SNAPSHOT
 */
package org.ronobot.engine.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A sprite-based renderer for debug and HUD overlays.
 *
 * <p>This renderer handles visual elements such as entity sprites,
 * health bars, ammunition displays, and other graphical overlays
 * used for debugging and user interface elements.</p>
 *
 * @author ronobot
 * @since 1.0-SNAPSHOT
 */
public class SpriteRenderer {

    /** Default width for rendered sprites. */
    public static final int DEFAULT_SPRITE_WIDTH = 64;

    /** Default height for rendered sprites. */
    public static final int DEFAULT_SPRITE_HEIGHT = 64;

    /** Background color for transparent areas. */
    public static final Color TRANSPARENT = null;

    /** Color for health bars. */
    public static final Color HEALTH_COLOR = new Color(0, 255, 0);

    /** Color for health warning. */
    public static final Color HEALTH_WARNING = new Color(255, 0, 0);

    /** Color for ammunition indicators. */
    public static final Color AMMO_COLOR = new Color(255, 255, 0);

    /** Color for debug text. */
    public static final Color DEBUG_TEXT_COLOR = new Color(200, 200, 200);

    private int spriteWidth;
    private int spriteHeight;
    private Graphics2D graphics;
    private BufferedImage buffer;
    private boolean debugMode;
    private boolean showHealthBars;
    private boolean showAmmoIndicators;
    private boolean showDebugInfo;

    /**
     * Constructs a new SpriteRenderer with default settings.
     */
    public SpriteRenderer() {
        this(64, 64, null, true, true, true, true);
    }

    /**
     * Constructs a SpriteRenderer with custom dimensions.
     *
     * @param width  The sprite width
     * @param height The sprite height
     */
    public SpriteRenderer(int width, int height) {
        this(width, height, null, true, true, true, true);
    }

    /**
     * Constructs a SpriteRenderer with debug mode enabled.
     *
     * @param graphics The graphics context (can be null)
     */
    public SpriteRenderer(Graphics2D graphics) {
        this(64, 64, graphics, true, true, true, true);
    }

    /**
     * Constructs a SpriteRenderer with custom settings.
     *
     * @param width           The sprite width
     * @param height          The sprite height
     * @param graphics        The graphics context (can be null)
     * @param debugMode       Whether to enable debug mode
     * @param showHealthBars  Whether to show health bars
     * @param showAmmoIndicators Whether to show ammo indicators
     * @param showDebugInfo   Whether to show debug info
     */
    public SpriteRenderer(int width, int height, Graphics2D graphics,
                          boolean debugMode, boolean showHealthBars,
                          boolean showAmmoIndicators, boolean showDebugInfo) {
        this.spriteWidth = width;
        this.spriteHeight = height;
        this.debugMode = debugMode;
        this.showHealthBars = showHealthBars;
        this.showAmmoIndicators = showAmmoIndicators;
        this.showDebugInfo = showDebugInfo;

        // Create buffer if graphics is null
        if (graphics == null) {
            this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            // Create a dedicated graphics context for our buffer
            this.graphics = this.buffer.createGraphics();
        } else {
            this.graphics = graphics;
            this.buffer = null;
        }
    }

    /**
     * Gets whether debug mode is enabled.
     *
     * @return true if debug mode is enabled
     */
    public boolean isDebugMode() {
        return this.debugMode;
    }

    /**
     * Gets whether health bars are shown.
     *
     * @return true if health bars are shown
     */
    public boolean isShowHealthBars() {
        return this.showHealthBars;
    }

    /**
     * Gets whether ammo indicators are shown.
     *
     * @return true if ammo indicators are shown
     */
    public boolean isShowAmmoIndicators() {
        return this.showAmmoIndicators;
    }

    /**
     * Gets whether debug info is shown.
     *
     * @return true if debug info is shown
     */
    public boolean isShowDebugInfo() {
        return this.showDebugInfo;
    }

    /**
     * Gets the sprite width.
     *
     * @return The sprite width
     */
    public int getSpriteWidth() {
        return this.spriteWidth;
    }

    /**
     * Gets the sprite height.
     *
     * @return The sprite height
     */
    public int getSpriteHeight() {
        return this.spriteHeight;
    }

    /**
     * Gets the graphics context.
     *
     * @return The graphics context
     */
    public Graphics2D getGraphics() {
        return this.graphics;
    }

    /**
     * Gets the rendering width.
     *
     * @return The rendering width
     */
    public int getWidth() {
        return this.spriteWidth;
    }

    /**
     * Gets the rendering height.
     *
     * @return The rendering height
     */
    public int getHeight() {
        return this.spriteHeight;
    }

    /**
     * Renders to the internal buffer if available.
     */
    public void render() {
        if (this.buffer != null) {
            // Render is internal - no action needed unless we're composing
        }
    }

    /**
     * Sets the health bar for an entity.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param health The current health
     * @param maxHealth The maximum health
     */
    public void setHealthBar(int x, int y, int health, int maxHealth) {
        if (this.graphics == null) {
            return;
        }

        // Calculate health bar width
        int barWidth = (int) (((double) health / maxHealth) * (this.spriteWidth - 10));

        // Draw health bar
        if (health < 0.4 * maxHealth) {
            this.graphics.setColor(HEALTH_WARNING);
        } else {
            this.graphics.setColor(HEALTH_COLOR);
        }
        this.graphics.fillRect(x + 5, y, barWidth, 10);

        // Draw border
        this.graphics.setColor(Color.WHITE);
        this.graphics.drawRect(x + 5, y - 2, barWidth, 14);
    }

    /**
     * Sets the ammunition indicator.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param ammo The ammunition count
     */
    public void setAmmoIndicator(int x, int y, int ammo) {
        if (this.graphics == null) {
            return;
        }

        this.graphics.setColor(AMMO_COLOR);
        this.graphics.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        String text = String.format("%d", ammo);
        this.graphics.drawString(text, x, y);
    }

    /**
     * Renders debug information.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param text The debug text
     */
    public void renderDebugInfo(int x, int y, String text) {
        if (!this.debugMode) {
            return;
        }

        if (this.graphics == null) {
            return;
        }

        this.graphics.setColor(DEBUG_TEXT_COLOR);
        this.graphics.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        this.graphics.drawString(text, x, y);
    }

    /**
     * Renders entity information.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param name The entity name
     * @param health The current health
     */
    public void renderEntityInfo(int x, int y, String name, int health) {
        if (this.graphics == null) {
            return;
        }

        this.graphics.setColor(Color.WHITE);
        this.graphics.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));

        String healthText = String.format("%d HP", health);
        this.graphics.drawString(name + ": " + healthText, x, y);
    }

    /**
     * Renders a damage indicator.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param damage The damage amount
     */
    public void renderDamageIndicator(int x, int y, int damage) {
        if (this.graphics == null) {
            return;
        }

        this.graphics.setColor(Color.RED);
        this.graphics.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        String text = "-" + damage;
        this.graphics.drawString(text, x, y);
    }

    /**
     * Renders a pickup notification.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param item The item name
     */
    public void renderPickupNotification(int x, int y, String item) {
        if (this.graphics == null) {
            return;
        }

        this.graphics.setColor(Color.YELLOW);
        this.graphics.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        String text = "+ " + item;
        this.graphics.drawString(text, x, y);
    }

    /**
     * Clears the rendering buffer.
     */
    public void clear() {
        if (this.graphics == null) {
            return;
        }

        this.graphics.setColor(TRANSPARENT);
        this.graphics.fillRect(0, 0, this.spriteWidth, this.spriteHeight);
    }

    /**
     * Creates a sprite from an image.
     *
     * @param image The source image
     * @param x X position
     * @param y Y position
     * @return The rendered sprite
     */
    public BufferedImage createSprite(java.awt.Image image, int x, int y) {
        if (this.buffer == null) {
            return null;
        }

        this.graphics.drawImage(image, x, y, null);
        return this.buffer;
    }

    /**
     * Sets the background color for the sprite.
     *
     * @param color The background color
     */
    public void setBackground(Color color) {
        if (this.graphics == null) {
            return;
        }

        this.graphics.setColor(color);
        this.graphics.fillRect(0, 0, this.spriteWidth, this.spriteHeight);
    }
}
