package org.ronobot.engine.render;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.map.GameMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Renderer class.
 * <p>
 * Tests verify texture caching, HUD element management, and rendering pipeline.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
class RendererTest {

    private Renderer renderer;
    private Game game;

    @BeforeEach
    void setUp() {
        renderer = new Renderer();
        game = new Game();
    }

    @Test
    @DisplayName("Renderer creates with default textures and HUD")
    void testCreate() {
        assertNotNull(renderer);
        assertTrue(renderer.getTextureCount() >= 0);
        assertEquals(6, renderer.getHUDElements().size());
    }

    @Test
    @DisplayName("Renderer screen dimensions are correct")
    void testScreenDimensions() {
        assertEquals(640, renderer.getScreenWidth());
        assertEquals(480, renderer.getScreenHeight());
    }

    @Test
    @DisplayName("Renderer loadTexture adds to cache")
    void testLoadTexture() {
        assertTrue(renderer.loadTexture("test", "path/to/test.png"));
        assertEquals(1, renderer.getTextureCount());
        assertEquals("path/to/test.png", renderer.getTexture("test"));
    }

    @Test
    @DisplayName("Renderer loadTexture returns false for null input")
    void testLoadTextureNull() {
        assertFalse(renderer.loadTexture(null, "path"));
        assertFalse(renderer.loadTexture("name", null));
    }

    @Test
    @DisplayName("Renderer loadTexture by tile type works")
    void testLoadTextureByType() {
        assertTrue(renderer.loadTexture(GameMap.TILE_WALL));
        assertTrue(renderer.loadTexture(GameMap.TILE_FLOOR));
        assertTrue(renderer.getTextureCount() >= 2);
    }

    @Test
    @DisplayName("Renderer removeTexture works")
    void testRemoveTexture() {
        renderer.loadTexture("test", "path");
        assertTrue(renderer.removeTexture("test"));
        assertEquals(0, renderer.getTextureCount());
        assertNull(renderer.getTexture("test"));
    }

    @Test
    @DisplayName("Renderer removeTexture returns false for missing texture")
    void testRemoveTextureMissing() {
        assertFalse(renderer.removeTexture("nonexistent"));
    }

    @Test
    @DisplayName("Renderer hasTexture checks existence")
    void testHasTexture() {
        renderer.loadTexture("test", "path");
        assertTrue(renderer.hasTexture("test"));
        assertFalse(renderer.hasTexture("nonexistent"));
    }

    @Test
    @DisplayName("Renderer clearTextures empties cache")
    void testClearTextures() {
        renderer.loadTexture("test1", "path1");
        renderer.loadTexture("test2", "path2");
        renderer.clearTextures();
        assertEquals(0, renderer.getTextureCount());
    }

    @Test
    @DisplayName("Renderer getHUDElements returns initialized list")
    void testGetHUDElements() {
        assertNotNull(renderer.getHUDElements());
        assertTrue(renderer.getHUDElements().size() > 0);
    }

    @Test
    @DisplayName("Renderer getHUDContainer returns initialized map")
    void testGetHUDContainer() {
        assertNotNull(renderer.getHUDContainer());
        assertTrue(renderer.getHUDContainer().size() > 0);
    }

