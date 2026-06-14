package org.ronobot.engine.map;

import org.ronobot.engine.collision.CollisionManager;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Rectangle;
import org.ronobot.engine.math.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameMap represents a level with tile-based storage and entity bounds enforcement.
 * <p>
 * The GameMap class provides a grid-based representation of game levels with
 * tile data for walls, floors, and decorative elements. It also enforces
 * boundaries and supports entity spawning within map bounds.
 * </p>
 * 
 * @author ronobot
 * @since 1.0
 */
public class GameMap {

    /**
     * Tile width in pixels.
     */
    private static final int TILE_WIDTH = 32;

    /**
     * Tile height in pixels.
     */
    private static final int TILE_HEIGHT = 32;

    /**
     * Wall tile ID.
     */
    public static final int TILE_WALL = 1;

    /**
     * Floor tile ID.
     */
    public static final int TILE_FLOOR = 0;

    /**
     * Door tile ID.
     */
    public static final int TILE_DOOR = 2;

    /**
     * Empty map ID.
     */
    public static final int TILE_EMPTY = -1;

    /**
     * Tile data grid.
     */
    private final int[][][] tiles; // [x][y][z] - supports 3D maps

    /**
     * Map dimensions (width x height).
     */
    private final int width;

    /**
     * Map dimensions (height).
     */
    private final int height;

    /**
     * Whether the map is loaded.
     */
    private boolean loaded = false;

    /**
     * Whether the map is enabled.
     */
    private boolean enabled = true;

    /**
     * Entity spawned at this position (cached).
     */
    private final Map<Position, Entity> spawnedEntities;

    /**
     * Entity spawned at this position (cached).
     */
    private final Map<Position, Projectile> spawnedProjectiles;

    /**
     * Tile texture cache (future).
     */
    private final Map<Integer, String> tileTextures;

    /**
     * Tile metadata cache (future).
     */
    private final Map<Integer, String> tileMetadata;

    /**
     * Decoration data by position (x, y) -> decoration type.
     */
    private final Map<Position, DecorationType> decorations;

    /**
     * Decoration types.
     */
    public enum DecorationType {
        /**
         * None - no decoration.
         */
        NONE,

        /**
         * Statue decoration.
         */
        STATUE,

        /**
         * Picture decoration.
         */
        PICTURE,

        /**
         * Table decoration.
         */
        TABLE,

        /**
         * Chest decoration.
         */
        CHEST,

        /**
         * Crate decoration.
         */
        CRATE,

        /**
         * Flag decoration.
         */
        FLAG,

        /**
         * Fountain decoration.
         */
        FOUNTAIN
    }

    /**
     * Creates a new GameMap with specified dimensions, filled with floor tiles.
     *
     * @param width  The map width in tiles
     * @param height The map height in tiles
     */
    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new int[width][height][1];
        this.spawnedEntities = new HashMap<>();
        this.spawnedProjectiles = new HashMap<>();
        this.tileTextures = new HashMap<>();
        this.tileMetadata = new HashMap<>();
        this.decorations = new HashMap<>();

