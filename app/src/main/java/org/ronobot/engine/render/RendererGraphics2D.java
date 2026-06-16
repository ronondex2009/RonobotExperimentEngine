package org.ronobot.engine.render;

import org.ronobot.engine.core.Game;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.map.GameMap.DecorationType;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RendererGraphics2D provides game rendering functionality using Graphics2D for
 * sprite display and texture management in a DOOM-like engine.
 * <p>
 * This class handles rendering of game tiles, entities, projectiles,
 * decorations, and UI elements using a BufferedImage buffer. It supports
 * basic 2D rendering with color-based graphics instead of external sprites.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class RendererGraphics2D {

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
     * Color palette for tiles.
     */
    private final Map<Integer, Color> tileColors;

    /**
     * Color palette for entities.
     */
    private final Map<Class<?>, Color> entityColors;

    /**
     * Font for HUD text.
     */
    private final Font hudFont;

    /**
     * Font metrics for text rendering.
     */
    private FontMetrics fontMetrics;

    /**
     * HUD overlay rectangle.
     */
    private final java.awt.Rectangle hudOverlay;

    /**
     * Creates a new Renderer with default configurations.
     */
    public RendererGraphics2D() {
        this.tileColors = new HashMap<>();
        this.entityColors = new HashMap<>();
        this.hudFont = new Font("Arial", Font.PLAIN, 14);
        this.hudOverlay = new java.awt.Rectangle(0, BUFFER_HEIGHT - HUD_HEIGHT, BUFFER_WIDTH, HUD_HEIGHT);
        initializeTileColors();
        initializeEntityColors();
    }

    /**
     * Initializes tile color palette.
     */
    private void initializeTileColors() {
        tileColors.put(GameMap.TILE_WALL, new Color(46, 48, 50));
        tileColors.put(GameMap.TILE_FLOOR, new Color(16, 14, 12));
        tileColors.put(GameMap.TILE_DOOR, new Color(70, 60, 40));
    }

    /**
     * Initializes entity color palette.
     */
    private void initializeEntityColors() {
        entityColors.put(PlayerEntity.class, new Color(68, 132, 156));
        // Add more entity types as needed
    }

    /**
     * Gets a color for the specified tile type.
     *
     * @param tileType The tile type
     * @return The tile color
     */
    public Color getTileColor(int tileType) {
        return tileColors.getOrDefault(tileType, tileColors.get(GameMap.TILE_FLOOR));
    }

    /**
     * Gets a color for the specified entity class.
     *
     * @param entityClass The entity class
     * @return The entity color
     */
    public Color getEntityColor(Class<?> entityClass) {
        return entityColors.getOrDefault(entityClass, new Color(100, 100, 100));
    }

    /**
     * Sets a custom color for a tile type.
     *
     * @param tileType The tile type
     * @param color The color
     */
    public void setTileColor(int tileType, Color color) {
        tileColors.put(tileType, color);
    }

    /**
     * Sets a custom color for an entity class.
     *
     * @param entityClass The entity class
     * @param color The color
     */
    public void setEntityColor(Class<?> entityClass, Color color) {
        entityColors.put(entityClass, color);
    }

    /**
     * Renders the complete game state including map tiles, entities,
     * projectiles, decorations, and HUD to a BufferedImage.
     *
     * @param game The game to render
     * @param graphics2D The Graphics2D context to draw to
     */
    public void render(Game game, Graphics2D graphics2D) {
        if (game == null) {
            return;
        }
        if (graphics2D == null) {
            return;
        }

        GameMap map = game.getMap();
        if (map == null || !map.isLoaded()) {
            return;
        }

        // Clear the graphics2D background
        graphics2D.setColor(tileColors.get(GameMap.TILE_FLOOR));
        graphics2D.fillRect(0, 0, BUFFER_WIDTH, BUFFER_HEIGHT);

        // Render map tiles
        renderMap(map, graphics2D);

        // Render decorations
        renderMapDecorations(map, graphics2D);

        // Render entities
        renderEntities(game, graphics2D);

        // Render projectiles
        renderProjectiles(game, graphics2D);

        // Render HUD/UI overlay
        renderHUD(game, graphics2D);
    }

    /**
     * Renders the map tiles.
     *
     * @param map The game map
     * @param graphics2D The Graphics2D context
     */
    private void renderMap(GameMap map, Graphics2D graphics2D) {
        if (map == null) {
            return;
        }

        // Render each tile in the map
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int tileType = map.getTile(x, y);
                if (tileType >= GameMap.TILE_FLOOR && tileType < GameMap.TILE_WALL + 10) {
                    int drawX = x * TILE_WIDTH;
                    int drawY = y * TILE_HEIGHT;
                    renderTile(drawX, drawY, tileType, graphics2D);
                }
            }
        }
    }

    /**
     * Renders a single tile.
     *
     * @param x     The tile x pixel position
     * @param y     The tile y pixel position
     * @param type  The tile type
     * @param graphics2D The Graphics2D context
     */
    private void renderTile(int x, int y, int type, Graphics2D graphics2D) {
        Color color = getTileColor(type);
        if (color != null) {
            graphics2D.setColor(color);
            graphics2D.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);

            // Add a subtle border for walls
            if (type == GameMap.TILE_WALL) {
                Color darkerWall = new Color(
                        Math.max(0, (int) (color.getRed() * 0.7)),
                        Math.max(0, (int) (color.getGreen() * 0.7)),
                        Math.max(0, (int) (color.getBlue() * 0.7))
                );
                graphics2D.setColor(darkerWall);
                graphics2D.drawRect(x, y, TILE_WIDTH - 1, TILE_HEIGHT - 1);
            }
        }
    }

    /**
     * Renders map decorations.
     *
     * @param map The game map with decorations
     * @param graphics2D The Graphics2D context
     */
    private void renderMapDecorations(GameMap map, Graphics2D graphics2D) {
        if (map == null || map.getDecorationPositions() == null) {
            return;
        }

        // Render each decoration in the map
        for (Position pos : map.getDecorationPositions()) {
            if (pos != null) {
                DecorationType decoration = map.getDecorationType(
                        (int) pos.getX(), (int) pos.getY());
                if (decoration != DecorationType.NONE) {
                    renderDecoration(pos, decoration, graphics2D);
                }
            }
        }
    }

    /**
     * Renders a decoration at the specified position.
     *
     * @param position The decoration position
     * @param decoration The decoration type
     * @param graphics2D The Graphics2D context
     */
    private void renderDecoration(Position position, DecorationType decoration, Graphics2D graphics2D) {
        if (position == null || decoration == DecorationType.NONE) {
            return;
        }

        // Render decoration shape (simple rectangles for now)
        Color decorationColor = switch (decoration) {
            case STATUE -> new Color(80, 60, 40);
            case PICTURE -> new Color(200, 150, 100);
            case TABLE -> new Color(60, 40, 30);
            case CHEST -> new Color(100, 50, 20);
            case CRATE -> new Color(80, 60, 40);
            case FLAG -> new Color(200, 20, 20);
            case FOUNTAIN -> new Color(150, 180, 200);
            default -> new Color(50, 50, 50);
        };

        graphics2D.setColor(decorationColor);
        graphics2D.fillOval(
                (int) position.getX() + 4,
                (int) position.getY() + 4,
                16, 16);
    }

    /**
     * Renders all entities in the game.
     *
     * @param game The game containing entities
     * @param graphics2D The Graphics2D context
     */
    private void renderEntities(Game game, Graphics2D graphics2D) {
        if (game == null) {
            return;
        }

        // Render each entity
        for (Entity entity : game.getEntityManager().getEntities()) {
            if (entity != null) {
                renderEntity(entity, graphics2D);
            }
        }
    }

    /**
     * Renders a single entity.
     *
     * @param entity The entity to render
     * @param graphics2D The Graphics2D context
     */
    private void renderEntity(Entity entity, Graphics2D graphics2D) {
        if (entity == null) {
            return;
        }

        // Get entity position and size
        Position pos = entity.getPosition();
        Size size = entity.getSize();
        if (pos != null && size != null) {
            Color entityColor = getEntityColor(entity.getClass());
            if (entityColor != null) {
                graphics2D.setColor(entityColor);
                graphics2D.fillOval(
                        (int) pos.getX(),
                        (int) pos.getY(),
                        (int) size.getWidth(),
                        (int) size.getHeight());

                // Add entity outline
                graphics2D.setColor(new Color(0, 0, 0, 100));
                graphics2D.drawOval(
                        (int) pos.getX(),
                        (int) pos.getY(),
                        (int) size.getWidth(),
                        (int) size.getHeight());
            }
        }
    }

    /**
     * Renders all projectiles in the game.
     *
     * @param game The game containing projectiles
     * @param graphics2D The Graphics2D context
     */
    private void renderProjectiles(Game game, Graphics2D graphics2D) {
        if (game == null) {
            return;
        }

        // Render each projectile
        for (Entity entity : game.getEntityManager().getEntities()) {
            if (entity instanceof Projectile projectile) {
                renderProjectile(projectile, graphics2D);
            }
        }
    }

    /**
     * Renders a single projectile.
     *
     * @param projectile The projectile to render
     * @param graphics2D The Graphics2D context
     */
    private void renderProjectile(Projectile projectile, Graphics2D graphics2D) {
        if (projectile == null) {
            return;
        }

        // Get projectile position
        Position pos = projectile.getPosition();
        if (pos != null) {
            Color projectileColor = new Color(200, 50, 50);
            graphics2D.setColor(projectileColor);
            graphics2D.fillOval(
                    (int) pos.getX() - 4,
                    (int) pos.getY() - 4,
                    8, 8);
        }
    }

    /**
     * Renders the HUD/UI overlay.
     *
     * @param game The game to render HUD for
     * @param graphics2D The Graphics2D context
     */
    private void renderHUD(Game game, Graphics2D graphics2D) {
        if (game == null) {
            return;
        }

        // Set up font metrics
        if (fontMetrics == null) {
            graphics2D.setFont(hudFont);
            fontMetrics = graphics2D.getFontMetrics();
        }

        // Render HUD elements from Renderer's container
        // Placeholder - will use Renderer's getHUDContainer in full implementation
        // for (HUDElement element : renderer.getHUDContainer().values()) {
        //     if (element != null && element.isVisible()) {
        //         element.render(game);
        //     }
        // }
    }

    /**
     * Gets the tile width.
     *
     * @return The tile width
     */
    public static int getTileWidth() {
        return TILE_WIDTH;
    }

    /**
     * Gets the tile height.
     *
     * @return The tile height
     */
    public static int getTileHeight() {
        return TILE_HEIGHT;
    }

    /**
     * Gets the buffer width.
     *
     * @return The buffer width
     */
    public static int getBufferWidth() {
        return BUFFER_WIDTH;
    }

    /**
     * Gets the buffer height.
     *
     * @return The buffer height
     */
    public static int getBufferHeight() {
        return BUFFER_HEIGHT;
    }

    @Override
    public String toString() {
        return "RendererGraphics2D{" +
                "tileColors=" + tileColors.size() +
                ", entityColors=" + entityColors.size() +
                ", hudFont=" + hudFont +
                "}";
    }
}
