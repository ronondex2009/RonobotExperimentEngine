package org.ronobot.engine;

import org.ronobot.engine.core.Game;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.input.InputHandler;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.physics.PhysicsEngine;
import org.ronobot.engine.render.Renderer;

/**
 * Main application entry point for the engine.
 * <p>
 * Manages game state, rendering, physics, and input handling.
 * This class serves as the central coordinator for all engine components.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class App {

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
     * Creates a new App instance.
     */
    public App() {
        this.physics = new PhysicsEngine();
        this.renderer = new Renderer();
        this.input = new InputHandler();
        this.game = null;
    }

    /**
     * Gets the active game.
     *
     * @return The current game, or null if not started
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
            game.start();
            return true;
        }
        return false;
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        if (game != null) {
            game.stop();
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
     * @return true if handled
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
     * Main entry point for the application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public String toString() {
        return "App{" +
                "game=" + (game != null ? game.getState() : "stopped") +
                '}';
    }
}
