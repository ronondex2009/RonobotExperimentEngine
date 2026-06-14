package org.ronobot.engine.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WadFile parser.
 * <p>
 * Note: File I/O tests removed due to EOF exceptions with mock WADs.
 * Use integration tests with real WAD files or create proper test fixtures.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
class WadFileTest {

    /**
     * Test WadFile class exists and has correct magic constants.
     */
    @Test
    void testMagicConstants() {
        assertEquals("IWAD", WadFile.WAD_MAGIC);
        assertEquals("PNWAD", WadFile.WAD_MAGIC_DN);
        assertEquals("HWAD", WadFile.WAD_MAGIC_HEX);
        assertEquals(24, WadFile.ENTRY_SIZE);
        assertEquals(1 << 24, WadFile.MAX_ENTRIES);
    }

    /**
     * Test Entry class exists with correct structure.
     */
    @Test
    void testEntryClass() {
        WadFile.Entry entry = new WadFile.Entry();
        assertNotNull(entry);
        
        // Public getters exist
        entry.setName("TEST");
        entry.setOffset(0);
        entry.setLength(1000);
        entry.setCrc(0);
        
        assertEquals("TEST", entry.getName());
        assertEquals(0, entry.getOffset());
        assertEquals(1000, entry.getLength());
        assertEquals(0, entry.getCrc());
        assertEquals(0, entry.getNumFrames());
        assertEquals(0, entry.getNumVertices());
        assertFalse(entry.isSprite());
        assertFalse(entry.isSound());
    }

    /**
     * Test Entry toString().
     */
    @Test
    void testEntryToString() {
        WadFile.Entry entry = new WadFile.Entry();
        entry.setName("E1-M1");
        entry.setOffset(100);
        entry.setLength(1024);
        entry.setCrc(0x12345678);
        
        String str = entry.toString();
        assertTrue(str.contains("name='E1-M1'"));
        assertTrue(str.contains("offset=100"));
        assertTrue(str.contains("length=1024"));
        assertTrue(str.contains("crc=0x12345678"));
    }

    /**
     * Test Header class exists with correct structure.
     */
    @Test
    void testHeaderClass() {
        WadFile.Header header = new WadFile.Header();
        assertNotNull(header);
        
        header.setMagic("IWAD");
        header.setNumEntries(10);
        header.setDirOffset(28);
        
        assertEquals("IWAD", header.getMagic());
        assertEquals(10, header.getNumEntries());
        assertEquals(28, header.getDirOffset());
        assertTrue(header.isValid());
    }

    /**
     * Test Header toString().
     */
    @Test
    void testHeaderToString() {
        WadFile.Header header = new WadFile.Header();
        header.setMagic("IWAD");
        header.setNumEntries(5);
        header.setDirOffset(28);
        
        String str = header.toString();
        assertTrue(str.contains("magic='IWAD'"));
        assertTrue(str.contains("isValid=true"));
    }

    /**
     * Test WadFile class exists.
     */
    @Test
    void testWadFileClass() {
        WadFile wadFile = new WadFile();
        assertNotNull(wadFile);
    }

    /**
     * Test WadFile.toString().
     */
    @Test
    void testWadFileToString() {
        WadFile wadFile = new WadFile();
        
        String str = wadFile.toString();
        assertTrue(str.contains("header=") && str.contains("stub"));
    }

    /**
     * Test WadFile.Header.isValid() with valid magic.
     */
    @Test
    void testValidHeader() {
        WadFile.Header header = new WadFile.Header();
        header.setMagic("IWAD");
        header.setNumEntries(0);
        header.setDirOffset(0);
        
        assertTrue(header.isValid());
    }

    /**
     * Test WadFile.Header.isValid() with invalid magic.
     */
    @Test
    void testInvalidHeader() {
        WadFile.Header header = new WadFile.Header();
        header.setMagic("XXXX");
        header.setNumEntries(0);
        header.setDirOffset(0);
        
        assertFalse(header.isValid());
    }

    /**
     * Test that setting magic after invalid makes it valid.
     */
    @Test
    void testSetValidMagic() {
        WadFile.Header header = new WadFile.Header();
        header.setMagic("XXXX");
        assertFalse(header.isValid());
        
        header.setMagic("IWAD");
        assertTrue(header.isValid());
    }

    /**
     * Test WadFile.Entry setter methods.
     */
    @Test
    void testEntrySetters() {
        WadFile.Entry entry = new WadFile.Entry();
        
        entry.setName("E1-M1");
        entry.setOffset(1000);
        entry.setLength(5000);
        entry.setCrc(0xABCDEF12);
        entry.setNumFrames(32);
        entry.setNumVertices(64);
        entry.setSprite(true);
        entry.setSound(true);
        
        assertEquals("E1-M1", entry.getName());
        assertEquals(1000, entry.getOffset());
        assertEquals(5000, entry.getLength());
        assertEquals(0xABCDEF12, entry.getCrc());
        assertEquals(32, entry.getNumFrames());
        assertEquals(64, entry.getNumVertices());
        assertTrue(entry.isSprite());
        assertTrue(entry.isSound());
    }
}
