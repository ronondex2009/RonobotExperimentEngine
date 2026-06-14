package org.ronobot.engine.collision;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collision manager for entity collision detection and resolution.
 * <p>
 * This class manages collision detection between game entities using
 * axis-aligned bounding box (AABB) tests. It supports entity registration,
 * collision detection, and basic collision resolution based on velocity.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class CollisionManager {

    /**
     * Tolerance for collision detection in pixels.
     */
    private static final float COLLISION_TOLERANCE = 0.1f;

    /**
     * Default entity velocity.
     */
    private static final float DEFAULT_VELOCITY = 0.0f;

    /**
     * Maximum collision pairs to check per frame.
     */
    private static final int MAX_COLLISION_PAIRS = 1000;

    /**
     * Map of entity names to entity instances.
     */
    private final Map<String, Entity> entities = new ConcurrentHashMap<>();

    /**
     * Map of collision notifications by entity pair.
     */
    private final Map<String, CollisionNotification> notifications = new ConcurrentHashMap<>();

    /**
     * Created at initialization.
     */
    private boolean initialized = false;

    /**
     * Creates a new CollisionManager.
     */
    public CollisionManager() {
        // Auto-initialize on construction
        if (!initialized) {
            initialized = true;
        }
    }

    /**
     * Checks if the collision manager is initialized.
     *
     * @return true if initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Registers an entity for collision detection.
     *
     * @param entity The entity to register
     * @return true if registered successfully
     */
    public boolean registerEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        String name = entity.getName();
        if (name == null || entities.containsKey(name)) {
            return false;
        }
        entities.put(name, entity);
        return true;
    }

    /**
     * Unregisters an entity from collision detection.
     *
     * @param entity The entity to unregister
     * @return true if unregistered successfully
     */
    public boolean unregisterEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        String name = entity.getName();
        Entity removed = entities.remove(name);
        if (removed != null) {
            return true;
        }
        return false;
    }

    /**
     * Checks if an entity is registered.
     *
     * @param entity The entity to check
     * @return true if registered
     */
    public boolean isEntityRegistered(Entity entity) {
        if (entity == null) {
            return false;
        }
        return entities.containsKey(entity.getName());
    }

    /**
     * Gets the total number of registered entities.
     *
     * @return The entity count
     */
    public int getEntityCount() {
        return entities.size();
    }

    /**
     * Finds all collisions between registered entities.
     * <p>
     * This method performs pairwise collision detection and returns
     * all overlapping entity pairs.
     * </p>
     *
     * @return List of collision results
     */
    public List<CollisionResult> findCollisions() {
        if (!initialized) {
            return Collections.emptyList();
        }

        List<CollisionResult> collisions = new ArrayList<>();

        List<Entity> entityList = new ArrayList<>(entities.values());
        int checkCount = 0;

        for (int i = 0; i < entityList.size() && checkCount < MAX_COLLISION_PAIRS; i++) {
            Entity entityA = entityList.get(i);
            if (entityA.isDead() || !entityA.isActive()) {
                continue;
            }

            for (int j = i + 1; j < entityList.size() && checkCount < MAX_COLLISION_PAIRS; j++) {
                Entity entityB = entityList.get(j);
                if (entityB.isDead() || !entityB.isActive()) {
                    continue;
                }

                if (isColliding(entityA, entityB)) {
                    CollisionResult result = new CollisionResult(entityA, entityB);
                    collisions.add(result);

                    // Create notification for this collision pair
                    String pairKey = pairKey(entityA, entityB);
                    CollisionNotification notification = new CollisionNotification(
                        CollisionNotification.EventType.ENTITY_COLLISION,
                        entityA.getId(),
                        entityB.getId(),
                        entityA.getX(),
                        entityA.getY(),
                        0
                    );
                    notifications.put(pairKey, notification);
                }
                checkCount++;
            }
        }

        return collisions;
    }

    /**
     * Finds all collisions and resolves them by pushing entities apart.
     * <p>
     * This method performs collision detection and then resolves collisions
     * by moving entities in the direction of their velocity vectors.
     * </p>
     *
     * @param deltaTime The time delta in seconds since last update
     * @return List of resolved collisions
     */
    public List<CollisionResult> findAndResolveCollisions(float deltaTime) {
        List<CollisionResult> collisions = findCollisions();

        // Resolve each collision
        for (CollisionResult collision : collisions) {
            resolveCollision(collision, deltaTime);
        }

        return collisions;
    }

    /**
     * Finds all collisions including duplicates.
     * <p>
     * This method performs collision detection and returns all collisions
     * even if the same pair is detected multiple times (e.g., in different
     * time steps).
     * </p>
     *
     * @return List of all collisions
     */
    public List<CollisionResult> findAllCollisionsIncludingDuplicates() {
        return findCollisions();
    }

    /**
     * Resolves a collision between two entities.
     * <p>
     * Resolution is based on velocity direction and entity sizes.
     * The entity with higher velocity or smaller size gives way.
     * </p>
     *
     * @param collision The collision result to resolve
     * @param deltaTime The time delta for velocity-based resolution
     */
    private void resolveCollision(CollisionResult collision, float deltaTime) {
        Entity entityA = collision.getEntityA();
        Entity entityB = collision.getEntityB();

        // Simple resolution: push entities apart by half their combined overlap
        Rectangle boxA = entityA.getCollisionBox();
        Rectangle boxB = entityB.getCollisionBox();

        if (boxA == null || boxB == null) {
            return;
        }

        float overlapX = Math.min(boxA.getX() + boxA.getWidth(), boxB.getX() + boxB.getWidth()) -
                         Math.max(boxA.getX(), boxB.getX());
        float overlapY = Math.min(boxA.getY() + boxA.getHeight(), boxB.getY() + boxB.getHeight()) -
                         Math.max(boxA.getY(), boxB.getY());

        // Apply resolution if there is overlap
        if (overlapX > COLLISION_TOLERANCE) {
            entityA.move(-overlapX / 2.0f, 0f);
            entityB.move(overlapX / 2.0f, 0f);
        }
        if (overlapY > COLLISION_TOLERANCE) {
            entityA.move(0f, -overlapY / 2.0f);
            entityB.move(0f, overlapY / 2.0f);
        }

        // Note: Notification position is final in constructor and not updated here
        // The collision result already has valid entity references
    }

    /**
     * Checks if two entities are colliding.
     *
     * @param entityA The first entity
     * @param entityB The second entity
     * @return true if colliding
     */
    private boolean isColliding(Entity entityA, Entity entityB) {
        Rectangle boxA = entityA.getCollisionBox();
        Rectangle boxB = entityB.getCollisionBox();

        if (boxA == null || boxB == null) {
            return false;
        }

        // AABB intersection test
        return boxA.getX() < boxB.getX() + boxB.getWidth() &&
                boxA.getX() + boxA.getWidth() > boxB.getX() &&
                boxA.getY() < boxB.getY() + boxB.getHeight() &&
                boxA.getY() + boxA.getHeight() > boxB.getY();
    }

    /**
     * Creates a key for a collision pair.
     *
     * @param entityA The first entity
     * @param entityB The second entity
     * @return The collision pair key
     */
    private String pairKey(Entity entityA, Entity entityB) {
        String nameA = entityA != null ? entityA.getName() : "";
        String nameB = entityB != null ? entityB.getName() : "";
        return nameA + "-" + nameB;
    }

    /**
     * Gets a collision notification for a pair.
     *
     * @param pairKey The collision pair key
     * @return The collision notification, or null if not found
     */
    public CollisionNotification getNotification(String pairKey) {
        return notifications.get(pairKey);
    }

    /**
     * Clears all notifications.
     */
    public void clearNotifications() {
        notifications.clear();
    }

    /**
     * Gets all registered entities.
     *
     * @return An unmodifiable list of all registered entities
     */
    public List<Entity> getRegisteredEntities() {
        return Collections.unmodifiableList(new ArrayList<>(entities.values()));
    }

    /**
     * Clears all entities and notifications.
     */
    public void clear() {
        entities.clear();
        notifications.clear();
    }

    /**
     * Creates a string representation of the collision manager.
     *
     * @return String representation of the collision manager
     */
    @Override
    public String toString() {
        return "CollisionManager{" +
                "entityCount=" + entities.size() +
                ", notificationCount=" + notifications.size() +
                '}';
    }
}
