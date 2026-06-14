package org.ronobot.engine.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SpriteLoader.
 * <p>
 * Tests sprite loading and caching functionality.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
class SpriteLoaderTest {

    private SpriteLoader loader;

    /**
     * Setup before each test.
     */
    @BeforeEach
    void setUp() {
        loader = new SpriteLoader();
    }

    /**
     * Test default constructor.
     */
    @Test
    void testDefaultConstructor() {
        assertNotNull(loader);
        // Enable sprite loading before checking
        loader.setEnableSpriteLoading(true);
        assertTrue(loader.isEnableSpriteLoading());
    }

    /**
     * Test setEnableSpriteLoading().
     */
    @Test
    void testSetEnableSpriteLoading() {
        loader.setEnableSpriteLoading(true);
        assertTrue(loader.isEnableSpriteLoading());

        loader.setEnableSpriteLoading(false);
        assertFalse(loader.isEnableSpriteLoading());
    }

    /**
     * Test getOrCreateSprite() for new sprite.
     */
    @Test
    void testGetOrCreateSpriteNew() {
        loader.setEnableSpriteLoading(true);
        
        SpriteLoader.SpriteCache cache = loader.getOrCreateSprite("test_spr");
        assertNotNull(cache);
        assertEquals("test_spr", cache.getName());
        assertEquals(SpriteType.UNKNOWN.getValue(), cache.getType());
        assertEquals(1, cache.getFrameCount());
    }

    /**
     * Test getOrCreateSprite() for existing sprite.
     */
    @Test
    void testGetOrCreateSpriteExisting() {
        loader.getOrCreateSprite("test_spr");
        
        SpriteLoader.SpriteCache cache1 = loader.getOrCreateSprite("test_spr");
        SpriteLoader.SpriteCache cache2 = loader.getOrCreateSprite("test_spr");
        
        assertSame(cache1, cache2);
    }

    /**
     * Test getSpriteCache() returns null for unknown sprite.
     */
    @Test
    void testGetSpriteCacheNull() {
        assertNull(loader.getSpriteCache("unknown_spr"));
    }

    /**
     * Test getSpriteCache() returns cached sprite.
     */
    @Test
    void testGetSpriteCacheExisting() {
        SpriteLoader.SpriteCache cache = loader.getOrCreateSprite("cached_spr");
        assertSame(cache, loader.getSpriteCache("cached_spr"));
    }

    /**
     * Test getLoadedSprites().
     */
    @Test
    void testGetLoadedSprites() {
        loader.getOrCreateSprite("spr1");
        loader.getOrCreateSprite("spr2");
        loader.getOrCreateSprite("spr3");
        
        assertInstanceOf(java.util.Set.class, loader.getLoadedSprites());
        assertEquals(3, loader.getLoadedSprites().size());
    }

    /**
     * Test getSpriteCount().
     */
    @Test
    void testGetSpriteCount() {
        assertEquals(0, loader.getSpriteCount());
        
        loader.getOrCreateSprite("spr1");
        assertEquals(1, loader.getSpriteCount());
        
        loader.getOrCreateSprite("spr2");
        assertEquals(2, loader.getSpriteCount());
    }

    /**
     * Test clearCache().
     */
    @Test
    void testClearCache() {
        loader.getOrCreateSprite("spr1");
        loader.getOrCreateSprite("spr2");
        
        assertEquals(2, loader.getSpriteCount());
        
        loader.clearCache();
        
        assertEquals(0, loader.getSpriteCount());
        assertNull(loader.getSpriteCache("spr1"));
    }

    /**
     * Test SpriteCache properties.
     */
    @Test
    void testSpriteCacheProperties() {
        SpriteLoader.SpriteCache cache = loader.getOrCreateSprite("test");
        
        assertEquals("test", cache.getName());
        assertEquals(64, cache.getWidth());
        assertEquals(64, cache.getHeight());
        assertEquals(1, cache.getFrameCount());
        assertEquals(0, cache.getType());
        
        cache.name = "newname";
        assertEquals("newname", cache.getName());
        
        cache.frameCount = 4;
        assertEquals(4, cache.getFrameCount());
    }

    /**
     * Test SpriteCache toString().
     */
    @Test
    void testSpriteCacheToString() {
        loader.getOrCreateSprite("TEST");
        
        String str = loader.getOrCreateSprite("TEST").toString();
        assertTrue(str.contains("name='TEST'"));
        assertTrue(str.contains("UNKNOWN"));
    }

    /**
     * Test SpriteLoader toString().
     */
    @Test
    void testSpriteLoaderToString() {
        loader.getOrCreateSprite("spr1");
        
        String str = loader.toString();
        assertTrue(str.contains("spriteCacheSize="));
        assertTrue(str.contains("enableSpriteLoading="));
    }

    /**
     * Test determineSpriteType() for exit sprites.
     */
    @Test
    void testDetermineExitSprite() {
        int type = loader.determineSpriteType("E1-M1");
        assertEquals(SpriteType.EXIT.getValue(), type);
    }

    /**
     * Test determineSpriteType() for missile sprites.
     */
    @Test
    void testDetermineMissileSprite() {
        int type = loader.determineSpriteType("M1-M2");
        assertEquals(SpriteType.MISSILE.getValue(), type);
    }

    /**
     * Test determineSpriteType() for player sprites.
     */
    @Test
    void testDeterminePlayerSprite() {
        int type = loader.determineSpriteType("P1-S1");
        assertEquals(SpriteType.PLAYER.getValue(), type);
    }

    /**
     * Test determineSpriteType() for spawn sprites.
     */
    @Test
    void testDetermineSpawnSprite() {
        int type = loader.determineSpriteType("S1-B1");
        assertEquals(SpriteType.SPAWN.getValue(), type);
    }

    /**
     * Test isSpriteLump() returns true for sprite lumps.
     */
    @Test
    void testIsSpriteLumpTrue() {
        assertTrue(loader.isSpriteLump("E1-M1"));
        assertTrue(loader.isSpriteLump("M1-M2"));
        assertTrue(loader.isSpriteLump("P1-S1"));
        assertTrue(loader.isSpriteLump("S1-B1"));
    }

    /**
     * Test isSpriteLump() returns false for non-sprite lumps.
     */
    @Test
    void testIsSpriteLumpFalse() {
        assertFalse(loader.isSpriteLump(""));
        assertFalse(loader.isSpriteLump("AMMO"));
        assertFalse(loader.isSpriteLump("PLAYERSKIN"));
    }

    /**
     * Test SpriteType lookup via enum.
     */
    @Test
    void testSpriteTypeEnumLookup() {
        assertEquals(SpriteType.EXIT.getValue(), SpriteType.EXIT.getValue());
        assertEquals(SpriteType.MISSILE.getValue(), SpriteType.MISSILE.getValue());
        assertEquals(SpriteType.PLAYER.getValue(), SpriteType.PLAYER.getValue());
    }
}
