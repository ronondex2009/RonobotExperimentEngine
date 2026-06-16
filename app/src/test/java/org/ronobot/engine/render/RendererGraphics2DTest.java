package org.ronobot.engine.render;

import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.math.Position;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.entity.PlayerEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RendererGraphics2D graphics conversion.
 * <p>
 * Validates that Graphics2D rendering works correctly for tiles,
 * entities, decorations, and basic HUD elements.
 * </p>
 * 
 * @author ronobot
 * @since 1.0
 */
public class RendererGraphics2DTest {

    /**
     * Creates a new RendererGraphics2D for testing.
     * 
     * @return A configured RendererGraphics2D instance
     */
    private RendererGraphics2D createRenderer() {
        return new RendererGraphics2D();
    }

    /**
     * Creates a test game with a simple map.
     * 
     * @return A configured Game instance
     */
    private Game createTestGame() {
        GameMap map = GameMap.createArenaMap(16, 16);
        map.addFloor(1, 1);
        Game game = new Game();
        game.setMap(map);
        return game;
    }

    /**
     * Test tile rendering with correct colors.
     */
    @Test
    public void testTileRendering() {
        RendererGraphics2D renderer = createRenderer();
        Color wallColor = renderer.getTileColor(GameMap.TILE_WALL);
        assertNotNull(wallColor);
        assertTrue(wallColor.getRed() >= 40 && wallColor.getRed() <= 50);
        assertTrue(wallColor.getGreen() >= 40 && wallColor.getGreen() <= 60);
        assertTrue(wallColor.getBlue() >= 40 && wallColor.getBlue() <= 60);
        
        Color floorColor = renderer.getTileColor(GameMap.TILE_FLOOR);
        assertNotNull(floorColor);
        assertTrue(floorColor.getRed() < 20);
        assertTrue(floorColor.getGreen() < 20);
        assertTrue(floorColor.getBlue() < 20);
    }

    /**
     * Test entity color retrieval.
     */
    @Test
    public void testEntityColors() {
        RendererGraphics2D renderer = createRenderer();
        assertNotNull(renderer.getEntityColor(PlayerEntity.class));
        
        // Custom entity color
        renderer.setEntityColor(Object.class, java.awt.Color.GREEN);
        assertEquals(java.awt.Color.GREEN.getRGB(), renderer.getEntityColor(Object.class).getRGB());
    }

    /**
     * Test decoration rendering.
     */
    @Test
    public void testDecorationRendering() {
        RendererGraphics2D renderer = createRenderer();
        Game game = createTestGame();
        GameMap map = game.getMap();
        
        // Add a decoration
        map.addDecoration(4, 4, "CHEST");
        assertNotNull(map.getDecorationType(4, 4));
    }

    /**
     * Test buffer dimensions.
     */
    @Test
    public void testBufferDimensions() {
        assertEquals(RendererGraphics2D.getBufferWidth(), 640, "Buffer width");
        assertEquals(RendererGraphics2D.getBufferHeight(), 480, "Buffer height");
        assertEquals(RendererGraphics2D.getTileWidth(), 32, "Tile width");
        assertEquals(RendererGraphics2D.getTileHeight(), 32, "Tile height");
    }

    /**
     * Test rendering to buffer image.
     */
    @Test
    public void testRenderToBuffer() {
        RendererGraphics2D renderer = createRenderer();
        Game game = createTestGame();
        
        // Create buffer image
        BufferedImage buffer = new BufferedImage(
                RendererGraphics2D.getBufferWidth(),
                RendererGraphics2D.getBufferHeight(),
                BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = buffer.createGraphics();
        
        // Render to buffer
        renderer.render(game, g2d);
        
        g2d.dispose();
        
        assertNotNull(buffer);
        assertNotNull(buffer.getWidth());
        assertEquals(RendererGraphics2D.getBufferWidth(), buffer.getWidth());
    }

    /**
     * Test HUD element rendering.
     */
    @Test
    public void testHUDRendering() {
        RendererGraphics2D renderer = createRenderer();
        Game game = createTestGame();
        
        // Renderer should not throw on empty HUD
        renderer.render(game, null);
        
        // Renderer should handle null graphics
        assertNotNull(renderer);
    }

    /**
     * Test tile color customization.
     */
    @Test
    public void testCustomTileColors() {
        RendererGraphics2D renderer = createRenderer();
        
        // Set custom floor color
        Color customColor = new java.awt.Color(50, 40, 30);
        renderer.setTileColor(GameMap.TILE_FLOOR, customColor);
        
        assertEquals(customColor.getRGB(), renderer.getTileColor(GameMap.TILE_FLOOR).getRGB());
    }

    /**
     * Test multiple decoration types.
     */
    @Test
    public void testMultipleDecorations() {
        RendererGraphics2D renderer = createRenderer();
        Game game = createTestGame();
        GameMap map = game.getMap();
        
        map.addDecoration(5, 5, "TABLE");
        map.addDecoration(6, 5, "PICTURE");
        map.addDecoration(7, 5, "FLAG");
        
        assertEquals(3, map.getDecorationPositions().size());
    }

    /**
     * Test toString representation.
     */
    @Test
    public void testToString() {
        RendererGraphics2D renderer = createRenderer();
        assertNotNull(renderer.toString());
        assertTrue(renderer.toString().contains("tileColors"));
        assertTrue(renderer.toString().contains("hudFont"));
    }
}
