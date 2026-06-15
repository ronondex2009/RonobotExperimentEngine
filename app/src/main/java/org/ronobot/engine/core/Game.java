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

import java.util.ArrayList;
import java.util.List;

/**
 * Core game state management and game loop control.
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
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
     * The input handler.
     */
    private InputHandler inputHandler;

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
     */
    public void setMap(GameMap map) {
        this.map = map;
    }

    /**
     * Gets the game map.
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Gets the collision manager.
     */
    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    /**
     * Sets the player entity.
     */
    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    /**
     * Gets the player entity.
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
            physicsEngine.update(16.0); // 60 FPS = 16ms per frame
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
     * Triggers a game action (shoot, jump, interact, etc).
     *
     * @param actionName The action name
     */
    public void triggerAction(String actionName) {
        // Action trigger logic goes here
        // Currently a stub - can be implemented later
    }

    /**
     * Gets the physics engine.
     */
    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    /**
     * Gets the frame count.
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
     */
    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    /**
     * Adds an entity to the game.
     *
     * @param entity The entity to add
     * @return true if entity added successfully
     */
    public boolean addEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        entities.addEntity(entity);
        return true;
    }

    /**
     * Removes an entity from the game.
     *
     * @param entity The entity to remove
     * @return true if entity removed successfully
     */
    public boolean removeEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        entities.removeEntity(entity);
        return true;
    }

    /**
     * Detects collisions between entities.
     */
    public List<CollisionResult> detectCollisions() {
        if (collisionManager != null && collisionManager.getEntityCount() > 0) {
            return collisionManager.findCollisions();
        }
        return new ArrayList<>();
    }

    /**
     * Registers an entity for collision detection.
     */
    public void registerEntity(Entity entity) {
        if (collisionManager != null) {
            collisionManager.registerEntity(entity);
        }
    }

    /**
     * Unregisters an entity from collision detection.
     */
    public void unregisterEntity(Entity entity) {
        if (collisionManager != null) {
            collisionManager.unregisterEntity(entity);
        }
    }

    /**
     * Gets whether the game is running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets whether the game has ended.
     */
    public boolean isEnded() {
        return ended;
    }

    /**
     * Gets the entity manager.
     */
    public EntityManager getEntityManager() {
        return entities;
    }

    /**
     * Gets the renderer.
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Sets the renderer.
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Runs the main game loop.
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
     * Gets the input handler.
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Sets the input handler.
     */
    public void setInputHandler(InputHandler handler) {
        this.inputHandler = handler;
    }

    /**
     * Gets the current state.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the current state.
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
