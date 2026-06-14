package org.ronobot.engine.core;

import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.ronobot.engine.math.Velocity;
import org.ronobot.engine.math.Rectangle;

/**
 * Base class for all game entities.
 * <p>
 * Each entity has:
 * - A unique ID for identification
 * - A position (x, y) for placement in the world
 * - A size (width, height) for bounding box collision
 * - Health management with damage/healing support
 * - Lifecycle management (alive/dead states)
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class Entity {

    // ==================== Fields ====================

    /**
     * Unique entity ID.
     */
    private int id;

    /**
     * Entity name for debugging and display.
     */
    private String name;

    /**
     * Entity position.
     */
    private Position position;

    /**
     * Entity size.
     */
    private Size size;

    /**
     * Entity velocity.
     */
    private Velocity velocity;

    /**
     * Frame counter for tracking entity lifetime.
     */
    private int frameCount = 0;

    /**
     * Whether the entity is active.
     */
    private boolean active = true;

    /**
     * Health of the entity.
     */
    private int health;

    /**
     * Maximum health of the entity.
     */
    private int maxHealth = 100;

    /**
     * Armor of the entity.
     */
    private int armor = 0;

    /**
     * Damage taken in current combat.
     */
    private int damageTaken = 0;

    // ==================== Constructors ==================

    /**
     * Creates a new Entity with the given ID.
     * <p>
     * This constructor sets a default position at (0, 0) and size of 1x1.
     * </p>
     *
     * @param id The entity ID
     */
    public Entity(int id) {
        this(id, 0f, 0f, 1, 1);
    }

    /**
     * Creates a new Entity with the given ID, position x, y, width, and height.
     * <p>
     * This constructor is deprecated in favor of the one that includes frameCount.
     * </p>
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     */
    public Entity(int id, float x, float y, int width, int height) {
        this(id, x, y, width, height, 0);
    }

    /**
     * Creates a new Entity with the given ID, position x, y, width, height, and frame count.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     * @param frameCount The initial frame count
     */
    public Entity(int id, float x, float y, int width, int height, int frameCount) {
        this.id = id;
        this.name = getClass().getSimpleName();
        this.position = Position.of(x, y);
        this.size = new Size(width, height);
        this.velocity = Velocity.ZERO;
        this.health = maxHealth;
        this.frameCount = frameCount;
    }

    /**
     * Creates a new Entity with the given ID, position x, y, and size.
     * <p>
     * This constructor is a convenience method that avoids the need to create
     * a Position object separately.
     * </p>
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param size The initial size
     */
    public Entity(int id, float x, float y, Size size) {
        this.id = id;
        this.name = getClass().getSimpleName();
        this.position = Position.of(x, y);
        this.size = size;
        this.velocity = Velocity.ZERO;
        this.health = maxHealth;
    }

    /**
     * Creates a new Entity with the given ID, position, and size.
     *
     * @param id The entity ID
     * @param position The initial position
     * @param size The initial size
     */
    public Entity(int id, Position position, Size size) {
        this.id = id;
        this.name = getClass().getSimpleName();
        this.position = position;
        this.size = size;
        this.velocity = Velocity.ZERO;
        this.health = maxHealth;
    }

    /**
     * Creates a new Entity with the given ID, position, size, and name.
     * <p>
     * This constructor allows custom naming for debugging.
     * </p>
     *
     * @param id The entity ID
     * @param position The initial position
     * @param size The initial size
     * @param name The entity name
     */
    public Entity(int id, Position position, Size size, String name) {
        this.id = id;
        this.name = name != null ? name : getClass().getSimpleName();
        this.position = position;
        this.size = size;
        this.velocity = Velocity.ZERO;
        this.health = maxHealth;
    }

    /**
     * Creates a new Entity with the given ID, position x, y, size, and name.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     * @param name The entity name
     */
    public Entity(int id, float x, float y, int width, int height, String name) {
        this.id = id;
        this.name = name != null ? name : getClass().getSimpleName();
        this.position = Position.of(x, y);
        this.size = new Size(width, height);
        this.velocity = Velocity.ZERO;
        this.health = maxHealth;
    }

    // ==================== Getters/Setters ==================

    /**
     * Gets the entity ID.
     *
     * @return The entity ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the entity ID.
     *
     * @param id The new entity ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the entity name.
     *
     * @return The entity name
     */
    public String getName() {
        return name;
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
     * Gets the entity position.
     *
     * @return The entity position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the entity position.
     * <p>
     * Updates both the position and collision box.
     * </p>
     *
     * @param x The x position
     * @param y The y position
     */
    public void setPosition(float x, float y) {
        if (position != null) {
            position = Position.of(x, y);
        }
    }

    /**
     * Sets the entity size.
     * <p>
     * Updates both the size and collision box.
     * </p>
     *
     * @param width The width
     * @param height The height
     */
    public void setSize(int width, int height) {
        if (size != null) {
            size = new Size(width, height);
        }
    }

    /**
     * Sets the entity velocity.
     *
     * @param velocity The new velocity
     */
    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets the entity velocity.
     *
     * @return The entity velocity
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * Gets the entity collision box.
     * <p>
     * Returns a new Rectangle representing the entity's bounding box.
     * </p>
     *
     * @return A new Rectangle for the entity's collision box
     */
    public Rectangle getCollisionBox() {
        if (position != null && size != null) {
            return Rectangle.of(position, size);
        }
        return Rectangle.unit();
    }

    /**
     * Gets the entity size.
     * <p>
     * Returns a new Size object representing the entity's dimensions.
     * </p>
     *
     * @return A new Size for the entity
     */
    public Size getSize() {
        return size != null ? new Size(size.getWidth(), size.getHeight()) : Size.unit();
    }

    /**
     * Gets the x position of the entity.
     *
     * @return The x position
     */
    public float getX() {
        return position != null ? position.getX() : 0f;
    }

    /**
     * Gets the y position of the entity.
     *
     * @return The y position
     */
    public float getY() {
        return position != null ? position.getY() : 0f;
    }

    /**
     * Gets the width of the entity.
     *
     * @return The width
     */
    public int getWidth() {
        return size != null ? size.getWidth() : 0;
    }

    /**
     * Gets the height of the entity.
     *
     * @return The height
     */
    public int getHeight() {
        return size != null ? size.getHeight() : 0;
    }

    /**
     * Gets whether the entity is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the entity is active.
     *
     * @param active Whether to be active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the health of the entity.
     *
     * @return The current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health of the entity.
     * <p>
     * Note: When health is set to 0 or less, the entity is marked as dead.
     * </p>
     *
     * @param health The new health
     */
    public void setHealth(int health) {
        this.health = Math.min(Math.max(health, 0), maxHealth);
        if (health == 0) {
            die();
        }
    }

    /**
     * Gets the maximum health.
     *
     * @return The maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the maximum health.
     *
     * @param maxHealth The new maximum health
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = Math.min(maxHealth, Integer.MAX_VALUE - 1);
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
    }

    /**
     * Gets the armor of the entity.
     *
     * @return The armor value
     */
    public int getArmor() {
        return armor;
    }

    /**
     * Sets the armor of the entity.
     *
     * @param armor The new armor
     */
    public void setArmor(int armor) {
        this.armor = Math.max(armor, 0);
    }

    /**
     * Gets the damage taken.
     *
     * @return The damage taken
     */
    public int getDamageTaken() {
        return damageTaken;
    }

    /**
     * Resets damage taken.
     */
    public void resetDamageTaken() {
        damageTaken = 0;
    }

    // ==================== Damage/Healing ==================

    /**
     * Takes damage.
     * <p>
     * Note: This method calls die() when health reaches 0.
     * </p>
     *
     * @param damage The amount of damage
     * @return The remaining health
     */
    public int takeDamage(int damage) {
        if (!active || isDead()) {
            return health;
        }
        health = Math.max(health - damage, 0);
        if (health == 0) {
            die();
        }
        return health;
    }

    /**
     * Heals the entity.
     *
     * @param heal The amount to heal
     * @return The new health
     */
    public int heal(int heal) {
        if (!active) {
            return health;
        }
        health = Math.min(health + heal, maxHealth);
        return health;
    }

    /**
     * Gets whether the entity is dead.
     * <p>
     * An entity is dead when health is zero or less.
     * </p>
     *
     * @return true if dead
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Marks the entity as dead.
     */
    public void die() {
        active = false;
        health = 0;
    }

    /**
     * Revives the entity at max health.
     */
    public void resurrect() {
        active = true;
        this.health = maxHealth;
    }

    // ==================== Movement ==================

    /**
     * Moves the entity by the given delta.
     * <p>
     * Updates both position and collision box.
     * </p>
     *
     * @param dx The x delta
     * @param dy The y delta
     */
    public void move(float dx, float dy) {
        if (position != null) {
            position = Position.of(position.getX() + dx, position.getY() + dy);
        }
    }

    /**
     * Updates the entity for one frame.
     * <p>
     * This method handles per-frame updates like velocity-based movement.
     * Currently a no-op in the base implementation.
     * </p>
     */
    public void update() {
        if (velocity != null) {
            float vx = velocity.getX();
            float vy = velocity.getY();

            if (vx != 0f || vy != 0f) {
                move(vx, vy);
            }
        }
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", name=" + name +
                ", position=" + position +
                ", velocity=" + velocity +
                ", active=" + active +
                ", health=" + health +
                ", maxHealth=" + maxHealth +
                ", armor=" + armor +
                ", damageTaken=" + damageTaken +
                '}';
    }

    @Override
    public int hashCode() {
        return 31 * Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }
}
