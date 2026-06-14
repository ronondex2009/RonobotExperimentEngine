package org.ronobot.engine.render;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.entity.EnemyEntity;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.entities.EntityManager;
import org.ronobot.engine.map.GameMap.DecorationType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameRenderer provides game-specific rendering functionality.
 * <p>
 * This class extends Renderer for map-specific rendering with support for:
 * - Map tiles and decorations
 * - Player and enemy entities
 * - Projectiles
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class GameRenderer extends Renderer {

    /**
     * Game map to render.
     */
    private GameMap gameMap;

    /**
     * Game renderer textures map.
     */
    private final Map<String, String> gameRendererTextures = new HashMap<>();

    /**
     * Creates a default GameRenderer with an arena map.
     */
    public GameRenderer() {
        this.gameMap = GameMap.createArenaMap(16, 16);
    }

    /**
     * Creates a GameRenderer with the given game map.
     * If null is passed, a default arena map is created.
     *
     * @param gameMap The game map to render, or null for default
     */
    public GameRenderer(GameMap gameMap) {
        if (gameMap == null) {
            this.gameMap = GameMap.createArenaMap(16, 16);
        } else {
            this.gameMap = gameMap;
        }
    }

    /**
     * Sets the game map to render.
     *
     * @param gameMap The game map to render. If null, a default arena map is created.
     */
    public void setGameMap(GameMap gameMap) {
        if (gameMap == null) {
            this.gameMap = GameMap.createArenaMap(16, 16);
        } else {
            this.gameMap = gameMap;
        }
    }

    /**
     * Gets the game map being rendered.
     *
     * @return The game map, or null if not set
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * Renders the complete game map including decorations.
     */
    public void renderMap() {
        if (gameMap == null) {
            return;
        }

        // Render all tiles
        for (int x = 0; x < gameMap.getWidth(); x++) {
            for (int y = 0; y < gameMap.getHeight(); y++) {
                int tileType = gameMap.getTile(x, y);
                if (tileType != GameMap.TILE_EMPTY) {
                    renderTile(x, y, tileType);
                }
            }
        }

        // Render decorations
        renderDecorations();
    }

    /**
     * Renders a single tile.
     *
     * @param x         Render x position
     * @param y         Render y position
     * @param tileType  The tile type to render
     */
    public void renderTile(int x, int y, int tileType) {
        if (tileType != GameMap.TILE_EMPTY) {
            // Load texture stub
            String texturePath = switch (tileType) {
                case GameMap.TILE_WALL -> "texture_wall.png";
                case GameMap.TILE_FLOOR -> "texture_floor.png";
                case GameMap.TILE_DOOR -> "texture_door.png";
                default -> null;
            };
            if (texturePath != null) {
                loadTexture("tile_" + tileType, texturePath);
            }
        }
    }

    /**
     * Renders all decorations in the map.
     * <p>
     * Decorations include statues, pictures, tables, chests, crates, flags, fountains.
     * Decorations do not block movement but are rendered for visual enhancement.
     * </p>
     * <p>If the map is null, no decorations are rendered and textures are not loaded.
     * </p>
     */
    public void renderDecorations() {
        if (gameMap == null) {
            return;
        }

        // Render each decoration position
        List<Position> decorationPositions = gameMap.getDecorationPositions();
        if (decorationPositions == null || decorationPositions.isEmpty()) {
            return;
        }
        
        for (Position pos : decorationPositions) {
            if (pos != null) {
                // Try to load decoration texture from game map
                try {
                    DecorationType decoration = gameMap.getDecorationType(
                        (int) pos.getX(), (int) pos.getY());
                    // Only load textures for non-NONE decorations
                    if (decoration != null && decoration != DecorationType.NONE) {
                        // Generate decoration texture key from the enum name
                        String decorationKey = "decoration_" + decoration.name();
                        if (!gameRendererTextures.containsKey(decorationKey)) {
                            String decorationPath = "decoration_" + decoration.name();
                            gameRendererTextures.put(decorationKey, decorationPath);
                        }
                    }
                } catch (Exception e) {
                    // Ignore decoration loading errors
                }
            }
        }
    }

    /**
     * Renders a single decoration.
     *
     * @param position The decoration position
     * @param decoration The decoration type
     */
    private void renderDecoration(Position position, DecorationType decoration) {
        if (position == null) {
            return;
        }

        // Generate decoration texture key from the enum name
        String typeName = decoration.name();
        String decorationKey = "decoration_" + typeName;
        if (!gameRendererTextures.containsKey(decorationKey)) {
            String decorationPath = "decoration_" + typeName;
            gameRendererTextures.put(decorationKey, decorationPath);
        }
    }

    /**
     * Renders all entities in the game.
     *
     * @param game The game containing entities
     */
    public void renderEntities(org.ronobot.engine.core.Game game) {
        if (game == null) {
            return;
        }

        // Try to get entities from the EntityManager
        org.ronobot.engine.entities.EntityManager entityManager = game.getEntityManager();
        if (entityManager != null) {
            List<org.ronobot.engine.core.Entity> entities = entityManager.getEntities();
            // Render any entities that exist (whether registered or not)
            if (entities != null && !entities.isEmpty()) {
                for (org.ronobot.engine.core.Entity entity : entities) {
                    renderEntity(entity);
                }
            }
        } else {
            // No EntityManager - try direct rendering if needed
            // Currently no direct entities parameter, so return early
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

        // Special handling for different entity types
        if (entity instanceof PlayerEntity player) {
            renderPlayerEntity(player);
        } else if (entity instanceof EnemyEntity enemy) {
            renderEnemyEntity(enemy);
        } else if (entity instanceof Projectile projectile) {
            renderProjectile(projectile);
        }
    }

    /**
     * Renders a player entity.
     *
     * @param player The player entity
     */
    private void renderPlayerEntity(PlayerEntity player) {
        if (player == null) {
            return;
        }

        String playerKey = "entity_player";
        if (!gameRendererTextures.containsKey(playerKey)) {
            gameRendererTextures.put(playerKey, "player_sprite.png");
        }

        // Render player at position
        Position pos = player.getPosition();
        if (pos != null) {
            // In a full implementation, draw player sprite at position
        }
    }

    /**
     * Renders an enemy entity.
     *
     * @param enemy The enemy entity
     */
    private void renderEnemyEntity(EnemyEntity enemy) {
        if (enemy == null) {
            return;
        }

        String enemyKey = "entity_enemy";
        if (!gameRendererTextures.containsKey(enemyKey)) {
            gameRendererTextures.put(enemyKey, "enemy_sprite.png");
        }

        // Render enemy at position
        Position pos = enemy.getPosition();
        if (pos != null) {
            // In a full implementation, draw enemy sprite at position
        }
    }

    /**
     * Renders a projectile.
     *
     * @param projectile The projectile to render
     */
    private void renderProjectile(Projectile projectile) {
        if (projectile == null) {
            return;
        }

        String projectileKey = "projectile_" + projectile.getId();
        if (!gameRendererTextures.containsKey(projectileKey)) {
            gameRendererTextures.put(projectileKey, "projectile_sprite.png");
        }

        // Render projectile at position
        Position pos = projectile.getPosition();
        if (pos != null) {
            // In a full implementation, draw projectile sprite at position
        }
    }

    /**
     * Clears all textures from the cache.
     */
    public void clearTextures() {
        textures.clear();
        gameRendererTextures.clear();
    }

    /**
     * Gets the number of loaded textures.
     *
     * @return The texture count
     */
    public int getTextureCount() {
        return textures.size() + gameRendererTextures.size();
    }

    /**
     * Checks if a texture exists.
     *
     * @param name The texture name
     * @return true if the texture exists
     */
    public boolean hasTexture(String name) {
        return textures.containsKey(name) || gameRendererTextures.containsKey(name);
    }

    /**
     * Checks if the map is available for rendering.
     *
     * @return true if map is available
     */
    public boolean hasMap() {
        return gameMap != null;
    }

    /**
     * Gets the game renderer textures map for access.
     *
     * @return The textures map
     */
    public Map<String, String> getTextures() {
        return gameRendererTextures;
    }

    @Override
    public String toString() {
        return "GameRenderer{" +
                "gameMap=" + (gameMap != null ? gameMap.toString() : "null") +
                ", decorations=" + (gameMap != null ? gameMap.getDecorationPositions().size() : 0) +
                ", textures=" + (gameRendererTextures != null ? gameRendererTextures.size() : 0) +
                '}';
    }
}
