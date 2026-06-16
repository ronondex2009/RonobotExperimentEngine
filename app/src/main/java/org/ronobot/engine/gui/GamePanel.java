package org.ronobot.engine.gui;

import org.ronobot.engine.core.Game;
import org.ronobot.engine.render.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * GamePanel provides the rendering surface for the game in a Swing window.
 * <p>
 * This panel handles displaying the game rendered by Renderer, including
 * map tiles, entities, projectiles, decorations, and HUD elements.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-06-16
 */
public class GamePanel extends JPanel {

    /**
     * Default panel width in pixels.
     */
    private static final int PANEL_WIDTH = 800;

    /**
     * Default panel height in pixels.
     */
    private static final int PANEL_HEIGHT = 512;

    /**
     * Renderer for game rendering.
     */
    private Renderer renderer;

    /**
     * Current game instance.
     */
    private Game game;

    /**
     * Image buffer for rendering.
     */
    private BufferedImage renderBuffer;

    /**
     * Creates a new GamePanel with default dimensions.
     */
    public GamePanel() {
        this(new Renderer());
    }

    /**
     * Creates a new GamePanel with specified renderer.
     *
     * @param renderer The renderer to use
     */
    public GamePanel(Renderer renderer) {
        if (renderer == null) {
            this.renderer = new Renderer();
        } else {
            this.renderer = renderer;
        }

        // Set up panel properties
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setFocusable(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(Color.BLACK);

        // Initialize render buffer
        initializeBuffer();
    }

    /**
     * Initializes the render buffer.
     */
    private void initializeBuffer() {
        int width = getPreferredSize().width;
        int height = getPreferredSize().height;
        renderBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Sets the game instance for rendering.
     *
     * @param game The game to render
     */
    public void setGame(Game game) {
        this.game = game;
        if (game != null) {
            // Set renderer on game if not already set
            game.setRenderer(renderer);
        }
    }

    /**
     * Sets the renderer.
     *
     * @param renderer The renderer to use
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Gets the renderer.
     *
     * @return The renderer
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Gets the current game.
     *
     * @return The game instance, or null if not set
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the render buffer.
     *
     * @return The render buffer
     */
    public BufferedImage getRenderBuffer() {
        return renderBuffer;
    }

    /**
     * Repaints the game frame.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics graphics = g;

        // Clear background
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        // Render game if available
        if (game != null && game.isRunning() && !game.isEnded()) {
            render();
        }

        // Flush render buffer
        if (renderBuffer != null) {
            graphics.drawImage(renderBuffer, 0, 0, this);
        }
    }

    /**
     * Renders the current game state.
     */
    private void render() {
        if (game == null || renderer == null) {
            return;
        }

        // Clear buffer
        Graphics2D graphics = (Graphics2D) renderBuffer.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, renderBuffer.getWidth(), renderBuffer.getHeight());

        // Render map
        renderer.render(game);

        // Flush buffer
        graphics.drawImage(renderBuffer, 0, 0, this);
    }

    /**
     * Handles keyboard input for the game.
     *
     * @param key The key that was pressed
     */
    public void handleKeyPress(int key) {
        if (game != null) {
            // Handle key in game - stub for now
        }
    }

    /**
     * Gets the render buffer width.
     *
     * @return The buffer width
     */
    public int getWidth() {
        return renderBuffer != null ? renderBuffer.getWidth() : 0;
    }

    /**
     * Gets the render buffer height.
     *
     * @return The buffer height
     */
    public int getHeight() {
        return renderBuffer != null ? renderBuffer.getHeight() : 0;
    }

    /**
     * Checks if game is being rendered.
     *
     * @return true if game is active
     */
    public boolean isRendering() {
        return game != null && renderer != null;
    }

    public String toString() {
        return "GamePanel{" +
                "width=" + getWidth() +
                ", height=" + getHeight() +
                ", rendering=" + isRendering() +
                '}';
    }
}
