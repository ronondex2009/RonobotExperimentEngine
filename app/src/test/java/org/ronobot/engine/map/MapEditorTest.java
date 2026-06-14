/**
 * Unit tests for MapEditor.
 *
 * @author ronobot
 * @since 1.0-SNAPSHOT
 */
package org.ronobot.engine.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for MapEditor.
 *
 * @author ronobot
 * @since 1.0-SNAPSHOT
 */
@DisabledOnOs(OS.WINDOWS)
@DisplayName("MapEditor Tests")
class MapEditorTest {

    private static final Path TEMP_DIR;

    static {
        try {
            TEMP_DIR = Files.createTempDirectory("mapeditor-test");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp directory", e);
        }
    }
    private static final Path TEST_MAP_FILE = Paths.get("test_map.txt");
    private MapEditor editor;

    @BeforeEach
    @DisplayName("Setup MapEditor")
    void setup() {
        this.editor = MapEditor.createBlankMap(MapEditor.DEFAULT_TILES.length, 20);
    }

    @Test
    @DisplayName("createBlankMap should return valid editor")
    void testCreateBlankMap() {
        MapEditor map = MapEditor.createBlankMap(10, 10);
        assertNotNull(map);
        assertEquals(10, map.getMapWidth());
    }

    @Test
    @DisplayName("createTestMap should return valid editor with test map")
    void testCreateTestMap() {
        MapEditor map = MapEditor.createTestMap();
        assertNotNull(map);
        assertTrue(map.toString().contains("P"));
    }

    @Test
    @DisplayName("getMapWidth should return correct width")
    void testGetMapWidth() {
        assertEquals(MapEditor.DEFAULT_TILES.length, this.editor.getMapWidth());
    }

    @Test
    @DisplayName("getMapHeight should return height")
    void testGetMapHeight() {
        assertEquals(20, this.editor.getMapHeight());
    }

    @Test
    @DisplayName("getTile should return TILE_EMPTY for empty map")
    void testGetTile() {
        assertEquals(MapEditor.TILE_EMPTY, this.editor.getTile(0, 0));
    }

    @Test
    @DisplayName("setTile should update tile")
    void testSetTile() {
        this.editor.setTile(0, 0, MapEditor.TILE_WALL);
        assertEquals(MapEditor.TILE_WALL, this.editor.getTile(0, 0));
    }

    @Test
    @DisplayName("setTile should return false for out of bounds")
    void testSetTileOutOfBounds() {
        boolean result = this.editor.setTile(-1, 0, MapEditor.TILE_WALL);
        assertFalse(result);
        result = this.editor.setTile(MapEditor.DEFAULT_TILES.length, 0, MapEditor.TILE_WALL);
        assertFalse(result);
    }

    @Test
    @DisplayName("removeTile should set to TILE_EMPTY")
    void testRemoveTile() {
        this.editor.setTile(0, 0, MapEditor.TILE_WALL);
        boolean result = this.editor.removeTile(0, 0);
        assertTrue(result);
        assertEquals(MapEditor.TILE_EMPTY, this.editor.getTile(0, 0));
    }

    @Test
    @DisplayName("fill should fill region with tile")
    void testFill() {
        this.editor.fill(0, 0, 4, 2, MapEditor.TILE_WALL);
        assertEquals(MapEditor.TILE_WALL, this.editor.getTile(0, 0));
        assertEquals(MapEditor.TILE_WALL, this.editor.getTile(1, 0));
    }

    @Test
    @DisplayName("fill should not modify out of bounds regions")
    void testFillOutOfBounds() {
        MapEditor map = MapEditor.createBlankMap(5, 5);
        map.fill(-1, 0, 2, 1, MapEditor.TILE_WALL);
        assertEquals(MapEditor.TILE_EMPTY, map.getTile(0, 0));
    }

    @Test
    @DisplayName("toString should return map representation")
    void testToString() {
        MapEditor map = MapEditor.createBlankMap(5, 3);
        String output = map.toString();
        assertNotNull(output);
        assertTrue(output.contains(MapEditor.TILE_EMPTY));
    }

    @Test
    @DisplayName("clear should reset all tiles")
    void testClear() {
        this.editor.fill(0, 0, 2, 1, MapEditor.TILE_WALL);
        this.editor.clear();
        assertEquals(MapEditor.TILE_EMPTY, this.editor.getTile(0, 0));
    }

