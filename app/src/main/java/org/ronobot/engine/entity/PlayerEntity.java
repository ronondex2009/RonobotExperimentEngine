package org.ronobot.engine.entity;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

/**
 * Player entity with special abilities and inventory.
 * <p>
 * Represents the player-controlled entity in the game.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class PlayerEntity extends Entity {

    /**
     * Current weapon.
     */
    private String weapon = "Pistol";

    /**
     * Health regeneration rate.
     */
    private int healthRegenRate = 1;

    /**
     * Maximum ammunition.
     */
    private int maxAmmunition = 100;

    /**
     * Current ammunition.
     */
    private int ammunition = 50;

    /**
     * Creates a new PlayerEntity at the given position.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     */
    public PlayerEntity(int id, float x, float y) {
        super(id, x, y, 32, 32, "Player");
    }

    /**
     * Creates a new PlayerEntity with custom size.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     */
    public PlayerEntity(int id, float x, float y, int width, int height) {
        super(id, x, y, width, height, "Player");
    }

    /**
     * Gets the current weapon.
     *
     * @return The current weapon
     */
    public String getWeapon() {
        return weapon;
    }

    /**
     * Sets the current weapon.
     *
     * @param weapon The new weapon
     */
    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the health regeneration rate.
     *
     * @return The regeneration rate
     */
    public int getHealthRegenRate() {
        return healthRegenRate;
    }

    /**
     * Sets the health regeneration rate.
     *
     * @param rate The new regeneration rate
     */
    public void setHealthRegenRate(int rate) {
        this.healthRegenRate = Math.max(rate, 0);
    }

    /**
     * Gets the maximum ammunition.
     *
     * @return The maximum ammunition
     */
    public int getMaxAmmunition() {
        return maxAmmunition;
    }

    /**
     * Sets the maximum ammunition.
     *
     * @param max The new maximum
     */
    public void setMaxAmmunition(int max) {
        this.maxAmmunition = max;
    }

    /**
     * Gets the current ammunition.
     *
     * @return The current ammunition
     */
    public int getAmmunition() {
        return ammunition;
    }

    /**
     * Sets the current ammunition.
     *
     * @param ammo The new ammunition
     */
    public void setAmmunition(int ammo) {
        this.ammunition = Math.min(ammo, maxAmmunition);
    }

    /**
     * Reloads the weapon.
     *
     * @param count The number of bullets to reload
     */
    public void reload(int count) {
        ammunition = Math.min(ammunition + count, maxAmmunition);
    }

    /**
     * Fires the weapon.
     *
     * @return true if a projectile was fired
     */
    public boolean fire() {
        if (ammunition > 0 && !isDead()) {
            ammunition--;
            // In a full implementation, this would create a projectile
            return true;
        }
        return false;
    }

    /**
     * Gets the name of this entity type.
     *
     * @return "PlayerEntity"
     */
    @Override
    public String toString() {
        return "PlayerEntity{" +
                "weapon='" + weapon + '\'' +
                ", health=" + getHealth() +
                ", ammunition=" + ammunition +
                '}';
    }
}
