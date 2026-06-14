package org.ronobot.engine.map;

import org.ronobot.engine.entity.PlayerEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Map loader for parsing map files and creating GameMap instances.
 * <p>
 * Supports simple text-based map files and future WAD file parsing.
 * The loader parses map data into tile grids and initializes collision callbacks.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class MapLoader {

    /**
     * Default map file extension.
     */
    public static final String MAP_EXTENSION = ".map";

    /**
     * WAD file extension.
     */
    public static final String WAD_EXTENSION = ".wad";

    /**
     * Creates a GameMap from a simple text file.
     *
     * @param path The path to the map file
     * @return The loaded GameMap, or null if loading failed
     */
    public static GameMap loadMap(String path) {
        try {
            return loadMapFromText(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Creates a GameMap from a text file (internal).
     *
     * @param path The path to the map file
     * @return The loaded GameMap
     * @throws IOException If the file cannot be read
     */
    private static GameMap loadMapFromText(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            return null;
        }

        // Parse header line for dimensions
        String header = lines.get(0).trim();
        String[] parts = header.split("\\s+");
        if (parts.length < 2) {
            return null;
        }

        int width = Integer.parseInt(parts[0]);
        int height = Integer.parseInt(parts[1]);

        // Create map with perimeter walls
        GameMap map = new GameMap(width, height);

        // Parse remaining lines for tile data
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty() || line.startsWith("//")) {
                continue;
            }

            String[] tiles = line.split("\\s+");
            if (tiles.length >= 2) {
                try {
                    int tx = Integer.parseInt(tiles[0]);
                    int ty = Integer.parseInt(tiles[1]);
                    if (tx >= 0 && tx < map.getWidth() && ty >= 0 && ty < map.getHeight()) {
                        int tile = tiles.length > 2 ? Integer.parseInt(tiles[2]) : 0;
                        map.setTile(tx, ty, tile);
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid tile data
                }
            }
        }

        return map;
    }

    /**
     * Loads a GameMap from a WAD file (stub for future implementation).
     *
     * @param path The path to the WAD file
     * @return The loaded GameMap, or null if loading failed
     */
    public static GameMap loadWadMap(String path) {
        try {
            // Future implementation: parse WAD lump data
            // This would require binary parsing of DOOM WAD files
            // For now, create an empty map and return
            GameMap map = new GameMap(16, 16);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a simple test GameMap.
     *
     * @return A test GameMap with player spawn
     */
    public static GameMap createTestMap() {
        GameMap map = GameMap.createArenaMap(20, 16);

        // Add some rooms
        map.addFloor(3, 2);
        map.addFloor(4, 2);
        map.addFloor(5, 2);
        map.addFloor(3, 3);
        map.addFloor(4, 3);
        map.addFloor(5, 3);
        map.addFloor(3, 4);
        map.addFloor(4, 4);
        map.addFloor(5, 4);

        // Spawn player in center
        map.spawnEntity(9, 7, new PlayerEntity(9, 7f, 7f));

        return map;
    }

    /**
     * Loads a map file and sets up initial entities.
     *
     * @param path The path to the map file
     * @param playerSpawnX The x coordinate for player spawn
     * @param playerSpawnY The y coordinate for player spawn
     * @return The loaded GameMap with player spawned, or null if loading failed
     */
    public static GameMap loadAndSpawn(String path, int playerSpawnX, int playerSpawnY) {
        GameMap map = loadMap(path);
        if (map != null) {
            // Spawn player if coordinates are valid
            if (playerSpawnX >= 0 && playerSpawnX < map.getWidth() &&
                playerSpawnY >= 0 && playerSpawnY < map.getHeight()) {
                map.spawnEntity(playerSpawnX, playerSpawnY, new PlayerEntity(playerSpawnX, playerSpawnY, playerSpawnY));
            }
        }
        return map;
    }

    /**
     * Validates map dimensions.
     *
     * @param width The width in tiles
     * @param height The height in tiles
     * @return true if dimensions are valid
     */
    public static boolean validateDimensions(int width, int height) {
        return width > 0 && height > 0 && width < 1000 && height < 1000;
    }

    /**
     * Gets the recommended GameMap width for a given aspect ratio.
     *
     * @param aspectRatio The desired aspect ratio (width/height)
     * @param maxDimension The maximum dimension in pixels
     * @return The recommended width in tiles
     */
    public static int getRecommendedWidth(float aspectRatio, int maxDimension) {
        return (int) Math.floor((maxDimension / GameMap.getTileHeight()) / aspectRatio);
    }

    /**
     * Gets the recommended GameMap height for a given aspect ratio.
     *
     * @param aspectRatio The desired aspect ratio (width/height)
     * @param maxDimension The maximum dimension in pixels
     * @return The recommended height in tiles
     */
    public static int getRecommendedHeight(float aspectRatio, int maxDimension) {
        return (int) Math.floor(maxDimension / GameMap.getTileHeight());
    }

    /**
     * Writes a GameMap to a text file.
     *
     * @param map The GameMap to save
     * @param path The save path
     * @return true if saved successfully
     */
    public static boolean writeMap(GameMap map, String path) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(map.getWidth()).append(" ").append(map.getHeight()).append("\n");

            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    sb.append(x).append(" ").append(y).append(" ")
                      .append(map.getTile(x, y)).append("\n");
                }
            }

            Files.write(Paths.get(path), sb.toString().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
