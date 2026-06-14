package org.ronobot.engine.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * MapDecorationLoader handles loading and registering decorations onto maps.
 * <p>
 * This class provides methods to load decorations from map files or
 * programmatically, and manages the decoration registry for efficient
 * lookup during rendering and game updates.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class MapDecorationLoader {

    // ==================== Constants ==================

    /**
     * Default decoration separator character (comma).
     */
    public static final char SEPARATOR = ',';

    /**
     * Decoration definition format: "type,name,row,col,visual,priority"
     */
    private static final String DEFINITION_PREFIX = "// DECORATIONS:";

    // ==================== Fields ====================

    /**
     * Registry of all registered decorations.
     */
    private final Map<String, MapDecoration> decorationRegistry = new HashMap<>();

    /**
     * Decorations per map name.
     */
    private final Map<String, List<MapDecoration>> mapDecorations = new HashMap<>();

    // ==================== Constructors ==================

    /**
     * Creates a new MapDecorationLoader.
     */
    public MapDecorationLoader() {
        // Default decorations
        registerDefaultDecorations();
    }

    // ==================== Methods ====================

    /**
     * Registers a single decoration.
     *
     * @param decoration The decoration to register
     */
    public void register(MapDecoration decoration) {
        if (decoration == null || !decoration.isValid()) {
            return;
        }
        decorationRegistry.put(decoration.getName(), decoration);
    }

    /**
     * Registers multiple decorations.
     *
     * @param decorations The decorations to register
     */
    public void registerAll(List<MapDecoration> decorations) {
        if (decorations == null) {
            return;
        }
        for (MapDecoration dec : decorations) {
            register(dec);
        }
    }

    /**
     * Loads decorations from a definition string.
     *
     * @param mapName  The map name
     * @param definition The decoration definition string
     */
    public void loadFromDefinition(String mapName, String definition) {
        if (definition == null || definition.isEmpty()) {
            return;
        }

        // Parse decorations
        List<String> lines = parseLines(definition);
        for (String line : lines) {
            List<String> parts = splitLine(line);
            if (parts.size() >= 6) {
                String type = parts.get(0);
                String name = parts.get(1);
                int row = parseInt(parts.get(2), 0);
                int col = parseInt(parts.get(3), 0);
                String visualStr = parts.get(4);
                int priority = parseInt(parts.get(5), 0);

                char visual = getDefaultVisual(visualStr, '.');
                register(new MapDecoration(row, col, type, name, visual, priority));
            }
        }

        // Clear map-specific decorations for new map
        mapDecorations.remove(mapName);
    }

    /**
     * Gets all decorations for a map.
     *
     * @param mapName The map name
     * @return List of decorations, empty list if none
     */
    public List<MapDecoration> getMapDecorations(String mapName) {
        List<MapDecoration> decorations = mapDecorations.get(mapName);
        if (decorations == null) {
            decorations = new ArrayList<>();
        }
        return decorations;
    }

    /**
     * Gets a decoration by name.
     *
     * @param name The decoration name
     * @return The decoration, or null if not found
     */
    public MapDecoration getDecoration(String name) {
        return decorationRegistry.get(name);
    }

    /**
     * Gets all registered decorations.
     *
     * @return All decorations
     */
    public List<MapDecoration> getAllDecorations() {
        return new ArrayList<>(decorationRegistry.values());
    }

    /**
     * Clears all decorations.
     */
    public void clearAll() {
        decorationRegistry.clear();
        mapDecorations.clear();
    }

    /**
     * Clears decorations for a specific map.
     *
     * @param mapName The map name
     */
    public void clearMapDecorations(String mapName) {
        mapDecorations.remove(mapName);
    }

    /**
     * Checks if a decoration exists by name.
     *
     * @param name The decoration name
     * @return true if exists
     */
    public boolean hasDecoration(String name) {
        return decorationRegistry.containsKey(name);
    }

    /**
     * Checks if any decorations exist for a map.
     *
     * @param mapName The map name
     * @return true if decorations exist
     */
    public boolean hasDecorations(String mapName) {
        return mapDecorations.containsKey(mapName) && !mapDecorations.get(mapName).isEmpty();
    }

    /**
     * Gets the decoration count.
     *
     * @return Decoration count
     */
    public int getDecorationCount() {
        return decorationRegistry.size();
    }

    // ==================== Private Methods ==================

    /**
     * Parses lines from a definition string.
     */
    private List<String> parseLines(String content) {
        List<String> lines = new ArrayList<>();
        if (content == null) {
            return lines;
        }

        int startLine = 0;
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) != '\n' && content.charAt(i) != '\r') {
                startLine = i;
                break;
            }
        }

        int endLine = content.length();
        for (int i = content.length() - 1; i >= startLine; i--) {
            if (content.charAt(i) == '\n') {
                endLine = i + 1;
                break;
            }
        }

        String contentStr = content.substring(startLine, endLine);
        String[] linesArray = contentStr.split("\n");

        for (String line : linesArray) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("//")) {
                lines.add(trimmed);
            }
        }

        return lines;
    }

    /**
     * Splits a line by separator.
     */
    private List<String> splitLine(String line) {
        List<String> parts = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == SEPARATOR) {
                parts.add(line.substring(start, i));
                start = i + 1;
            }
        }
        if (start < line.length()) {
            parts.add(line.substring(start));
        }
        return parts;
    }

    /**
     * Parses an integer from a string.
     */
    private int parseInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Gets default visual character.
     */
    private char getDefaultVisual(String visualStr, char defaultValue) {
        if (visualStr == null || visualStr.isEmpty()) {
            return defaultValue;
        }
        return visualStr.charAt(0);
    }

    /**
     * Registers default decorations.
     */
    private void registerDefaultDecorations() {
        register(new MapDecoration(5, 10, MapDecoration.TYPE_WALL, "default_wall", '#', 1));
        register(new MapDecoration(5, 15, MapDecoration.TYPE_WALL, "default_wall2", '#', 1));
        register(new MapDecoration(8, 12, MapDecoration.TYPE_LIGHTING, "torch", '@', 2));
    }
}
