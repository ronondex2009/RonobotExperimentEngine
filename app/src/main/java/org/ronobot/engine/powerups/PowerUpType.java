package org.ronobot.engine.powerups;

/**
 * Power-up type enumeration for pickup items.
 * <p>
 * Power-ups provide various benefits to the player:
 * - Health: Restores player health
 * - Armor: Adds armor protection
 * - Ammo: Restores ammunition
 * - Weapon: Upgrades the current weapon
 * - Speed: Increases player movement speed
 * - Invisibility: Makes player invisible (temporary)
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public enum PowerUpType {

    /**
     * Health power-up.
     * <p>
     * Restores 50 health points to the player.
     * Visual: Red cross icon (🛡️❤️)
     * </p>
     */
    HEALTH("Health Pack", 50, "🛡️❤️"),

    /**
     * Armor power-up.
     * <p>
     * Adds 25 armor points to the player.
     * Visual: Shield icon (🛡️)
     * </p>
     */
    ARMOR("Armor Plate", 25, "🛡️"),

    /**
     * Ammunition power-up.
     * <p>
     * Restores 50 ammunition points.
     * Visual: Ammo icon (🔫)
     * </p>
     */
    AMMO("Ammo Box", 50, "🔫"),

    /**
     * Rocket launcher weapon power-up.
     * <p>
     * Upgrades the player's weapon to rocket launcher.
     * Visual: Rocket icon (🚀)
     * </p>
     */
    ROCKET("Rocket Launcher", 0, "🚀"),

    /**
     * Shotgun weapon power-up.
     * <p>
     * Upgrades the player's weapon to shotgun.
     * Visual: Shotgun icon (💥)
     * </p>
     */
    SHOTGUN("Shotgun", 0, "💥"),

    /**
     * Chain gun weapon power-up.
     * <p>
     * Upgrades the player's weapon to chain gun.
     * Visual: Machine gun icon (🔫⚡)
     * </p>
     */
    CHAIN("Chain Gun", 0, "🔫⚡"),

    /**
     * BFG weapon power-up.
     * <p>
     * Upgrades the player's weapon to BFG.
     * Visual: BFG icon (☢️)
     * </p>
     */
    BFG("BFG", 0, "☢️"),

    /**
     * Speed power-up.
     * <p>
     * Increases player movement speed by 50%.
     * Visual: Speed icon (⚡)
     * </p>
     */
    SPEED("Speed Boost", 0, "⚡"),

    /**
     * Invisibility power-up.
     * <p>
     * Makes the player invisible temporarily.
     * Visual: Ghost icon (👻)
     * </p>
     */
    INVISIBILITY("Invisibility", 0, "👻"),

    /**
     * Medkit power-up.
     * <p>
     * Fully restores player health (mega health).
     * Visual: Cross icon (❇️)
     * </p>
     */
    MEGAMEDKIT("Mega Medkit", 100, "❇️");

    // ==================== Fields ====================

    /**
     * Name displayed in game UI.
     */
    private final String name;

    /**
     * Effect value (health restored, armor added, or 0 for upgrades).
     */
    private final int effectValue;

    /**
     * Visual representation emoji.
     */
    private final String visual;

    /**
     * Creates a new PowerUpType.
     *
     * @param name      The display name
     * @param effectValue The effect value or 0 for upgrades
     * @param visual    The visual representation
     */
    PowerUpType(String name, int effectValue, String visual) {
        this.name = name;
        this.effectValue = effectValue;
        this.visual = visual;
    }

    // ==================== Getters ==================

    /**
     * Gets the display name.
     *
     * @return The display name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the effect value.
     *
     * @return The effect value
     */
    public int getEffectValue() {
        return effectValue;
    }

    /**
     * Gets the visual representation.
     *
     * @return The visual representation
     */
    public String getVisual() {
        return visual;
    }

    /**
     * Gets whether this power-up provides a value (not an upgrade).
     *
     * @return true if this is a value-based power-up
     */
    public boolean isValuePowerUp() {
        return effectValue > 0;
    }

    /**
     * Gets whether this power-up provides an upgrade (not a value).
     *
     * @return true if this is an upgrade power-up
     */
    public boolean isUpgradePowerUp() {
        return effectValue == 0;
    }

    /**
     * Checks if this is a weapon upgrade power-up.
     *
     * @return true if this is a weapon upgrade
     */
    public boolean isWeaponUpgrade() {
        return switch (this) {
            case ROCKET, SHOTGUN, CHAIN, BFG -> true;
            default -> false;
        };
    }

    /**
     * Checks if this is a health-related power-up.
     *
     * @return true if this affects health
     */
    public boolean isHealthRelated() {
        return switch (this) {
            case HEALTH, MEGAMEDKIT -> true;
            default -> false;
        };
    }

    /**
     * Checks if this is an armor-related power-up.
     *
     * @return true if this affects armor
     */
    public boolean isArmorRelated() {
        return this == ARMOR;
    }

    /**
     * Checks if this is an ammo-related power-up.
     *
     * @return true if this affects ammo
     */
    public boolean isAmmoRelated() {
        return this == AMMO;
    }

    /**
     * Gets a human-readable description of this power-up.
     *
     * @return A description string
     */
    public String getDescription() {
        return name + " - " + visual + (isValuePowerUp() ?
                " Provides " + effectValue + " units of benefit." :
                " Upgrades current weapon.");
    }

    /**
     * Gets the category of this power-up.
     *
     * @return The category name
     */
    public String getCategory() {
        return switch (this) {
            case HEALTH, MEGAMEDKIT -> "Health";
            case ARMOR -> "Armor";
            case AMMO -> "Ammo";
            case ROCKET, SHOTGUN, CHAIN, BFG -> "Weapon";
            case SPEED -> "Speed";
            case INVISIBILITY -> "Special";
            default -> "Unknown";
        };
    }
}
