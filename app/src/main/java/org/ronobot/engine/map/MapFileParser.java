package org.ronobot.engine.map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * MapFileParser provides functionality for parsing map files into GameMap instances.
 * <p>
 * This class supports various text-based map file formats commonly used in 2D game engines:
 * - Simple grid format (wall, floor, empty tiles)
 * - Spawn position markers
 * - Decoration and sprite placement hints
 * - Map metadata (name, difficulty, size)
 * </p>
 *
 * Supported tile characters:
 * - `#`: Wall (solid, collision)
 * - `.`: Floor (walkable)
 * - ` `: Empty floor (same as `.`)
 * - `@`: Player spawn position
 * - `*`: Enemy spawn position
 * - `P`: Power-up spawn position
 * - `/`: Ammo spawn position
 *
 * @author ronobot
 * @since 1.0
 */
public class MapFileParser {

    // ==================== Constants ==================

    /**
     * Wall tile character.
     */
    public static final char WALL = '#';

    /**
     * Floor tile character.
     */
    public static final char FLOOR = '.';

    /**
     * Empty floor tile character.
     */
    public static final char EMPTY = ' ';

    /**
     * Player spawn character.
     */
    public static final char PLAYER_SPAWN = '@';

    /**
     * Enemy spawn character.
     */
    public static final char ENEMY_SPAWN = '*';

    /**
     * Power-up spawn character.
     */
    public static final char POWERUP_SPAWN = 'P';

    /**
     * Ammo spawn character.
     */
    public static final char AMMO_SPAWN = '/';

    /**
     * Default columns (width) for a map.
     */
    private static final int DEFAULT_COLUMNS = 40;

    /**
     * Default rows (height) for a map.
     */
    private static final int DEFAULT_ROWS = 25;

    // ==================== Fields ====================

    /**
     * Name of the parsed map.
     */
    private final String name;

    /**
     * Width of the map in tiles (columns).
     */
    private final int columns;

    /**
     * Height of the map in tiles (rows).
     */
    private final int rows;

    /**
     * Grid of tiles in the map.
     */
    private final char[][] grid;

    /**
     * List of entity spawn positions (optional).
     */
    private final List<EntitySpawn> spawnPositions = new ArrayList<>();

    // ==================== Constructors ==================

    /**
     * Creates a new MapFileParser with the given name.
     *
     * @param name The map name
     */
    public MapFileParser(String name) {
        this.name = name;
        this.columns = DEFAULT_COLUMNS;
        this.rows = DEFAULT_ROWS;
        this.grid = new char[rows][columns];
        initializeGrid();
    }

    /**
     * Creates a new MapFileParser with the given name and dimensions.
     *
     * @param name  The map name
     * @param rows  The number of rows (height)
     * @param cols  The number of columns (width)
     */
    public MapFileParser(String name, int rows, int cols) {
        this.name = name;
        this.rows = Math.max(1, rows);
        this.columns = Math.max(1, cols);
        this.grid = new char[rows][columns];
        initializeGrid();
    }

