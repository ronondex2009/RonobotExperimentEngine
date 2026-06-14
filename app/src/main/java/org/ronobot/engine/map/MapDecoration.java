/**
 * MapDecoration represents decorative elements that can be placed on a map.
 * <p>
 * Decorations are non-interactable visual elements that enhance the map's
 * appearance without affecting gameplay mechanics. Examples include:
 * - Wall decorations (tall walls, barriers)
 * - Floor decorations (rugs, textures)
 * - Environmental elements (trees, rocks, pillars)
 * - Lighting elements (torches, glow effects)
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
package org.ronobot.engine.map;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import java.util.Objects;

/**
 * MapDecoration represents decorative elements that can be placed on a map.
 * <p>
 * Decorations are non-interactable visual elements that enhance the map's
 * appearance without affecting gameplay mechanics. Examples include:
 * - Wall decorations (tall walls, barriers)
 * - Floor decorations (rugs, textures)
 * - Environmental elements (trees, rocks, pillars)
 * - Lighting elements (torches, glow effects)
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class MapDecoration {

    /**
     * Decorator type for wall decorations.
     */
    public static final String TYPE_WALL = "wall";

    /**
     * Decorator type for floor decorations.
     */
    public static final String TYPE_FLOOR = "floor";

    /**
     * Decorator type for environmental elements.
     */
    public static final String TYPE_ENVIRONMENTAL = "environmental";

    /**
     * Decorator type for lighting elements.
     */
    public static final String TYPE_LIGHTING = "lighting";

    /**
     * The type of decoration.
     */
    private final String type;

    /**
     * The row position of the decoration.
     */
    private final int row;

    /**
     * The column position of the decoration.
     */
    private final int col;

    /**
     * The name/identifier of the decoration.
     */
    private final String name;

    /**
     * The visual representation character.
     */
    private final char visualChar;

    /**
     * The priority of the decoration (for rendering order).
     */
    private final int priority;

    /**
     * Creates a new MapDecoration with default parameters.
     *
     * @param row    The row position
     * @param col    The column position
     * @param type   The decoration type
     * @param name   The decoration name
     * @param visual The visual character
     */
    public MapDecoration(int row, int col, String type, String name, char visual) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
        this.visualChar = visual;
        this.priority = 0;
    }

    /**
     * Creates a new MapDecoration with priority.
     *
     * @param row      The row position
     * @param col      The column position
     * @param type     The decoration type
     * @param name     The decoration name
     * @param visual   The visual character
     * @param priority The decoration priority
     */
    public MapDecoration(int row, int col, String type, String name,
                         char visual, int priority) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
        this.visualChar = visual;
        this.priority = priority;
    }

    /**
     * Gets the decoration type.
     *
     * @return The decoration type
     */
    public String getType() {
        return type;
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
     * Gets the column position.
     *
     * @return The column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the decoration name.
     *
     * @return The decoration name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the visual representation character.
     *
     * @return The visual character
     */
    public char getVisualChar() {
        return visualChar;
    }

    /**
     * Gets the decoration priority.
     *
     * @return The decoration priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Creates a MapDecoration from a JSON object.
     *
     * @param json the JSON object containing decoration data
     * @return a new MapDecoration instance
     */
    public static MapDecoration fromJson(JsonObject json) {
        String type = json.get("type").getAsString();
        int row = json.get("row").getAsInt();
        int col = json.get("col").getAsInt();
        String name = json.get("name").getAsString();
        String visual = json.has("visual") ? json.get("visual").getAsString() : " ";
        int priority = json.has("priority") ? json.get("priority").getAsInt() : 0;
        return new MapDecoration(row, col, type, name, visual.charAt(0), priority);
    }

    /**
     * Validates this decoration.
     *
     * @return true if valid
     */
    public boolean isValid() {
        return type != null && !type.isEmpty() &&
                name != null && !name.isEmpty();
    }

    /**
     * Gets a string representation of the decoration.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "MapDecoration{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", row=" + row +
                ", col=" + col +
                ", visual='" + visualChar +
                ", priority=" + priority +
                '}';
    }

    /**
     * Converts this MapDecoration to a JSON object.
     *
     * @return a JSON object representing this map decoration
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("row", row);
        json.addProperty("col", col);
        json.addProperty("name", name);
        json.addProperty("visual", String.valueOf(visualChar));
        json.addProperty("priority", priority);
        return json;
    }

    /**
     * Checks if this object equals another.
     *
     * @param obj the object to compare to
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MapDecoration that = (MapDecoration) obj;
        return row == that.row &&
                col == that.col &&
                type.equals(that.type) &&
                name.equals(that.name) &&
                priority == that.priority;
    }

    /**
     * Gets the hash code.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, name, row, col, priority);
    }
}
