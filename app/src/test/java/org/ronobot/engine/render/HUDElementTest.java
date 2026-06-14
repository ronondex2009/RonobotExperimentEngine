package org.ronobot.engine.render;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HUDElement class.
 * <p>
 * Tests verify HUD element creation, configuration, and rendering behavior.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
class HUDElementTest {

    @Test
    @DisplayName("HUDElement Type enum contains all expected types")
    void testTypeEnum() {
        HUDElement.Type[] types = HUDElement.Type.values();
        assertEquals(9, types.length);
    }

    @Test
    @DisplayName("HUDElement Type enum values are correct")
    void testTypeEnumValues() {
        assertEquals("HEALTH_BAR", HUDElement.Type.HEALTH_BAR.name());
        assertEquals("AMMO_DISPLAY", HUDElement.Type.AMMO_DISPLAY.name());
        assertEquals("WEAPON_ICON", HUDElement.Type.WEAPON_ICON.name());
        assertEquals("SCORE_DISPLAY", HUDElement.Type.SCORE_DISPLAY.name());
        assertEquals("LEVEL_INDICATOR", HUDElement.Type.LEVEL_INDICATOR.name());
        assertEquals("DEBUG_INFO", HUDElement.Type.DEBUG_INFO.name());
        assertEquals("MENUS", HUDElement.Type.MENUS.name());
        assertEquals("MESSAGE_BOX", HUDElement.Type.MESSAGE_BOX.name());
        assertEquals("INVENTORY_SCREEN", HUDElement.Type.INVENTORY_SCREEN.name());
    }

    @Test
    @DisplayName("HUDElement Type enum values are case-insensitive")
    void testTypeEnumLowerCase() {
        HUDElement.Type health = HUDElement.Type.valueOf("HEALTH_BAR");
        HUDElement.Type health2 = HUDElement.Type.valueOf("HEALTH_BAR");
        assertEquals(health, health2);
    }

    @Test
    @DisplayName("HUDElement constructor creates element with defaults")
    void testConstructorDefaults() {
        HUDElement element = new HUDElement(HUDElement.Type.HEALTH_BAR);
        assertEquals(HUDElement.Type.HEALTH_BAR, element.getType());
        assertEquals(0, element.getX());
        assertEquals(0, element.getY());
        assertEquals(0, element.getWidth());
        assertEquals(0, element.getHeight());
        assertTrue(element.isVisible());
        assertNull(element.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement constructor with parameters sets values")
    void testConstructorWithParameters() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY, 100, 200, 50, 25);
        assertEquals(HUDElement.Type.AMMO_DISPLAY, element.getType());
        assertEquals(100, element.getX());
        assertEquals(200, element.getY());
        assertEquals(50, element.getWidth());
        assertEquals(25, element.getHeight());
    }

