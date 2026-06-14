package org.ronobot.engine.powerups;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;

/**
 * Power-up entity representing a pickup item in the game.
 * <p>
 * Power-ups are entities that the player can interact with to gain benefits:
 * - Health restoration
 * - Armor addition
 * - Ammunition refills
 * - Weapon upgrades
 * - Speed boosts
 * - Invisibility effects
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class PowerUp extends Entity {

    // ==================== Constants ==================

    /**
     * Power-up size (wider to make them easily visible).
     */
    private static final int POWERUP_WIDTH = 48;

    /**
     * Power-up height.
     */
    private static final int POWERUP_HEIGHT = 48;

    /**
     * Pickup range (distance within which player can collect).
     */
    public static final float PICKUP_RANGE = 64f;

    /**
     * Power-up lifespan in frames (auto-remove if not collected).
     */
    private static final int DEFAULT_LIFESPAN = 300;

    /**
     * Whether the power-up is active (can be picked up).
     */
    private boolean active = true;

    // ==================== Fields ====================

    /**
     * Power-up type (HEALTH, ARMOR, AMMO, etc.).
     */
    private PowerUpType type;

    /**
     * Power-up lifespan in frames.
     */
    private int lifespan;

    /**
     * Time remaining until power-up expires.
     */
    private int timeRemaining;

    /**
     * Whether the power-up has been collected.
     */
    protected boolean collected = false;

    /**
     * Creates a new PowerUp with health pickup.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     */
    public PowerUp(int id, float x, float y) {
        this(id, x, y, PowerUpType.HEALTH);
    }

    /**
     * Creates a new PowerUp with custom size.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     */
    public PowerUp(int id, float x, float y, int width, int height) {
        this(id, x, y, POWERUP_WIDTH, POWERUP_HEIGHT, PowerUpType.HEALTH);
    }

    /**
     * Creates a new PowerUp.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param type The power-up type
     */
    public PowerUp(int id, float x, float y, PowerUpType type) {
        super(id, x, y, POWERUP_WIDTH, POWERUP_HEIGHT);
        this.type = type;
        this.lifespan = DEFAULT_LIFESPAN;
        this.timeRemaining = DEFAULT_LIFESPAN;
        this.active = true;
        this.collected = false;
        this.name = "PowerUp: " + type.getName();
    }

    /**
     * Creates a new PowerUp with custom size and type.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     * @param type The power-up type
     */
    public PowerUp(int id, float x, float y, int width, int height, PowerUpType type) {
        super(id, x, y, width, height);
        this.type = type;
        this.lifespan = DEFAULT_LIFESPAN;
        this.timeRemaining = DEFAULT_LIFESPAN;
        this.active = true;
        this.collected = false;
        this.name = "PowerUp: " + type.getName();
    }

    /**
     * Gets the power-up type.
     *
     * @return The power-up type
     */
    public PowerUpType getType() {
        return type;
    }

    /**
     * Sets the power-up type.
     * <p>
     * Note: Changing type resets the lifespan.
     * </p>
     *
     * @param type The new power-up type
     */
    public void setType(PowerUpType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
        this.timeRemaining = lifespan;
        this.name = "PowerUp: " + type.getName();
    }

    /**
     * Gets the remaining lifespan.
     *
     * @return The remaining frames
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Sets the remaining lifespan.
     *
     * @param frames The new remaining frames
     */
    public void setTimeRemaining(int frames) {
        this.timeRemaining = Math.max(0, Math.min(frames, lifespan));
    }

    /**
     * Reduces the lifespan by the given amount.
     *
     * @param frames The amount to reduce by
     */
    public void reduceLifespan(int frames) {
        this.timeRemaining = Math.max(0, this.timeRemaining - frames);
    }

    /**
     * Checks if the power-up is ready to be picked up.
     *
     * @return true if active and not collected
     */
    public boolean canPickup() {
        return active && !collected && timeRemaining > 0;
    }

    /**
     * Picks up the power-up.
     * <p>
     * This method:
     * 1. Marks as collected
     * 2. Decreases remaining time to 0
     * 3. Returns the power-up type for the caller to handle
     * </p>
     *
     * @return The power-up type that was picked up
     */
    public PowerUpType pickup() {
        if (!canPickup()) {
            return null;
        }
        this.collected = true;
        this.timeRemaining = 0;
        return type;
    }

    /**
     * Gets the power-up effect value.
     *
     * @return The effect value
     */
    public int getEffectValue() {
        return type.getEffectValue();
    }

    /**
     * Gets the power-up visual representation.
     *
     * @return The visual representation
     */
    public String getVisual() {
        return type.getVisual();
    }

    /**
     * Gets the power-up category.
     *
     * @return The category
     */
    public String getCategory() {
        return type.getCategory();
    }

    /**
     * Gets a description of this power-up.
     *
     * @return A description string
     */
    public String getDescription() {
        return type.getDescription();
    }

    /**
     * Handles per-frame updates for the power-up.
     * <p>
     * This method:
     * 1. Decrements remaining time
     * 2. Marks as collected and deactivates when time runs out
     * 3. Does nothing when dead
     * </p>
     */
    @Override
    public void update() {
        if (isDead() || collected) {
            return;
        }

        timeRemaining--;
        if (timeRemaining <= 0) {
            // Power-up expired - mark as collected and inactive
            collected = true;
            active = false;
        }
    }

    /**
     * Gets whether the power-up is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the power-up is active.
     *
     * @param active Whether to be active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the power-up lifespan.
     *
     * @return The lifespan in frames
     */
    public int getLifespan() {
        return lifespan;
    }

    /**
     * Sets the power-up lifespan.
     * <p>
     * Lifespan is clamped to a range of 60-999999 frames.
     * Minimum 60 frames ensures power-ups don't expire too quickly.
     * Maximum 999999 frames provides unlimited duration.
     * </p>
     *
     * @param lifespan The new lifespan
     */
    public void setLifespan(int lifespan) {
        this.lifespan = Math.max(60, Math.min(lifespan, 999999));
        this.timeRemaining = this.lifespan;
    }

    /**
     * Sets the power-up lifespan with a default minimum.
     * <p>
     * Use this to set a very long lifespan that won't expire.
     * </p>
     *
     * @param lifespan The new lifespan (minimum 60 frames)
     * @deprecated Use setLifespan(int) directly for flexibility
     */
    @Deprecated
    public void setLifespanLong(int lifespan) {
        this.lifespan = Math.max(60, Math.min(lifespan, 999999));
        this.timeRemaining = this.lifespan;
    }

    /**
     * Sets the power-up as inactive.
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Revives the power-up.
     * <p>
     * Restores the power-up to active state with full lifespan.
     * Only works if the power-up has not been collected.
     * </p>
     */
    public void resurrect() {
        if (collected) {
            return;
        }
        this.active = true;
        this.health = getMaxHealth();
        this.timeRemaining = lifespan;
    }

    /**
     * Marks the power-up as dead.
     * <p>
     * This is an override of the Entity.die() method that does not set the
     * collected flag, since collected tracks pickup status, not death status.
     * </p>
     */
    public void die() {
        this.active = false;
        this.health = 0;
    }

    @Override
    public String toString() {
        return "PowerUp{" +
                "id=" + getId() +
                ", type=" + type +
                ", timeRemaining=" + timeRemaining +
                ", active=" + active +
                ", collected=" + collected +
                ", position=" + position +
                '}';
    }
}
