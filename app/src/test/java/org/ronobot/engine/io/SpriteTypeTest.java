package org.ronobot.engine.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SpriteType enumeration.
 * <p>
 * Tests sprite type enumeration and lookup functionality.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
class SpriteTypeTest {

    /**
     * Test all enum constants are defined correctly.
     */
    @Test
    void testEnumConstants() {
        assertNotNull(SpriteType.UNKNOWN);
        assertEquals(0, SpriteType.UNKNOWN.getValue());
        
        assertNotNull(SpriteType.EXIT);
        assertEquals(1, SpriteType.EXIT.getValue());
        
        assertNotNull(SpriteType.MISSILE);
        assertEquals(2, SpriteType.MISSILE.getValue());
        
        assertNotNull(SpriteType.PLAYER);
        assertEquals(3, SpriteType.PLAYER.getValue());
        
        assertNotNull(SpriteType.SPAWN);
        assertEquals(4, SpriteType.SPAWN.getValue());
        
        assertNotNull(SpriteType.STATUE);
        assertEquals(5, SpriteType.STATUE.getValue());
        
        assertNotNull(SpriteType.PICTURE);
        assertEquals(6, SpriteType.PICTURE.getValue());
        
        assertNotNull(SpriteType.TABLE);
        assertEquals(7, SpriteType.TABLE.getValue());
        
        assertNotNull(SpriteType.CHEST);
        assertEquals(8, SpriteType.CHEST.getValue());
        
        assertNotNull(SpriteType.CRATE);
        assertEquals(9, SpriteType.CRATE.getValue());
    }

    /**
     * Test getValue() method returns correct value.
     */
    @Test
    void testGetValue() {
        assertEquals(0, SpriteType.UNKNOWN.getValue());
        assertEquals(1, SpriteType.EXIT.getValue());
        assertEquals(9, SpriteType.CRATE.getValue());
    }

    /**
     * Test getName() returns correct name for valid types.
     */
    @Test
    void testGetNameValidTypes() {
        assertEquals("UNKNOWN", SpriteType.getName(0));
        assertEquals("EXIT", SpriteType.getName(1));
        assertEquals("MISSILE", SpriteType.getName(2));
        assertEquals("PLAYER", SpriteType.getName(3));
        assertEquals("SPAWN", SpriteType.getName(4));
        assertEquals("STATUE", SpriteType.getName(5));
        assertEquals("PICTURE", SpriteType.getName(6));
        assertEquals("TABLE", SpriteType.getName(7));
        assertEquals("CHEST", SpriteType.getName(8));
        assertEquals("CRATE", SpriteType.getName(9));
    }

    /**
     * Test getName() returns UNKNOWN for invalid types.
     */
    @Test
    void testGetNameInvalidTypes() {
        assertEquals("UNKNOWN", SpriteType.getName(-1));
        assertEquals("UNKNOWN", SpriteType.getName(10));
        assertEquals("UNKNOWN", SpriteType.getName(100));
    }

    /**
     * Test toString() returns readable representation.
     */
    @Test
    void testToString() {
        assertEquals("SpriteType.UNKNOWN = 0", SpriteType.UNKNOWN.toString());
        assertEquals("SpriteType.EXIT = 1", SpriteType.EXIT.toString());
        assertEquals("SpriteType.CRATE = 9", SpriteType.CRATE.toString());
    }
}