        // Initialize all tiles as floor
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.tiles[x][y][0] = TILE_FLOOR;
            }
        }
    }

    /**
     * Sets the decoration texture for a tile type.
     *
     * @param tileType The tile type
     * @param texture  The texture path
     */
    public void setDecorationTexture(int tileType, String texture) {
        tileTextures.put(tileType, texture);
    }

    /**
     * Gets the decoration texture for a tile type.
     *
     * @param tileType The tile type
     * @return The texture path, or null if not found
     */
    public String getDecorationTexture(int tileType) {
        return tileTextures.get(tileType);
    }

    /**
     * Gets the map width in tiles.
     *
     * @return The map width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the map height in tiles.
     *
     * @return The map height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the tile at the specified position.
     *
     * @param x The x coordinate (tile index)
     * @param y The y coordinate (tile index)
     * @return The tile ID, or TILE_EMPTY if out of bounds
     */
    public int getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return TILE_EMPTY;
        }
        return tiles[x][y][0];
    }

    /**
     * Sets a tile at the specified position.
     *
     * @param x    The x coordinate (tile index)
     * @param y    The y coordinate (tile index)
     * @param tile The tile ID
     * @return true if the tile was changed
     */
    public boolean setTile(int x, int y, int tile) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        int previous = tiles[x][y][0];
        tiles[x][y][0] = tile;
        return previous != tile;
    }

    /**
     * Gets the pixel width of a tile.
     *
     * @return The tile width in pixels
     */
    public static int getTileWidth() {
        return TILE_WIDTH;
    }

    /**
     * Gets the pixel height of a tile.
     *
     * @return The tile height in pixels
     */
    public static int getTileHeight() {
        return TILE_HEIGHT;
    }

    /**
     * Gets the world position for a tile coordinate.
     *
     * @param x The tile x coordinate
     * @param y The tile y coordinate
     * @return The Position object representing the tile's world position
     */
    public Position toWorldPosition(int x, int y) {
        return new Position(x * TILE_WIDTH, y * TILE_HEIGHT);
    }

    /**
     * Gets the tile coordinate for a world position.
     *
     * @param x The world x coordinate
     * @param y The world y coordinate
     * @return The tile position, or null if out of bounds
     */
    public Position toTilePosition(float x, float y) {
        int tx = (int) Math.floor(x / TILE_WIDTH);
        int ty = (int) Math.floor(y / TILE_HEIGHT);
        if (tx < 0 || tx >= width || ty < 0 || ty >= height) {
            return null;
        }
        return new Position(tx, ty);
    }

    /**
     * Checks if a position is within the map boundaries.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return true if within bounds
     */
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Checks if a world position is within the map boundaries.
     *
     * @param x The world x coordinate
     * @param y The world y coordinate
     * @return true if within bounds
     */
    public boolean isInBounds(float x, float y) {
        return x >= 0 && x < width * TILE_WIDTH && y >= 0 && y < height * TILE_HEIGHT;
    }

    /**
     * Creates a bounding box for a tile at the specified position.
     *
     * @param x The tile x coordinate
     * @param y The tile y coordinate
     * @return The collision rectangle for the tile
     */
    public Rectangle getTileCollisionBox(int x, int y) {
        Position pos = toWorldPosition(x, y);
        Size size = new Size(TILE_WIDTH, TILE_HEIGHT);
        return Rectangle.of(pos, size);
    }

    /**
     * Adds a wall at the specified position.
     *
     * @param x The x coordinate (tile index)
     * @param y The y coordinate (tile index)
     */
    public void addWall(int x, int y) {
        if (isInBounds(x, y)) {
            setTile(x, y, TILE_WALL);
        }
    }

    /**
     * Adds a floor at the specified position.
     *
     * @param x The x coordinate (tile index)
     * @param y The y coordinate (tile index)
     */
    public void addFloor(int x, int y) {
        if (isInBounds(x, y)) {
            setTile(x, y, TILE_FLOOR);
        }
    }

    /**
     * Adds a door at the specified position.
     *
     * @param x The x coordinate (tile index)
     * @param y The y coordinate (tile index)
     */
    public void addDoor(int x, int y) {
        if (isInBounds(x, y)) {
            setTile(x, y, TILE_DOOR);
        }
    }

    /**
     * Creates a simple arena map with walls around the perimeter.
     * <p>
     * Walls are placed along the edges of the map to create a contained arena.
     * The player spawns at (1, 1) and can move around within the walls.
     * </p>
     */
    public static GameMap createArenaMap(int width, int height) {
        GameMap map = new GameMap(width, height);

        // Add perimeter walls at all edges
        for (int x = 0; x < width; x++) {
            map.addWall(x, 0);
            map.addWall(x, height - 1);
        }
        for (int y = 0; y < height; y++) {
            map.addWall(0, y);
            map.addWall(width - 1, y);
        }

        return map;
    }

    /**
     * Creates a room layout map.
     *
     * @param width  The total map width in tiles
     * @param height The total map height in tiles
     * @param rooms  List of room definitions [cx, cy, rw, rh]
     * @return The created map
     */
    public static GameMap createRoomMap(int width, int height, List<int[]> rooms) {
        GameMap map = new GameMap(width, height);

        // Create floor
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map.addFloor(x, y);
            }
        }

        // Create rooms (empty areas)
        if (rooms != null) {
            for (int[] room : rooms) {
                if (room != null && room.length >= 4) {
                    int cx = room[0];
                    int cy = room[1];
                    int rw = room[2];
                    int rh = room[3];

                    for (int rx = cx; rx < cx + rw && rx < width; rx++) {
                        for (int ry = cy; ry < cy + rh && ry < height; ry++) {
                            map.addFloor(rx, ry);
                        }
                    }
                }
            }
        }

        return map;
    }

    /**
     * Gets the entity spawned at the specified position.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The spawned entity, or null if none
     */
    public Entity getSpawnedEntity(int x, int y) {
        Position pos = toTilePosition((float) x, (float) y);
        if (pos != null) {
            return spawnedEntities.get(pos);
        }
        return null;
    }

    /**
     * Gets the spawned projectile at the specified position.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The spawned projectile, or null if none
     */
    public Projectile getSpawnedProjectile(int x, int y) {
        Position pos = toTilePosition((float) x, (float) y);
        if (pos != null) {
            return spawnedProjectiles.get(pos);
        }
        return null;
    }

    /**
     * Spawns an entity at the specified position.
     *
     * @param x     The x coordinate
     * @param y     The y coordinate
     * @param entity The entity to spawn
     * @return true if spawned successfully
     */
    public boolean spawnEntity(int x, int y, Entity entity) {
        if (entity == null || !isInBounds(x, y)) {
            return false;
        }
        Position pos = toTilePosition((float) x, (float) y);
        if (pos != null) {
            spawnedEntities.put(pos, entity);
            return true;
        }
        return false;
    }

    /**
     * Spawns a projectile at the specified position.
     *
     * @param x        The x coordinate
     * @param y        The y coordinate
     * @param projectile The projectile to spawn
     * @return true if spawned successfully
     */
    public boolean spawnProjectile(int x, int y, Projectile projectile) {
        if (projectile == null || !isInBounds(x, y)) {
            return false;
        }
        Position pos = toTilePosition((float) x, (float) y);
        if (pos != null) {
            spawnedProjectiles.put(pos, projectile);
            return true;
        }
        return false;
    }

    /**
     * Removes an entity from the map spawn cache.
     *
     * @param entity The entity to remove
     * @return true if removed
     */
    public boolean removeEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        Position pos = findEntityPosition(entity);
        if (pos != null) {
            return spawnedEntities.remove(pos) != null;
        }
        return false;
    }

    /**
     * Removes a projectile from the map spawn cache.
     *
     * @param projectile The projectile to remove
     * @return true if removed
     */
    public boolean removeProjectile(Projectile projectile) {
        if (projectile == null) {
            return false;
        }
        Position pos = findProjectilePosition(projectile);
        if (pos != null) {
            return spawnedProjectiles.remove(pos) != null;
        }
        return false;
    }

    /**
     * Finds the tile position for an entity based on its position.
     *
     * @param entity The entity
     * @return The tile position, or null if not found
     */
    private Position findEntityPosition(Entity entity) {
        Position entPos = entity.getPosition();
        if (entPos == null) {
            return null;
        }
        return toTilePosition(entPos.getX(), entPos.getY());
    }

    /**
     * Finds the tile position for a projectile based on its position.
     *
     * @param projectile The projectile
     * @return The tile position, or null if not found
     */
    private Position findProjectilePosition(Projectile projectile) {
        Position projPos = projectile.getPosition();
        if (projPos == null) {
            return null;
        }
        return toTilePosition(projPos.getX(), projPos.getY());
    }

    /**
     * Clears all spawned entities from the map.
     */
    public void clearSpawnedEntities() {
        spawnedEntities.clear();
    }

    /**
     * Clears all spawned projectiles from the map.
     */
    public void clearSpawnedProjectiles() {
        spawnedProjectiles.clear();
    }

    /**
     * Gets all spawned entities.
     *
     * @return Unmodifiable list of spawned entities
     */
    public List<Entity> getSpawnedEntities() {
        return new ArrayList<>(spawnedEntities.values());
    }

    /**
     * Gets all spawned projectiles.
     *
     * @return Unmodifiable list of spawned projectiles
     */
    public List<Projectile> getSpawnedProjectiles() {
        return new ArrayList<>(spawnedProjectiles.values());
    }

    /**
     * Checks if a tile is a wall.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return true if the tile is a wall
     */
    public boolean isWall(int x, int y) {
        return getTile(x, y) == TILE_WALL;
    }

    /**
     * Checks if a tile is a door.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return true if the tile is a door
     */
    public boolean isDoor(int x, int y) {
        return getTile(x, y) == TILE_DOOR;
    }

    /**
     * Checks if a tile is empty.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return true if the tile is empty
     */
    public boolean isEmpty(int x, int y) {
        return getTile(x, y) == TILE_EMPTY;
    }

    /**
     * Checks if a position contains a wall.
     *
     * @param x The world x coordinate
     * @param y The world y coordinate
     * @return true if there is a wall at this position
     */
    public boolean containsWall(float x, float y) {
        Position pos = toTilePosition(x, y);
        if (pos == null) {
            return false;
        }
        return isWall((int) Math.floor(pos.getX()), (int) Math.floor(pos.getY()));
    }

    /**
     * Checks if a position contains a decoration.
     *
     * @param x The world x coordinate
     * @param y The world y coordinate
     * @return true if there is a decoration at this position
     */
    public boolean containsDecoration(float x, float y) {
        Position pos = toTilePosition(x, y);
        if (pos == null) {
            return false;
        }
        return decorations.containsKey(pos);
    }

    /**
     * Adds a decorative element to the map.
     * <p>
     * Decorations are stored by their position and can be of various types.
     * Decorations do not block movement but are rendered by the renderer.
     * Decorations are stored internally as DecorationType enum values.
     * </p>
     *
     * @param x         The world x coordinate
     * @param y         The world y coordinate
     * @param decoration The decoration type
     * @return true if added successfully
     */
    public boolean addDecoration(float x, float y, String decoration) {
        Position pos = toTilePosition(x, y);
        if (pos == null || decoration == null) {
            return false;
        }
        // Convert string name to DecorationType enum
        DecorationType type = parseDecorationType(decoration);
        decorations.put(pos, type);
        return true;
    }

    /**
     * Adds a decorative element at a tile position.
     *
     * @param x    The tile x coordinate
     * @param y    The tile y coordinate
     * @param decoration The decoration type
     * @return true if added successfully
     */
    public boolean addDecoration(int x, int y, String decoration) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        if (decoration == null) {
            return false;
        }
        // Convert string name to DecorationType enum
        DecorationType type = parseDecorationType(decoration);
        decorations.put(new Position(x, y), type);
        return true;
    }

    /**
     * Adds a decorative element with DecorationType enum.
     *
     * @param x    The tile x coordinate
     * @param y    The tile y coordinate
     * @param type The decoration type
     * @return true if added successfully
     */
    public boolean addDecoration(int x, int y, DecorationType type) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        if (type == null) {
            return false;
        }
        decorations.put(new Position(x, y), type);
        return true;
    }

    /**
     * Adds a decorative element at world position with DecorationType enum.
     *
     * @param x         The world x coordinate
     * @param y         The world y coordinate
     * @param type      The decoration type
     * @return true if added successfully
     */
    public boolean addDecoration(float x, float y, DecorationType type) {
        Position pos = toTilePosition(x, y);
        if (pos == null || type == null) {
            return false;
        }
        decorations.put(pos, type);
        return true;
    }

    /**
     * Parses a decoration type name string to DecorationType enum.
     *
     * @param name The decoration type name (STATUE, PICTURE, etc.)
     * @return The DecorationType enum, or NONE if unknown
     */
    private DecorationType parseDecorationType(String name) {
        String upperName = name.toUpperCase();
        return switch (upperName) {
            case "STATUE" -> DecorationType.STATUE;
            case "PICTURE" -> DecorationType.PICTURE;
            case "TABLE" -> DecorationType.TABLE;
            case "CHEST" -> DecorationType.CHEST;
            case "CRATE" -> DecorationType.CRATE;
            case "FLAG" -> DecorationType.FLAG;
            case "FOUNTAIN" -> DecorationType.FOUNTAIN;
            default -> DecorationType.NONE;
        };
    }

    /**
     * Checks if a position contains a decoration.
     *
     * @param x The world x coordinate
     * @param y The world y coordinate
     * @return The decoration type, or null if none
     */
    public DecorationType getDecorationType(float x, float y) {
        Position pos = toTilePosition(x, y);
        if (pos != null) {
            DecorationType type = decorations.get(pos);
            return type;
        }
        return null;
    }

    /**
     * Gets the decoration at a tile position.
     *
     * @param x The tile x coordinate
     * @param y The tile y coordinate
     * @return The decoration type, or null if none
     */
    public DecorationType getDecorationType(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return decorations.get(new Position(x, y));
    }

    /**
     * Removes a decoration from the map.
     *
     * @param x The world x coordinate
     * @param y The world y coordinate
     * @return true if decoration was removed
     */
    public boolean removeDecoration(float x, float y) {
        Position pos = toTilePosition(x, y);
        if (pos != null) {
            return decorations.remove(pos) != null;
        }
        return false;
    }

    /**
     * Clears all decorations from the map.
     */
    public void clearDecorations() {
        decorations.clear();
    }

    /**
     * Gets all decoration positions.
     *
     * @return Unmodifiable list of decoration positions
     */
    public List<Position> getDecorationPositions() {
        return new ArrayList<>(decorations.keySet());
    }

    /**
     * Gets the collision manager if loaded.
     *
     * @return The collision manager, or null if not loaded
     */
    public CollisionManager getCollisionManager() {
        return loaded ? new CollisionManager() : null;
    }

    /**
     * Loads the map and initializes collision manager.
     */
    public void load() {
        if (loaded) {
            return;
        }
        loaded = true;
    }

    /**
     * Checks if the map is loaded.
     *
     * @return true if loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Checks if the map is enabled.
     *
     * @return true if enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Disables the map (prevents updates).
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * Enables the map (allows updates).
     */
    public void enable() {
        this.enabled = true;
    }

    /**
     * Gets the loaded map ID.
     *
     * @return The map ID string, or null if not loaded
     */
    public String getMapId() {
        return loaded ? "map_loaded" : null;
    }

    /**
     * Gets the world position of a tile.
     *
     * @param x The tile x coordinate
     * @param y The tile y coordinate
     * @return The world position, or null if out of bounds
     */
    public Position getWorldTilePosition(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return new Position(x * TILE_WIDTH, y * TILE_HEIGHT);
    }

    /**
     * Gets the world dimensions of the map.
     *
     * @return The world Size object
     */
    public org.ronobot.engine.math.Size getWorldSize() {
        return new org.ronobot.engine.math.Size(width * TILE_WIDTH, height * TILE_HEIGHT);
    }

    /**
     * Clears the map (removes all tiles and entities).
     */
    public void clear() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y][0] = TILE_FLOOR;
            }
        }
        spawnedEntities.clear();
        spawnedProjectiles.clear();
        decorations.clear();
        loaded = false;
    }

    /**
     * Cleans up the map and releases resources.
     */
    public void cleanup() {
        clear();
        spawnedEntities.clear();
        spawnedProjectiles.clear();
        tileTextures.clear();
        tileMetadata.clear();
        decorations.clear();
        loaded = false;
        enabled = false;
    }

    /**
     * Creates a deep copy of this map.
     *
     * @return A deep copy of the map
     */
    public GameMap copy() {
        GameMap copy = new GameMap(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                copy.tiles[x][y][0] = tiles[x][y][0];
            }
        }
        copy.spawnedEntities.putAll(spawnedEntities);
        copy.spawnedProjectiles.putAll(spawnedProjectiles);
        copy.tileTextures.putAll(tileTextures);
        copy.tileMetadata.putAll(tileMetadata);
        copy.decorations.putAll(decorations);
        return copy;
    }

    /**
     * Prints the map to string representation.
     *
     * @return String representation of the map
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameMap{").append("width=").append(width).append(", height=").append(height)
          .append(", loaded=").append(loaded).append(", decorations=").append(decorations.size())
          .append('}');
        return sb.toString();
    }

    /**
     * Saves the map to a file (stub for future implementation).
     *
     * @param path The save path
     * @return true if saved successfully
     */
    public boolean save(String path) {
        // Future implementation: save map to file
        return false;
    }

    /**
     * Loads the map from a file (stub for future implementation).
     *
     * @param path The load path
     * @return true if loaded successfully
     */
    public boolean loadFromFile(String path) {
        // Future implementation: load map from file
        return false;
    }

    /**
     * Sets the tile grid from a 2D array.
     *
     * @param grid The tile grid to set
     */
    public void setGrid(char[][] grid) {
        if (grid == null) {
            return;
        }
        for (int row = 0; row < grid.length && row < height; row++) {
            for (int col = 0; col < grid[row].length && col < width; col++) {
                char tile = grid[row][col];
                switch (tile) {
                    case '#':
                        tiles[col][row][0] = TILE_WALL;
                        break;
                    case '.':
                    case ' ':
                        tiles[col][row][0] = TILE_FLOOR;
                        break;
                    case '@':
                    case '*':
                    case 'P':
                    case '/':
                        tiles[col][row][0] = TILE_FLOOR;
                        break;
                    default:
                        tiles[col][row][0] = TILE_FLOOR;
                }
            }
        }
    }
}
