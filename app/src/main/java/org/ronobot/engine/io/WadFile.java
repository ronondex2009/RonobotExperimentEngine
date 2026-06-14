package org.ronobot.engine.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * WAD file parser for DOOM-style WAD files.
 * <p>
 * This class handles reading DOOM WAD files, which contain lump data
 * for sprites, maps, sounds, and other game assets. WAD files use
 * a simple format with 8-byte headers and 16-byte directory entries.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class WadFile {

    /**
     * Magic number for DOOM WAD files.
     */
    public static final int MAGIC_DUMMY = 0x34; // "DUMMY"

    /**
     * Magic number for DOOM WAD files.
     */
    public static final int MAGIC_DOF = 0x46; // "DOOM"

    /**
     * Magic number for ZDoom WAD files.
     */
    public static final int MAGIC_ZD = 0x5A; // "ZDoom"

    /**
     * Magic number for DOOM2 WAD files.
     */
    public static final int MAGIC_D2 = 0x64; // "D2"

    /**
     * WAD directory entry size in bytes.
     */
    public static final int ENTRY_SIZE = 20;

    /**
     * Maximum number of entries.
     */
    public static final int MAX_ENTRIES = 1 << 24;

    /**
     * Lumps found in the WAD file.
     */
    private final List<Lump> lumps;

    /**
     * WAD file name.
     */
    private String name;

    /**
     * WAD file path.
     */
    private String path;

    /**
     * Creates a new empty WadFile.
     */
    public WadFile() {
        this.lumps = new ArrayList<>();
        this.name = "unnamed.wad";
        this.path = null;
    }

    /**
     * Creates a WadFile from a file path.
     *
     * @param path The path to the WAD file
     * @return The WadFile, or null if loading failed
     */
    public static WadFile load(String path) {
        try {
            WadFile wad = new WadFile();
            wad.name = path;
            wad.path = path;

            try (FileInputStream fis = new FileInputStream(path);
                 DataInputStream dis = new DataInputStream(fis)) {

                // Read magic number
                byte[] magic = new byte[4];
                dis.readFully(magic);
                int magicNum = (magic[0] & 0xFF) |
                               ((magic[1] & 0xFF) << 8) |
                               ((magic[2] & 0xFF) << 16) |
                               ((magic[3] & 0xFF) << 24);

                // Verify magic number
                if (magicNum != MAGIC_DUMMY && magicNum != MAGIC_DOF &&
                    magicNum != MAGIC_ZD && magicNum != MAGIC_D2) {
                    return null;
                }

                // Read file type
                int fileType = dis.read();

                // Read number of lumps
                int numLumps = dis.readInt();

                // Read lumps directory
                for (int i = 0; i < numLumps; i++) {
                    Lump lump = new Lump();
                    lump.name = dis.readUTF();
                    lump.offset = dis.readInt();
                    lump.compression = dis.read();
                    lump.size = dis.readInt();

                    // Read CRC32 (4 bytes)
                    byte[] crcBytes = new byte[4];
                    dis.readFully(crcBytes);
                    lump.crc = (crcBytes[0] & 0xFF) |
                               ((crcBytes[1] & 0xFF) << 8) |
                               ((crcBytes[2] & 0xFF) << 16) |
                               ((crcBytes[3] & 0xFF) << 24);

                    lump.lumpType = parseLumpType(lump.name);
                    wad.lumps.add(lump);
                }
            }

            return wad;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Parses a lump type from its name.
     *
     * @param name The lump name
     * @return The lump type
     */
    private static LumpType parseLumpType(String name) {
        if (name == null || name.isEmpty()) {
            return LumpType.UNKNOWN;
        }

        String lowerName = name.toLowerCase();

        return switch (lowerName) {
            case "sprites", "spr1", "spr2", "spr3", "spr4" -> LumpType.SPRITES;
            case "thingdef" -> LumpType.THING;
            case "mapinfo" -> LumpType.MAPINFO;
            case "lmp" -> LumpType.MAP;
            case "sounds" -> LumpType.SOUNDS;
            case "music" -> LumpType.MUSIC;
            case "blocks" -> LumpType.BLOCKS;
            case "startthings", "starts", "startm" -> LumpType.STARTTHINGS;
            case "intermission" -> LumpType.INTERMISSION;
            case "pats" -> LumpType.PATS;
            case "pinms" -> LumpType.PINMS;
            case "ssectors", "sectors", "ssound" -> LumpType.SECTORS;
            case "deco" -> LumpType.DECORATIONS;
            case "palette" -> LumpType.PALLETTE;
            case "textures", "tx" -> LumpType.TEXTURES;
            case "sfx" -> LumpType.SFX;
            default -> LumpType.UNKNOWN;
        };
    }

    /**
     * Gets the WAD file name.
     *
     * @return The WAD file name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the path to the WAD file.
     *
     * @return The WAD file path, or null if not set
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the number of lumps in the WAD.
     *
     * @return The number of lumps
     */
    public int getLumpCount() {
        return lumps.size();
    }

    /**
     * Gets all lumps in the WAD.
     *
     * @return Unmodifiable list of lumps
     */
    public List<Lump> getLumps() {
        return Collections.unmodifiableList(lumps);
    }

    /**
     * Gets a lump by name.
     *
     * @param name The lump name
     * @return The lump, or null if not found
     */
    public Lump getLump(String name) {
        for (Lump lump : lumps) {
            if (lump.name.equals(name)) {
                return lump;
            }
        }
        return null;
    }

    /**
     * Gets all lumps of a specific type.
     *
     * @param type The lump type
     * @return Unmodifiable list of lumps of the specified type
     */
    public List<Lump> getLumpsOfType(LumpType type) {
        List<Lump> result = new ArrayList<>();
        for (Lump lump : lumps) {
            if (lump.lumpType == type) {
                result.add(lump);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Gets sprites from the WAD.
     *
     * @return Unmodifiable list of sprite lumps
     */
    public List<Lump> getSpriteLumps() {
        return getLumpsOfType(LumpType.SPRITES);
    }

    /**
     * Gets sound data from the WAD.
     *
     * @return Unmodifiable list of sound lumps
     */
    public List<Lump> getSoundLumps() {
        return getLumpsOfType(LumpType.SOUNDS);
    }

    /**
     * Gets all maps in the WAD.
     *
     * @return Unmodifiable list of map lumps
     */
    public List<Lump> getMapLumps() {
        return getLumpsOfType(LumpType.MAP);
    }

    /**
     * Gets decorations from the WAD.
     *
     * @return Unmodifiable list of decoration lumps
     */
    public List<Lump> getDecorationLumps() {
        return getLumpsOfType(LumpType.DECORATIONS);
    }

    /**
     * Checks if the WAD contains sprites.
     *
     * @return true if sprites are present
     */
    public boolean hasSprites() {
        return getLumpsOfType(LumpType.SPRITES).size() > 0;
    }

    /**
     * Checks if the WAD contains sounds.
     *
     * @return true if sounds are present
     */
    public boolean hasSounds() {
        return getLumpsOfType(LumpType.SOUNDS).size() > 0;
    }

    /**
     * Checks if the WAD contains maps.
     *
     * @return true if maps are present
     */
    public boolean hasMaps() {
        return getLumpsOfType(LumpType.MAP).size() > 0;
    }

    /**
     * Checks if the WAD contains decorations.
     *
     * @return true if decorations are present
     */
    public boolean hasDecorations() {
        return getLumpsOfType(LumpType.DECORATIONS).size() > 0;
    }

    @Override
    public String toString() {
        return "WadFile{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", lumpCount=" + lumps.size() +
                ", hasSprites=" + hasSprites() +
                ", hasSounds=" + hasSounds() +
                ", hasMaps=" + hasMaps() +
                ", hasDecorations=" + hasDecorations() +
                '}';
    }

    /**
     * Represents a WAD directory entry.
     */
    public static class Entry {

        /**
         * Entry name.
         */
        private String name;

        /**
         * File offset of the entry.
         */
        private int offset;

        /**
         * Size of the entry data.
         */
        private int length;

        /**
         * CRC32 checksum.
         */
        private int crc;

        /**
         * Number of animation frames (for sprites).
         */
        private int numFrames;

        /**
         * Number of vertices (for maps).
         */
        private int numVertices;

        /**
         * Whether this entry is a sprite.
         */
        private boolean sprite;

        /**
         * Whether this entry is a sound.
         */
        private boolean sound;

        /**
         * Creates a new Entry.
         */
        public Entry() {
            this.name = "";
            this.offset = 0;
            this.length = 0;
            this.crc = 0;
            this.numFrames = 0;
            this.numVertices = 0;
            this.sprite = false;
            this.sound = false;
        }

        /**
         * Gets the entry name.
         *
         * @return The entry name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the entry name.
         *
         * @param name The entry name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the file offset.
         *
         * @return The file offset
         */
        public int getOffset() {
            return offset;
        }

        /**
         * Sets the file offset.
         *
         * @param offset The file offset
         */
        public void setOffset(int offset) {
            this.offset = offset;
        }

        /**
         * Gets the data length.
         *
         * @return The data length
         */
        public int getLength() {
            return length;
        }

        /**
         * Sets the data length.
         *
         * @param length The data length
         */
        public void setLength(int length) {
            this.length = length;
        }

        /**
         * Gets the CRC32 checksum.
         *
         * @return The CRC32 checksum
         */
        public int getCrc() {
            return crc;
        }

        /**
         * Sets the CRC32 checksum.
         *
         * @param crc The CRC32 checksum
         */
        public void setCrc(int crc) {
            this.crc = crc;
        }

        /**
         * Gets the number of frames.
         *
         * @return The number of frames
         */
        public int getNumFrames() {
            return numFrames;
        }

        /**
         * Sets the number of frames.
         *
         * @param numFrames The number of frames
         */
        public void setNumFrames(int numFrames) {
            this.numFrames = numFrames;
        }

        /**
         * Gets the number of vertices.
         *
         * @return The number of vertices
         */
        public int getNumVertices() {
            return numVertices;
        }

        /**
         * Sets the number of vertices.
         *
         * @param numVertices The number of vertices
         */
        public void setNumVertices(int numVertices) {
            this.numVertices = numVertices;
        }

        /**
         * Checks if this entry is a sprite.
         *
         * @return true if this is a sprite
         */
        public boolean isSprite() {
            return sprite;
        }

        /**
         * Sets whether this entry is a sprite.
         *
         * @param sprite true if this is a sprite
         */
        public void setSprite(boolean sprite) {
            this.sprite = sprite;
        }

        /**
         * Checks if this entry is a sound.
         *
         * @return true if this is a sound
         */
        public boolean isSound() {
            return sound;
        }

        /**
         * Sets whether this entry is a sound.
         *
         * @param sound true if this is a sound
         */
        public void setSound(boolean sound) {
            this.sound = sound;
        }

        @Override
        public String toString() {
            return "Entry{\n" +
                    ", name='" + name + "'\n" +
                    ", offset=" + offset + ",\n" +
                    ", length=" + length + ",\n" +
                    ", crc=0x" + Integer.toHexString(crc) + ",\n" +
                    ", numFrames=" + numFrames + ",\n" +
                    ", numVertices=" + numVertices + ",\n" +
                    ", sprite=" + sprite + ",\n" +
                    ", sound=" + sound + "}\n";
        }
    }

    /**
     * Represents a WAD file header.
     */
    public static class Header {

        /**
         * Magic number string.
         */
        private String magic;

        /**
         * Number of directory entries.
         */
        private int numEntries;

        /**
         * Directory offset in file.
         */
        private int dirOffset;

        /**
         * Creates a new Header.
         */
        public Header() {
            this.magic = "";
            this.numEntries = 0;
            this.dirOffset = 0;
        }

        /**
         * Gets the magic number.
         *
         * @return The magic number
         */
        public String getMagic() {
            return magic;
        }

        /**
         * Sets the magic number.
         *
         * @param magic The magic number
         */
        public void setMagic(String magic) {
            this.magic = magic;
        }

        /**
         * Gets the number of entries.
         *
         * @return The number of entries
         */
        public int getNumEntries() {
            return numEntries;
        }

        /**
         * Sets the number of entries.
         *
         * @param numEntries The number of entries
         */
        public void setNumEntries(int numEntries) {
            this.numEntries = numEntries;
        }

        /**
         * Gets the directory offset.
         *
         * @return The directory offset
         */
        public int getDirOffset() {
            return dirOffset;
        }

        /**
         * Sets the directory offset.
         *
         * @param dirOffset The directory offset
         */
        public void setDirOffset(int dirOffset) {
            this.dirOffset = dirOffset;
        }

        /**
         * Checks if the header is valid.
         *
         * @return true if valid
         */
        public boolean isValid() {
            return "IWAD".equals(magic) || "Doom".equals(magic) ||
                   "DOOM".equals(magic) || "DOOM II".equals(magic);
        }

        @Override
        public String toString() {
            return "Header{\n" +
                    ", magic='" + magic + "'\n" +
                    ", numEntries=" + numEntries + ",\n" +
                    ", dirOffset=" + dirOffset + ",\n" +
                    ", isValid=" + isValid() + "}\n";
        }
    }

    /**
     * Represents a lump in a WAD file.
     */
    public static class Lump {

        /**
         * Lump name.
         */
        public String name;

        /**
         * File offset of the lump data.
         */
        public int offset;

        /**
         * Compression type.
         */
        public int compression;

        /**
         * Size of the lump data.
         */
        public int size;

        /**
         * CRC32 checksum.
         */
        public int crc;

        /**
         * Lump type.
         */
        public LumpType lumpType;
    }

    /**
     * WAD file lump types.
     */
    public enum LumpType {
        /**
         * Unknown lump type.
         */
        UNKNOWN,

        /**
         * MAPINFO lump.
         */
        MAPINFO,

        /**
         * Sprite data lump.
         */
        SPRITES,

        /**
         * Thing definition lump.
         */
        THING,

        /**
         * Map data lump.
         */
        MAP,

        /**
         * Sound data lump.
         */
        SOUNDS,

        /**
         * Music lump.
         */
        MUSIC,

        /**
         * Block list lump.
         */
        BLOCKS,

        /**
         * Start things lump.
         */
        STARTTHINGS,

        /**
         * Intermission lump.
         */
        INTERMISSION,

        /**
         * PATs lump.
         */
        PATS,

        /**
         * PINMs lump.
         */
        PINMS,

        /**
         * Sector sounds lump.
         */
        SECTORS,

        /**
         * Decoration lump.
         */
        DECORATIONS,

        /**
         * Palette lump.
         */
        PALLETTE,

        /**
         * Texture lump.
         */
        TEXTURES,

        /**
         * Sound effects lump.
         */
        SFX
    }
}
