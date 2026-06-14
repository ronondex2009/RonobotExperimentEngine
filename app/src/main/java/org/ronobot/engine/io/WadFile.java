package org.ronobot.engine.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * WAD file parser for Doom-style game assets.
 * <p>
 * This class handles reading WAD (WOLF4 DOOM Archive) files which contain
 * sprite data, sound effects, and other game assets. WAD files use a
 * simple header format with directory entries pointing to lump data.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class WadFile {

    /**
     * Magic number for standard WAD files.
     */
    public static final String WAD_MAGIC = "IWAD";

    /**
     * Magic number for Duke Nukem 3D WAD files.
     */
    public static final String WAD_MAGIC_DN = "PNWAD";

    /**
     * Magic number for Hexen WAD files.
     */
    public static final String WAD_MAGIC_HEX = "HWAD";

    /**
     * Standard WAD entry size in bytes.
     */
    public static final int ENTRY_SIZE = 24;

    /**
     * Maximum number of entries in a WAD file.
     */
    public static final int MAX_ENTRIES = 1 << 24; // 16MB address space

    /**
     * WAD directory entry structure.
     */
    public static class Entry {
        /**
         * Lump name (up to 8 characters, null-padded).
         */
        private String name;

        /**
         * File offset of the lump data in bytes.
         */
        private int offset;

        /**
         * Size of the lump data in bytes.
         */
        private int length;

        /**
         * CRC32 checksum of the lump data.
         */
        private int crc;

        /**
         * Number of frames in sprites (for sprite lumps).
         */
        private int numFrames = 0;

        /**
         * Number of vertices in sprites (for sprite lumps).
         */
        private int numVertices = 0;

        /**
         * Whether this entry is a sprite lump.
         */
        private boolean isSprite;

        /**
         * Whether this entry is a sound lump.
         */
        private boolean isSound;

        /**
         * Default constructor.
         */
        public Entry() {
        }

        /**
         * Gets the lump name.
         *
         * @return The lump name, or empty string if not set
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the lump name.
         *
         * @param name The lump name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the file offset of the lump data.
         *
         * @return The file offset in bytes
         */
        public int getOffset() {
            return offset;
        }

        /**
         * Sets the file offset of the lump data.
         *
         * @param offset The file offset in bytes
         */
        public void setOffset(int offset) {
            this.offset = offset;
        }

        /**
         * Gets the size of the lump data.
         *
         * @return The lump size in bytes
         */
        public int getLength() {
            return length;
        }

        /**
         * Sets the size of the lump data.
         *
         * @param length The lump size in bytes
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
         * Checks if this entry is a sprite lump.
         *
         * @return true if this is a sprite lump
         */
        public boolean isSprite() {
            return isSprite;
        }

        /**
         * Sets whether this entry is a sprite lump.
         *
         * @param isSprite true if this is a sprite lump
         */
        public void setSprite(boolean isSprite) {
            this.isSprite = isSprite;
        }

        /**
         * Checks if this entry is a sound lump.
         *
         * @return true if this is a sound lump
         */
        public boolean isSound() {
            return isSound;
        }

        /**
         * Sets whether this entry is a sound lump.
         *
         * @param isSound true if this is a sound lump
         */
        public void setSound(boolean isSound) {
            this.isSound = isSound;
        }

        /**
         * Creates a string representation of the entry.
         *
         * @return String representation of the entry
         */
        @Override
        public String toString() {
            return "Entry{" +
                    "name='" + name + '\'' +
                    ", offset=" + offset +
                    ", length=" + length +
                    ", crc=0x" + Integer.toHexString(crc) +
                    ", numFrames=" + numFrames +
                    ", numVertices=" + numVertices +
                    ", isSprite=" + isSprite +
                    ", isSound=" + isSound +
                    '}';
        }
    }

    /**
     * WAD header information.
     */
    public static class Header {
        /**
         * Magic number string (4 bytes).
         */
        private String magic;

        /**
         * Number of entries in the directory.
         */
        private int numEntries;

        /**
         * Directory offset in bytes.
         */
        private int dirOffset;

        /**
         * Whether this header is valid.
         */
        private boolean isValid;

        /**
         * Default constructor.
         */
        public Header() {
            this.isValid = false;
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
            // Validate magic
            this.isValid = magic != null && (magic.equals(WAD_MAGIC) || 
                    magic.equals(WAD_MAGIC_DN) || magic.equals(WAD_MAGIC_HEX));
        }

        /**
         * Gets the number of directory entries.
         *
         * @return The number of entries
         */
        public int getNumEntries() {
            return numEntries;
        }

        /**
         * Sets the number of directory entries.
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
         * Checks if this is a valid WAD file.
         *
         * @return true if this is a valid WAD file
         */
        public boolean isValid() {
            return this.isValid;
        }

        /**
         * Creates a string representation of the header.
         *
         * @return String representation of the header
         */
        @Override
        public String toString() {
            return "Header{" +
                    "magic='" + magic + '\'' +
                    ", numEntries=" + numEntries +
                    ", dirOffset=" + dirOffset +
                    ", isValid=" + this.isValid +
                    '}';
        }
    }

    /**
     * Creates a new WAD file parser.
     */
    public WadFile() {
    }

    /**
     * Creates a string representation of the WAD file.
     *
     * @return String representation of the WAD file
     */
    @Override
    public String toString() {
        return "WadFile{header=(stub)}";
    }

    /**
     * Loads a WAD file from the specified file.
     * <p>
     * Parses the WAD file header and reads all directory entries.
     * Does not load lump data - use getEntry() to access individual entries.
     * </p>
     *
     * @param file The WAD file to load
     * @return The WAD header
     * @throws IOException If an error occurs while reading the file
     * @throws RuntimeException If the file is not a valid WAD file
     */
    public Header load(Path file) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(file.toFile()));

        try {
            // Read magic number (4 bytes)
            byte[] magicBytes = new byte[4];
            dis.readFully(magicBytes);
            String magic = new String(magicBytes).trim();
            if (magic.isEmpty() || (magic.equals("PNWAD") && magic.length() > 4)) {
                // Handle compressed/different WAD formats
                magic = new String(magicBytes, 0, 4).trim();
            }

            Header header = new Header();
            header.magic = magic;
            header.setNumEntries(0);
            header.setDirOffset(0);

            // Validate magic
            header.isValid = magic != null && (magic.equals(WAD_MAGIC) || 
                    magic.equals(WAD_MAGIC_DN) || magic.equals(WAD_MAGIC_HEX));

            // Read number of entries (4 bytes, little-endian)
            if (header.isValid) {
                int numEntries = dis.readInt();
                header.numEntries = numEntries;

                // Read directory offset (4 bytes, little-endian)
                int dirOffset = dis.readInt();
                header.dirOffset = dirOffset;

                // Read each directory entry
                byte[] entryBuffer = new byte[ENTRY_SIZE];
                for (int i = 0; i < numEntries; i++) {
                    dis.readFully(entryBuffer);
                    Entry entry = new Entry();

                    // Read name (8 bytes, null-padded)
                    int nameLen = entryBuffer[0] & 0xFF;
                    entry.name = new String(entryBuffer, 1, nameLen);

                    // Read offset (4 bytes)
                    entry.offset = dis.readInt();

                    // Read length (4 bytes)
                    entry.length = dis.readInt();

                    // Read CRC (4 bytes)
                    entry.crc = dis.readInt();

                    // Read additional fields (for sprites)
                    entry.numFrames = dis.readInt();
                    entry.numVertices = dis.readInt();

                    // Determine lump type based on name
                    entry.isSprite = nameLen > 0 && nameLen <= 8 && (entry.name.endsWith("-spr") ||
                            entry.name.startsWith("E1") || entry.name.startsWith("E2") ||
                            entry.name.startsWith("M1") || entry.name.startsWith("M2") ||
                            entry.name.startsWith("P1") || entry.name.startsWith("P2") ||
                            entry.name.startsWith("S1") || entry.name.startsWith("S2"));

                    entry.isSound = nameLen > 0 && nameLen <= 8 && entry.name.equals("sfx1") ||
                            entry.name.equals("sfx2") || entry.name.equals("sfx3") ||
                            entry.name.equals("sfx4");

                    header.numEntries++;
                }
            }

            return header;

        } finally {
            dis.close();
        }
    }
}