    @Test
    @DisplayName("Renderer addHUDElement adds to collection")
    void testAddHUDElement() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        renderer.addHUDElement(element);
        assertTrue(renderer.getHUDElements().contains(element));
        assertEquals(7, renderer.getHUDElements().size());
    }

    @Test
    @DisplayName("Renderer removeHUDElement removes by type")
    void testRemoveHUDElement() {
        renderer.clearHUD();
        renderer.addHUDElement(new HUDElement(HUDElement.Type.AMMO_DISPLAY));
        boolean removed = renderer.removeHUDElement(HUDElement.Type.AMMO_DISPLAY);
        assertTrue(removed);
        assertEquals(6, renderer.getHUDElements().size());
    }

    @Test
    @DisplayName("Renderer removeHUDElement returns false for missing type")
    void testRemoveHUDElementMissing() {
        renderer.clearHUD();
        boolean removed = renderer.removeHUDElement(HUDElement.Type.HEALTH_BAR);
        assertFalse(removed);
    }

    @Test
    @DisplayName("Renderer getHUDElement retrieves by type")
    void testGetHUDElement() {
        HUDElement element = new HUDElement(HUDElement.Type.HEALTH_BAR);
        renderer.addHUDElement(element);
        HUDElement retrieved = renderer.getHUDElement(HUDElement.Type.HEALTH_BAR);
        assertNotNull(retrieved);
        assertEquals(element, retrieved);
    }

    @Test
    @DisplayName("Renderer getHUDElement returns null for missing type")
    void testGetHUDElementMissing() {
        renderer.clearHUD();
        HUDElement element = renderer.getHUDElement(HUDElement.Type.AMMO_DISPLAY);
        assertNull(element);
    }

    @Test
    @DisplayName("Renderer clearHUD resets to defaults")
    void testClearHUD() {
        renderer.addHUDElement(new HUDElement(HUDElement.Type.MENUS));
        renderer.clearHUD();
        assertEquals(6, renderer.getHUDElements().size()); // 6 default elements
    }

    @Test
    @DisplayName("Renderer render with null game handles gracefully")
    void testRenderNullGame() {
        // Should not throw exception
        renderer.render(null);
    }

    @Test
    @DisplayName("Renderer render with unloaded map handles gracefully")
    void testRenderUnloadedMap() {
        GameMap unloadedMap = new GameMap(10, 10);
        game.setMap(unloadedMap);
        renderer.render(game);
        // Should not throw exception
    }

    @Test
    @DisplayName("Renderer render with null map handles gracefully")
    void testRenderNullMap() {
        GameMap nullMap = null;
        game.setMap(nullMap);
        renderer.render(game);
        // Should not throw exception
    }

    @Test
    @DisplayName("Renderer HUDElement healthBar creates correct element")
    void testHealthBar() {
        HUDElement healthBar = HUDElement.healthBar(4, 4);
        assertEquals(HUDElement.Type.HEALTH_BAR, healthBar.getType());
        assertEquals(4, healthBar.getX());
        assertEquals(4, healthBar.getY());
        assertEquals(200, healthBar.getWidth());
        assertEquals(16, healthBar.getHeight());
    }

    @Test
    @DisplayName("Renderer HUDElement ammoDisplay creates correct element")
    void testAmmoDisplay() {
        HUDElement ammo = HUDElement.ammoDisplay(4, 24);
        assertEquals(HUDElement.Type.AMMO_DISPLAY, ammo.getType());
        assertEquals(4, ammo.getX());
        assertEquals(24, ammo.getY());
        assertEquals(120, ammo.getWidth());
        assertEquals(20, ammo.getHeight());
    }

    @Test
    @DisplayName("Renderer HUDElement scoreDisplay creates correct element")
    void testScoreDisplay() {
        HUDElement score = HUDElement.scoreDisplay(500, 24, 120, 20);
        assertEquals(HUDElement.Type.SCORE_DISPLAY, score.getType());
        assertEquals(500, score.getX());
        assertEquals(24, score.getY());
        assertEquals(120, score.getWidth());
        assertEquals(20, score.getHeight());
    }

    @Test
    @DisplayName("Renderer HUDElement levelIndicator creates correct element")
    void testLevelIndicator() {
        HUDElement level = HUDElement.levelIndicator(540, 24, 100, 20);
        assertEquals(HUDElement.Type.LEVEL_INDICATOR, level.getType());
        assertEquals(540, level.getX());
        assertEquals(24, level.getY());
        assertEquals(100, level.getWidth());
        assertEquals(20, level.getHeight());
    }

    @Test
    @DisplayName("Renderer HUDElement debugInfo creates correct element")
    void testDebugInfo() {
        HUDElement debug = HUDElement.debugInfo(400, 432, 240, 48);
        assertEquals(HUDElement.Type.DEBUG_INFO, debug.getType());
        assertEquals(400, debug.getX());
        assertEquals(432, debug.getY());
        assertEquals(240, debug.getWidth());
        assertEquals(48, debug.getHeight());
    }

    @Test
    @DisplayName("Renderer HUDElement menu creates correct element")
    void testMenu() {
        HUDElement menu = HUDElement.menu(320, 240, 400, 300);
        assertEquals(HUDElement.Type.MENUS, menu.getType());
        assertEquals(320, menu.getX());
        assertEquals(240, menu.getY());
        assertEquals(400, menu.getWidth());
        assertEquals(300, menu.getHeight());
    }

    @Test
    @DisplayName("Renderer HUDElement messageBox creates correct element")
    void testMessageBox() {
        HUDElement message = HUDElement.messageBox(40, 240, 600, 200);
        assertEquals(HUDElement.Type.MESSAGE_BOX, message.getType());
        assertEquals(40, message.getX());
        assertEquals(240, message.getY());
        assertEquals(600, message.getWidth());
        assertEquals(200, message.getHeight());
    }

    @Test
    @DisplayName("Renderer toString returns expected format")
    void testToString() {
        String toString = renderer.toString();
        assertTrue(toString.contains("textures="));
        assertTrue(toString.contains("hudElements="));
    }

    @Test
    @DisplayName("Renderer HUDElement toString returns expected format")
    void testHUDElementToString() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY, 100, 200, 50, 25);
        String toString = element.toString();
        assertTrue(toString.contains("type=AMMO_DISPLAY"));
        assertTrue(toString.contains("x=100"));
        assertTrue(toString.contains("y=200"));
    }

    @Test
    @DisplayName("Renderer HUDElement setVisible changes visibility")
    void testSetVisible() {
        HUDElement element = new HUDElement(HUDElement.Type.HEALTH_BAR);
        assertTrue(element.isVisible());
        element.setVisible(false);
        assertFalse(element.isVisible());
        element.setVisible(true);
        assertTrue(element.isVisible());
    }

    @Test
    @DisplayName("Renderer HUDElement getters/setters work correctly")
    void testGettersSetters() {
        HUDElement element = new HUDElement(HUDElement.Type.HEALTH_BAR);
        
        // Test getters
        assertEquals(HUDElement.Type.HEALTH_BAR, element.getType());
        assertEquals(0, element.getX());
        assertEquals(0, element.getY());
        assertEquals(0, element.getWidth());
        assertEquals(0, element.getHeight());
        assertTrue(element.isVisible());
        
        // Test setters
        element.setX(50)
                .setY(100)
                .setWidth(100)
                .setHeight(20)
                .setVisible(false);
        
        assertEquals(50, element.getX());
        assertEquals(100, element.getY());
        assertEquals(100, element.getWidth());
        assertEquals(20, element.getHeight());
        assertFalse(element.isVisible());
    }

    @Test
    @DisplayName("Renderer HUDElement render callback returns true")
    void testRenderCallback() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        boolean result = element.render(game);
        assertTrue(result);
    }

    @Test
    @DisplayName("Renderer HUDElement render returns false when not visible")
    void testRenderNotVisible() {
        HUDElement element = HUDElement.healthBar(100, 100);
        element.setVisible(false);
        boolean result = element.render(game);
        assertFalse(result);
    }

    @Test
    @DisplayName("Renderer HUDElement healthBar sets correct callback")
    void testHealthBarCallback() {
        HUDElement healthBar = HUDElement.healthBar(100, 100);
        assertNotNull(healthBar.getRenderCallback());
    }

    @Test
    @DisplayName("Renderer test object equality for HUDElement")
    void testHUDElementEquality() {
        HUDElement element1 = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        HUDElement element2 = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        
        // Different instances, not equal
        assertFalse(element1.equals(element2));
        assertFalse(element1.hashCode() == element2.hashCode());
    }
}
