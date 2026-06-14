package org.ronobot.engine.entities;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages all game entities.
 * <p>
 * Provides entity creation, lifecycle management, and entity access.
 * Uses thread-safe ConcurrentHashMap for concurrent access safety.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class EntityManager {

    /**
     * Map of entity names to entity instances.
     */
    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    /**
     * List of all entities for iteration.
     */
    private final List<Entity> entityList = new ArrayList<>();

    /**
     * Creates a new EntityManager with no entities.
     */
    public EntityManager() {
    }

    /**
     * Creates a new entity and adds it to the manager.
     *
     * @param entity The entity to add
     * @return The entity that was added
     */
    public Entity addEntity(Entity entity) {
        if (entity == null) {
            return null;
        }
        entityMap.put(entity.getName(), entity);
        entityList.add(entity);
        return entity;
    }

    /**
     * Removes an entity from the manager.
     *
     * @param entity The entity to remove
     * @return true if removed, false if not found
     */
    public boolean removeEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        String name = entity.getName();
        boolean removed = entityMap.remove(name) != null;
        if (removed) {
            entityList.remove(entity);
        }
        return removed;
    }

    /**
     * Removes an entity by name.
     *
     * @param name The entity name
     * @return true if removed, false if not found
     */
    public boolean removeEntity(String name) {
        if (name == null) {
            return false;
        }
        Entity entity = entityMap.remove(name);
        if (entity != null) {
            entityList.remove(entity);
            return true;
        }
        return false;
    }

    /**
     * Gets an entity by name.
     *
     * @param name The entity name
     * @return The entity, or null if not found
     */
    public Entity getEntity(String name) {
        return entityMap.get(name);
    }

    /**
     * Gets an entity by ID.
     *
     * @param id The entity ID
     * @return The entity, or null if not found
     */
    public Entity getEntityById(int id) {
        for (Entity entity : entityList) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Gets an entity by index (0-based).
     *
     * @param index The entity index
     * @return The entity at the index, or null if not found
     */
    public Entity getEntity(int index) {
        if (index < 0 || index >= entityList.size()) {
            return null;
        }
        return entityList.get(index);
    }

    /**
     * Gets all entities.
     *
     * @return An unmodifiable list of all entities
     */
    public List<Entity> getEntities() {
        return new ArrayList<>(entityList);
    }

    /**
     * Gets the total number of entities.
     *
     * @return The entity count
     */
    public int getEntityCount() {
        return entityMap.size();
    }

    /**
     * Clears all entities.
     */
    public void clear() {
        entityMap.clear();
        entityList.clear();
    }

    /**
     * Updates all managed entities.
     * <p>
     * This method calls update() on each entity to advance them in time.
     * Handles velocity-based movement and other per-frame updates.
     * </p>
     */
    public void update() {
        for (Entity entity : entityList) {
            if (entity.isActive() && !entity.isDead()) {
                entity.update();
            }
        }
    }

    /**
     * Gets all active (non-dead) entities.
     *
     * @return An unmodifiable list of active entities
     */
    public List<Entity> getActiveEntities() {
        List<Entity> active = new ArrayList<>();
        for (Entity entity : entityList) {
            if (entity.isActive() && !entity.isDead()) {
                active.add(entity);
            }
        }
        return active;
    }

    @Override
    public String toString() {
        return "EntityManager{" +
                "entityCount=" + entityMap.size() +
                ", activeCount=" + getActiveEntities().size() +
                '}';
    }
}
