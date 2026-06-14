package org.ronobot.engine.core;

import org.ronobot.engine.collision.CollisionManager;
import org.ronobot.engine.collision.CollisionResult;
import org.ronobot.engine.entities.EntityManager;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.input.InputHandler;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.physics.PhysicsEngine;
import org.ronobot.engine.render.Renderer;
import org.ronobot.engine.render.Renderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Core game state management.
 * <p>
 * Manages entities, collision detection, rendering, and game loop state.
 * This class serves as the central game state container and provides
 * the game loop implementation for continuous rendering and processing.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class Game {

    /**
     * Whether the game is running.
     */
    private boolean running = false;

    /**
     * Whether the game has ended.
     */
    private boolean ended = false;

    /**
     * The entity manager.
     */
    private EntityManager entities;

    /**
     * The collision manager.
     */
    private CollisionManager collisionManager;

    /**
     * The game map.
     */
    private GameMap map;

    /**
     * The player entity.
     */
    private PlayerEntity player;

    /**
     * Current state description.
     */
    private String state = "stopped";

    /**
     * The physics engine.
     */
    private PhysicsEngine physicsEngine;

    /**
     * The renderer.
     */
    private Renderer renderer;

    /**
     * Frame counter.
     */
    private int frameCount = 0;

    /**
     * Creates a new Game instance.
     */
    public Game() {
        this.entities = new EntityManager();
        this.collisionManager = new CollisionManager();
        this.physicsEngine = new PhysicsEngine();
        this.renderer = new Renderer();
    }

    /**
     * Initializes the game.
     */
    public void init() {
        this.running = true;
        this.ended = false;
        this.state = "initialized";
    }

    /**
     * Sets the game map.
     *
     * @param map The game map
     */
    public void setMap(GameMap map) {
        this.map = map;
    }

    /**
     * Gets the game map.
     *
     * @return The game map
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Gets the collision manager.
     *
     * @return The collision manager
     */
    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    /**
     * Sets the player entity.
     *
     * @param player The player entity
     */
    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    /**
     * Gets the player entity.
     *
     * @return The player entity
     */
    public PlayerEntity getPlayer() {
        return player;
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        this.running = true;
        this.ended = false;
        this.state = "running";
        this.inputHandler = null;
        // Note: Use runLoop() for actual game loop execution
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        this.running = false;
        this.state = "stopped";
    }

    /**
     * Ends the game.
     */
    public void end() {
        this.ended = true;
        this.running = false;
        this.state = "ended";
    }

    /**
     * Updates all game entities.
     */
    public void update() {
        if (running && !ended) {
            entities.update();
            physicsEngine.process(this);
            incrementFrame();
        }
    }

    /**
     * Increments the frame count.
     */
    private void incrementFrame() {
        frameCount++;
    }

    /**
     * Gets the physics engine.
     *
     * @return The physics engine
     */
    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    /**
     * Gets the frame count.
     *
     * @return The current frame count
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * Cleans up the game.
     */
    public void cleanup() {
        this.running = false;
        this.ended = true;
        this.state = "cleanup";
    }

    /**
     * Sets the physics engine.
     *
     * @param physicsEngine The physics engine
     */
    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    /**
     * Detects collisions between entities.
     *
     * @return List of collision results
     */
    public List<CollisionResult> detectCollisions() {
        if (collisionManager != null && collisionManager.getEntityCount() > 0) {
            return collisionManager.findCollisions();
        }
        return new ArrayList<>();
    }

    /**
     * Registers an entity for collision detection.
     *
     * @param entity The entity to register
     */
    public void registerEntity(org.ronobot.engine.core.Entity entity) {
        if (collisionManager != null) {
            collisionManager.registerEntity(entity);
        }
    }

    /**
     * Unregisters an entity from collision detection.
     *
     * @param entity The entity to unregister
     */
    public void unregisterEntity(org.ronobot.engine.core.Entity entity) {
        if (collisionManager != null) {
            collisionManager.unregisterEntity(entity);
        }
    }

    /**
     * Gets whether the game is running.
     *
     * @return true if running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets whether the game has ended.
     *
     * @return true if ended
     */
    public boolean isEnded() {
        return ended;
    }

    /**
     * Gets the entity manager.
     *
     * @return The entity manager
     */
    public EntityManager getEntityManager() {
        return entities;
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
     * Sets the renderer.
     *
     * @param renderer The renderer to set
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Runs the main game loop.
     * <p>
     * This method continuously updates game state and renders frames
     * until the game is stopped or ended. Use this to create a continuous
     * game loop with proper rendering support.
     * </p>
     * <p>
     * The loop will:
     * - Process input
     * - Update entities and physics
     * - Detect collisions
     * - Render the current frame
     * </p>
     */
    public void runLoop() {
        while (running && !ended) {
            try {
                // Process input
                if (inputHandler != null) {
                    inputHandler.handle(this);
                }

                // Update game state
                update();

                // Detect collisions
                List<CollisionResult> collisions = detectCollisions();
                for (CollisionResult collision : collisions) {
                    collision.resolve(this);
                }

                // Render the frame
                if (renderer != null) {
                    renderer.render(this);
                }

                // Yield control to allow other threads to run
                Thread.sleep(16); // Approx 60 FPS
                incrementFrame();

            } catch (InterruptedException e) {
                // Thread interrupted, exit loop
                break;
            } catch (IllegalArgumentException e) {
                // Input was not handled, but continue the loop
                continue;
            }
        }
    }

    /**
     * Input handler for the game loop.
     *
     * @return The input handler, or null if not initialized
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Sets the input handler for the game loop.
     *
     * @param handler The input handler to set
     */
    public void setInputHandler(InputHandler handler) {
        this.inputHandler = handler;
    }

    /**
     * The input handler for the game loop.
     */
    private InputHandler inputHandler;

    /**
     * Gets the current state.
     *
     * @return The current state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the current state.
     *
     * @param state The new state
     */
    public void setState(String state) {
        this.state = state != null ? state : "unknown";
    }

    @Override
    public String toString() {
        return "Game{" +
                "running=" + running +
                ", ended=" + ended +
                ", state='" + state + '\'' +
                ", entityCount=" + (entities != null ? entities.getEntityCount() : 0) +
                '}';
    }
}
