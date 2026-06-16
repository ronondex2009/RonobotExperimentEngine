package org.ronobot.engine;

import org.ronobot.engine.core.Game;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.input.InputHandler;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.gui.GameWindow;
import org.ronobot.engine.gui.GamePanel;
import org.ronobot.engine.physics.PhysicsEngine;
import org.ronobot.engine.render.Renderer;

/**
 * Main application entry point for the engine.
 * <p>
 * This class creates and manages the game window, providing
 * a GUI interface for the DOOM-like engine. It initializes
 * the game, renderer, input, and physics components.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
 */
public class App {

    /**
     * Whether to create a GUI window.
     */
    private boolean createWindow = false;

    /**
     * The game window.
     */
    private GameWindow window;

    /**
     * The active game instance.
     */
    private Game game;

    /**
     * The physics engine.
     */
    private PhysicsEngine physics;

    /**
     * The renderer.
     */
    private Renderer renderer;

    /**
     * The input handler.
     */
    private InputHandler input;


    /**
     * Creates a new App instance without GUI (for testing).
     */
    public App() {
        this(false);
    }

    /**
     * Creates a new App instance with optional GUI.
     *
     * @param createWindow Whether to create a window
     */
    public App(boolean createWindow) {
        this.createWindow = createWindow;
        this.renderer = new Renderer();
        this.physics = new PhysicsEngine();
        this.input = new InputHandler();
        this.game = new Game();
        if (createWindow) {
            this.window = new GameWindow();
            window.setGame(this.game);
        }
    }

    /**
     * Creates a new App instance with a GUI window.
     */
    public App(boolean createWindow, int width, int height) {
        this(createWindow);
        if (createWindow) {
            this.window = new GameWindow(width, height);
            window.setGame(this.game);
        }
    }

    /**
     * Gets whether a window should be created.
     *
     * @return true if window should be created
     */
    public boolean isCreateWindow() {
        return createWindow;
    }

    /**
     * Sets whether to create a window.
     *
     * @param createWindow Whether to create a window
     */
    public void setCreateWindow(boolean createWindow) {
        this.createWindow = createWindow;
    }

    /**
     * Gets the game window.
     *
     * @return The window, or null if not created
     */
    public GameWindow getGameWindow() {
        return window;
    }

    /**
     * Gets the active game.
     *
     * @return The game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the active game.
     *
     * @param game The game to set
     */
    public void setGame(Game game) {
        this.game = game;
        if (window != null) {
            window.setGame(game);
        }
    }

    /**
     * Gets the physics engine.
     *
     * @return The physics engine
     */
    public PhysicsEngine getPhysicsEngine() {
        return physics;
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
     * Gets the input handler.
     *
     * @return The input handler
     */
    public InputHandler getInputHandler() {
        return input;
    }

    /**
     * Starts the game loop.
     *
     * @return true if started successfully
     */
    public boolean start() {
        if (game != null) {
            game.init();
            game.setRenderer(this.renderer);
            game.setInputHandler(this.input);

            // Start game loop without GUI thread
            new Thread(() -> {
                while (game.isRunning() && !game.isEnded()) {
                    try {
                        // Process input
                        if (input != null) {
                            input.handle(game);
                        }

                        // Update game state
                        game.update();

                        // Render the frame
                        if (renderer != null) {
                            renderer.render(game);
                        }

                        // Yield control
                        Thread.sleep(16); // Approx 60 FPS

                    } catch (InterruptedException e) {
                        // Thread interrupted, exit loop
                        break;
                    } catch (IllegalArgumentException e) {
                        // Input was not handled, but continue the loop
                        continue;
                    }
                }
            }, "game-loop").start();

            return true;
        }
        return false;
    }

    /**
     * Starts the game and creates a window.
     *
     * @return true if started successfully
     */
    public boolean startWithWindow() {
        if (window != null && game != null) {
            window.show();
            return start();
        }
        return false;
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        if (game != null) {
            game.stop();
            if (window != null) {
                window.dispose();
            }
            game = null;
        }
    }

    /**
     * Updates the game state for one frame.
     */
    public void update() {
        if (game != null) {
            game.update();
        }
    }

    /**
     * Renders the game frame.
     */
    public void render() {
        if (game != null) {
            renderer.render(game);
        }
    }

    /**
     * Handles input for the current frame.
     *
     * @return true if input was handled
     */
    public boolean handleInput() {
        if (game != null) {
            GameMap map = game.getMap();
            if (map != null && map.isLoaded()) {
                input.handle(game);
                return true;
            }
        }
        return false;
    }

    /**
     * Main entry point for the application with GUI.
     */
    public static void main(String[] args) {
        App app = new App();
        app.startWithWindow();
    }

    @Override
    public String toString() {
        return "App{" +
                "createWindow=" + createWindow +
                ", game=" + (game != null ? game.getState() : "stopped") +
                '}';
    }
}
