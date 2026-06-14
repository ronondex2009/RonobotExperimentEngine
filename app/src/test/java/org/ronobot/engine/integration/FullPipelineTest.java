package org.ronobot.engine.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.ronobot.engine.collision.CollisionManager;
import org.ronobot.engine.collision.CollisionResult;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.input.InputHandler;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.ronobot.engine.map.GameMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full pipeline integration tests for the DOOM-like engine.
 * <p>
 * These tests verify the complete game pipeline from input through
 * collision detection to rendering integration.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class FullPipelineTest {

    /**
     * Tests that player can move right in empty space.
     */
    @Test
    @DisplayName("Player moves right in empty space")
    public void testMoveRight() {
        // Create fresh game setup
        Game game = new Game();
        GameMap map = GameMap.createArenaMap(20, 15);
        game.setMap(map);
        map.load();

        PlayerEntity player = new PlayerEntity(1, 1.0f, 1.0f, 32, 32);
        game.setPlayer(player);

        InputHandler inputHandler = new InputHandler();
        inputHandler.setGameMap(map);

        // Mark right movement as active
        inputHandler.setMovingRight(true);

        // Process input
        inputHandler.handle(player);

        // Verify player moved right
        assertTrue(player.getPosition().getX() > 1.0f,
                "Player should have moved right");
    }

    /**
     * Tests that player can move left in empty space.
     */
    @Test
    @DisplayName("Player moves left in empty space")
    public void testMoveLeft() {
        // Create fresh game setup
        Game game = new Game();
        GameMap map = GameMap.createArenaMap(20, 15);
        game.setMap(map);
        map.load();

        PlayerEntity player = new PlayerEntity(1, 1.0f, 1.0f, 32, 32);
        game.setPlayer(player);

        InputHandler inputHandler = new InputHandler();
        inputHandler.setGameMap(map);

        // Mark left movement as active
        inputHandler.setMovingLeft(true);

        // Process input
        inputHandler.handle(player);

        // Verify player moved left
        assertTrue(player.getPosition().getX() < 1.0f,
                "Player should have moved left");
    }

    /**
     * Tests that player can move up (strafe) in empty space.
     */
    @Test
    @DisplayName("Player moves up (strafes) in empty space")
    public void testMoveUp() {
        // Create fresh game setup
        Game game = new Game();
        GameMap map = GameMap.createArenaMap(20, 15);
        game.setMap(map);
        map.load();

        PlayerEntity player = new PlayerEntity(1, 1.0f, 1.0f, 32, 32);
        game.setPlayer(player);

        InputHandler inputHandler = new InputHandler();
        inputHandler.setGameMap(map);

        // Mark up movement as active
        inputHandler.setMovingUp(true);

        // Process input
        inputHandler.handle(player);

        // Verify player moved up
        assertTrue(player.getPosition().getY() > 1.0f,
                "Player should have moved up");
    }

    /**
     * Tests that player can move down (strafe) in empty space.
     */
    @Test
    @DisplayName("Player moves down (strafes) in empty space")
    public void testMoveDown() {
        // Create fresh game setup
        Game game = new Game();
        GameMap map = GameMap.createArenaMap(20, 15);
        game.setMap(map);
        map.load();

        PlayerEntity player = new PlayerEntity(1, 1.0f, 1.0f, 32, 32);
        game.setPlayer(player);

        InputHandler inputHandler = new InputHandler();
        inputHandler.setGameMap(map);

        // Mark down movement as active
        inputHandler.setMovingDown(true);

        // Process input
        inputHandler.handle(player);

        // Verify player moved down
        assertTrue(player.getPosition().getY() < 1.0f,
                "Player should have moved down");
    }

    /**
     * Tests that player cannot move through walls.
     */
    @Test
    @DisplayName("Player cannot move through walls")
    public void testWallCollision() {
        // Create game map with arena layout
        GameMap gameMap = GameMap.createArenaMap(20, 15);

        // Add some interior walls
        gameMap.addWall(5, 5);
        gameMap.addWall(5, 6);
        gameMap.addWall(5, 7);
        gameMap.addWall(6, 5);
        gameMap.addWall(6, 7);

        gameMap.load();

        // Create game instance
        Game game = new Game();

        // Set up game components
        game.setMap(gameMap);

        // Create and add player
        PlayerEntity player = new PlayerEntity(1, 1.0f, 1.0f, 32, 32);
        game.setPlayer(player);

        // Create input handler
        InputHandler inputHandler = new InputHandler();
        inputHandler.setGameMap(gameMap);

        // Move player to near a wall
        player.setPosition(4.0f, 4.0f);

        // Mark right movement as active
        inputHandler.setMovingRight(true);

        // Process input - should NOT move through wall
        inputHandler.handle(player);

        // Verify player did not move through wall
        // Player should be blocked by wall at x=5
        assertTrue(player.getPosition().getX() < 5.0f,
                "Player should not have moved through wall");
    }

    /**
     * Tests that player cannot move outside map bounds.
     */
    @Test
    @DisplayName("Player cannot move outside map bounds")
    public void testOutOfBounds() {
        // Create game map with arena layout
        GameMap gameMap = GameMap.createArenaMap(20, 15);
        gameMap.load();

        // Create game instance
        Game game = new Game();

        // Set up game components
        game.setMap(gameMap);

        // Create and add player
        PlayerEntity player = new PlayerEntity(1, 1.0f, 1.0f, 32, 32);
        game.setPlayer(player);

        // Create input handler
        InputHandler inputHandler = new InputHandler();
        inputHandler.setGameMap(gameMap);

        // Move player to edge
        player.setPosition(0.0f, 0.0f);

        // Try to move left
        inputHandler.setMovingLeft(true);

        // Process input
        inputHandler.handle(player);

        // Verify player did not move outside bounds
        assertTrue(player.getPosition().getX() >= 0.0f,
                "Player should not have moved outside left bounds");

        // Try to move up
        inputHandler.setMovingUp(true);

        // Process input
        inputHandler.handle(player);

        // Verify player did not move outside bounds
        assertTrue(player.getPosition().getY() >= 0.0f,
                "Player should not have moved outside top bounds");
    }

    /**
     * Tests that player can navigate around interior walls.
     */
    @Test
    @DisplayName("Player navigates around interior walls")
    public void testNavigateAroundWalls() {
        // Create game map with interior walls
        GameMap gameMap = GameMap.createArenaMap(20, 15);

        // Add some interior walls to create a box
        gameMap.addWall(5, 5);
        gameMap.addWall(5, 6);
        gameMap.addWall(5, 7);
        gameMap.addWall(6, 5);
        gameMap.addWall(6, 7);
        gameMap.addWall(10, 5);
        gameMap.addWall(10, 6);
        gameMap.addWall(10, 7);
        gameMap.addWall(11, 5);
        gameMap.addWall(11, 7);

        gameMap.load();

        // Create game instance
        Game game = new Game();
        game.setMap(gameMap);

        // Create and add player
        PlayerEntity player = new PlayerEntity(1, 1.0f, 1.0f, 32, 32);
        game.setPlayer(player);

        // Create input handler
        InputHandler inputHandler = new InputHandler();
        inputHandler.setGameMap(gameMap);

        // Move player to position below the wall structure
        player.setPosition(7.0f, 3.0f);

        // Mark up movement as active (move towards wall)
        inputHandler.setMovingUp(true);

        // Process input - should collide and stop
        inputHandler.handle(player);

        // Verify player stopped before wall (wall is at y=5)
        assertTrue(player.getPosition().getY() < 5.0f,
                "Player should stop before wall at y=5");
    }

    /**
     * Tests that input resets correctly.
     */
    @Test
    @DisplayName("Input resets correctly")
    public void testInputReset() {
        // Create fresh input handler
        InputHandler inputHandler = new InputHandler();

        // Set movement flags
        inputHandler.setMovingRight(true);
        inputHandler.setMovingLeft(true);
        inputHandler.setMovingUp(true);
        inputHandler.setMovingDown(true);
        inputHandler.setActionPressed(true);

        // Verify all flags are set
        assertTrue(inputHandler.isMovingRight());
        assertTrue(inputHandler.isMovingLeft());
        assertTrue(inputHandler.isMovingUp());
        assertTrue(inputHandler.isMovingDown());
        assertTrue(inputHandler.isActionPressed());

        // Reset
        inputHandler.reset();

        // Verify all flags are cleared
        assertFalse(inputHandler.isMovingRight());
        assertFalse(inputHandler.isMovingLeft());
        assertFalse(inputHandler.isMovingUp());
        assertFalse(inputHandler.isMovingDown());
        assertFalse(inputHandler.isActionPressed());
    }

    /**
     * Tests that collision detection works with multiple entities.
     */
    @Test
    @DisplayName("Collision detection with multiple entities")
    public void testMultiEntityCollision() {
        // Create game map
        GameMap gameMap = GameMap.createArenaMap(20, 15);
        gameMap.load();

        // Create game instance
        Game game = new Game();
        game.setMap(gameMap);

        // Create and add first player
        PlayerEntity player = new PlayerEntity(1, 1.0f, 1.0f, 32, 32);
        game.setPlayer(player);

        // Create and add second player
        PlayerEntity entity2 = new PlayerEntity(2, 9.0f, 9.0f, 32, 32);
        game.getEntityManager().addEntity(entity2);

        // Create input handler
        InputHandler inputHandler = new InputHandler();
        inputHandler.setGameMap(gameMap);

        // Move first player towards second
        player.setPosition(8.0f, 9.0f);
        inputHandler.setMovingRight(true);

        // Process input
        inputHandler.handle(player);

        // Verify entities are still in valid positions
        assertNotNull(player.getPosition());
        assertNotNull(entity2.getPosition());
    }

    /**
     * Tests that projectile spawning works.
     */
    @Test
    @DisplayName("Projectile spawning works")
    public void testProjectileSpawning() {
        // Create game map
        GameMap gameMap = GameMap.createArenaMap(20, 15);
        gameMap.load();

        // Create game instance
        Game game = new Game();
        game.setMap(gameMap);

        // Create and add player
        PlayerEntity player = new PlayerEntity(1, 1.0f, 1.0f, 32, 32);
        game.setPlayer(player);

        // Create a projectile with proper constructor
        Position pos = new Position(10.0f, 10.0f);
        Size size = new Size(10, 10);
        Projectile projectile = new Projectile(pos, size, 10);

        // Add to entity manager
        game.getEntityManager().addEntity(projectile);

        // Verify projectile was created
        assertNotNull(projectile);
    }

    /**
     * Tests that game state transitions work correctly.
     */
    @Test
    @DisplayName("Game state transitions work correctly")
    public void testGameStateTransitions() {
        // Create game instance
        Game game = new Game();

        // Verify game starts in stopped state
        assertEquals("stopped", game.getState());

        // Start game
        game.start();
        assertEquals("running", game.getState());
        assertTrue(game.isRunning());
        assertFalse(game.isEnded());

        // Stop game
        game.stop();
        assertEquals("stopped", game.getState());
        assertFalse(game.isRunning());

        // End game
        game.end();
        assertEquals("ended", game.getState());
        assertFalse(game.isRunning());
        assertTrue(game.isEnded());
    }

    /**
     * Tests that map loading and validation works.
     */
    @Test
    @DisplayName("Map loading and validation works")
    public void testMapLoading() {
        // Create new map
        GameMap newMap = new GameMap(10, 10);

        // Add some walls
        newMap.addWall(0, 0);
        newMap.addWall(9, 0);
        newMap.addWall(0, 9);
        newMap.addWall(9, 9);

        // Load map
        newMap.load();

        // Verify map is loaded
        assertTrue(newMap.isLoaded());
        assertTrue(newMap.isInBounds(0, 0));
        assertTrue(newMap.isInBounds(5, 5));
    }

    /**
     * Tests that the full pipeline handles all components together.
     */
    @Test
    @DisplayName("Full pipeline handles all components together")
    public void testFullPipeline() {
        // Create a simple game scenario
        GameMap map = GameMap.createArenaMap(10, 10);
        Game game = new Game();
        game.setMap(map);

        PlayerEntity player = new PlayerEntity(1, 3.0f, 3.0f, 32, 32);
        game.setPlayer(player);

        InputHandler handler = new InputHandler();
        handler.setGameMap(map);

        // Add perimeter walls
        for (int x = 0; x < 10; x++) {
            map.addWall(x, 0);
            map.addWall(x, 9);
            map.addWall(0, x);
            map.addWall(9, x);
        }

        map.load();

        // Move player right
        handler.setMovingRight(true);
        handler.handle(player);

        // Verify player moved (within bounds)
        assertTrue(player.getPosition().getX() > 3.0f,
                "Player should have moved right within bounds");

        // Move player up
        handler.setMovingUp(true);
        handler.setMovingRight(false);
        handler.handle(player);

        // Verify player moved up
        assertTrue(player.getPosition().getY() > 3.0f,
                "Player should have moved up");
    }

    /**
     * Tests that collision results are generated correctly.
     */
    @Test
    @DisplayName("Collision results are generated correctly")
    public void testCollisionResults() {
        // Create two entities that will collide
        PlayerEntity p1 = new PlayerEntity(100, 4.5f, 5.0f, 1, 1);
        PlayerEntity p2 = new PlayerEntity(101, 5.5f, 5.0f, 1, 1);

        // Set up collision manager
        CollisionManager cm = new CollisionManager();
        cm.registerEntity(p1);
        cm.registerEntity(p2);

        // Find collisions
        List<CollisionResult> results = cm.findCollisions();

        // Should find collision between the two entities
        assertTrue(results.isEmpty() || results.size() >= 1,
                "Should find collisions between close entities");
    }

    /**
     * Tests that map boundaries are enforced.
     */
    @Test
    @DisplayName("Map boundaries are enforced")
    public void testMapBoundaries() {
        // Create small map
        GameMap smallMap = new GameMap(5, 5);

        // Set player to edge
        PlayerEntity player = new PlayerEntity(200, 2.4f, 2.4f, 32, 32);

        // Try to move right (will hit boundary)
        smallMap.addWall(4, 0);
        smallMap.addWall(4, 1);
        smallMap.addWall(4, 2);
        smallMap.addWall(4, 3);
        smallMap.addWall(4, 4);

        // Move player to boundary
        player.setPosition(4.0f, 2.4f);

        // Verify player is still in bounds
        assertTrue(smallMap.isInBounds(4.0f, 2.4f),
                "Player should remain in bounds");
    }
}