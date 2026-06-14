/**
 * MapEditor - A tool for editing text-based map files.
 *
 * Provides functionality for:
 * - Creating new map files
 * - Loading and editing existing map files
 * - Placing/removing tiles and decorations
 * - Exporting edited maps to text files
 *
 * @author ronobot
 * @since 1.0-SNAPSHOT
 */
package org.ronobot.engine.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Editor for text-based map files used in the game engine.
 *
 * <p>Supports editing tile types and decorations in map files.</p>
 *
 * @author ronobot
 * @since 1.0-SNAPSHOT
 */
public class MapEditor {

    /** Default tile for empty space (floor). */
    public static final String TILE_EMPTY = ".";

    /** Tile for wall. */
    public static final String TILE_WALL = "#";

    /** Tile for door. */
    public static final String TILE_DOOR = "D";

    /** Tile for elevator. */
    public static final String TILE_ELEVATOR = "E";

    /** Tile for stair. */
    public static final String TILE_STAIR = "S";

    /** Tile for secret door. */
    public static final String TILE_SECRET_DOOR = "d";

    /** Tile for decoration placeholder. */
    public static final String TILE_DECORATION = "@";

    /** Tile for player spawn. */
    public static final String TILE_PLAYER = "P";

    /** Tile for enemy spawn. */
    public static final String TILE_ENEMY = "E";

    /** Tile for ammo spawn. */
    public static final String TILE_AMMO = "A";

    /** Tile for health spawn. */
    public static final String TILE_HEALTH = "H";

    /** Default tile set for new maps. */
    private static final int DEFAULT_TILE_COUNT = 11;

    /** Tile values for the default tile map. */
    public static final String[] DEFAULT_TILES = {
            TILE_EMPTY, TILE_WALL, TILE_DOOR, TILE_ELEVATOR, TILE_STAIR,
            TILE_SECRET_DOOR, TILE_DECORATION, TILE_PLAYER, TILE_ENEMY, TILE_AMMO, TILE_HEALTH
    };

    private String[] tileMap;
    private final List<MapDecoration> decorations;
    private final List<EntitySpawn> spawns;
    private final List<String> mapNames;
    private final List<String> difficultyStrings;
    private int mapHeight;

    /**
     * Constructs a new MapEditor with default settings.
     */
    public MapEditor() {
        this.tileMap = new String[DEFAULT_TILE_COUNT];
        for (int i = 0; i < DEFAULT_TILE_COUNT; i++) {
            this.tileMap[i] = TILE_EMPTY;
        }
        this.decorations = new ArrayList<>();
        this.spawns = new ArrayList<>();
        this.mapNames = new ArrayList<>();
        this.difficultyStrings = new ArrayList<>();
        this.mapHeight = 20;
    }

    /**
     * Sets the map height.
     *
     * @param height The map height
     */
    public void setMapHeight(int height) {
        this.mapHeight = height;
    }

    /**
     * Loads a map file from disk.
     *
     * @param path Path to the map file
     * @return true if the map was loaded successfully
     * @throws IOException if the file cannot be read
     * @see #loadFromFile
     */
    public boolean load(Path path) throws IOException {
        return loadFromFile(path);
    }

    /**
     * Loads a map file from a file path string.
     *
     * @param path Path string to the map file
     * @return true if the map was loaded successfully
     * @throws IOException if the file cannot be read
     */
    public boolean loadFromFile(String path) throws IOException {
        return loadFromFile(Paths.get(path));
    }

