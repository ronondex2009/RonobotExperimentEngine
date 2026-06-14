package org.ronobot.engine.entity;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;

/**
 * Item entity representing inventory items in the game.
 * <p>
 * Items represent things the player can collect and use:
 * - Ammo packs
 * - Health packs
 * - Armor plates
 * - Keycards
 * - Secrets
 * - Monsters
 * - Meds
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class Item extends Entity {

    // ==================== Constants ==================

    /**
     * Item size.
     */
    private static final int ITEM_WIDTH = 32;

    /**
     * Item height.
     */
    private static final int ITEM_HEIGHT = 32;

    // ==================== Fields ====================

    /**
     * Item type (AMMO, HEALTH, KEYCARD, etc.).
     */
    private ItemType type;

    /**
     * Quantity of this item in the inventory.
     */
    private int quantity;

    /**
     * Whether the item is currently held by the player.
     */
    private boolean held = false;

    /**
     * Creates a new Item.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param type The item type
     * @param quantity The initial quantity
     */
    public Item(int id, float x, float y, ItemType type, int quantity) {
        super(id, x, y, ITEM_WIDTH, ITEM_HEIGHT);
        this.type = type;
        this.quantity = Math.max(0, quantity);
        this.held = false;
        this.name = "Item: " + type.getName();
    }

    /**
     * Creates a new Item with default quantity of 1.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param type The item type
     */
    public Item(int id, float x, float y, ItemType type) {
        this(id, x, y, type, 1);
    }

    // ==================== Getters/Setters ====================

    /**
     * Gets the item type.
     *
     * @return The item type
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Sets the item type.
     *
     * @param type The new item type
     */
    public void setType(ItemType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }

    /**
     * Gets the quantity of this item.
     *
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of this item.
     *
     * @param quantity The new quantity (minimum 0)
     */
    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    /**
     * Increments the quantity by 1.
     *
     * @param amount The amount to increment by
     */
    public void addQuantity(int amount) {
        this.quantity += Math.max(0, amount);
    }

    /**
     * Decrements the quantity by the given amount.
     *
     * @param amount The amount to decrement by
     * @return true if the quantity is still positive after decrementing
     */
    public boolean removeQuantity(int amount) {
        if (amount <= 0) {
            return quantity > 0;
        }
        this.quantity = Math.max(0, this.quantity - amount);
        return this.quantity > 0;
    }

    /**
     * Sets the quantity to 0.
     */
    public void clear() {
        this.quantity = 0;
    }

    /**
     * Gets whether the item is held by the player.
     *
     * @return true if held
     */
    public boolean isHeld() {
        return held;
    }

    /**
     * Sets whether the item is held by the player.
     *
     * @param held Whether to be held
     */
    public void setHeld(boolean held) {
        this.held = held;
    }

    /**
     * Holds the item (takes it into inventory).
     *
     * @return true if the item was successfully held
     */
    public boolean hold() {
        if (held || quantity <= 0) {
            return false;
        }
        this.held = true;
        return true;
    }

    /**
     * Releases the item (returns it to the world).
     *
     * @return true if the item was successfully released
     */
    public boolean release() {
        if (!held) {
            return false;
        }
        this.held = false;
        return true;
    }

    /**
     * Gets a display name for this item.
     *
     * @return The display name
     */
    public String getDisplayName() {
        return type.getDisplayName();
    }

    /**
     * Gets the item icon/emoji.
     *
     * @return The icon string
     */
    public String getIcon() {
        return type.getIcon();
    }

    /**
     * Gets whether this item is usable.
     *
     * @return true if usable
     */
    public boolean isUsable() {
        return held && quantity > 0;
    }

    /**
     * Gets a description of this item.
     *
     * @return A description string
     */
    public String getDescription() {
        return type.getDescription();
    }

    /**
     * Handles per-frame updates for the item.
     * <p>
     * This method:
     * 1. Does nothing if not held or dead
     * </p>
     */
    @Override
    public void update() {
        // Items don't need frame updates when in inventory
        if (isDead() || held) {
            return;
        }
        // Items that are not held may have lifetime tracking here
    }

    /**
     * Creates an ammo item.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param quantity The quantity of ammo
     * @return The created ammo item
     */
    public static Item createAmmo(int id, float x, float y, int quantity) {
        return new Item(id, x, y, ItemType.AMMO, quantity);
    }

    /**
     * Creates a health item.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param quantity The quantity of health
     * @return The created health item
     */
    public static Item createHealth(int id, float x, float y, int quantity) {
        return new Item(id, x, y, ItemType.HEALTH, quantity);
    }

    /**
     * Creates an armor item.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param quantity The quantity of armor
     * @return The created armor item
     */
    public static Item createArmor(int id, float x, float y, int quantity) {
        return new Item(id, x, y, ItemType.ARMOR, quantity);
    }

    /**
     * Creates a keycard item.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param quantity The quantity (usually 1)
     * @return The created keycard item
     */
    public static Item createKeycard(int id, float x, float y, int quantity) {
        return new Item(id, x, y, ItemType.KEYCARD, quantity);
    }

    /**
     * Creates a secret item.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @return The created secret item
     */
    public static Item createSecret(int id, float x, float y) {
        return new Item(id, x, y, ItemType.SECRET);
    }

    /**
     * Creates a monster item.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @param quantity The quantity of monsters
     * @return The created monster item
     */
    public static Item createMonster(int id, float x, float y, int quantity) {
        return new Item(id, x, y, ItemType.MONSTER, quantity);
    }

    /**
     * Creates a medkit item.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @return The created medkit item
     */
    public static Item createMedkit(int id, float x, float y) {
        return new Item(id, x, y, ItemType.MEDKIT);
    }

    /**
     * Creates a weapon item.
     *
     * @param id The entity ID
     * @param x The x position
     * @param y The y position
     * @return The created weapon item
     */
    public static Item createWeapon(int id, float x, float y) {
        return new Item(id, x, y, ItemType.WEAPON);
    }

    /**
     * Gets the item type category.
     *
     * @return The category string
     */
    public String getCategory() {
        return type.getCategory();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + getId() +
                ", type=" + type +
                ", quantity=" + quantity +
                ", held=" + held +
                ", position=" + position +
                '}';
    }
}
