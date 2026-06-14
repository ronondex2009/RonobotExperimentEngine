package org.ronobot.engine.entity;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.ronobot.engine.math.Velocity;

/**
 * Enemy entity with AI behavior.
 * <p>
 * Represents an enemy in the game that can move, patrol, fire projectiles,
 * and take damage. Supports multiple enemy types (Zombie, Demon, Knight, Imp, Baron).
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
     * Movement speed base value.
     */
    private static final float BASE_MOVE_SPEED = 2f;

    /**
     * Maximum attack cooldown in frames.
     */
    public static final int ATTACK_COOLDOWN_MAX = 60;

    /**
     * Maximum health for enemy.
     */
    public static final int MAX_HEALTH = 100;

    // Current health is managed through the inherited Entity.health field.
    // Use setHealth(), takeDamage(), and heal() methods instead of direct field access.

    /**
     * Target entity (usually the player).
     */
    private Entity target;

    /**
     * Enemy type (for varied behavior: Zombie, Demon, Knight, etc.).
     */
    private EnemyType type = EnemyType.ZOMBIE;

    /**
     * Sound sensitivity (0-1, 0 = immune, 1 = full effect).
     */
    private float soundSensitivity = 1.0f;

    /**
     * Last sound heard timestamp (frame count).
     */
    private int lastSoundTime = -1;

    /**
     * Sound reaction cooldown.
     */
    private int soundReactionCooldown = 0;

    /**
     * Whether the enemy is currently patrolling.
     */
    private boolean isPatrolling = false;

    /**
     * Patrol position (for patrol behavior).
     */
    private Position patrolPosition;

    /**
     * Patrol direction (1 = moving forward, -1 = returning).
     */
    private int patrolDirection = 1;

    /**
     * Sound source entity.
     */
    private Entity soundSource;

    /**
     * Cooldown frames until next attack.
     */
    private int attackCooldown = ATTACK_COOLDOWN_MAX;

    // ==================== Constructors ==================

    /**
     * Creates a new EnemyEntity at the given position with default Zombie behavior.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     */
    public EnemyEntity(int id, float x, float y) {
        this(id, x, y, EnemyType.ZOMBIE);
    }

    /**
     * Creates a new EnemyEntity with custom size and default Zombie behavior.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     */
    public EnemyEntity(int id, float x, float y, int width, int height) {
        this(id, x, y, width, height, EnemyType.ZOMBIE);
    }

    /**
     * Creates a new EnemyEntity with default Zombie behavior and custom name.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param name The entity name
     */
    public EnemyEntity(int id, float x, float y, String name) {
        super(id, x, y, 32, 32, EnemyType.ZOMBIE.name());
        this.health = EnemyEntity.MAX_HEALTH;
        this.name = "Enemy";
    }

    /**
     * Sets the entity name.
     *
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Creates a new EnemyEntity with custom size, name, and default Zombie behavior.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     * @param name The entity name
     */
    public EnemyEntity(int id, float x, float y, int width, int height, String name) {
        this(id, x, y, width, height, EnemyType.ZOMBIE);
        this.name = name;
    }

    /**
     * Creates a new EnemyEntity with default Zombie behavior.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param enemyType The enemy type
     */
    public EnemyEntity(int id, float x, float y, EnemyType enemyType) {
        this(id, x, y, 32, 32, enemyType);
    }

    /**
     * Creates a new EnemyEntity with custom size.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     * @param enemyType The enemy type
     */
    public EnemyEntity(int id, float x, float y, int width, int height, EnemyType enemyType) {
        super(id, x, y, width, height, enemyType.name());
        this.type = enemyType;
        this.health = (int) (enemyType.getBaseHealth() * enemyType.getHealthMultiplier());
        this.attackCooldown = enemyType.getCooldownFrames();
        this.soundSensitivity = enemyType.getSoundSensitivity();
        this.patrolPosition = Position.of(x, y);
        this.isPatrolling = enemyType.getPatrolRange() > 0;
        this.soundReactionCooldown = (int) (this.attackCooldown * 0.5);
    }

    // ==================== Getters/Setters ==================

    /**
     * Gets the current enemy type.
     *
     * @return The enemy type
     */
    public EnemyType getType() {
        return type;
    }

    /**
     * Sets the enemy type.
     * <p>
     * Note: Setting a new type recalculates health, cooldown, and patrol behavior
     * based on the new type's multipliers.
     * </p>
     *
     * @param enemyType The new enemy type
     */
    public void setType(EnemyType enemyType) {
        if (enemyType == null) {
            throw new IllegalArgumentException("EnemyType cannot be null");
        }
        this.type = enemyType;
        this.health = (int) (enemyType.getBaseHealth() * enemyType.getHealthMultiplier());
        this.attackCooldown = enemyType.getCooldownFrames();
        this.soundSensitivity = enemyType.getSoundSensitivity();
        this.patrolPosition = Position.of(getX(), getY());
        this.isPatrolling = enemyType.getPatrolRange() > 0;
        this.soundReactionCooldown = (int) (this.attackCooldown * 0.5);
        this.setName("Enemy");
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
     * 2. Handles sound reactions
     * 3. Finds and sets target if needed
     * 4. Implements patrol behavior if enabled
     * 5. Moves towards target if ALIVE and WITHIN ATTACK_RANGE
     * 6. Checks if enemy is dead and skips update if so
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

        // Handle sound reactions
        processSoundReaction();

        // Find new target if needed
        findTarget();

        // Handle patrol behavior if enabled
        if (isPatrolling && patrolPosition != null) {
            handlePatrol();
        }

        // Move towards target only if alive and WITHIN ATTACK_RANGE
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
            } else if (isPatrolling && patrolPosition != null) {
                // If out of range and patrolling, patrol back and forth
                handlePatrolMovement();
            }
        }
    }

    /**
     * Processes sound reactions from nearby entities.
     * <p>
     * This method:
     * 1. Checks if a sound source exists
     * 2. Applies reaction if cooldown allows
     * 3. Manages reaction cooldown
     * </p>
     */
    private void processSoundReaction() {
        // Check if cooldown allows reaction
        if (soundReactionCooldown <= 0 || soundSource == null) {
            return;
        }

        // Reset cooldown
        soundReactionCooldown = 0;

        // Determine reaction based on sensitivity
        float sensitivity = type.getSoundSensitivity();
        if (sensitivity > 0.5f) {
            // High sensitivity: move away from sound briefly and get distracted
            // Find direction away from sound source
            Position sourcePos = soundSource.getPosition();
            Position myPos = getPosition();
            
            float dx = sourcePos.getX() - myPos.getX();
            float dy = sourcePos.getY() - myPos.getY();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            
            if (dist > 0 && dist < 500) {
                // Move away from sound
                if (dx != 0) {
                    move(-dx / dist * MOVE_SPEED * 1.5f, 0);
                }
                if (dy != 0) {
                    move(0, -dy / dist * MOVE_SPEED * 1.5f);
                }
            }
        } else if (sensitivity > 0) {
            // Medium sensitivity: slight distraction
            // Flash a visual indicator or slightly change behavior
        }
    }

    /**
     * Handles patrol behavior.
     * <p>
     * Patrols between a starting position and patrol position.
     * </p>
     */
    private void handlePatrol() {
        if (patrolPosition == null) {
            return;
        }

        float targetX = patrolPosition.getX();
        float targetY = patrolPosition.getY();

        float currentX = getPosition().getX();
        float currentY = getPosition().getY();

        // Calculate patrol direction
        if (isPatrolling) {
            // Move towards patrol position
            if (currentX < targetX - 4 || currentX > targetX + 4) {
                patrolDirection = 1;
            } else if (Math.abs(currentX - targetX) < 4) {
                // Reached patrol position, wait briefly then return
                // In a full implementation, would add patrol delay
            }
        }
    }

    /**
     * Handles patrol movement.
     * <p>
     * Moves left/right based on patrol direction.
     * </p>
     */
    private void handlePatrolMovement() {
        if (patrolPosition == null) {
            return;
        }

        // Simple back-and-forth patrol
        // In a full implementation, would add complex patrol patterns
        float patrolX = patrolPosition.getX();
        float currentX = getPosition().getX();

        if (currentX < patrolX) {
            move(MOVE_SPEED, 0);
        } else if (currentX > patrolX + type.getPatrolRange()) {
            move(-MOVE_SPEED, 0);
        }
    }

    /**
     * Finds a new target to attack.
     * <p>
     * This method looks for active entities and sets them as targets.
     * Currently prioritizes player over other enemies.
     * </p>
     */
    private void findTarget() {
        // Keep current target if alive
        if (target != null && !target.isDead()) {
            return;
        }

        // Find player target if exists
        if (game != null) {
            this.target = game.getPlayer();
        }
        
        // Fallback: find nearest enemy for team mechanics
        // For now, this is a stub for future multiplayer/team AI
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
     * Sets the sound source for reaction testing.
     *
     * @param source The sound source entity
     */
    public void setSoundSource(Entity source) {
        this.soundSource = source;
    }

    /**
     * Gets the sound source.
     *
     * @return The sound source entity
     */
    public Entity getSoundSource() {
        return soundSource;
    }

    /**
     * Gets the sound reaction cooldown.
     *
     * @return The cooldown frames
     */
    public int getSoundReactionCooldown() {
        return soundReactionCooldown;
    }

    /**
     * Sets the sound reaction cooldown.
     *
     * @param cooldown The new cooldown frames
     */
    public void setSoundReactionCooldown(int cooldown) {
        this.soundReactionCooldown = Math.max(cooldown, 0);
    }

    /**
     * Increments the sound reaction cooldown.
     *
     * @param frames The amount to increment by
     */
    public void incrementSoundReactionCooldown(int frames) {
        this.soundReactionCooldown = Math.min(this.soundReactionCooldown + frames, this.attackCooldown);
    }

    /**
     * Gets the sound sensitivity.
     *
     * @return The sensitivity value (0-1)
     */
    public float getSoundSensitivity() {
        return soundSensitivity;
    }

    /**
     * Sets the sound sensitivity.
     *
     * @param sensitivity The new sensitivity (0-1)
     */
    public void setSoundSensitivity(float sensitivity) {
        this.soundSensitivity = Math.max(0f, Math.min(1f, sensitivity));
    }

    /**
     * Gets whether the enemy is patrolling.
     *
     * @return true if patrolling
     */
    public boolean isPatrolling() {
        return isPatrolling;
    }

    /**
     * Sets whether the enemy is patrolling.
     *
     * @param patrolling Whether to patrol
     */
    public void setPatrolling(boolean patrolling) {
        this.isPatrolling = patrolling;
    }

    /**
     * Gets the patrol position.
     *
     * @return The patrol position
     */
    public Position getPatrolPosition() {
        return patrolPosition;
    }

    /**
     * Sets the patrol position.
     *
     * @param position The patrol position
     */
    public void setPatrolPosition(Position position) {
        this.patrolPosition = position;
    }

    /**
     * Gets the movement speed.
     *
     * @return The movement speed
     */
    public float getMoveSpeed() {
        return BASE_MOVE_SPEED * type.getMoveSpeedMultiplier();
    }

    /**
     * Gets the movement speed constant.
     *
     * @return The movement speed constant
     */
    private static final float MOVE_SPEED = 2f;

    /**
     * Gets the name of this entity type.
     *
     * @return The type name
     */
    @Override
    public String toString() {
        return "EnemyEntity{" +
                "name='" + getName() + '\'' +
                ", type=" + type +
                ", health=" + getHealth() +
                ", maxHealth=" + getMaxHealth() +
                ", attackCooldown=" + attackCooldown +
                ", target=" + (target != null ? target.getName() : "null") +
                ", isPatrolling=" + isPatrolling +
                ", patrolPosition=" + (patrolPosition != null ? patrolPosition : "null") +
                ", soundSensitivity=" + soundSensitivity +
                '}';
    }
}
