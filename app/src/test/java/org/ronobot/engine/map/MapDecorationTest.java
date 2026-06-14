package org.ronobot.engine.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MapDecoration class.
 *
 * @author ronobot
 * @since 1.0
 */
class MapDecorationTest {

    /**
     * Test constructor with default priority.
     */
    @Test
    void testDefaultConstructor() {
        MapDecoration dec = new MapDecoration(5, 10, MapDecoration.TYPE_WALL, "test_wall", '#');
        assertEquals(5, dec.getRow());
        assertEquals(10, dec.getCol());
        assertEquals(MapDecoration.TYPE_WALL, dec.getType());
        assertEquals("test_wall", dec.getName());
        assertEquals('#', dec.getVisualChar());
        assertEquals(0, dec.getPriority());
        assertTrue(dec.isValid());
    }

    /**
     * Test constructor with priority.
     */
    @Test
    void testConstructorWithPriority() {
        MapDecoration dec = new MapDecoration(5, 10, MapDecoration.TYPE_WALL, "test_wall", '#', 2);
        assertEquals(2, dec.getPriority());
    }

    /**
     * Test decoration with null type.
     */
    @Test
    void testNullType() {
        MapDecoration dec = new MapDecoration(5, 10, null, "test", '#');
        assertFalse(dec.isValid());
    }

    /**
     * Test decoration with null name.
     */
    @Test
    void testNullName() {
        MapDecoration dec = new MapDecoration(5, 10, MapDecoration.TYPE_WALL, null, '#');
        assertFalse(dec.isValid());
    }

    /**
     * Test toString method.
     */
    @Test
    void testToString() {
        MapDecoration dec = new MapDecoration(5, 10, MapDecoration.TYPE_WALL, "test_wall", '#', 1);
        String str = dec.toString();
        assertTrue(str.contains("type='wall'"));
        assertTrue(str.contains("name='test_wall'"));
        assertTrue(str.contains("row=5"));
        assertTrue(str.contains("col=10"));
        assertTrue(str.contains("priority=1"));
    }

    /**
     * Test all decorator types are constants.
     */
    @Test
    void testConstants() {
        assertNotNull(MapDecoration.TYPE_WALL);
        assertNotNull(MapDecoration.TYPE_FLOOR);
        assertNotNull(MapDecoration.TYPE_ENVIRONMENTAL);
        assertNotNull(MapDecoration.TYPE_LIGHTING);
    }

    /**
     * Test getter methods.
     */
    @Test
    void testGetters() {
        MapDecoration dec = new MapDecoration(100, 200, "lighting", "torch", '@', 5);
        assertEquals("lighting", dec.getType());
        assertEquals(100, dec.getRow());
        assertEquals(200, dec.getCol());
        assertEquals("torch", dec.getName());
        assertEquals('@', dec.getVisualChar());
        assertEquals(5, dec.getPriority());
    }
}
