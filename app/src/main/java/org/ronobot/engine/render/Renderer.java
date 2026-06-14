package org.ronobot.engine.render;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.map.GameMap.DecorationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Renderer provides game rendering functionality including sprite display
 * and texture management for a DOOM-like engine.
 * <p>
 * This class handles rendering of game tiles, entities, projectiles,
 * decorations, and UI elements. It supports basic 2D rendering with
 * sprite management and texture caching.
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
     * Screen dimensions.
     */
    public static final int BUFFER_WIDTH = 640;

    /**
     * Screen dimensions.
     */
    public static final int BUFFER_HEIGHT = 480;

    /**
     * HUD overlay height in pixels.
     */
    private static final int HUD_HEIGHT = 64;

    /**
     * HUD overlay width in pixels.
     */
    private static final int HUD_WIDTH = 320;

    /**
     * Texture cache for loaded sprites.
     */
    protected final Map<String, String> textures;

    /**
     * HUD elements list.
     */
    private final List<HUDElement> hudElements;

    /**
     * HUD container map for efficient lookups.
     */
    private final Map<String, HUDElement> hudContainer;

    /**
     * Screen dimensions.
     */
    private final int screenWidth = BUFFER_WIDTH;

    /**
     * Screen dimensions.
     */
    private final int screenHeight = BUFFER_HEIGHT;

    /**
     * Creates a new Renderer with default HUD elements.
     */
    public Renderer() {
        this.textures = new HashMap<>();
        this.hudElements = new ArrayList<>();
        this.hudContainer = new HashMap<>();
        initializeHUD();
    }

    /**
     * Creates a new Renderer with empty texture cache and HUD.
     */
    public Renderer(boolean createDefaultHUD) {
        if (createDefaultHUD) {
            this.textures = new HashMap<>();
            this.hudElements = new ArrayList<>();
            this.hudContainer = new HashMap<>();
            initializeHUD();
        } else {
            this.textures = new HashMap<>();
            this.hudElements = new ArrayList<>();
            this.hudContainer = new HashMap<>();
        }
    }

    /**
     * Initializes default HUD elements.
     */
    private void initializeHUD() {
        // Health bar
        HUDElement healthBar = HUDElement.healthBar(4, 4);
        healthBar.setRenderCallback(new HUDElement.DefaultRenderCallback());
        hudElements.add(healthBar);
        hudContainer.put("health", healthBar);

        // Ammo display
        HUDElement ammo = HUDElement.ammoDisplay(4, 24);
        ammo.setRenderCallback(new HUDElement.DefaultRenderCallback());
        hudElements.add(ammo);
        hudContainer.put("ammo", ammo);

        // Score display
        HUDElement score = HUDElement.scoreDisplay(500, 24, 120, 20);
        score.setRenderCallback(new HUDElement.DefaultRenderCallback());
        hudElements.add(score);
        hudContainer.put("score", score);

        // Level indicator
        HUDElement level = HUDElement.levelIndicator(540, 24, 100, 20);
        level.setRenderCallback(new HUDElement.DefaultRenderCallback());
        hudElements.add(level);
        hudContainer.put("level", level);

        // Debug info
        HUDElement debug = HUDElement.debugInfo(400, 432, 240, 48);
        debug.setRenderCallback(new HUDElement.DefaultRenderCallback());
        hudElements.add(debug);
        hudContainer.put("debug", debug);

        // Message box
        HUDElement messageBox = HUDElement.messageBox(40, 240, 600, 200);
        messageBox.setRenderCallback(new HUDElement.DefaultRenderCallback());
        hudElements.add(messageBox);
        hudContainer.put("message", messageBox);
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
     * Renders the complete game state including map tiles, entities,
     * projectiles, decorations, and HUD.
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

        // Render decorations
        if (map.getDecorationPositions() != null) {
            renderMapDecorations(map);
        }

        // Render entities
        renderEntities(game);

        // Render projectiles
        renderProjectiles(game);

        // Render HUD/UI overlay
        renderHUD(game);
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
                if (tileType >= GameMap.TILE_FLOOR && tileType < GameMap.TILE_WALL + 10) {
                    renderTile(x * TILE_WIDTH, y * TILE_HEIGHT, tileType);
                }
            }
        }
    }

    /**
     * Renders map decorations.
     *
     * @param map The game map with decorations
     */
    private void renderMapDecorations(GameMap map) {
        if (map == null || map.getDecorationPositions() == null) {
            return;
        }

        // Render each decoration in the map
        for (Position pos : map.getDecorationPositions()) {
            if (pos != null) {
                DecorationType decoration = map.getDecorationType(
                        (int) pos.getX(), (int) pos.getY());
                if (decoration != DecorationType.NONE) {
                    renderDecoration(pos, decoration);
                }
            }
        }
    }

    /**
     * Renders a decoration at the specified position.
     *
     * @param position The decoration position
     * @param decoration The decoration type
     */
    private void renderDecoration(Position position, DecorationType decoration) {
        if (position == null || decoration == DecorationType.NONE) {
            return;
        }

        // Generate decoration texture key from the enum name
        String typeName = decoration.name();
        String decorationKey = "decoration_" + typeName;
        if (!textures.containsKey(decorationKey)) {
            String decorationPath = "decoration_" + typeName;
            textures.put(decorationKey, decorationPath);
        }

        // Render decoration at position
        // In full implementation, this would draw with proper texture
    }

    /**
     * Renders a single tile.
     *
     * @param x     The tile x pixel position
     * @param y     The tile y pixel position
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
        // In full implementation, draw sprite to buffer at (x, y)
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

        // Get entity position and size
        Position pos = entity.getPosition();
        Size size = entity.getSize();
        if (pos != null && size != null) {
            // Render entity sprite at position
            // In full implementation, use texture manager to draw
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

        // Get projectile position
        Position pos = projectile.getPosition();
        if (pos != null) {
            // Render projectile sprite at position
        }
    }

    /**
     * Renders the HUD/UI overlay.
     *
     * @param game The game to render HUD for
     */
    private void renderHUD(Game game) {
        if (game == null) {
            return;
        }

        // Clear and rebuild HUD if needed
        clearHUD();

        // Get player for HUD data
        PlayerEntity player = game.getPlayer();
        if (player != null) {
            // Health bar - shows current/max health
            float health = player.getHealth();
            float maxHealth = player.getMaxHealth();
            HUDElement healthBar = hudContainer.get("health");
            if (healthBar != null) {
                healthBar.setY(BUFFER_HEIGHT - HUD_HEIGHT);
                healthBar.setX(4);
                healthBar.setRenderCallback(new HUDElement.RenderCallback() {
                    @Override
                    public boolean render(Game game, HUDElement element) {
                        HUDElement healthBar = (HUDElement) element;
            float pct = health / maxHealth;
                        int barWidth = (int) (healthBar.getWidth() * pct);
                        System.out.println(String.format("HUD: Health [%d/%d] (%.0f%%)",
                                (int) health, (int) maxHealth, pct * 100));
                        return true;
                    }
                });
            }

            // Ammo display
            int ammo = player.getAmmunition();
            int maxAmmo = player.getMaxAmmunition();
            HUDElement ammoDisplay = hudContainer.get("ammo");
            if (ammoDisplay != null) {
                ammoDisplay.setY(BUFFER_HEIGHT - HUD_HEIGHT + 16);
                ammoDisplay.setRenderCallback(new HUDElement.RenderCallback() {
                    @Override
                    public boolean render(Game game, HUDElement element) {
                        HUDElement ammoDisplayEl = (HUDElement) element;
                        System.out.println(String.format("HUD: Ammo: %d/%d", ammo, maxAmmo));
                        return true;
                    }
                });
            }
        }

        // Render all HUD elements
        for (HUDElement element : hudElements) {
            element.render(game);
        }
    }

    /**
     * Clears all HUD elements.
     */
    public void clearHUD() {
        hudElements.clear();
        hudContainer.clear();
        initializeHUD();
    }

    /**
     * Adds a HUD element.
     *
     * @param element The HUD element to add
     */
    public void addHUDElement(HUDElement element) {
        if (element != null && !hudElements.contains(element)) {
            hudElements.add(element);
            String typeStr = element.getType().name().toLowerCase();
            hudContainer.put(typeStr, element);
        }
    }

    /**
     * Removes a HUD element by type.
     *
     * @param type The element type to remove
     * @return true if element was removed
     */
    public boolean removeHUDElement(HUDElement.Type type) {
        String key = type.name().toLowerCase();
        HUDElement element = hudContainer.remove(key);
        if (element != null) {
            hudElements.remove(element);
            return true;
        }
        return false;
    }

    /**
     * Gets a HUD element by type.
     *
     * @param type The element type
     * @return The HUD element, or null if not found
     */
    public HUDElement getHUDElement(HUDElement.Type type) {
        String key = type.name().toLowerCase();
        return hudContainer.get(key);
    }

    /**
     * Loads a texture/sprite into the texture cache.
     *
     * @param name  The texture name
     * @param path  The texture path (stub for future file loading)
     * @return true if texture loaded successfully
     */
    public boolean loadTexture(String name, String path) {
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

    /**
     * Gets the HUD elements list (package-private access).
     *
     * @return The HUD elements list
     */
    protected List<HUDElement> getHUDElements() {
        return hudElements;
    }

    /**
     * Gets the HUD container map (package-private access).
     *
     * @return The HUD container map
     */
    protected Map<String, HUDElement> getHUDContainer() {
        return hudContainer;
    }

    @Override
    public String toString() {
        return "Renderer{" +
                "textures=" + textures.size() +
                ", hudElements=" + hudElements.size() +
                '}';
    }
}
