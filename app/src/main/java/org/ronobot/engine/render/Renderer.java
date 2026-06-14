package org.ronobot.engine.render;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.ronobot.engine.map.GameMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Renderer provides game rendering functionality including sprite display
 * and texture management for a DOOM-like engine.
 * <p>
 * This class handles rendering of game tiles, entities, projectiles,
 * and UI elements. It supports basic 2D rendering with sprite management
 * and texture caching.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class Renderer {

    /**
     * Tile width in pixels.
     */
    private static final int TILE_WIDTH = 32;

    /**
     * Tile height in pixels.
     */
    private static final int TILE_HEIGHT = 32;

    /**
     * Render buffer width in pixels.
     */
    private static final int BUFFER_WIDTH = 640;

    /**
     * Render buffer height in pixels.
     */
    private static final int BUFFER_HEIGHT = 480;

    /**
     * Texture cache for loaded sprites.
     */
    protected final Map<String, String> textures;

    /**
     * Screen dimensions.
     */
    private final int screenWidth = BUFFER_WIDTH;

    /**
     * Screen dimensions.
     */
    private final int screenHeight = BUFFER_HEIGHT;

    /**
     * Creates a new Renderer with an empty texture cache.
     */
    public Renderer() {
        this.textures = new HashMap<>();
    }

    /**
     * Gets the screen width.
     *
     * @return The screen width in pixels
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * Gets the screen height.
     *
     * @return The screen height in pixels
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Renders the game state including map tiles, entities, and projectiles.
     *
     * @param game The game to render
     */
    public void render(Game game) {
        if (game == null) {
            return;
        }

        GameMap map = game.getMap();
        if (map == null || !map.isLoaded()) {
            return;
        }

        // Render map tiles
        renderMap(map);

        // Render entities
        renderEntities(game);

        // Render projectiles
        renderProjectiles(game);
    }

    /**
     * Renders the map tiles.
     *
     * @param map The game map
     */
    private void renderMap(GameMap map) {
        if (map == null) {
            return;
        }

        // Render each tile in the map
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int tileType = map.getTile(x, y);
                if (tileType != GameMap.TILE_EMPTY) {
                    renderTile(x, y, tileType);
                }
            }
        }
    }

    /**
     * Renders a single tile.
     *
     * @param x     The tile x coordinate
     * @param y     The tile y coordinate
     * @param type  The tile type
     */
    private void renderTile(int x, int y, int type) {
        String key = "tile_" + type;
        if (!textures.containsKey(key)) {
            String texturePath = switch (type) {
                case GameMap.TILE_WALL -> "texture_wall.png";
                case GameMap.TILE_FLOOR -> "texture_floor.png";
                case GameMap.TILE_DOOR -> "texture_door.png";
                default -> "default_tile.png";
            };
            textures.put(key, texturePath);
        }
        // In a full implementation, this would draw the sprite to the buffer
        // For now, we're just tracking what needs to be rendered
    }

    /**
     * Renders all entities in the game.
     *
     * @param game The game containing entities
     */
    private void renderEntities(Game game) {
        if (game == null) {
            return;
        }

        // Render each entity
        for (Entity entity : game.getEntityManager().getEntities()) {
            renderEntity(entity);
        }
    }

    /**
     * Renders a single entity.
     *
     * @param entity The entity to render
     */
    private void renderEntity(Entity entity) {
        if (entity == null) {
            return;
        }

        // Get entity texture/key
        String entityKey = "entity_" + entity.getId();
        if (!textures.containsKey(entityKey)) {
            textures.put(entityKey, "player_default");
        }

        // Render entity at position with its size
        Position pos = entity.getPosition();
        Size size = entity.getSize();
        if (pos != null && size != null) {
            // In a full implementation, draw entity sprite at position
        }
    }

    /**
     * Renders all projectiles in the game.
     *
     * @param game The game containing projectiles
     */
    private void renderProjectiles(Game game) {
        if (game == null) {
            return;
        }

        // Render each projectile
        for (Entity entity : game.getEntityManager().getEntities()) {
            if (entity instanceof Projectile projectile) {
                renderProjectile(projectile);
            }
        }
    }

    /**
     * Renders a single projectile.
     *
     * @param projectile The projectile to render
     */
    private void renderProjectile(Projectile projectile) {
        if (projectile == null) {
            return;
        }

        // Get projectile texture/key
        String projectileKey = "projectile_" + projectile.getId();
        if (!textures.containsKey(projectileKey)) {
            textures.put(projectileKey, "projectile_default");
        }

        // Render projectile at position
        Position pos = projectile.getPosition();
        if (pos != null) {
            // In a full implementation, draw projectile sprite at position
        }
    }

    /**
     * Loads a texture/sprite into the texture cache.
     *
     * @param name  The texture name
     * @param path  The texture path (stub for future file loading)
     * @return true if texture loaded successfully
     */
    public boolean loadTexture(String name, String path) {
        // Stub implementation - in full version would load texture data
        if (name == null || path == null) {
            return false;
        }
        textures.put(name, path);
        return true;
    }

    /**
     * Loads a texture by type ID (convenience method for map tiles).
     *
     * @param tileType The tile type to load
     * @return true if texture loaded successfully
     */
    public boolean loadTexture(int tileType) {
        // Map tile types to texture paths
        String path = switch (tileType) {
            case GameMap.TILE_WALL -> "texture_wall.png";
            case GameMap.TILE_FLOOR -> "texture_floor.png";
            case GameMap.TILE_DOOR -> "texture_door.png";
            default -> null;
        };
        if (path != null) {
            return loadTexture("tile_type_" + tileType, path);
        }
        return true; // Accept any tile type for now
    }

    /**
     * Removes a texture from the cache by name.
     *
     * @param name The texture name to remove
     * @return true if the texture was removed, false if it didn't exist
     */
    public boolean removeTexture(String name) {
        return textures.remove(name) != null;
    }

    /**
     * Gets a texture by name.
     *
     * @param name The texture name
     * @return The texture path, or null if not found
     */
    public String getTexture(String name) {
        return textures.get(name);
    }

    /**
     * Gets the number of textures currently loaded.
     *
     * @return The number of loaded textures
     */
    public int getTextureCount() {
        return textures.size();
    }

    /**
     * Clears all textures from the cache.
     */
    public void clearTextures() {
        textures.clear();
    }

    /**
     * Checks if a texture with the given name exists in the cache.
     *
     * @param name The texture name to check
     * @return true if the texture exists, false otherwise
     */
    public boolean hasTexture(String name) {
        return textures.containsKey(name);
    }

    /**
     * Gets the texture cache map (package-private access).
     *
     * @return The textures map
     */
    protected Map<String, String> getTextures() {
        return textures;
    }
}
