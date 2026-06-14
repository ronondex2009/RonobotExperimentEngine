package org.ronobot.engine.physics;

import org.ronobot.engine.collision.CollisionManager;
import org.ronobot.engine.collision.CollisionResult;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Rectangle;
import org.ronobot.engine.math.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Physics engine for collision resolution and damage calculation.
 * <p>
 * Handles position correction, damage application, and entity separation.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class PhysicsEngine {

    /**
     * No resolution action - skip.
     */
    public static final int NONE = 0;

    /**
     * Position correction resolution.
     */
    public static final int POSITION_CORRECTION = 1;

    /**
     * Damage resolution.
     */
    public static final int DAMAGE = 2;

    /**
     * Item pickup resolution.
     */
    public static final int ITEM_PICKUP = 3;

    /**
     * Trigger activation resolution.
     */
    public static final int TRIGGER_ACTIVATION = 4;

    /**
     * Creates a new PhysicsEngine.
     */
    public PhysicsEngine() {
    }

    /**
     * Updates all entities and processes collisions.
     *
     * @param collisionManager The collision manager
     */
    public void update(CollisionManager collisionManager) {
        List<CollisionResult> collisions = collisionManager.findCollisions();
        for (CollisionResult collision : collisions) {
            resolve(collision, collisionManager);
        }
    }

    /**
     * Processes the game state and updates physics.
     *
     * @param game The game instance
     */
    public void process(Game game) {
        CollisionManager collisionManager = game.getCollisionManager();
        if (game == null || collisionManager == null) {
            return;
        }
        update(collisionManager);
        // Frame increment handled in Game.update()
    }

    /**
     * Resolves a collision between two entities.
     *
     * @param collision The collision result
     * @param collisionManager The collision manager
     */
    private void resolve(CollisionResult collision, CollisionManager collisionManager) {
        Entity entityA = collision.getEntityA();
        Entity entityB = collision.getEntityB();

        // Calculate overlap
        Position positionA = entityA.getPosition();
        Position positionB = entityB.getPosition();
        Size sizeA = entityA.getSize();
        Size sizeB = entityB.getSize();

        float xOverlap = 0;
        float yOverlap = 0;

        // Calculate overlap in x
        float minX = Math.min(positionA.getX(), positionB.getX());
        float maxX = Math.max(positionA.getX() + sizeA.getWidth(), positionB.getX() + sizeB.getWidth());
        float xSpan = maxX - minX;
        float totalX = sizeA.getWidth() + sizeB.getWidth();
        xOverlap = (xSpan - totalX) / 2;

        // Calculate overlap in y
        float minY = Math.min(positionA.getY(), positionB.getY());
        float maxY = Math.max(positionA.getY() + sizeA.getHeight(), positionB.getY() + sizeB.getHeight());
        float ySpan = maxY - minY;
        float totalY = sizeA.getHeight() + sizeB.getHeight();
        yOverlap = (ySpan - totalY) / 2;

        // Apply position correction
        if (xOverlap != 0) {
            entityA.move(-xOverlap, 0);
            entityB.move(xOverlap, 0);
        }

        if (yOverlap != 0) {
            entityA.move(0, -yOverlap);
            entityB.move(0, yOverlap);
        }

        // Apply damage if applicable
        if (entityA.isActive() && entityB.isActive()) {
            int damageA = calculateDamage(entityA, entityB);
            int damageB = calculateDamage(entityB, entityA);

            if (damageA > 0) {
                entityA.takeDamage(damageA);
            }
            if (damageB > 0) {
                entityB.takeDamage(damageB);
            }
        }
    }

    /**
     * Calculates damage from one entity to another.
     *
     * @param from The attacking entity
     * @param to The attacked entity
     * @return The damage amount
     */
    private int calculateDamage(Entity from, Entity to) {
        if (from.isDead() || to.isDead()) {
            return 0;
        }

        // Simple damage calculation based on entity type
        // In a full implementation, this would consider weapons, stats, etc.
        return 10;
    }
}
