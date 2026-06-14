package org.ronobot.engine.entity;

/**
 * Item type enumeration for inventory items.
 * <p>
 * Items in the game include:
 * - Ammo: Ammunition for weapons
 * - Health: Health restoration
 * - Armor: Armor protection
 * - Keycards: Access to restricted areas
 * - Secrets: Unlockable achievements
 * - Monsters: Spawn enemy units
 * - Meds: Medical supplies
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public enum ItemType {

    /**
     * Ammunition item.
     * <p>
     * Provides bullets/energy for weapons.
     * Common drop from enemies.
     * </p>
     */
    AMMO("Ammo", "🔫", "Weapon", "Ammunition"),

    /**
     * Health item.
     * <p>
     * Restores player health points.
     * Common pickup item.
     * </p>
     */
    HEALTH("Health", "❤️", "Consumable", "Health restoration"),

    /**
     * Armor item.
     * <p>
     * Provides armor protection to the player.
     * Less common than health.
     * </p>
     */
    ARMOR("Armor", "🛡️", "Consumable", "Armor protection"),

    /**
     * Keycard item.
     * <p>
     * Opens locked doors/areas.
     * Keyed to specific door levels.
     * </p>
     */
    KEYCARD("Keycard", "🗝️", "Key", "Door access key"),

    /**
     * Secret item.
     * <p>
     * Represents finding a secret.
     * Triggers achievement notifications.
     * </p>
     */
    SECRET("Secret", "✨", "Achievement", "Secret discovered"),

    /**
     * Monster item.
     * <p>
     * Spawns enemy units.
     * Used for level design.
     * </p>
     */
    MONSTER("Monster", "👹", "Spawn", "Spawn enemies"),

    /**
     * Medkit item.
     * <p>
     * Full health restoration.
     * Rare, high-value pickup.
     * </p>
     */
    MEDKIT("Medkit", "🏥", "Consumable", "Emergency medical"),

    /**
     * Weapon item.
     * <p>
     * Provides weapon pickups.
     * Upgrades or alternative weapons.
     * </p>
     */
    WEAPON("Weapon", "🔫", "Weapon", "Pickup weapon");

    // ==================== Fields ====================

    /**
     * Display name shown in UI.
     */
    private final String displayName;

    /**
     * Emoji/icon representation.
     */
    private final String icon;

    /**
     * Item category.
     */
    private final String category;

    /**
     * Description for tooltips/UI.
     */
    private final String description;

    /**
     * Creates a new ItemType.
     *
     * @param displayName The display name
     * @param icon The icon emoji
     * @param category The item category
     * @param description The description
     */
    ItemType(String displayName, String icon, String category, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.category = category;
        this.description = description;
    }

    // ==================== Getters ====================

    /**
     * Gets the display name.
     *
     * @return The display name
     */
    public String getName() {
        return displayName;
    }

    /**
     * Gets the icon/emoji representation.
     *
     * @return The icon string
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Gets the item category.
     *
     * @return The category string
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the description.
     *
     * @return The description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the full display name.
     *
     * @return The full display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
