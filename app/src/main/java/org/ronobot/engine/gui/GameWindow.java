package org.ronobot.engine.gui;

import org.ronobot.engine.core.Game;
import org.ronobot.engine.render.Renderer;

import javax.swing.*;
import java.awt.*;

/**
 * GameWindow provides a Swing-based window for displaying the game.
 * <p>
 * This class creates a JFrame with proper game rendering and window controls.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-06-16
 */
public class GameWindow extends JFrame {

    /**
     * Default window title.
     */
    private static final String DEFAULT_TITLE = "DOOM-like Engine";

    /**
     * Default window width in pixels.
     */
    private static final int DEFAULT_WIDTH = 800;

    /**
     * Default window height in pixels.
     */
    private static final int DEFAULT_HEIGHT = 576;

    /**
     * Game renderer.
     */
    private Renderer renderer;

    /**
     * Game instance.
     */
    private Game game;

    /**
     * Game rendering panel.
     */
    private GamePanel gamePanel;

    /**
     * Creates a new GameWindow with the default title and dimensions.
     */
    public GameWindow() {
        this(DEFAULT_TITLE, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Creates a new GameWindow with specified dimensions.
     *
     * @param width  The window width in pixels
     * @param height The window height in pixels
     */
    public GameWindow(int width, int height) {
        this(DEFAULT_TITLE, width, height);
    }

    /**
     * Creates a new GameWindow with custom title and dimensions.
     *
     * @param title  The window title
     * @param width  The window width in pixels
     * @param height The window height in pixels
     */
    public GameWindow(String title, int width, int height) {
        super(title);

        // Set up the renderer with default configuration
        this.renderer = new Renderer();
        this.gamePanel = new GamePanel(this.renderer);

        // Add components to frame
        setLayout(new BorderLayout(0, 0));
        add(gamePanel, BorderLayout.CENTER);

        // Configure window properties
        setResizable(true);
        setSize(width, height);
        setLocationRelativeTo(null); // Center on screen

        // Set close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Sets the game to render in this window.
     *
     * @param game The game instance to display
     */
    public void setGame(Game game) {
        if (game != null) {
            this.game = game;
            gamePanel.setGame(game);
            if (game.isRunning()) {
                show();
            }
        }
    }

    /**
     * Gets the game panel.
     *
     * @return The game panel
     */
    public GamePanel getGamePanel() {
        return gamePanel;
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
     * Gets the game instance.
     *
     * @return The game instance, or null if not set
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the renderer for this window.
     *
     * @param renderer The renderer to use
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
        if (gamePanel != null) {
            gamePanel.setRenderer(renderer);
        }
    }

    /**
     * Checks if a game is set.
     *
     * @return true if game is set
     */
    public boolean hasGame() {
        return game != null;
    }

    /**
     * Checks if the window is displayable.
     *
     * @return true if window is displayable
     */
    public boolean isVisible() {
        return isDisplayable();
    }

    @Override
    public String toString() {
        return "GameWindow{" +
                "title='" + getTitle() + '\'' +
                ", size=" + getSize() +
                ", hasGame=" + hasGame() +
                '}';
    }
}
