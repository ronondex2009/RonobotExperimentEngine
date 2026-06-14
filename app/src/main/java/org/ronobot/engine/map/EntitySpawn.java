/**
 * Entity spawn data structure representing spawn positions for game entities.
 * <p>
 * This class holds spawn position information for players, enemies,
 * power-ups, and other game entities. Used by LevelLoader to manage
 * spawn registry and entity placement.
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
 * Entity spawn data structure representing spawn positions for game entities.
 * <p>
 * This class holds spawn position information for players, enemies,
 * power-ups, and other game entities. Used by LevelLoader to manage
 * spawn registry and entity placement.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class EntitySpawn {

    /**
     * Entity type name (e.g., "player", "enemy", "powerup", "ammo").
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
    public EntitySpawn(String typeName, MapFileParser.EntitySpawn.Type type, int col, int row) {
        this.typeName = Objects.requireNonNull(typeName, "typeName cannot be null");
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.col = col;
        this.row = row;
    }

    /**
     * Creates an EntitySpawn from a JSON object.
     *
     * @param json the JSON object containing spawn data
     * @return a new EntitySpawn instance
     */
    public static EntitySpawn fromJson(JsonObject json) {
        String typeName = json.get("typeName").getAsString();
        MapFileParser.EntitySpawn.Type type = MapFileParser.EntitySpawn.Type.valueOf(
            json.get("type").getAsString()
        );
        int col = json.get("col").getAsInt();
        int row = json.get("row").getAsInt();
        return new EntitySpawn(typeName, type, col, row);
    }

    /**
     * Converts this EntitySpawn to a JSON object.
     *
     * @return a JSON object representing this entity spawn
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("typeName", typeName);
        json.addProperty("type", type.name());
        json.addProperty("col", col);
        json.addProperty("row", row);
        return json;
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
        EntitySpawn that = (EntitySpawn) obj;
        return col == that.col &&
                row == that.row &&
                typeName.equals(that.typeName) &&
                type == that.type;
    }

    /**
     * Gets the hash code.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(typeName, type, col, row);
    }
}
