package org.ronobot.engine.io;

/**
 * Enumeration of sprite types found in WAD files.
 * <p>
 * Each sprite type corresponds to a category of graphical elements
 * in Doom-style games. This enum is used for organizing and caching
 * sprite data by type.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public enum SpriteType {

    /**
     * Unknown sprite type.
     */
    UNKNOWN(0),

    /**
     * Exit sprites (E1-E9 in WAD files).
     */
    EXIT(1),

    /**
     * Missile sprites (M1-M9 in WAD files).
     */
    MISSILE(2),

    /**
     * Player sprites (P1-P2 in WAD files).
     */
    PLAYER(3),

    /**
     * Spawn sprites (S1-S2 in WAD files).
     */
    SPAWN(4),

    /**
     * Exit sprites (E1-E9 in WAD files).
     */
    STATUE(5),

    /**
     * Picture sprites.
     */
    PICTURE(6),

    /**
     * Table sprites.
     */
    TABLE(7),

    /**
     * Chest sprites.
     */
    CHEST(8),

    /**
     * Crate sprites.
     */
    CRATE(9);

    /**
     * Integer value for the sprite type.
     */
    public final int value;

    /**
     * Creates a new SpriteType with the specified value.
     *
     * @param value The integer value for this sprite type
     */
    SpriteType(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value for this sprite type.
     *
     * @return The integer value
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the human-readable name for a sprite type value.
     * <p>
     * Returns the enum name for known values, or "UNKNOWN" for values
     * outside the defined range.
     * </p>
     *
     * @param type The sprite type value
     * @return The human-readable name
     */
    public static String getName(int type) {
        switch (type) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "EXIT";
            case 2:
                return "MISSILE";
            case 3:
                return "PLAYER";
            case 4:
                return "SPAWN";
            case 5:
                return "STATUE";
            case 6:
                return "PICTURE";
            case 7:
                return "TABLE";
            case 8:
                return "CHEST";
            case 9:
                return "CRATE";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * Creates a string representation of this sprite type.
     *
     * @return String representation of the sprite type
     */
    @Override
    public String toString() {
        return "SpriteType." + this.name() + " = " + value;
    }
}
