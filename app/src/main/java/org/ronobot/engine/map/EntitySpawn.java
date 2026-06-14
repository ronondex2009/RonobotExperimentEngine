package org.ronobot.engine.map;

import org.ronobot.engine.map.MapFileParser;

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
