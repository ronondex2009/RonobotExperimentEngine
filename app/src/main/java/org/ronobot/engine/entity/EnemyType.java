package org.ronobot.engine.entity;

import org.ronobot.engine.math.Position;

/**
 * Enemy type enumeration for varied enemy AI behaviors.
 * <p>
 * Each enemy type has unique characteristics:
 * - Zombie: Basic slow walker that moves directly to player
 * - Demon: Fast attacker with higher damage
 * - Knight: Tanky unit with higher health and defense
 * - Imp: Fast, fragile unit that attacks from range
 * - Baron: Boss-type enemy with area abilities
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public enum EnemyType {

    /**
     * Basic zombie enemy.
     * <p>
     * Characteristics:
     * - Movement speed: 2.0
     * - Attack damage: 25
     * - Attack cooldown: 60 frames
     * - Health: 100
     * - Patrol range: 100 units
     * - Sound sensitivity: Low
     * </p>
     */
    ZOMBIE(2f, 25, 60, 100, 100, 0f, 0f),

    /**
     * Demon enemy.
     * <p>
     * Characteristics:
     * - Movement speed: 3.5
     * - Attack damage: 40
     * - Attack cooldown: 45 frames
     * - Health: 70
     * - Patrol range: 0
     * - Sound sensitivity: High
     * </p>
     */
    DEMON(3.5f, 40, 45, 70, 0, 1.5f, 1.5f),

    /**
     * Knight enemy.
     * <p>
     * Characteristics:
     * - Movement speed: 1.5
     * - Attack damage: 30
     * - Attack cooldown: 80 frames
     * - Health: 180
     * - Patrol range: 80 units
     * - Sound sensitivity: Medium
     * </p>
     */
    KNIGHT(1.5f, 30, 80, 180, 80, 0.5f, 1.0f),

    /**
     * Imp enemy.
     * <p>
     * Characteristics:
     * - Movement speed: 2.5
     * - Attack damage: 20
     * - Attack cooldown: 40 frames
     * - Health: 60
     * - Patrol range: 0
     * - Sound sensitivity: Medium
     * </p>
     */
    IMP(2.5f, 20, 40, 60, 0, 0.5f, 0.5f),

    /**
     * Baron enemy (boss).
     * <p>
     * Characteristics:
     * - Movement speed: 2.2
     * - Attack damage: 100
     * - Attack cooldown: 90 frames
     * - Health: 500
     * - Patrol range: 150 units
     * - Sound sensitivity: Low
     * </p>
     */
    BARON(2.2f, 100, 90, 500, 150, 0f, 0f);

    // ==================== Fields ==================

    /**
     * Base movement speed.
     */
    private final float moveSpeed;

    /**
     * Attack damage.
     */
    private final float damage;

    /**
     * Attack cooldown in frames.
     */
    private final int cooldownFrames;

    /**
     * Health points.
     */
    private final float health;

    /**
     * Patrol range in units.
     * <p>
     * Zero means aggressive (no patrol), non-zero enables patrol behavior.
     * </p>
     */
    private final int patrolRange;

    /**
     * Sound sensitivity (0 = immune, 1 = full effect).
     */
    private final float soundSensitivity;

    /**
     * Size multiplier (affects sprite scaling).
     */
    private final float sizeMultiplier;

    /**
     * Creates a new EnemyType with the given characteristics.
     *
     * @param moveSpeed        Base movement speed
     * @param damage           Attack damage
     * @param cooldownFrames   Attack cooldown in frames
     * @param health           Health points
     * @param patrolRange      Patrol range (0 = no patrol)
     * @param soundSensitivity Sound sensitivity (0-1)
     * @param sizeMultiplier   Size/visual scale multiplier
     */
    EnemyType(float moveSpeed, float damage, int cooldownFrames,
              float health, int patrolRange, float soundSensitivity, float sizeMultiplier) {
        this.moveSpeed = moveSpeed;
        this.damage = damage;
        this.cooldownFrames = cooldownFrames;
        this.health = health;
        this.patrolRange = patrolRange;
        this.soundSensitivity = soundSensitivity;
        this.sizeMultiplier = sizeMultiplier;
    }

    // ==================== Getters ==================

    /**
     * Gets the movement speed.
     * <p>
     * This is also the base movement speed for this enemy type.
     * </p>
     *
     * @return The base movement speed
     */
    public float getBaseMoveSpeed() {
        return moveSpeed;
    }

    /**
     * Gets the attack damage for this enemy type.
     *
     * @return The attack damage
     */
    public float getBaseDamage() {
        return damage;
    }

    /**
     * Gets the attack cooldown in frames.
     *
     * @return The cooldown frames
     */
    public int getCooldownFrames() {
        return cooldownFrames;
    }

    /**
     * Gets the health for this enemy type.
     *
     * @return The health
     */
    public float getBaseHealth() {
        return health;
    }

    /**
     * Gets the patrol range.
     *
     * @return The patrol range (0 = no patrol)
     */
    public int getPatrolRange() {
        return patrolRange;
    }

    /**
     * Gets the sound sensitivity.
     *
     * @return The sound sensitivity (0-1)
     */
    public float getSoundSensitivity() {
        return soundSensitivity;
    }

    /**
     * Gets the health multiplier for this enemy type.
     * <p>
     * This returns the raw health value as a multiplier.
     * </p>
     *
     * @return The health multiplier (same as base health value)
     */
    public float getHealthMultiplier() {
        return health;
    }

    /**
     * Gets the move speed multiplier for this enemy type.
     * <p>
     * This returns the raw move speed value as a multiplier.
     * </p>
     *
     * @return The move speed multiplier (same as base move speed value)
     */
    public float getMoveSpeedMultiplier() {
        return moveSpeed;
    }

    /**
     * Gets the size multiplier.
     *
     * @return The size multiplier
     */
    public float getSizeMultiplier() {
        return sizeMultiplier;
    }

    /**
     * Gets a human-readable description of this enemy type.
     *
     * @return A description string
     */
    public String getDescription() {
        return switch (this) {
            case ZOMBIE -> "Basic zombie, slow but persistent hunter";
            case DEMON -> "Fast demon with high damage output";
            case KNIGHT -> "Tanky knight with defensive abilities";
            case IMP -> "Fast, fragile imps that attack quickly";
            case BARON -> "Boss-type baron with overwhelming power";
        };
    }

    /**
     * Gets a visual representation of this enemy type.
     *
     * @return A string representing the enemy's visual appearance
     */
    public String getVisualName() {
        return switch (this) {
            case ZOMBIE -> "🧟";
            case DEMON -> "👹";
            case KNIGHT -> "⚔️";
            case IMP -> "👺";
            case BARON -> "👺👑";
        };
    }
}
