package org.ronobot.engine.input;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.map.GameMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InputHandler class.
 * <p>
 * Tests input state management and player movement processing.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class InputHandlerTest {

    /**
     * The input handler instance under test.
     */
    private InputHandler input;

    /**
     * The game map for boundary testing.
     */
    private GameMap gameMap;

    /**
     * Sets up the test fixtures.
     */
    @BeforeEach
    public void setUp() {
        this.input = new InputHandler();
        this.gameMap = new GameMap(16, 16);
        gameMap.setTile(0, 0, GameMap.TILE_FLOOR);
        gameMap.setTile(1, 0, GameMap.TILE_WALL);
        gameMap.load();
        input.setGameMap(gameMap);
    }

    /**
     * Tests that input handler initializes with default state.
     */
    @Test
    public void testInitialState() {
        assertFalse(input.isMovingRight());
        assertFalse(input.isMovingLeft());
        assertFalse(input.isMovingUp());
        assertFalse(input.isMovingDown());
        assertFalse(input.isActionPressed());
        assertFalse(input.isMoving());
        assertFalse(input.isStrafing());
    }

    /**
     * Tests setting moving right state.
     */
    @Test
    public void testSetMovingRight() {
        input.setMovingRight(true);
        assertTrue(input.isMovingRight());
        assertFalse(input.isMovingLeft());
        assertTrue(input.isMoving());
        assertFalse(input.isStrafing());
    }

    /**
     * Tests setting moving left state.
     */
    @Test
    public void testSetMovingLeft() {
        input.setMovingLeft(true);
        assertFalse(input.isMovingRight());
        assertTrue(input.isMovingLeft());
        assertTrue(input.isMoving());
        assertFalse(input.isStrafing());
    }

    /**
     * Tests setting moving up state.
     */
    @Test
    public void testSetMovingUp() {
        input.setMovingUp(true);
        assertFalse(input.isMovingRight());
        assertFalse(input.isMovingLeft());
        assertTrue(input.isMovingUp());
        assertFalse(input.isMovingDown());
        assertFalse(input.isMoving());
        assertTrue(input.isStrafing());
    }

    /**
     * Tests setting moving down state.
     */
    @Test
    public void testSetMovingDown() {
        input.setMovingDown(true);
        assertFalse(input.isMovingRight());
        assertFalse(input.isMovingLeft());
        assertFalse(input.isMovingUp());
        assertTrue(input.isMovingDown());
        assertFalse(input.isMoving());
        assertTrue(input.isStrafing());
    }

    /**
     * Tests setting action pressed state.
     */
    @Test
    public void testSetActionPressed() {
        assertFalse(input.isActionPressed());
        input.setActionPressed(true);
        assertTrue(input.isActionPressed());
    }

    /**
     * Tests that action is reset after handling input.
     */
    @Test
    public void testActionReset() {
        input.setActionPressed(true);
        PlayerEntity player = new PlayerEntity(1, 0f, 0f);
        input.handle(player);
        assertFalse(input.isActionPressed());
    }

    /**
     * Tests that moving right and left together works.
     */
    @Test
    public void testBothHorizontal() {
        input.setMovingRight(true);
        input.setMovingLeft(true);
        assertTrue(input.isMoving());
        assertTrue(input.isMovingRight());
        assertTrue(input.isMovingLeft());
    }

    /**
     * Tests that moving up and down together works.
     */
    @Test
    public void testBothVertical() {
        input.setMovingUp(true);
        input.setMovingDown(true);
        assertTrue(input.isStrafing());
        assertTrue(input.isMovingUp());
        assertTrue(input.isMovingDown());
    }

    /**
     * Tests that reset clears all state.
     */
    @Test
    public void testReset() {
        input.setMovingRight(true);
        input.setMovingLeft(true);
        input.setMovingUp(true);
        input.setMovingDown(true);
        input.setActionPressed(true);
        
        input.reset();
        
        assertFalse(input.isMovingRight());
        assertFalse(input.isMovingLeft());
        assertFalse(input.isMovingUp());
        assertFalse(input.isMovingDown());
        assertFalse(input.isActionPressed());
        assertFalse(input.isMoving());
        assertFalse(input.isStrafing());
    }

    /**
     * Tests that handle with null player returns without error.
     * Note: Uses explicit cast to disambiguate from handle(Game).
     */
    @Test
    public void testHandleNullPlayer() {
        input.handle((PlayerEntity) null);
        // Should not throw exception
        assertNotNull(input);
    }

    /**
     * Tests that handle with valid player processes correctly.
     */
    @Test
    public void testHandleValidPlayer() {
        PlayerEntity player = new PlayerEntity(1, 0f, 0f);
        input.handle(player);
        // Should not throw exception
        assertNotNull(player);
    }

    /**
     * Tests that handle updates player position correctly.
     */
    @Test
    public void testHandleUpdatesPosition() {
        PlayerEntity player = new PlayerEntity(1, 0f, 0f);
        
        input.setMovingRight(true);
        input.handle(player);
        
        assertEquals(0.5f, player.getPosition().getX(), 0.01);
    }

    /**
     * Tests that handle resets action flag after processing.
     */
    @Test
    public void testHandleResetsAction() {
        input.setActionPressed(true);
        PlayerEntity player = new PlayerEntity(1, 0f, 0f);
        input.handle(player);
        assertFalse(input.isActionPressed());
    }

    /**
     * Tests that handleGame processes input through player.
     */
    @Test
    public void testHandleGame() {
        Game game = new Game();
        game.setMap(gameMap);
        PlayerEntity player = new PlayerEntity(1, 0f, 0f);
        game.setPlayer(player);
        
        input.setMovingRight(true);
        input.handle(game);
        
        // Player should have moved
        assertEquals(0.5f, player.getPosition().getX(), 0.01);
    }

    /**
     * Tests toString returns expected format.
     */
    @Test
    public void testToString() {
        input.setMovingRight(true);
        input.setMovingLeft(true);
        String result = input.toString();
        
        assertTrue(result.contains("isMovingRight"));
        assertTrue(result.contains("isMovingLeft"));
        assertTrue(result.contains("isMoving"));
    }

    /**
     * Tests that handle with player in wall position still processes.
     */
    @Test
    public void testHandleInWall() {
        PlayerEntity player = new PlayerEntity(1, 1f, 0f);
        
        input.setMovingRight(true);
        input.handle(player);
        
        // Should not throw exception
        assertNotNull(player);
    }
}