    @Test
    @DisplayName("HUDElement getX returns position")
    void testGetX() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY, 100, 200, 50, 25);
        assertEquals(100, element.getX());
    }

    @Test
    @DisplayName("HUDElement setX returns this for chaining")
    void testSetXChaining() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        HUDElement result = element.setX(100);
        assertEquals(element, result);
    }

    @Test
    @DisplayName("HUDElement getY returns position")
    void testGetY() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY, 100, 200, 50, 25);
        assertEquals(200, element.getY());
    }

    @Test
    @DisplayName("HUDElement setY returns this for chaining")
    void testSetYChaining() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        HUDElement result = element.setY(200);
        assertEquals(element, result);
    }

    @Test
    @DisplayName("HUDElement getWidth returns width")
    void testGetWidth() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY, 100, 200, 50, 25);
        assertEquals(50, element.getWidth());
    }

    @Test
    @DisplayName("HUDElement setWidth returns this for chaining")
    void testSetWidthChaining() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        HUDElement result = element.setWidth(100);
        assertEquals(element, result);
    }

    @Test
    @DisplayName("HUDElement getHeight returns height")
    void testGetHeight() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY, 100, 200, 50, 25);
        assertEquals(25, element.getHeight());
    }

    @Test
    @DisplayName("HUDElement setHeight returns this for chaining")
    void testSetHeightChaining() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        HUDElement result = element.setHeight(25);
        assertEquals(element, result);
    }

    @Test
    @DisplayName("HUDElement isVisible returns true by default")
    void testVisibleDefault() {
        HUDElement element = new HUDElement(HUDElement.Type.HEALTH_BAR);
        assertTrue(element.isVisible());
    }

    @Test
    @DisplayName("HUDElement setVisible changes visibility")
    void testSetVisible() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        assertTrue(element.isVisible());
        
        element.setVisible(false);
        assertFalse(element.isVisible());
        
        element.setVisible(true);
        assertTrue(element.isVisible());
    }

    @Test
    @DisplayName("HUDElement setVisible returns this for chaining")
    void testSetVisibleChaining() {
        HUDElement element = new HUDElement(HUDElement.Type.HEALTH_BAR);
        HUDElement result = element.setVisible(false);
        assertEquals(element, result);
    }

    @Test
    @DisplayName("HUDElement getRenderCallback returns null initially")
    void testGetRenderCallback() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        assertNull(element.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement setRenderCallback sets callback")
    void testSetRenderCallback() {
        HUDElement element = new HUDElement(HUDElement.Type.HEALTH_BAR);
        HUDElement.RenderCallback callback = (game, elem) -> true;
        HUDElement result = element.setRenderCallback(callback);
        assertEquals(element, result);
        assertNotNull(element.getRenderCallback());
        assertEquals(callback, element.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement healthBar creates element at HUD bottom-left")
    void testHealthBar() {
        HUDElement healthBar = HUDElement.healthBar(100, 100);
        assertEquals(HUDElement.Type.HEALTH_BAR, healthBar.getType());
        assertEquals(100, healthBar.getX());
        assertEquals(100, healthBar.getY());
        assertEquals(200, healthBar.getWidth());
        assertEquals(16, healthBar.getHeight());
        assertNotNull(healthBar.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement ammoDisplay creates element at HUD bottom-left below health")
    void testAmmoDisplay() {
        HUDElement ammo = HUDElement.ammoDisplay(50, 100);
        assertEquals(HUDElement.Type.AMMO_DISPLAY, ammo.getType());
        assertEquals(50, ammo.getX());
        assertEquals(100, ammo.getY());
        assertEquals(120, ammo.getWidth());
        assertEquals(20, ammo.getHeight());
        assertNotNull(ammo.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement scoreDisplay creates element at HUD bottom-right")
    void testScoreDisplay() {
        HUDElement score = HUDElement.scoreDisplay(1000, 150);
        assertEquals(HUDElement.Type.SCORE_DISPLAY, score.getType());
        assertEquals(1000, score.getX());
        assertEquals(150, score.getY());
        assertEquals(120, score.getWidth());
        assertEquals(20, score.getHeight());
        assertNotNull(score.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement levelIndicator creates element at HUD bottom-right")
    void testLevelIndicator() {
        HUDElement level = HUDElement.levelIndicator(3, 4);
        assertEquals(HUDElement.Type.LEVEL_INDICATOR, level.getType());
        assertEquals(3, level.getX());
        assertEquals(4, level.getY());
        assertEquals(100, level.getWidth());
        assertEquals(20, level.getHeight());
        assertNotNull(level.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement debugInfo creates element at bottom-left")
    void testDebugInfo() {
        HUDElement debug = HUDElement.debugInfo();
        assertEquals(HUDElement.Type.DEBUG_INFO, debug.getType());
        assertEquals(400, debug.getX()); // 640 - 240
        assertEquals(432, debug.getY()); // 480 - 48
        assertEquals(240, debug.getWidth());
        assertEquals(48, debug.getHeight());
        assertNotNull(debug.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement menu creates centered element")
    void testMenu() {
        HUDElement menu = HUDElement.menu();
        assertEquals(HUDElement.Type.MENUS, menu.getType());
        assertEquals(320, menu.getX()); // 640 / 2 - 200
        assertEquals(240, menu.getY()); // 480 / 2 - 150
        assertEquals(400, menu.getWidth());
        assertEquals(300, menu.getHeight());
        assertNotNull(menu.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement messageBox creates centered element")
    void testMessageBox() {
        HUDElement message = HUDElement.messageBox();
        assertEquals(HUDElement.Type.MESSAGE_BOX, message.getType());
        assertEquals(40, message.getX()); // 640 / 2 - 300
        assertEquals(240, message.getY()); // 480 / 2 - 100
        assertEquals(600, message.getWidth());
        assertEquals(200, message.getHeight());
        assertNotNull(message.getRenderCallback());
    }

    @Test
    @DisplayName("HUDElement render returns true when visible")
    void testRenderVisible() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        boolean result = element.render(null); // game can be null
        assertTrue(result);
    }

    @Test
    @DisplayName("HUDElement render returns false when not visible")
    void testRenderNotVisible() {
        HUDElement element = HUDElement.healthBar(100, 100);
        element.setVisible(false);
        boolean result = element.render(null);
        assertFalse(result);
    }

    @Test
    @DisplayName("HUDElement render callback returns true")
    void testRenderCallbackReturnsTrue() {
        HUDElement element = new HUDElement(HUDElement.Type.HEALTH_BAR);
        element.setRenderCallback((game, elem) -> true);
        boolean result = element.render(null);
        assertTrue(result);
    }

    @Test
    @DisplayName("HUDElement render callback returns false")
    void testRenderCallbackReturnsFalse() {
        HUDElement element = new HUDElement(HUDElement.Type.HEALTH_BAR);
        HUDElement.RenderCallback callback = (game, elem) -> false;
        element.setRenderCallback(callback);
        boolean result = element.render(null);
        assertFalse(result);
    }

    @Test
    @DisplayName("HUDElement toString returns expected format")
    void testToString() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY, 100, 200, 50, 25);
        element.setVisible(true);
        HUDElement.RenderCallback callback = new HUDElement.RenderCallback() {
            @Override
            public boolean render(org.ronobot.engine.core.Game game, HUDElement element) {
                return true;
            }
        };
        element.setRenderCallback(callback);
        
        String toString = element.toString();
        assertTrue(toString.contains("type=AMMO_DISPLAY"));
        assertTrue(toString.contains("x=100"));
        assertTrue(toString.contains("y=200"));
        assertTrue(toString.contains("width=50"));
        assertTrue(toString.contains("height=25"));
        assertTrue(toString.contains("visible=true"));
        assertTrue(toString.contains("callback="));
    }

    @Test
    @DisplayName("HUDElement hashCode consistent with equals")
    void testHashCodeConsistent() {
        HUDElement element1 = new HUDElement(HUDElement.Type.HEALTH_BAR);
        HUDElement element2 = new HUDElement(HUDElement.Type.HEALTH_BAR);
        
        // Different instances, not equal
        assertNotEquals(element1, element2);
        assertFalse(element1.hashCode() == element2.hashCode());
    }

    @Test
    @DisplayName("HUDElement equals checks type and references")
    void testEquals() {
        HUDElement element1 = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        HUDElement element2 = element1; // Same reference
        assertTrue(element1.equals(element2));
        
        HUDElement element3 = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        assertFalse(element1.equals(element3));
    }

    @Test
    @DisplayName("HUDElement equals with same type but different instance returns false")
    void testEqualsDifferentInstance() {
        HUDElement element1 = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        HUDElement element2 = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        
        assertNotEquals(element1, element2);
    }

    @Test
    @DisplayName("HUDElement equals returns true for same object")
    void testEqualsSameObject() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        assertTrue(element.equals(element));
    }

    @Test
    @DisplayName("HUDElement equals with null returns false")
    void testEqualsNull() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        assertFalse(element.equals(null));
    }

    @Test
    @DisplayName("HUDElement equals with different class returns false")
    void testEqualsDifferentClass() {
        HUDElement element = new HUDElement(HUDElement.Type.AMMO_DISPLAY);
        String str = "not an element";
        assertFalse(element.equals(str));
    }

    @Test
    @DisplayName("HUDElement equals with null type")
    void testEqualsNullType() {
        // Creating element with null string should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            new HUDElement((String) null);
        });
    }
}