    @Test
    @DisplayName("addDecoration should add decoration")
    void testAddDecoration() {
        boolean result = this.editor.addDecoration(0, 0, "tree");
        assertTrue(result);
    }

    @Test
    @DisplayName("getDecorations should return decorations list")
    void testGetDecorations() {
        this.editor.addDecoration(0, 0, "tree");
        List<MapDecoration> decorations = this.editor.getDecorations();
        assertNotNull(decorations);
        assertEquals(1, decorations.size());
    }

    @Test
    @DisplayName("getSpawns should return spawns list")
    void testGetSpawns() {
        List<EntitySpawn> spawns = this.editor.getSpawns();
        assertNotNull(spawns);
        assertEquals(0, spawns.size());
    }

    @Test
    @DisplayName("loadFromFile should load map")
    void testLoadFromFile() throws IOException {
        // Create a simple test map file
        Path testFile = TEMP_DIR.resolve("test.txt");
        String mapContent = "P.....\n" +
                            "......\n" +
                            "....H.\n" +
                            "......\n" +
                            "......\n" +
                            "......\n";
        Files.write(testFile, mapContent.getBytes());

        boolean result = this.editor.loadFromFile(testFile);
        assertTrue(result);
    }

    @Test
    @DisplayName("saveToFile should save map")
    void testSaveToFile() throws IOException {
        // Create a simple test map
        MapEditor map = MapEditor.createBlankMap(5, 3);
        map.fill(0, 0, 2, 1, MapEditor.TILE_WALL);

        Path testFile = TEMP_DIR.resolve("output.txt");
        boolean result = map.saveToFile(testFile);
        assertTrue(result);

        String content = Files.readString(testFile);
        assertNotNull(content);
        assertTrue(content.contains(MapEditor.TILE_WALL));
    }

    @Test
    @DisplayName("saveToWriter should write map to writer")
    void testSaveToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        MapEditor map = MapEditor.createBlankMap(5, 3);

