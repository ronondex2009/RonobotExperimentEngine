package org.ronobot.engine.input;

import org.ronobot.engine.core.Game;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.math.Position;

/**
 * InputHandler processes user input from keyboard and converts it to game actions.
 * <p>
 * This class handles keyboard input processing and translates key presses
 * into player movement and game actions. It supports basic movement keys:
 * WASD/Arrow keys for movement, space for actions (jump/shoot), etc.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class InputHandler {

    /**
     * Horizontal movement speed factor.
     */
    private static final float MOVE_SPEED = 0.5f;

    /**
     * Vertical movement speed factor (strafing).
     */
    private static final float STRAFING_SPEED = 0.5f;

    /**
     * Tile width in pixels.
     */
    private static final int TILE_WIDTH = 32;

    /**
     * Tile height in pixels.
     */
    private static final int TILE_HEIGHT = 32;

    /**
     * Whether the user is moving right.
     */
    private boolean isMovingRight = false;

    /**
     * Whether the user is moving left.
     */
    private boolean isMovingLeft = false;

    /**
     * Whether the user is moving up (strafing).
     */
    private boolean isMovingUp = false;

    /**
     * Whether the user is moving down (strafing).
     */
    private boolean isMovingDown = false;

    /**
     * Whether an action is triggered (space bar or action key).
     */
    private boolean actionPressed = false;

    /**
     * Whether an action is held down.
     */
    private boolean actionHeld = false;

    /**
     * Whether the jump key is pressed.
     */
    private boolean jumpPressed = false;

    /**
     * Whether the shoot key is pressed.
     */
    private boolean shootPressed = false;

    /**
     * The current game map for boundary checking.
     */
    private GameMap gameMap;

    /**
     * Creates a new InputHandler with default key bindings.
     */
    public InputHandler() {
    }

    /**
     * Sets the game map for boundary checking.
     *
     * @param gameMap The game map
     */
    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * Gets the current game map.
     *
     * @return The game map, or null if not set
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * Gets whether the user is moving right (WASD: D, Arrows: →).
     *
     * @return true if moving right
     */
    public boolean isMovingRight() {
        return isMovingRight;
    }

    /**
     * Gets whether the user is moving left (WASD: A, Arrows: ←).
     *
     * @return true if moving left
     */
    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    /**
     * Gets whether the user is moving up (WASD: W, Arrows: ↑).
     *
     * @return true if moving up
     */
    public boolean isMovingUp() {
        return isMovingUp;
    }

    /**
     * Gets whether the user is moving down (WASD: S, Arrows: ↓).
     *
     * @return true if moving down
     */
    public boolean isMovingDown() {
        return isMovingDown;
    }

    /**
     * Gets whether an action was pressed (space bar or action key).
     *
     * @return true if action was pressed
     */
    public boolean isActionPressed() {
        return actionPressed;
    }

    /**
     * Gets whether an action is currently held.
     *
     * @return true if action is held
     */
    public boolean isActionHeld() {
        return actionHeld;
    }

    /**
     * Gets whether the jump key is pressed.
     *
     * @return true if jump key is pressed
     */
    public boolean isJumpPressed() {
        return jumpPressed;
    }

    /**
     * Gets whether the shoot key is pressed.
     *
     * @return true if shoot key is pressed
     */
    public boolean isShootPressed() {
        return shootPressed;
    }

    /**
     * Checks if horizontal movement is active.
     *
     * @return true if moving horizontally
     */
    public boolean isMoving() {
        return isMovingRight || isMovingLeft;
    }

    /**
     * Checks if any vertical movement is active.
     *
     * @return true if moving vertically
     */
    public boolean isStrafing() {
        return isMovingUp || isMovingDown;
    }

    /**
     * Marks the right movement key as pressed.
     *
     * @param pressed true if moving right
     */
    public void setMovingRight(boolean pressed) {
        this.isMovingRight = pressed;
    }

    /**
     * Marks the left movement key as pressed.
     *
     * @param pressed true if moving left
     */
    public void setMovingLeft(boolean pressed) {
        this.isMovingLeft = pressed;
    }

    /**
     * Marks the up movement key as pressed.
     *
     * @param pressed true if moving up
     */
    public void setMovingUp(boolean pressed) {
        this.isMovingUp = pressed;
    }

    /**
     * Marks the down movement key as pressed.
     *
     * @param pressed true if moving down
     */
    public void setMovingDown(boolean pressed) {
        this.isMovingDown = pressed;
    }

    /**
     * Marks an action key as pressed.
     *
     * @param pressed true if action was pressed
     */
    public void setActionPressed(boolean pressed) {
        this.actionPressed = pressed;
    }

    /**
     * Marks an action key as held.
     *
     * @param held true if action is held
     */
    public void setActionHeld(boolean held) {
        this.actionHeld = held;
    }

    /**
     * Marks the jump key as pressed.
     *
     * @param pressed true if jump key was pressed
     */
    public void setJumpPressed(boolean pressed) {
        this.jumpPressed = pressed;
    }

    /**
     * Marks the shoot key as pressed.
     *
     * @param pressed true if shoot key was pressed
     */
    public void setShootPressed(boolean pressed) {
        this.shootPressed = pressed;
    }

    /**
     * Processes keyboard input and updates player movement state.
     * <p>
     * This method handles player movement. Movement is applied to the player
     * without boundary clamping in the InputHandler. Boundary checks should be
     * handled at the game level or by the PhysicsEngine.
     * </p>
     *
     * @param player The player entity (can be null)
     */
    public void handle(PlayerEntity player) {
        if (player == null) {
            return;
        }

        Position pos = player.getPosition();
        if (pos == null) {
            return;
        }

        float dx = 0;
        float dy = 0;

        // Handle horizontal movement
        if (isMovingRight) {
            dx += MOVE_SPEED;
        }
        if (isMovingLeft) {
            dx -= MOVE_SPEED;
        }

        // Handle vertical movement (strafing)
        if (isMovingUp) {
            dy += STRAFING_SPEED;
        }
        if (isMovingDown) {
            dy -= STRAFING_SPEED;
        }

        // Apply movement to player position
        if (dx != 0f || dy != 0f) {
            // Calculate new position
            float newX = pos.getX() + dx;
            float newY = pos.getY() + dy;

            // Apply boundary checking if a map is set
            if (gameMap != null) {
                // Check map boundaries
                float mapWidth = gameMap.getWidth() * TILE_WIDTH;
                float mapHeight = gameMap.getHeight() * TILE_HEIGHT;

                // Clamp to map bounds
                newX = Math.max(0, Math.min(newX, mapWidth));
                newY = Math.max(0, Math.min(newY, mapHeight));
            }

            player.setPosition(newX, newY);
        }

        // Reset action pressed flag after movement handling
        actionPressed = false;
    }

    /**
     * Processes keyboard input for the game.
     *
     * @param game The game state (can be null)
     */
    public void handle(Game game) {
        if (game == null) {
            return;
        }
        PlayerEntity player = game.getPlayer();
        if (player == null) {
            return;
        }
        handle(player);
    }

    /**
     * Processes action input and triggers game actions (shoot, jump, interact).
     * <p>
     * This method handles action key input and can be extended to trigger
     * various game actions like shooting, jumping, or interacting with items.
     * </p>
     *
     * @param game The game state containing player and world
     */
    public void handleActions(Game game) {
        if (game == null) {
            return;
        }
        PlayerEntity player = game.getPlayer();
        if (player == null) {
            return;
        }

        // Handle jump action
        if (jumpPressed) {
            game.triggerAction("jump");
            jumpPressed = false; // Auto-reset on each frame
        }

        // Handle shoot action
        if (shootPressed) {
            game.triggerAction("shoot");
            shootPressed = false; // Auto-reset on each frame
        }

        // Handle action key (e.g., use item, interact)
        if (actionPressed) {
            game.triggerAction("action");
            actionPressed = false; // Auto-reset on each frame
        }

        // Handle held action
        if (actionHeld) {
            game.triggerAction("action_held");
            // Keep held flag true, don't auto-reset
        }
    }

    /**
     * Resets all input state.
     */
    public void reset() {
        isMovingRight = false;
        isMovingLeft = false;
        isMovingUp = false;
        isMovingDown = false;
        actionPressed = false;
        actionHeld = false;
        jumpPressed = false;
        shootPressed = false;
    }

    /**
     * Gets a string representation of the current input state.
     *
     * @return String representation of input state
     */
    @Override
    public String toString() {
        return "InputHandler{" +
                "isMovingRight=" + isMovingRight +
                ", isMovingLeft=" + isMovingLeft +
                ", isMovingUp=" + isMovingUp +
                ", isMovingDown=" + isMovingDown +
                ", actionPressed=" + actionPressed +
                ", actionHeld=" + actionHeld +
                ", jumpPressed=" + jumpPressed +
                ", shootPressed=" + shootPressed +
                '}';
    }
}
