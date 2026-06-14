package org.ronobot.engine.entity;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

import java.util.Random;

/**
 * Represents a projectile entity.
 * <p>
 * A projectile is an entity that travels through the world,
 * typically fired by a player or enemy.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
 */
public class Projectile extends Entity {

    /**
     * Creates a new Projectile.
     * <p>
     * The projectile starts at the given position with the given size.
     * Velocity is not set by default.
     * </p>
     *
     * @param position The initial position
     * @param size The initial size
     * @param damage The damage dealt on impact
     */
    public Projectile(Position position, Size size, int damage) {
        this(new Random(), position, size, damage);
    }

    /**
     * Creates a new Projectile with custom name.
     *
     * @param position The initial position
     * @param size The initial size
     * @param damage The damage dealt on impact
     * @param name The custom name
     */
    public Projectile(Position position, Size size, int damage, String name) {
        this(new Random(), position, size, damage, name);
    }

    /**
     * Creates a new Projectile with a random ID.
     *
     * @param random The random number generator for ID generation
     * @param position The initial position
     * @param size The initial size
     * @param damage The damage dealt on impact
     */
    public Projectile(Random random, Position position, Size size, int damage) {
        super(random.nextInt(), position, size);
        setDamage(damage);
        this.lifetime = 60;
    }

    /**
     * Creates a new Projectile with a random ID and custom name.
     *
     * @param random The random number generator for ID generation
     * @param position The initial position
     * @param size The initial size
     * @param damage The damage dealt on impact
     * @param name The custom name
     */
    public Projectile(Random random, Position position, Size size, int damage, String name) {
        super(random.nextInt(), position, size, name);
        setDamage(damage);
        this.lifetime = 60;
        setName(name);
    }

    /**
     * Gets the damage dealt by this projectile.
     *
     * @return The damage value
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the damage dealt by this projectile.
     *
     * @param damage The damage value
     */
    public void setDamage(int damage) {
        this.damage = Math.max(damage, 0);
    }

    /**
     * Gets the lifetime of this projectile.
     *
     * @return The lifetime in frames
     */
    public int getLifetime() {
        return lifetime;
    }

    /**
     * Sets the lifetime of this projectile.
     *
     * @param lifetime The lifetime in frames
     */
    public void setLifetime(int lifetime) {
        this.lifetime = Math.max(lifetime, 0);
        if (lifetime <= 0) {
            die();
        }
    }

    /**
     * Gets the remaining lifetime of this projectile.
     *
     * @return The remaining lifetime in frames
     */
    public int getRemainingLifetime() {
        return Math.max(0, lifetime - frameCount);
    }

    /**
     * Sets the frame counter for testing purposes.
     *
     * @param frameCount The frame count
     */
    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    /**
     * Handles per-frame updates for the projectile.
     * <p>
     * Decrements the lifetime counter and marks the projectile as dead
     * if the lifetime reaches zero.
     * </p>
     */
    @Override
    public void update() {
        super.update();
        if (lifetime <= 0) {
            die();
            return;
        }
        frameCount++;
        lifetime--;
        if (lifetime <= 0) {
            die();
        }
    }

    /**
     * Represents the damage dealt by this projectile.
     */
    private int damage;

    /**
     * Represents the lifetime of this projectile.
     */
    private int lifetime = 60;

    /**
     * Frame counter for tracking projectile lifetime.
     */
    private int frameCount = 0;

    @Override
    public String toString() {
        return "Projectile{" +
                "damage=" + damage +
                ", lifetime=" + lifetime +
                ", frameCount=" + frameCount +
                ", damageTaken=" + getDamageTaken() +
                ", super=" + super.toString() +
                '}';
    }
}