        map.saveToWriter(writer);
        String output = writer.toString();
        assertNotNull(output);
    }

    @Test
    @DisplayName("loadFromFile should handle empty lines")
    void testLoadFromFileEmptyLines() throws IOException {
        Path testFile = TEMP_DIR.resolve("empty.txt");
        Files.write(testFile, "\n\n\n".getBytes(StandardCharsets.UTF_8));

        boolean result = this.editor.loadFromFile(testFile);
        assertTrue(result);
    }

    @Test
    @DisplayName("loadFromFile should skip comment lines")
    void testLoadFromFileComments() throws IOException {
        Path testFile = TEMP_DIR.resolve("comments.txt");
        String content = "// this is a comment\n" +
                          "// another comment\n" +
                          "P...\n" +
                          "...\n" +
                          "...\n" +
                          "...\n" +
                          "...\n";
        Files.write(testFile, content.getBytes());

        boolean result = this.editor.loadFromFile(testFile);
        assertTrue(result);
    }

    @Test
    @DisplayName("loadFromFile should handle MAP_NAME directive")
    void testLoadFromFileMapName() throws IOException {
        Path testFile = TEMP_DIR.resolve("named.txt");
        String content = "MAP_NAME=MyLevel\n" +
                          "P...\n" +
                          "...\n" +
                          "...\n" +
                          "...\n" +
                          "...\n";
        Files.write(testFile, content.getBytes());

        boolean result = this.editor.loadFromFile(testFile);
        assertTrue(result);
    }

    @Test
    @DisplayName("loadFromFile should handle DIFFICULTY directive")
    void testLoadFromFileDifficulty() throws IOException {
        Path testFile = TEMP_DIR.resolve("difficulty.txt");
        String content = "DIFFICULTY=Hard\n" +
                          "P...\n" +
                          "...\n" +
                          "...\n" +
                          "...\n" +
                          "...\n";
        Files.write(testFile, content.getBytes());

        boolean result = this.editor.loadFromFile(testFile);
        assertTrue(result);
    }

    @Test
    @DisplayName("loadFromFile should handle DECORATION directive")
    void testLoadFromFileDecoration() throws IOException {
        Path testFile = TEMP_DIR.resolve("decor.txt");
        Files.write(testFile, ("DECORATION=tree@🌳\n" +
                          "P..\n" +
                          "...\n" +
                          "...\n" +
                          "...\n" +
                          "...\n").getBytes(StandardCharsets.UTF_8));

        boolean result = this.editor.loadFromFile(testFile);
        assertTrue(result);
    }

    @Test
    @DisplayName("loadFromFile should handle spawn directives")
    void testLoadFromFileSpawn() throws IOException {
        Path testFile = TEMP_DIR.resolve("spawn.txt");
        Files.write(testFile, ("SPAWN_PLAYER=0,0\n" +
                          "P..\n" +
                          "...\n" +
                          "...\n" +
                          "...\n" +
                          "...\n").getBytes(StandardCharsets.UTF_8));

        boolean result = this.editor.loadFromFile(testFile);
        assertTrue(result);
    }

    @Test
    @DisplayName("MapEditor constant TILE_EMPTY is correct")
    void testTileEmptyConstant() {
        assertEquals(".", MapEditor.TILE_EMPTY);
    }

    @Test
    @DisplayName("MapEditor constant TILE_WALL is correct")
    void testTileWallConstant() {
        assertEquals("#", MapEditor.TILE_WALL);
    }

    @Test
    @DisplayName("MapEditor constant TILE_DOOR is correct")
    void testTileDoorConstant() {
        assertEquals("D", MapEditor.TILE_DOOR);
    }

    @Test
    @DisplayName("MapEditor constant TILE_ELEVATOR is correct")
    void testTileElevatorConstant() {
        assertEquals("E", MapEditor.TILE_ELEVATOR);
    }

    @Test
    @DisplayName("MapEditor constant TILE_STAIR is correct")
    void testTileStairConstant() {
        assertEquals("S", MapEditor.TILE_STAIR);
    }

    @Test
    @DisplayName("MapEditor constant TILE_SECRET_DOOR is correct")
    void testTileSecretDoorConstant() {
        assertEquals("d", MapEditor.TILE_SECRET_DOOR);
    }

    @Test
    @DisplayName("MapEditor constant TILE_DECORATION is correct")
    void testTileDecorationConstant() {
        assertEquals("@", MapEditor.TILE_DECORATION);
    }

    @Test
    @DisplayName("MapEditor constant TILE_PLAYER is correct")
    void testTilePlayerConstant() {
        assertEquals("P", MapEditor.TILE_PLAYER);
    }

    @Test
    @DisplayName("MapEditor constant TILE_ENEMY is correct")
    void testTileEnemyConstant() {
        assertEquals("e", MapEditor.TILE_ENEMY);
    }

    @Test
    @DisplayName("MapEditor constant TILE_AMMO is correct")
    void testTileAmmoConstant() {
        assertEquals("A", MapEditor.TILE_AMMO);
    }

    @Test
    @DisplayName("MapEditor constant TILE_HEALTH is correct")
    void testTileHealthConstant() {
        assertEquals("H", MapEditor.TILE_HEALTH);
    }

    @Test
    @DisplayName("MapEditor constant DEFAULT_TILES has correct length")
    void testDefaultTilesLength() {
        assertEquals(12, MapEditor.DEFAULT_TILES.length);
    }

    @Test
    @DisplayName("MapEditor default tiles are correct")
    void testDefaultTilesValues() {
        String[] expected = {
                MapEditor.TILE_EMPTY, MapEditor.TILE_WALL, MapEditor.TILE_DOOR,
                MapEditor.TILE_ELEVATOR, MapEditor.TILE_STAIR, MapEditor.TILE_SECRET_DOOR,
                MapEditor.TILE_DECORATION, MapEditor.TILE_PLAYER, MapEditor.TILE_ENEMY,
                MapEditor.TILE_AMMO, MapEditor.TILE_HEALTH
        };

        String[] actual = MapEditor.DEFAULT_TILES;
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"P", "H", "A"})
    @DisplayName("setTile should accept spawn type tiles")
    void testSetTileSpawnType(String spawnType) {
        boolean result = this.editor.setTile(0, 0, spawnType);
        assertTrue(result);
    }

    @ParameterizedTest
    @CsvSource({
            ".",
            "#",
            "D",
            "E",
            "S",
            "d",
            "@",
            "P",
            "H",
            "A"
    })
    @DisplayName("setTile should accept valid tiles")
    void testSetTileValidTiles(String tile) {
        boolean result = this.editor.setTile(0, 0, tile);
        assertTrue(result);
    }
}
