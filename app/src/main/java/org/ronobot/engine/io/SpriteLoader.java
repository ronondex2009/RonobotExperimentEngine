package org.ronobot.engine.io;

import org.ronobot.engine.math.Size;
import org.ronobot.engine.math.Point;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

/**
 * Sprite loader for Doom-style sprite data.
 * <p>
 * This class handles loading sprite data from WAD lumps, including
 * frame data, vertex definitions, and sprite bounding information.
 * Sprites are cached by their lump name for efficient access.
 * Supports both compressed and uncompressed WAD files.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class SpriteLoader {

    /**
     * Whether sprite loading is enabled.
     */
    private boolean enableSpriteLoading = true;

    /**
     * WAD header for sprite lookup.
     */
    private WadFile wadFile;

    /**
     * Sprite cache map.
     */
    private final Map<String, SpriteCache> spriteCache = new ConcurrentHashMap<>();

    /**
     * Standard sprite page width (320 pixels).
     */
    private static final int PAGE_WIDTH = 320;

    /**
     * Standard sprite page height (200 pixels).
     */
    private static final int PAGE_HEIGHT = 200;

    /**
     * Maximum number of sprite frames per lump.
     */
    private static final int MAX_FRAMES = 255;

    /**
     * Maximum vertices per frame.
     */
    private static final int MAX_VERTEX = 1000;

    /**
     * Default width for unknown sprites.
     */
    private static final int DEFAULT_WIDTH = 64;

    /**
     * Default height for unknown sprites.
     */
    private static final int DEFAULT_HEIGHT = 64;

    /**
     * Minimum valid file size (magic + header + 1 entry).
     */
    private static final int MIN_VALID_SIZE = 40;

    /**
     * Default constructor.
     */
    public SpriteLoader() {
    }

    /**
     * Sets whether sprite loading is enabled.
     *
     * @param enabled true if sprite loading is enabled
     */
    public void setEnableSpriteLoading(boolean enabled) {
        this.enableSpriteLoading = enabled;
    }

    /**
     * Gets whether sprite loading is enabled.
     *
     * @return true if sprite loading is enabled
     */
    public boolean isEnableSpriteLoading() {
        return enableSpriteLoading;
    }

    /**
     * Sets the WAD file for sprite lookup.
     *
     * @param wadFile The WAD file
     */
    public void setWadFile(WadFile wadFile) {
        this.wadFile = wadFile;
    }

    /**
     * Gets the WAD file.
     *
     * @return The WAD file, or null if not set
     */
    public WadFile getWadFile() {
        return wadFile;
    }

    /**
     * Loads all sprite lumps from the WAD file.
     * <p>
     * Parses the WAD directory and loads each sprite lump into the cache.
     * Sprites are stored by lump name for quick lookup.
     * Handles both compressed (gz) and uncompressed WAD files.
     * </p>
     *
     * @param wadPath Path to the WAD file
     * @throws IOException If an error occurs while loading sprites
     */
    public void loadSprites(Path wadPath) throws IOException {
        if (wadFile == null) {
            wadFile = WadFile.load(wadPath.toAbsolutePath().toString());
        }

        if (wadFile == null || !wadFile.hasSprites()) {
            return;
        }

        DataInputStream dis;

        // Try uncompressed first, then gzip
        try {
            dis = new DataInputStream(new FileInputStream(wadPath.toFile()));
        } catch (IOException e) {
            // Try gzipped version
            dis = new DataInputStream(new GZIPInputStream(new FileInputStream(wadPath.toFile())));
        }

        try {
            int numEntries = wadFile.getLumpCount();

            // Iterate through lumps
            for (int i = 0; i < numEntries; i++) {
                if (enableSpriteLoading) {
                    WadFile.Lump lump = getLumpAt(wadFile, i);
                    if (lump != null) {
                        String lumpName = lump.name;

                        // Only load sprite lumps
                        if (!isSpriteLump(lumpName)) {
                            continue;
                        }

                        // Create sprite cache entry
                        SpriteCache sprite = new SpriteCache();
                        sprite.name = lumpName;
                        sprite.width = DEFAULT_WIDTH;
                        sprite.height = DEFAULT_HEIGHT;
                        sprite.frameCount = 0;
                        sprite.offset = lump.offset;
                        sprite.length = lump.size;

                        // Determine sprite type
                        sprite.type = determineSpriteType(lumpName);

                        spriteCache.put(sprite.name, sprite);

                    }
                }
            }

        } finally {
            if (dis != null) {
                dis.close();
            }
        }
    }

    /**
     * Loads a single sprite lump by name.
     *
     * @param lumpName The name of the lump to load
     * @param wadPath  Path to the WAD file
     * @return The loaded sprite cache, or null if not found
     * @throws IOException If an error occurs while loading the WAD file
     */
    public SpriteCache loadSprite(String lumpName, Path wadPath) throws IOException {
        if (wadFile == null) {
            wadFile = WadFile.load(wadPath.toAbsolutePath().toString());
        }

        if (wadFile == null) {
            return null;
        }

        // Check if we have this sprite already
        SpriteCache cache = spriteCache.get(lumpName);
        if (cache != null) {
            return cache;
        }

        // Find the lump in the WAD file
        for (WadFile.Lump lump : wadFile.getLumps()) {
            if (lump.name.equals(lumpName)) {
                SpriteCache sprite = new SpriteCache();
                sprite.name = lump.name;
                sprite.width = DEFAULT_WIDTH;
                sprite.height = DEFAULT_HEIGHT;
                sprite.offset = lump.offset;
                sprite.length = lump.size;
                sprite.type = determineSpriteType(lumpName);
                spriteCache.put(lumpName, sprite);
                return sprite;
            }
        }

        return null;
    }

    /**
     * Checks if a lump name indicates a sprite lump.
     *
     * @param name The lump name
     * @return true if this is a sprite lump
     */
    public boolean isSpriteLump(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        // Common sprite lump patterns
        if (name.length() < 2 || name.length() > 8) {
            return false;
        }

        // E* = Exits, M* = Missiles, P* = Players, S* = Spawns
        char type = name.charAt(0);
        char subtype = name.length() >= 2 ? name.charAt(1) : '\0';

        if (type == 'E' && (subtype >= '1' && subtype <= '9')) {
            return true;
        }
        if (type == 'M' && (subtype >= '1' && subtype <= '9')) {
            return true;
        }
        if (type == 'P' && (subtype >= '1' && subtype <= '9')) {
            return true;
        }
        if (type == 'S' && (subtype >= '1' && subtype <= '2')) {
            return true;
        }

        // SFX2 are also sprites (in some WADs)
        if (name.equals("SFX2")) {
            return true;
        }

        // Check for spr suffix
        if (name.length() >= 3 && name.substring(name.length() - 3).equals("spr")) {
            return true;
        }

        return false;
    }

    /**
     * Determines the sprite type from the lump name.
     *
     * @param name The lump name
     * @return The sprite type constant
     */
    public int determineSpriteType(String name) {
        if (name == null) {
            return SpriteType.UNKNOWN.getValue();
        }

        if (name.startsWith("E")) {
            return SpriteType.EXIT.getValue();
        }
        if (name.startsWith("M")) {
            return SpriteType.MISSILE.getValue();
        }
        if (name.startsWith("P")) {
            return SpriteType.PLAYER.getValue();
        }
        if (name.startsWith("S")) {
            return SpriteType.SPAWN.getValue();
        }
        return SpriteType.UNKNOWN.getValue();
    }

    /**
     * Gets a sprite cache entry by name.
     *
     * @param name The sprite name
     * @return The sprite cache, or null if not found
     */
    public SpriteCache getSpriteCache(String name) {
        if (name == null) {
            return null;
        }

        return spriteCache.get(name);
    }

    /**
     * Gets a sprite cache entry by name, creating a default entry if not found.
     *
     * @param name The sprite name
     * @return The sprite cache
     */
    public SpriteCache getOrCreateSprite(String name) {
        if (name == null) {
            return new SpriteCache();
        }

        SpriteCache cache = spriteCache.get(name);
        if (cache == null) {
            cache = new SpriteCache();
            cache.name = name;
            cache.width = DEFAULT_WIDTH;
            cache.height = DEFAULT_HEIGHT;
            cache.frameCount = 1;
            spriteCache.put(name, cache);
        }

        return cache;
    }

    /**
     * Gets all loaded sprite names.
     *
     * @return Set of sprite names
     */
    public Set<String> getLoadedSprites() {
        return new HashSet<>(spriteCache.keySet());
    }

    /**
     * Gets the number of loaded sprites.
     *
     * @return The number of loaded sprites
     */
    public int getSpriteCount() {
        return spriteCache.size();
    }

    /**
     * Clears the sprite cache.
     */
    public void clearCache() {
        spriteCache.clear();
        wadFile = null;
    }

    /**
     * Creates a string representation of the sprite loader.
     *
     * @return String representation of the sprite loader
     */
    @Override
    public String toString() {
        return "SpriteLoader{" +
                "spriteCacheSize=" + spriteCache.size() +
                ", enableSpriteLoading=" + enableSpriteLoading +
                ", wadFile=" + (wadFile != null ? wadFile.getName() : "null") +
                '}';
    }

    /**
     * Represents a lump in the WAD file iteration.
     */
    public static class Lump {
        public String name;
        public int offset;
        public int compression;
        public int size;
        public int crc;
    }

    /**
     * Gets the lump at the given index.
     *
     * @param wadFile The WAD file
     * @param index The index
     * @return The lump, or null if not found
     */
    public static WadFile.Lump getLumpAt(WadFile wadFile, int index) {
        if (wadFile == null || index < 0 || index >= wadFile.getLumpCount()) {
            return null;
        }
        return wadFile.getLumps().get(index);
    }

    /**
     * Sprite cache entry structure.
     */
    public static class SpriteCache {
        /**
         * Sprite name.
         */
        public String name;

        /**
         * Sprite width in pixels.
         */
        public int width;

        /**
         * Sprite height in pixels.
         */
        public int height;

        /**
         * Number of frames in the sprite.
         */
        public int frameCount;

        /**
         * File offset of the sprite data.
         */
        public int offset;

        /**
         * Size of the sprite data in bytes.
         */
        public int length;

        /**
         * Sprite type.
         */
        public int type;

        /**
         * Gets the sprite name.
         *
         * @return The sprite name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the sprite width.
         *
         * @return The sprite width
         */
        public int getWidth() {
            return width;
        }

        /**
         * Gets the sprite height.
         *
         * @return The sprite height
         */
        public int getHeight() {
            return height;
        }

        /**
         * Gets the number of frames.
         *
         * @return The number of frames
         */
        public int getFrameCount() {
            return frameCount;
        }

        /**
         * Gets the sprite type.
         *
         * @return The sprite type
         */
        public int getType() {
            return type;
        }

        /**
         * Creates a string representation of the sprite cache.
         *
         * @return String representation of the sprite cache
         */
        @Override
        public String toString() {
            return "SpriteCache{" +
                    "name='" + name + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    ", frameCount=" + frameCount +
                    ", offset=" + offset +
                    ", length=" + length +
                    ", type=" + SpriteType.getName(type) +
                    '}';
        }
    }
}
