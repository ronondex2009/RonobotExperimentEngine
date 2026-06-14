package org.ronobot.engine.map;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.entity.EnemyEntity;
import org.ronobot.engine.entity.EnemyType;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.map.MapFileParser.EntitySpawn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * LevelLoader provides comprehensive level loading functionality for the game engine.
 * <p>
 * This class extends MapFileParser capabilities to handle complete level loading
 * including entity spawning, decoration registration, and metadata extraction.
 * It supports loading map files from disk and converting them into fully
 * initialized GameMap instances ready for gameplay.
 *
 * Supported map features:
 * - Grid-based tile maps (walls, floors, empty spaces)
 * - Entity spawn positions (players, enemies, power-ups, ammo)
 * - Map decorations (statues, pictures, tables, etc.)
 * - Map metadata (name, difficulty, spawn points)
 * - Level metadata (metadata storage and retrieval)
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class LevelLoader {

    // ==================== Constants ==================

    /**
     * Default difficulty for loaded levels.
     */
    public static final String DEFAULT_DIFFICULTY = "normal";

    /**
     * Prefix for default map names.
     */
    public static final String DEFAULT_MAP_NAME_PREFIX = "level";

    /**
     * Wall tile ID.
     */
    public static final int TILE_WALL = 1;

    /**
     * Floor tile ID.
     */
    public static final int TILE_FLOOR = 0;

    /**
     * Empty map ID.
     */
    public static final int TILE_EMPTY = -1;

    /**
     * Door tile ID.
     */
    public static final int TILE_DOOR = 2;

    // ==================== Fields ====================

    /**
     * Spawn registry for entity types.
     */
    private final Map<String, EntitySpawn> spawnRegistry = new HashMap<>();

    /**
     * Metadata stored for each level.
     */
    private final Map<String, String> levelMetadata = new HashMap<>();

    // ==================== Methods ====================

    /**
     * Loads a complete level from the given file path.
     * <p>
     * This method parses the map file, extracts entity spawn positions,
     * and returns a fully initialized GameMap ready for gameplay.
     * Decorations are also loaded from the map content if present.
     * </p>
     *
     * @param path The path to the map file
     * @return A loaded GameMap, or null if loading failed
     * @throws IOException If an I/O error occurs while reading the file
     */
    public GameMap loadLevel(String path) throws IOException {
        if (path == null) {
            throw new IOException("Map path cannot be null");
        }

        java.nio.file.Path mapPath = Paths.get(path);
        if (!Files.exists(mapPath)) {
            throw new IOException("Map file does not exist: " + path);
        }

        String content = Files.readString(mapPath);
        return loadFromContent(content, path);
    }

    /**
     * Loads a level from string content.
     *
     * @param content The map content as a string
     * @param sourcePath The source file path (for metadata)
     * @return A loaded GameMap, or null if loading failed
     */
    public GameMap loadFromContent(String content, String sourcePath) {
        if (content == null || content.isEmpty()) {
            return null;
        }

        // Get dimensions from first line
        int contentCols = content.lines().findFirst().map(String::length).orElse(40);
        int contentRows = (int) content.lines().count();

        // Extract map name from source path
        String mapName = extractMapName(sourcePath);

        // Parse using MapFileParser
        MapFileParser parser = new MapFileParser(mapName, contentRows, contentCols);
        
        // Extract metadata first before parsing grid
        extractMetadata(content);

        // If no name was found in comments, use the filename-based name
        if (!levelMetadata.containsKey("name")) {
            levelMetadata.put("name", mapName);
        }

        // Parse each line
        for (int row = 0; row < contentRows; row++) {
            String line = content.lines().skip(row).findFirst().orElse("");
            for (int col = 0; col < Math.min(line.length(), parser.getColumns()); col++) {
                char ch = line.charAt(col);
                // Set tiles for valid characters: #=wall, .=floor, @/=*/P/player, *=enemy, space=floor
                if (ch == '#' || ch == '.' || ch == '@' || ch == ' ' || ch == '*' || ch == 'P' || ch == '/') {
                    parser.setTile(row, col, ch);
                }
            }
        }

        // Convert parser to GameMap
        GameMap gameMap = new GameMap(parser.getColumns(), parser.getRows());

        // Copy the tile grid from parser to gameMap, converting characters to tile IDs
        char[][] parserGrid = parser.getGrid();
        for (int row = 0; row < parserGrid.length; row++) {
            for (int col = 0; col < parserGrid[row].length; col++) {
                char ch = parserGrid[row][col];
                if (ch == '#') {
                    gameMap.setTile(col, row, GameMap.TILE_WALL);
                } else if (ch == '.' || ch == ' ') {
                    // Spaces and dots represent empty floor
                    gameMap.setTile(col, row, GameMap.TILE_EMPTY);
                } else if (ch == '@' || ch == '*' || ch == 'P' || ch == '/') {
                    // Keep spawn markers - they'll be replaced by spawned entities
                    gameMap.setTile(col, row, GameMap.TILE_EMPTY);
                }
            }
        }

        // Convert spawn positions to registry and spawn entities
        for (MapFileParser.EntitySpawn spawn : parser.getSpawnPositions()) {
            EntitySpawn entitySpawn = new EntitySpawn(
                    spawn.getTypeName(),
                    spawn.getType(),
                    spawn.getCol(),
                    spawn.getRow()
            );
            spawnRegistry.put(spawn.getTypeName(), entitySpawn);

            // Convert spawn position to world coordinates
            Position pos = gameMap.getWorldTilePosition(spawn.getCol(), spawn.getRow());
            // Store spawn position in metadata for retrieval
            levelMetadata.put(spawn.getTypeName(), "" + spawn.getCol() + "," + spawn.getRow());
            
            if (pos != null) {
                switch (spawn.getType()) {
                    case PLAYER:
                        spawnPlayerAt(gameMap, pos.getX(), pos.getY());
                        break;
                    case ENEMY:
                        spawnEnemyAt(gameMap, pos.getX(), pos.getY());
                        break;
                    case POWERUP:
                        spawnPowerupAt(gameMap, pos.getX(), pos.getY());
                        break;
                    case AMMO:
                        spawnAmmoAt(gameMap, pos.getX(), pos.getY());
                        break;
                }
            }
        }

        // Extract decorations from map content
        extractDecorations(content, gameMap);

        gameMap.load();

        return gameMap;
    }

    /**
     * Extracts the map name from the source path or comments.
     *
     * @param path The source file path
     * @return The extracted map name
     */
    private String extractMapName(String path) {
        // Extract from file path - use filename without extension as default
        String fileName = Paths.get(path).getFileName().toString();
        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }

        // Use the filename without extension as map name
        String pathName = fileName != null && !fileName.isEmpty() ? fileName : DEFAULT_MAP_NAME_PREFIX + "unknown";
        
        // Check for name in metadata first - prefer metadata over path name
        String metadataName = levelMetadata.get("name");
        if (metadataName != null && !metadataName.isEmpty()) {
            return metadataName;
        }
        
        // If no metadata, return path-based name
        // If filename contains special characters (underscores, hyphens, numbers), keep them
        // This is useful for map names like "level_01" -> "level_01"
        return pathName;
    }

    /**
     * Extracts difficulty from map comments.
     *
     * @param path The source file path
     * @return The difficulty level
     */
    private String getDifficulty(String path) {
        // Check for difficulty in metadata first
        String difficulty = levelMetadata.get("difficulty");
        if (difficulty != null && !difficulty.isEmpty()) {
            return difficulty;
        }

        // Default to easy for now
        return DEFAULT_DIFFICULTY;
    }

    /**
     * Extracts metadata from map content.
     *
     * @param content The map content
     */
    private void extractMetadata(String content) {
        // Look for metadata comments
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                String contentPart = line.substring(line.indexOf('#') + 1).trim();
                if (contentPart.startsWith("name=")) {
                    levelMetadata.put("name", contentPart.substring(5));
                } else if (contentPart.startsWith("difficulty=")) {
                    levelMetadata.put("difficulty", contentPart.substring(11));
                } else if (contentPart.startsWith("author=")) {
                    levelMetadata.put("author", contentPart.substring(7));
                }
            }
        }
    }

    /**
     * Extracts decorations from the map content.
     *
     * @param content The map content
     * @param gameMap The game map to add decorations to
     */
    private void extractDecorations(String content, GameMap gameMap) {
        // Look for decoration markers in comments
        // Example: #decor=chest @30,10 or #decor=statue @15,5
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                // Look for decoration comments
                String contentPart = line.substring(line.indexOf('#') + 1).trim();
                if (contentPart.startsWith("decor=")) {
                    String decorationSpec = contentPart.substring(6);
                    parseDecorationSpec(gameMap, decorationSpec);
                }
            }
        }
    }

    /**
     * Parses a decoration specification.
     *
     * @param gameMap The game map
     * @param spec The decoration specification string
     */
    private void parseDecorationSpec(GameMap gameMap, String spec) {
        try {
            String[] parts = spec.split("@");
            if (parts.length == 2) {
                String decorationName = parts[0].trim();
                String[] coords = parts[1].split(",");
                if (coords.length == 2) {
                    int x = Integer.parseInt(coords[0].trim());
                    int y = Integer.parseInt(coords[1].trim());
                    String typeName = convertToDecorationType(decorationName);
                    gameMap.addDecoration((float) x, (float) y, typeName);
                }
            }
        } catch (NumberFormatException e) {
            // Ignore invalid coordinates
        }
    }

    /**
     * Converts a decoration name to DecorationType enum.
     *
     * @param name The decoration name
     * @return The decoration type name
     */
    private String convertToDecorationType(String name) {
        String upperName = name.toUpperCase();
        if ("STATUE".equals(upperName)) {
            return "STATUE";
        } else if ("PICTURE".equals(upperName)) {
            return "PICTURE";
        } else if ("TABLE".equals(upperName)) {
            return "TABLE";
        } else if ("CHEST".equals(upperName)) {
            return "CHEST";
        } else if ("CRATE".equals(upperName)) {
            return "CRATE";
        } else if ("FLAG".equals(upperName)) {
            return "FLAG";
        } else if ("FOUNTAIN".equals(upperName)) {
            return "FOUNTAIN";
        }
        return name;
    }

    /**
     * Spawns a player entity at the given position.
     *
     * @param gameMap The game map
     * @param x The world x position
     * @param y The world y position
     */
    private void spawnPlayerAt(GameMap gameMap, float x, float y) {
        // Convert world position to tile position
        Position pos = gameMap.toTilePosition(x, y);
        if (pos == null) {
            return;
        }
        PlayerEntity player = new PlayerEntity(1, x, y);
        gameMap.spawnEntity((int) pos.getX(), (int) pos.getY(), player);
    }

    /**
     * Spawns an enemy entity at the given position.
     *
     * @param gameMap The game map
     * @param x The world x position
     * @param y The world y position
     */
    private void spawnEnemyAt(GameMap gameMap, float x, float y) {
        // Convert world position to tile position
        Position pos = gameMap.toTilePosition(x, y);
        if (pos == null) {
            return;
        }
        // Create default zombie enemy
        EnemyEntity enemy = new EnemyEntity(100, x, y, 100, 32);
        // Default to zombie type
        enemy.setType(EnemyType.ZOMBIE);
        // Spawn entity using GameMap.spawnEntity(int x, int y, Entity entity)
        gameMap.spawnEntity((int) pos.getX(), (int) pos.getY(), enemy);
    }

    /**
     * Spawns a power-up entity at the given position.
     *
     * @param gameMap The game map
     * @param x The world x position
     * @param y The world y position
     */
    private void spawnPowerupAt(GameMap gameMap, float x, float y) {
        // For now, just register the spawn position
        // Power-up spawning will be implemented in a future cycle
        // This is a placeholder
        // TODO: Implement proper powerup entity spawning
    }

    /**
     * Spawns an ammo entity at the given position.
     *
     * @param gameMap The game map
     * @param x The world x position
     * @param y The world y position
     */
    private void spawnAmmoAt(GameMap gameMap, float x, float y) {
        // For now, just register the spawn position
        // Ammo spawning will be implemented in a future cycle
        // This is a placeholder
        // TODO: Implement proper ammo entity spawning
    }

    /**
     * Gets the level metadata.
     *
     * @return Unmodifiable map of level metadata
     */
    public Map<String, String> getLevelMetadata() {
        return new HashMap<>(levelMetadata);
    }

    /**
     * Gets spawn position by entity type.
     *
     * @param typeName The entity type name
     * @return The spawn position, or null if not found
     */
    public EntitySpawn getSpawnPosition(String typeName) {
        if (typeName == null) {
            return null;
        }
        return spawnRegistry.get(typeName);
    }

    /**
     * Gets the difficulty for the loaded level.
     *
     * @return The difficulty, or DEFAULT_DIFFICULTY
     */
    public String getDifficulty() {
        return levelMetadata.getOrDefault("difficulty", DEFAULT_DIFFICULTY);
    }

    /**
     * Sets the difficulty for future loaded levels.
     *
     * @param difficulty The difficulty level
     */
    public void setDifficulty(String difficulty) {
        levelMetadata.put("difficulty", difficulty);
    }

    /**
     * Gets the map name for the loaded level.
     *
     * @return The map name
     */
    public String getMapName() {
        return levelMetadata.getOrDefault("name", DEFAULT_MAP_NAME_PREFIX + "unknown");
    }

    /**
     * Validates the loaded level.
     *
     * @return true if the level is valid
     */
    public boolean isLevelValid() {
        return spawnRegistry.size() > 0 || levelMetadata.containsKey("name");
    }

    /**
     * Clears level metadata and spawn registry.
     */
    public void clear() {
        levelMetadata.clear();
        spawnRegistry.clear();
    }

    /**
     * Registers a spawn position for later use.
     *
     * @param typeName The entity type name
     * @param spawn The spawn position
     */
    public void registerSpawn(String typeName, EntitySpawn spawn) {
        if (typeName != null && spawn != null) {
            spawnRegistry.put(typeName, spawn);
        }
    }

    /**
     * Gets a string representation of the level loader.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "LevelLoader{levelMetadata=" + levelMetadata.size() +
                ", spawns=" + spawnRegistry.size() +
                ", difficulty=" + levelMetadata.getOrDefault("difficulty", "N/A") +
                '}';
    }

    /**
     * Entity spawn data structure.
     */
    public static class EntitySpawn {

        /**
         * Entity type name.
         */
        private final String typeName;

        /**
         * Spawn entity type.
         */
        private final MapFileParser.EntitySpawn.Type type;

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
        EntitySpawn(String typeName, MapFileParser.EntitySpawn.Type type, int col, int row) {
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
        public MapFileParser.EntitySpawn.Type getType() {
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
    }
}