    /**
     * Initializes the grid with default empty tiles.
     * <p>
     * This method fills the grid with EMPTY tiles, which can be
     * later replaced with walls or other tile types during parsing.
     * </p>
     */
    private void initializeGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                grid[row][col] = EMPTY;
            }
        }
    }

    // ==================== Methods ====================

    /**
     * Parses a map file from the given path.
     * <p>
     * This method reads a text-based map file and converts it into a
     * GameMap instance that can be used by the engine.
     * </p>
     *
     * @param path The path to the map file
     * @return A GameMap instance representing the parsed map, or null if parsing failed
     * @throws IOException If an I/O error occurs while reading the file
     */
    public static GameMap parseFile(String path) throws IOException {
        if (path == null) {
            return null;
        }

        java.nio.file.Path mapPath = Paths.get(path);
        if (!Files.exists(mapPath)) {
            throw new IOException("Map file does not exist: " + path);
        }

        String content = Files.readString(mapPath);
        return parseContent(content);
    }

    /**
     * Converts a spawn list to a GameMap.
     *
     * @param spawns The spawn positions
     * @return A new GameMap with spawns
     */
    private static GameMap fromSpawns(List<EntitySpawn> spawns) {
        GameMap map = new GameMap(40, 25, spawns);
        return map;
    }

    /**
     * Parses map content from a string.
     * <p>
     * This method accepts map content in text format and converts it
     * into a GameMap instance that includes any spawn positions found.
     * </p>
     *
     * @param content The map content as a string
     * @return A GameMap instance with spawn positions, or null if parsing failed
     */
    public static GameMap parseContent(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }

        List<String> lines = parseLines(content);
        if (lines.isEmpty()) {
            return null;
        }

        // Get dimensions from first line
        int contentCols = lines.get(0).length();
        int contentRows = lines.size();

        // Create parser with parsed dimensions
        MapFileParser parser = new MapFileParser(
                "Parsed Map",
                contentRows,
                contentCols
        );

        // Parse each line
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < Math.min(line.length(), parser.getColumns()); col++) {
                char c = line.charAt(col);
                parser.setTile(row, col, c);
            }
        }

        // Parse spawn positions
        List<EntitySpawn> spawns = parser.getSpawnPositions();

        // Convert to GameMap using static factory method
        GameMap gameMap = fromSpawns(spawns);

        // Set the grid
        gameMap.setGrid(parser.getGrid());

        return gameMap;
    }

    /**
     * Parses lines from content, preserving line widths.
     * <p>
     * This method preserves the original line width and structure without trimming.
     * Empty lines (only whitespace) are skipped to handle leading/trailing blank lines.
     * </p>
     *
     * @param content The content to parse
     * @return List of non-empty lines
     */
    private static List<String> parseLines(String content) {
        List<String> lines = new ArrayList<>();
        
        // Split on newline
        String[] allLines = content.split("\\n");
        
        for (String line : allLines) {
            // Check if line has any non-whitespace characters
            boolean hasNonWhitespace = false;
            for (int i = 0; i < line.length(); i++) {
                if (!Character.isWhitespace(line.charAt(i))) {
                    hasNonWhitespace = true;
                    break;
                }
            }
            
            // Skip truly empty lines (only whitespace)
            if (hasNonWhitespace) {
                lines.add(line);
            }
        }
        
        return lines;
    }

    /**
     * Sets a tile at the given position.
     * <p>
     * This method validates the tile character and handles special spawn characters.
     * </p>
     *
     * @param row The row index
     * @param col The column index
     * @param tile The tile character
     */
    public void setTile(int row, int col, char tile) {
        if (row < 0 || row >= rows || col < 0 || col >= columns) {
            return; // Out of bounds
        }

        char normalized = getNormalizedTile(tile);
        grid[row][col] = normalized;

        // Handle spawn positions
        if (isSpawnCharacter(normalized)) {
            handleSpawnPosition(row, col, normalized);
        }
    }

    /**
     * Gets a tile at the given position.
     *
     * @param row The row index
     * @param col The column index
     * @return The tile character, or EMPTY if out of bounds
     */
    public char getTile(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= columns) {
            return EMPTY;
        }
        return grid[row][col];
    }

    /**
     * Gets a normalized string representation of the grid.
     *
     * @return String representation of the grid
     */
    public String getGridString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                sb.append(grid[row][col]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Normalizes a tile character to a valid tile type.
     *
     * @param tile The tile character to normalize
     * @return The normalized tile character
     */
    private char getNormalizedTile(char tile) {
        switch (tile) {
            case '#':
                return WALL;
            case '.':
            case ' ':
                return EMPTY;
            case PLAYER_SPAWN:
            case ENEMY_SPAWN:
            case POWERUP_SPAWN:
            case AMMO_SPAWN:
                return tile; // Keep spawn markers
            default:
                // Unknown character - treat as floor
                return EMPTY;
        }
    }

    /**
     * Checks if a character is a spawn position marker.
     *
     * @param tile The tile character
     * @return true if this is a spawn marker
     */
    private boolean isSpawnCharacter(char tile) {
        return tile == PLAYER_SPAWN || tile == ENEMY_SPAWN ||
                tile == POWERUP_SPAWN || tile == AMMO_SPAWN;
    }

    /**
     * Handles a spawn position marker.
     *
     * @param row The row index
     * @param col The column index
     * @param tile The spawn character
     */
    private void handleSpawnPosition(int row, int col, char tile) {
        switch (tile) {
            case PLAYER_SPAWN:
                spawnPositions.add(new EntitySpawn(
                        "player",
                        EntitySpawn.Type.PLAYER,
                        col, row
                ));
                break;
            case ENEMY_SPAWN:
                spawnPositions.add(new EntitySpawn(
                        "enemy",
                        EntitySpawn.Type.ENEMY,
                        col, row
                ));
                break;
            case POWERUP_SPAWN:
                spawnPositions.add(new EntitySpawn(
                        "powerup",
                        EntitySpawn.Type.POWERUP,
                        col, row
                ));
                break;
            case AMMO_SPAWN:
                spawnPositions.add(new EntitySpawn(
                        "ammo",
                        EntitySpawn.Type.AMMO,
                        col, row
                ));
                break;
        }
    }

    /**
     * Gets the name of the map.
     *
     * @return The map name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the number of columns (width).
     *
     * @return The column count
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Gets the number of rows (height).
     *
     * @return The row count
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the grid of tiles.
     *
     * @return The tile grid
     */
    public char[][] getGrid() {
        return grid;
    }

    /**
     * Gets the list of spawn positions.
     *
     * @return The spawn positions list
     */
    public List<EntitySpawn> getSpawnPositions() {
        return new ArrayList<>(spawnPositions);
    }

    /**
     * Validates the map content.
     *
     * @return true if the map is valid
     */
    public boolean isValid() {
        if (rows <= 0 || columns <= 0) {
            return false;
        }

        // Check for player spawn
        boolean hasPlayerSpawn = false;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                char tile = grid[row][col];
                if (tile == WALL) {
                    continue; // Valid wall
                }
                hasPlayerSpawn = hasPlayerSpawn || tile == PLAYER_SPAWN;
            }
        }

        // At least one player spawn is required
        return hasPlayerSpawn;
    }

    /**
     * Gets a string representation of the map.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MapFileParser{name=").append(name)
                .append(", rows=").append(rows)
                .append(", cols=").append(columns)
                .append(", isValid=").append(isValid())
                .append('}');

        // Append grid preview
        sb.append("\nGrid Preview:");
        for (int row = 0; row < Math.min(rows, 5); row++) {
            for (int col = 0; col < Math.min(columns, 20); col++) {
                sb.append(grid[row][col]);
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Entity spawn position data structure.
     */
    public static class EntitySpawn {

        /**
         * Entity type name.
         */
        private final String typeName;

        /**
         * Spawn entity type.
         */
        private final Type type;

        /**
         * Column position.
         */
        private final int col;

        /**
         * Row position.
         */
        private final int row;

        /**
         * Creates a new EntitySpawn.
         *
         * @param typeName The entity type name
         * @param type The spawn type
         * @param col The column position
         * @param row The row position
         */
        EntitySpawn(String typeName, Type type, int col, int row) {
            this.typeName = typeName;
            this.type = type;
            this.col = col;
            this.row = row;
        }

        /**
         * Gets the type name.
         *
         * @return The type name
         */
        public String getTypeName() {
            return typeName;
        }

        /**
         * Gets the spawn type.
         *
         * @return The spawn type
         */
        public Type getType() {
            return type;
        }

        /**
         * Gets the column position.
         *
         * @return The column position
         */
        public int getCol() {
            return col;
        }

        /**
         * Gets the row position.
         *
         * @return The row position
         */
        public int getRow() {
            return row;
        }

        /**
         * Gets a string representation.
         *
         * @return String representation
         */
        @Override
        public String toString() {
            return "EntitySpawn{typeName='" + typeName + '\'' +
                    ", type=" + type +
                    ", col=" + col +
                    ", row=" + row +
                    '}';
        }

        /**
         * Spawn entity types.
         */
        public enum Type {
            /**
             * Player spawn.
             */
            PLAYER("player"),

            /**
             * Enemy spawn.
             */
            ENEMY("enemy"),

            /**
             * Power-up spawn.
             */
            POWERUP("powerup"),

            /**
             * Ammo spawn.
             */
            AMMO("ammo");

            /**
             * Type description.
             */
            private final String description;

            /**
             * Creates a new Type.
             *
             * @param description The description
             */
            Type(String description) {
                this.description = description;
            }

            /**
             * Gets the description.
             *
             * @return The description
             */
            public String getDescription() {
                return description;
            }
        }
    }
}
