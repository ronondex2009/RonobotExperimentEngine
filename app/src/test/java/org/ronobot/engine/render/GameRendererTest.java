package org.ronobot.engine.render;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entity.EnemyEntity;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.entities.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for GameRenderer class.
 * <p>
 * This test suite verifies rendering behavior for GameRenderer,
 * including map tiles, decorations, entities, and projectiles.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class GameRendererTest {

    private GameRenderer renderer;

    /**
     * Test GameRenderer default initialization with arena map.
     */
    @Test
    public void testDefaultInitialization() {
        // Should create an arena map by default
        renderer = new GameRenderer();
        assertNotNull(renderer);
        assertTrue(renderer.hasMap());
    }

    /**
     * Test GameRenderer with null map uses arena map default.
     */
    @Test
    public void testNullMapUsesDefault() {
        renderer = new GameRenderer(null);
        assertNotNull(renderer);
        assertTrue(renderer.hasMap());
    }

    /**
     * Test setGameMap with valid map.
     */
    @Test
    public void testSetGameMap() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer();
        renderer.setGameMap(map);
        assertEquals(map, renderer.getGameMap());
    }

    /**
     * Test setGameMap with null resets to arena map.
     */
    @Test
    public void testSetGameMapNull() {
        GameMap map = GameMap.createArenaMap(32, 32);
        renderer = new GameRenderer();
        renderer.setGameMap(map);
        
        // Now set null - should reset to default arena
        renderer.setGameMap(null);
        assertNotNull(renderer.getGameMap());
        // Should be a new arena map, not the same one
        assertNotSame(map, renderer.getGameMap());
    }

    /**
     * Test renderMap renders all tiles and decorations.
     */
    @Test
    public void testRenderMap() {
        GameMap map = GameMap.createRoomMap(16, 16, new ArrayList<>());
        renderer = new GameRenderer(map);
        
        // Render should not throw exceptions
        renderer.renderMap();
        // Textures should be loaded
        assertTrue(renderer.getTextureCount() > 0);
    }

    /**
     * Test renderMap with null map returns early.
     */
    @Test
    public void testRenderMapNullMap() {
        renderer = new GameRenderer(null);
        
        renderer.renderMap();
        // Should not throw
        assertNotNull(renderer);
    }

    /**
     * Test renderTile with different tile types.
     */
    @Test
    public void testRenderTile() {
        GameMap map = GameMap.createArenaMap(8, 8);
        renderer = new GameRenderer(map);
        
        // Render a wall tile
        renderer.renderTile(0, 0, GameMap.TILE_WALL);
        assertTrue(renderer.hasTexture("tile_" + GameMap.TILE_WALL));
        
        // Render a floor tile
        renderer.renderTile(1, 0, GameMap.TILE_FLOOR);
        assertTrue(renderer.hasTexture("tile_" + GameMap.TILE_FLOOR));
        
        // Render a door tile
        renderer.renderTile(2, 0, GameMap.TILE_DOOR);
        assertTrue(renderer.hasTexture("tile_" + GameMap.TILE_DOOR));
    }

    /**
     * Test renderTile with empty tile does nothing.
     */
    @Test
    public void testRenderTileEmpty() {
        GameMap map = GameMap.createArenaMap(8, 8);
        renderer = new GameRenderer(map);
        
        renderer.renderTile(0, 0, GameMap.TILE_EMPTY);
        // Empty tiles should not create textures
        // (This is expected behavior - empty tiles have no texture)
    }

    /**
     * Test renderDecorations renders all decorations.
     */
    @Test
    public void testRenderDecorations() {
        GameMap map = GameMap.createRoomMap(16, 16, new ArrayList<>());
        
        // Add some decorations
        map.addDecoration(10, 10, "STATUE");
        map.addDecoration(12, 12, "PICTURE");
        
        renderer = new GameRenderer(map);
        renderer.renderMap();
        renderer.renderDecorations();
        
        // Decorations should have been loaded with enum name keys
        assertTrue(renderer.hasTexture("decoration_STATUE"));
        assertTrue(renderer.hasTexture("decoration_PICTURE"));
    }

    /**
     * Test renderDecorations with no decorations.
     */
    @Test
    public void testRenderDecorationsNoDecorations() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        // No decorations added
        renderer.renderDecorations();
        // Should not throw
        assertNotNull(renderer);
    }

    /**
     * Test renderDecorations with null map.
     */
    @Test
    public void testRenderDecorationsNullMap() {
        renderer = new GameRenderer(null);
        renderer.renderDecorations();
        // Should not throw
        assertNotNull(renderer);
    }

    /**
     * Test renderEntities with empty entity list.
     */
    @Test
    public void testRenderEntitiesEmpty() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        // Create a minimal game with empty entity list
        org.ronobot.engine.core.Game game = new org.ronobot.engine.core.Game();
        
        renderer.renderEntities(game);
        // Should not throw
        assertNotNull(renderer);
    }

    /**
     * Test renderDecorations with player entity.
     */
    @Test
    public void testRenderDecorationsPlayer() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        // Create a player entity at position (5, 5)
        PlayerEntity playerEntity = new PlayerEntity(1, 5f, 5f);
        
        // Add player to the game's entity manager
        org.ronobot.engine.core.Game game = new org.ronobot.engine.core.Game();
        game.getEntityManager().addEntity(playerEntity);
        
        renderer.renderEntities(game);
        // Player texture should be loaded
        assertTrue(renderer.hasTexture("entity_player"));
    }

    /**
     * Test renderDecorations with enemy entity.
     */
    @Test
    public void testRenderDecorationsEnemy() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        // Create an enemy entity at position (10, 10)
        org.ronobot.engine.core.Game game = new org.ronobot.engine.core.Game();
        EnemyEntity enemyEntity = new EnemyEntity(2, 10f, 10f);
        game.getEntityManager().addEntity(enemyEntity);
        
        renderer.renderEntities(game);
        // Enemy texture should be loaded
        assertTrue(renderer.hasTexture("entity_enemy"));
    }

    /**
     * Test renderDecorations with projectile.
     */
    @Test
    public void testRenderDecorationsProjectile() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        // Create a projectile at position (100, 0) with size (1, 1)
        org.ronobot.engine.core.Game game = new org.ronobot.engine.core.Game();
        Size projectileSize = new Size(1, 1);
        Projectile projectile = new Projectile(new Position(100f, 0f), projectileSize, 5);
        
        // Add projectile to the game's entity manager
        game.getEntityManager().addEntity(projectile);
        
        renderer.renderEntities(game);
        // Projectile texture should be loaded
        assertTrue(renderer.hasTexture("projectile_" + projectile.getId()));
    }

    /**
     * Test renderDecorations with null entities.
     */
    @Test
    public void testRenderDecorationsNullEntities() {
        renderer = new GameRenderer();
        org.ronobot.engine.core.Game game = new org.ronobot.engine.core.Game();
        renderer.renderEntities(game);
        // Should not throw
        assertNotNull(renderer);
    }

    /**
     * Test clearTextures clears both texture maps.
     */
    @Test
    public void testClearTextures() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        renderer.renderMap();
        int countBefore = renderer.getTextureCount();
        assertTrue(countBefore > 0);
        
        renderer.clearTextures();
        assertEquals(0, renderer.getTextureCount());
    }

    /**
     * Test hasTexture checks renderer textures.
     */
    @Test
    public void testHasTexture() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        renderer.renderMap();
        
        // Should have textures
        assertTrue(renderer.hasTexture("tile_1"));
        assertTrue(renderer.hasTexture("tile_0"));
    }

    /**
     * Test hasMap returns true when map is set.
     */
    @Test
    public void testHasMapTrue() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        assertTrue(renderer.hasMap());
    }

    /**
     * Test hasMap returns true by default since a default arena map is always created.
     * The renderer always initializes with a map (either provided or default).
     */
    @Test
    public void testHasMapNull() {
        renderer = new GameRenderer(null);
        
        // Default arena map is always created
        assertTrue(renderer.hasMap());
    }

    /**
     * Test getTextureCount returns combined texture count.
     */
    @Test
    public void testGetTextureCount() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        renderer.renderMap();
        int count = renderer.getTextureCount();
        // Should have at least tile textures
        assertTrue(count >= 2);
    }

    /**
     * Test toString returns expected format.
     */
    @Test
    public void testToString() {
        GameMap map = GameMap.createArenaMap(16, 16);
        renderer = new GameRenderer(map);
        
        renderer.renderMap();
        
        String result = renderer.toString();
        assertNotNull(result);
        assertTrue(result.contains("gameMap"));
        assertTrue(result.contains("textures"));
    }

    /**
     * Test renderMap with small map.
     */
    @Test
    public void testRenderMapSmall() {
        GameMap map = GameMap.createArenaMap(4, 4);
        renderer = new GameRenderer(map);
        
        renderer.renderMap();
        // Should work with small maps
        assertTrue(renderer.hasMap());
    }

    /**
     * Test renderMap with large map.
     */
    @Test
    public void testRenderMapLarge() {
        GameMap map = GameMap.createArenaMap(64, 64);
        renderer = new GameRenderer(map);
        
        renderer.renderMap();
        // Should work with large maps
        assertTrue(renderer.hasMap());
    }

    /**
     * Test renderDecorations with multiple decorations.
     */
    @Test
    public void testRenderDecorationsMultiple() {
        List<int[]> rooms = new ArrayList<>();
        rooms.add(new int[]{5, 5, 6, 6});
        
        GameMap map = GameMap.createRoomMap(32, 32, rooms);
        map.addDecoration(10, 10, "STATUE");
        map.addDecoration(12, 12, "PICTURE");
        map.addDecoration(15, 15, "TABLE");
        
        renderer = new GameRenderer(map);
        renderer.renderMap();
        renderer.renderDecorations();
        
        // Decorations should have been loaded with enum name keys
        assertTrue(renderer.hasTexture("decoration_STATUE"));
        assertTrue(renderer.hasTexture("decoration_PICTURE"));
        assertTrue(renderer.hasTexture("decoration_TABLE"));
    }
}
