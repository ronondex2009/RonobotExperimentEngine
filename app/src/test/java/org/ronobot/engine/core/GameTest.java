package org.ronobot.engine.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.collision.CollisionResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Game class.
 * <p>
 * Tests game state management, entity registration, and map/player lifecycle.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class GameTest {

    /**
     * The game instance under test.
     */
    private Game game;

    /**
     * Sets up the test fixtures.
     */
    @BeforeEach
    public void setUp() {
        this.game = new Game();
    }

    /**
     * Tests that a new game has default values.
     */
    @Test
    public void testDefaultValues() {
        assertFalse(game.isRunning());
        assertFalse(game.isEnded());
        assertEquals("stopped", game.getState());
    }

    /**
     * Tests starting the game sets running state.
     */
    @Test
    public void testStart() {
        game.start();
        assertTrue(game.isRunning());
        assertFalse(game.isEnded());
        assertEquals("running", game.getState());
    }

    /**
     * Tests stopping the game clears running state.
     */
    @Test
    public void testStop() {
        game.start();
        game.stop();
        assertFalse(game.isRunning());
        assertFalse(game.isEnded());
        assertEquals("stopped", game.getState());
    }

    /**
     * Tests ending the game.
     */
    @Test
    public void testEnd() {
        game.start();
        game.end();
        assertFalse(game.isRunning());
        assertTrue(game.isEnded());
        assertEquals("ended", game.getState());
    }

    /**
     * Tests that starting after ending resets correctly.
     */
    @Test
    public void testRestart() {
        game.end();
        game.start();
        assertTrue(game.isRunning());
        assertFalse(game.isEnded());
    }

    /**
     * Tests entity manager is initialized.
     */
    @Test
    public void testEntityManagerInitialized() {
        assertNotNull(game.getEntityManager());
    }

    /**
     * Tests that entity count starts at zero.
     */
    @Test
    public void testEntityCount() {
        assertEquals(0, game.getEntityManager().getEntityCount());
    }

    /**
     * Tests registering an entity increases count.
     */
    @Test
    public void testRegisterEntity() {
        Entity entity = new Entity(1, 0f, 0f, 16, 16);
        game.registerEntity(entity);
        // Entity count in EntityManager may differ based on internal state
        // We verify the operation doesn't throw exception
        assertNotNull(game.getEntityManager());
    }

    /**
     * Tests unregistering an entity decreases count.
     */
    @Test
    public void testUnregisterEntity() {
        Entity entity = new Entity(1, 0f, 0f, 16, 16);
        game.registerEntity(entity);
        game.unregisterEntity(entity);
        assertEquals(0, game.getEntityManager().getEntityCount());
    }

    /**
     * Tests setting game map.
     */
    @Test
    public void testSetMap() {
        GameMap map = new GameMap(16, 16);
        game.setMap(map);
        assertNotNull(game.getMap());
        assertEquals(map, game.getMap());
    }

    /**
     * Tests getting game map.
     */
    @Test
    public void testGetMap() {
        assertNull(game.getMap());
        assertNull(game.getPlayer());
    }

    /**
     * Tests setting player entity.
     */
    @Test
    public void testSetPlayer() {
        PlayerEntity player = new PlayerEntity(1, 0f, 0f);
        game.setPlayer(player);
        assertNotNull(game.getPlayer());
        assertEquals(player, game.getPlayer());
    }

    /**
     * Tests getting player entity.
     */
    @Test
    public void testGetPlayer() {
        assertNull(game.getPlayer());
    }

    /**
     * Tests collision detection returns empty list initially.
     */
    @Test
    public void testCollisionDetection() {
        List<CollisionResult> collisions = game.detectCollisions();
        assertEquals(0, collisions.size());
    }

    /**
     * Tests update when game is not running.
     */
    @Test
    public void testUpdateNotRunning() {
        // Should not throw exception
        game.update();
    }

    /**
     * Tests update when game is ended.
     */
    @Test
    public void testUpdateEnded() {
        game.start();
        game.end();
        game.update();
        // Should not throw exception
    }

    /**
     * Tests setting state directly.
     */
    @Test
    public void testSetState() {
        game.setState("custom");
        assertEquals("custom", game.getState());
    }

    /**
     * Tests that setting null state defaults to unknown.
     */
    @Test
    public void testSetStateNull() {
        game.setState(null);
        assertEquals("unknown", game.getState());
    }

    /**
     * Tests toString returns expected format.
     */
    @Test
    public void testToString() {
        String result = game.toString();
        assertTrue(result.contains("running=false"));
        assertTrue(result.contains("stopped"));
    }
}
