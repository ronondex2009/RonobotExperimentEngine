package org.ronobot.engine.entity;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.ronobot.engine.math.Velocity;

/**
 * Enemy entity with AI behavior.
 * <p>
 * Represents an enemy in the game that can move towards the player,
 * fire projectiles, and take damage.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class EnemyEntity extends Entity {

    // ==================== Constants ==================

    /**
     * Basic attack damage.
     */
    private static final int BASE_DAMAGE = 25;

    /**
     * Attack range in pixels.
     */
    private static final int ATTACK_RANGE = 128;

    /**
     * Movement speed.
     */
    private static final float MOVE_SPEED = 2f;

    /**
     * Maximum attack cooldown in frames.
     */
    public static final int ATTACK_COOLDOWN_MAX = 60;

    /**
     * Cooldown frames until next attack.
     */
    private int attackCooldown = ATTACK_COOLDOWN_MAX;

    /**
     * Maximum health for enemy.
     */
    public static final int MAX_HEALTH = 100;

    /**
     * Current health.
     */
    private int health = MAX_HEALTH;

    /**
     * Target entity (usually the player).
     */
    private Entity target;

    /**
     * Creates a new EnemyEntity at the given position.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     */
    public EnemyEntity(int id, float x, float y) {
        this(id, x, y, "Enemy");
    }

    /**
     * Creates a new EnemyEntity with custom size.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     */
    public EnemyEntity(int id, float x, float y, int width, int height) {
        this(id, x, y, width, height, "Enemy");
    }

    /**
     * Creates a new EnemyEntity with custom name.
     * <p>
     * Note: The name parameter is ignored; enemies always have the name "Enemy".
     * </p>
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param name The entity name (ignored, always uses "Enemy")
     */
    public EnemyEntity(int id, float x, float y, String name) {
        super(id, x, y, 32, 32, "Enemy");
        this.health = MAX_HEALTH;
        this.target = null;
    }

    /**
     * Creates a new EnemyEntity with custom size and name.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     * @param name The entity name
     */
    public EnemyEntity(int id, float x, float y, int width, int height, String name) {
        super(id, x, y, width, height, name);
        this.health = MAX_HEALTH;
        this.target = null;
    }

    /**
     * Gets the current health.
     *
     * @return The current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health.
     * <p>
     * Note: This method calls die() when health is set to 0 or less.
     * </p>
     *
     * @param health The new health
     */
    @Override
    public void setHealth(int health) {
        this.health = Math.min(Math.max(health, 0), MAX_HEALTH);
        if (health == 0) {
            die();
        }
    }

    /**
     * Gets the target entity.
     *
     * @return The target entity, or null if no target
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Sets the target entity.
     *
     * @param target The new target
     */
    public void setTarget(Entity target) {
        this.target = target;
    }

    /**
     * Gets the attack cooldown.
     *
     * @return The current attack cooldown
     */
    public int getAttackCooldown() {
        return attackCooldown;
    }

    /**
     * Sets the attack cooldown.
     *
     * @param cooldown The new attack cooldown (must be non-negative)
     */
    public void setAttackCooldown(int cooldown) {
        this.attackCooldown = Math.max(cooldown, 0);
    }

    /**
     * Reduces attack cooldown by the given amount.
     *
     * @param frames The amount to reduce by
     */
    public void reduceAttackCooldown(int frames) {
        this.attackCooldown = Math.max(0, this.attackCooldown - frames);
    }

    /**
     * Attacks the target if in range and valid.
     * <p>
     * This method:
     * 1. Checks if a target exists
     * 2. Checks if the target is in attack range
     * 3. Checks if the target is alive
     * 4. Applies damage to the target
     * 5. Sets cooldown
     * </p>
     *
     * @return true if an attack was performed
     */
    public boolean attack() {
        if (target == null || isDead()) {
            return false;
        }

        // Check if target is alive
        if (target.isDead()) {
            return false;
        }

        // Check attack range
        double distance = getDistanceTo(target);
        if (distance > ATTACK_RANGE) {
            return false;
        }

        // Apply damage
        target.takeDamage(BASE_DAMAGE);

        // Set cooldown
        attackCooldown = ATTACK_COOLDOWN_MAX;

        return true;
    }

    /**
     * Gets whether the enemy can attack (cooldown is ready).
     *
     * @return true if attack is ready
     */
    public boolean canAttack() {
        return attackCooldown <= 0;
    }

    /**
     * Handles per-frame updates for the enemy.
     * <p>
     * This method:
     * 1. Decrements attack cooldown
     * 2. Finds and sets target if needed
     * 3. Moves towards target if ALIVE and WITHIN ATTACK_RANGE
     * 4. Checks if enemy is dead and skips update if so
     * </p>
     */
    @Override
    public void update() {
        if (isDead()) {
            return;
        }

        // Decrement cooldown
        if (attackCooldown > 0) {
            attackCooldown--;
        }

        // Find new target if needed
        findTarget();

        // Move towards target only if alive and within ATTACK_RANGE
        if (target != null && !target.isDead()) {
            double distance = getDistanceTo(target);

            // Only move if target is within attack range
            if (distance <= ATTACK_RANGE && distance > 0) {
                // Move towards target
                float dx = (float) (target.getPosition().getX() - getPosition().getX());
                float dy = (float) (target.getPosition().getY() - getPosition().getY());
                float dist = (float) Math.sqrt(dx * dx + dy * dy);

                // Normalize and move
                if (dist > 0) {
                    move(dx / dist * MOVE_SPEED, dy / dist * MOVE_SPEED);
                }
            }
        }
    }

    /**
     * Finds a new target to attack.
     * <p>
     * This method looks for active entities and sets them as targets.
     * </p>
     */
    private void findTarget() {
        // Keep current target if alive, otherwise find a new one
        // For now, this is a stub - in a full implementation, would find player
        // or nearest enemy for team mechanics
    }

    /**
     * Gets the distance to another entity.
     *
     * @param other The other entity
     * @return The Euclidean distance
     */
    private double getDistanceTo(Entity other) {
        if (other == null || getPosition() == null || other.getPosition() == null) {
            return Double.MAX_VALUE;
        }
        double dx = other.getPosition().getX() - getPosition().getX();
        double dy = other.getPosition().getY() - getPosition().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Gets the name of this entity type.
     *
     * @return "EnemyEntity"
     */
    @Override
    public String toString() {
        return "EnemyEntity{" +
                "name='" + getName() + '\'' +
                ", health=" + health +
                ", maxHealth=" + MAX_HEALTH +
                ", attackCooldown=" + attackCooldown +
                ", target=" + (target != null ? target.getName() : "null") +
                '}';
    }
}