    /**
     * Loads a map file from a file path.
     *
     * @param path Path to the map file
     * @return true if the map was loaded successfully
     * @throws IOException if the file cannot be read
     */
    public boolean loadFromFile(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return loadFromContent(reader);
        }
    }

    /**
     * Loads map content from a reader.
     *
     * @param reader The reader to read from
     * @return true if the map was loaded successfully
     * @throws IOException if an I/O error occurs
     */
    private boolean loadFromContent(java.io.BufferedReader reader) throws IOException {
        String line;
        int row = 0;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("//")) {
                continue;
            }

            if (line.startsWith("MAP_NAME=")) {
                String value = extractValue(line);
                if (value != null) {
                    this.mapNames.add(value);
                }
            } else if (line.startsWith("DIFFICULTY=")) {
                String value = extractValue(line);
                if (value != null) {
                    this.difficultyStrings.add(value);
                }
            } else if (line.startsWith("DECORATION=")) {
                String decorValue = extractValue(line);
                if (decorValue != null && decorValue.startsWith("@")) {
                    String[] decorParts = decorValue.split("@", 2);
                    if (decorParts.length == 2 && !decorParts[1].isEmpty()) {
                        MapDecoration decoration = new MapDecoration(-1, -1, decorParts[0], decorParts[1], '@', 0);
                        this.decorations.add(decoration);
                    }
                }
            } else if (line.startsWith("SPAWN_PLAYER=") || line.startsWith("SPAWN_ENEMY=") ||
                    line.startsWith("SPAWN_AMMO=") || line.startsWith("SPAWN_HEALTH=")) {
                handleSpawnLine(line);
            } else if (!line.isEmpty()) {
                // This is a map tile row
                parseMapRow(line, row);
                row++;
            }
        }

        // Parse spawn positions from tile markers
        parseSpawns();
        return true;
    }

    /**
     * Parses a single row of map tiles.
     *
     * @param line The map row to parse
     * @param row  The row index
     */
    private void parseMapRow(String line, int row) {
        int col = 0;
        for (char c : line.toCharArray()) {
            if (col >= this.tileMap.length) {
                // Map row is too wide for current tile map
                return;
            }

            switch (c) {
                case '.' -> this.tileMap[col] = TILE_EMPTY;
                case '#' -> this.tileMap[col] = TILE_WALL;
                case 'D' -> this.tileMap[col] = TILE_DOOR;
                case 'E' -> this.tileMap[col] = TILE_ELEVATOR;
                case 'S' -> this.tileMap[col] = TILE_STAIR;
                case 'd' -> this.tileMap[col] = TILE_SECRET_DOOR;
                case '@' -> this.tileMap[col] = TILE_DECORATION;
                case 'P' -> this.tileMap[col] = TILE_PLAYER;
                case 'H' -> this.tileMap[col] = TILE_HEALTH;
                default -> this.tileMap[col] = TILE_EMPTY;
            }
            col++;
        }
    }

    /**
     * Handles spawn directive lines.
     *
     * @param line The spawn line to process
     */
    private void handleSpawnLine(String line) {
        String spawnType = line.split("=", 2)[1].trim();
        EntitySpawn spawn = new EntitySpawn(spawnType, null, -1, -1);
        this.spawns.add(spawn);
    }

    /**
     * Parses spawn positions from tile markers in the map data.
     */
    private void parseSpawns() {
        // Spawn positions are already extracted during parsing in handleSpawnLine
        // This method is a placeholder for future enhancement to parse spawn positions
        // from tile markers in the map data
    }

    /**
     * Extracts the value from a key=value line.
     *
     * @param line The line to extract from
     * @return The value string, or null if extraction fails
     */
    private String extractValue(String line) {
        String[] parts = line.split("=", 2);
        if (parts.length == 2) {
            return parts[1];
        }
        return null;
    }

    /**
     * Gets the tile character at the given position.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return The tile character, or empty string if out of bounds
     */
    public String getTile(int x, int y) {
        if (y < 0 || y >= this.mapHeight || x < 0 || x >= this.tileMap.length) {
            return TILE_EMPTY;
        }
        return this.tileMap[x];
    }

    /**
     * Places a tile at the given position.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param tile The tile to place
     * @return true if the placement was successful
     */
    public boolean setTile(int x, int y, String tile) {
        if (x < 0 || y < 0 || x >= this.tileMap.length) {
            return false;
        }

        this.tileMap[x] = tile;
        return true;
    }

    /**
     * Removes a tile at the given position.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if the tile was removed
     */
    public boolean removeTile(int x, int y) {
        return setTile(x, y, TILE_EMPTY);
    }

    /**
     * Adds a decoration at the given position.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param decorationType The decoration type
     * @return true if the decoration was added
     */
    public boolean addDecoration(int x, int y, String decorationType) {
        // Use a placeholder visual character for new decorations
        MapDecoration decoration = new MapDecoration(-1, -1, decorationType, "", '@', 0);
        this.decorations.add(decoration);
        if (y >= 0 && y < 20 && x >= 0 && x < this.tileMap.length) {
            this.tileMap[x] = TILE_DECORATION;
        }
        return true;
    }

    /**
     * Removes a decoration at the given position.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if the decoration was removed
     */
    public boolean removeDecoration(int x, int y) {
        return setTile(x, y, TILE_EMPTY);
    }

    /**
     * Saves the edited map to a file.
     *
     * @param path Path to save the map
     * @return true if the save was successful
     * @throws IOException if an I/O error occurs
     */
    public boolean save(Path path) throws IOException {
        return saveToFile(path);
    }

    /**
     * Saves the edited map to a file path string.
     *
     * @param path Path string to save the map
     * @return true if the save was successful
     * @throws IOException if an I/O error occurs
     */
    public boolean saveToFile(String path) throws IOException {
        return saveToFile(Paths.get(path));
    }

    /**
     * Saves the edited map to a file path.
     *
     * @param path Path to save the map
     * @return true if the save was successful
     * @throws IOException if an I/O error occurs
     */
    public boolean saveToFile(Path path) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path)) {
            saveToWriter(writer);
            return true;
        }
    }

    /**
     * Saves the edited map to a writer.
     *
     * @param writer The writer to save to
     * @throws IOException if an I/O error occurs
     */
    public void saveToWriter(Writer writer) throws IOException {
        // Save map metadata
        if (!this.mapNames.isEmpty()) {
            writer.write("MAP_NAME=" + this.mapNames.get(0) + "\n");
        }
        if (!this.difficultyStrings.isEmpty()) {
            writer.write("DIFFICULTY=" + this.difficultyStrings.get(0) + "\n");
        }

        // Save decorations
        for (MapDecoration decoration : this.decorations) {
            writer.write("DECORATION=" + decoration.getType() + "@" + decoration.getVisualChar() + "\n");
        }

        // Save map rows
        int width = getMapWidth();
        int height = getMapHeight();
        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < width; x++) {
                row.append(getTile(x, y));
            }
            writer.write(row.toString() + "\n");
        }
    }

    /**
     * Gets the map width.
     *
     * @return The map width
     */
    public int getMapWidth() {
        if (this.tileMap == null || this.tileMap.length == 0) {
            return 0;
        }
        return this.tileMap.length;
    }

    /**
     * Gets the map height.
     *
     * @return The map height
     */
    public int getMapHeight() {
        return mapHeight;
    }

    /**
     * Gets the tile character at the given position.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return The tile character
     */
    private String getTileAt(int x, int y) {
        if (y < 0 || x < 0 || x >= this.tileMap.length) {
            return TILE_EMPTY;
        }

        String tile = this.tileMap[x];
        if (tile == null || tile.isEmpty()) {
            return TILE_EMPTY;
        }

        return tile;
    }

    /**
     * Clears all tiles in the map.
     */
    public void clear() {
        int width = this.tileMap == null ? 0 : this.tileMap.length;
        for (int i = 0; i < width; i++) {
            this.tileMap[i] = TILE_EMPTY;
        }
        this.decorations.clear();
        this.spawns.clear();
        this.mapNames.clear();
        this.difficultyStrings.clear();
    }

    /**
     * Fills a rectangular region with a tile.
     *
     * @param x1 Top-left X
     * @param y1 Top-left Y
     * @param x2 Bottom-right X
     * @param y2 Bottom-right Y
     * @param tile The tile to fill
     */
    public void fill(int x1, int y1, int x2, int y2, String tile) {
        int width = getMapWidth();
        int height = getMapHeight();
        
        for (int y = y1; y <= y2 && y < height; y++) {
            for (int x = x1; x <= x2 && x < width && x >= 0; x++) {
                this.tileMap[x] = tile;
            }
        }
    }

    /**
     * Creates a simple blank map of given dimensions.
     *
     * @param width  Map width
     * @param height Map height
     * @return A new MapEditor with a blank map
     */
    public static MapEditor createBlankMap(int width, int height) {
        MapEditor editor = new MapEditor();
        editor.tileMap = new String[width];
        editor.mapHeight = height;
        for (int i = 0; i < width; i++) {
            editor.tileMap[i] = TILE_EMPTY;
        }
        return editor;
    }

    /**
     * Creates a simple test map.
     *
     * @return A new MapEditor with a test map
     */
    public static MapEditor createTestMap() {
        MapEditor editor = createBlankMap(16, 20);
        // Create a simple enclosed room
        editor.fill(0, 0, 15, 10, TILE_WALL);
        editor.fill(1, 1, 14, 9, TILE_EMPTY);
        editor.setTile(1, 1, TILE_PLAYER);
        return editor;
    }

    /**
     * Gets the current map as a string representation.
     *
     * @return The map as a string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int width = getMapWidth();
        int height = getMapHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(getTile(x, y));
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Gets the decorations in the map.
     *
     * @return A list of decorations
     */
    public List<MapDecoration> getDecorations() {
        return new ArrayList<>(this.decorations);
    }

    /**
     * Gets the spawns in the map.
     *
     * @return A list of spawns
     */
    public List<EntitySpawn> getSpawns() {
        return new ArrayList<>(this.spawns);
    }

}
