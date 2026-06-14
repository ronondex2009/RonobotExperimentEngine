package org.ronobot.engine.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Item entity.
 *
 * @author ronobot
 * @since 1.0
 */
@DisplayName("Item Tests")
class ItemTest {

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item(1, 100f, 100f, ItemType.AMMO, 5);
    }

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create item with default quantity")
        void testCreationDefaultQuantity() {
            Item item = new Item(1, 100f, 100f, ItemType.HEALTH);
            assertEquals(1, item.getQuantity(), "Default quantity should be 1");
            assertFalse(item.isHeld(), "New item should not be held");
        }

        @Test
        @DisplayName("Should create item with specified quantity")
        void testCreationWithQuantity() {
            Item item = new Item(1, 100f, 100f, ItemType.AMMO, 10);
            assertEquals(10, item.getQuantity(), "Quantity should be 10");
        }

        @Test
        @DisplayName("Should clamp negative quantity to 0")
        void testCreationNegativeQuantity() {
            Item item = new Item(1, 100f, 100f, ItemType.AMMO, -5);
            assertEquals(0, item.getQuantity(), "Negative quantity should be clamped to 0");
        }
    }

    @Nested
    @DisplayName("Type Tests")
    class TypeTests {

        @Test
        @DisplayName("Should get item type")
        void testGetType() {
            assertEquals(ItemType.AMMO, item.getType());
        }

        @Test
        @DisplayName("Should set item type")
        void testSetType() {
            item.setType(ItemType.HEALTH);
            assertEquals(ItemType.HEALTH, item.getType());
        }

        @Test
        @DisplayName("Should throw on null type")
        void testSetNullType() {
            assertThrows(IllegalArgumentException.class, () -> item.setType(null));
        }
    }

    @Nested
    @DisplayName("Quantity Tests")
    class QuantityTests {

        @Test
        @DisplayName("Should increment quantity")
        void testAddQuantity() {
            item.addQuantity(3);
            assertEquals(8, item.getQuantity());
        }

        @Test
        @DisplayName("Should decrement quantity")
        void testRemoveQuantity() {
            item.removeQuantity(2);
            assertEquals(3, item.getQuantity());
        }

        @Test
        @DisplayName("Should return true when removing positive quantity")
        void testRemoveQuantityReturnsTrue() {
            boolean result = item.removeQuantity(1);
            assertTrue(result);
            assertEquals(4, item.getQuantity());
        }

        @Test
        @DisplayName("Should return true when removing 0 quantity")
        void testRemoveQuantityZero() {
            boolean result = item.removeQuantity(0);
            assertTrue(result);
            assertEquals(5, item.getQuantity());
        }

        @Test
        @DisplayName("Should not go negative when removing more than available")
        void testRemoveQuantityExceedsAvailable() {
            // When removing more than available, quantity goes to 0 and returns false
            boolean result = item.removeQuantity(100);
            assertFalse(result, "Should return false when quantity becomes 0");
            assertEquals(0, item.getQuantity());
        }

        @Test
        @DisplayName("Should set quantity to 0 via clear")
        void testClear() {
            item.clear();
            assertEquals(0, item.getQuantity());
        }

        @Test
        @DisplayName("Should clamp negative quantity removal")
        void testRemoveNegativeQuantity() {
            boolean result = item.removeQuantity(-5);
            assertTrue(result);
            assertEquals(5, item.getQuantity());
        }
    }

    @Nested
    @DisplayName("Held State Tests")
    class HeldStateTests {

        @Test
        @DisplayName("Should not hold if already held")
        void testHoldWhenAlreadyHeld() {
            item.setHeld(true);
            boolean result = item.hold();
            assertFalse(result, "Cannot hold already held item");
        }

        @Test
        @DisplayName("Should not hold if quantity is 0")
        void testHoldWhenQuantityZero() {
            item.clear();
            boolean result = item.hold();
            assertFalse(result, "Cannot hold item with 0 quantity");
        }

        @Test
        @DisplayName("Should hold item successfully")
        void testHold() {
            boolean result = item.hold();
            assertTrue(result);
            assertTrue(item.isHeld());
        }

        @Test
        @DisplayName("Should release held item")
        void testRelease() {
            item.setHeld(true);
            boolean result = item.release();
            assertTrue(result);
            assertFalse(item.isHeld());
        }

        @Test
        @DisplayName("Should not release if not held")
        void testReleaseWhenNotHeld() {
            boolean result = item.release();
            assertFalse(result);
            assertFalse(item.isHeld());
        }
    }

    @Nested
    @DisplayName("Usable State Tests")
    class UsableStateTests {

        @Test
        @DisplayName("Should be usable when held and quantity > 0")
        void testUsableWhenHeld() {
            item.setHeld(true);
            assertTrue(item.isUsable());
        }

        @Test
        @DisplayName("Should not be usable when quantity is 0")
        void testNotUsableWhenQuantityZero() {
            item.clear();
            item.setHeld(true);
            assertFalse(item.isUsable());
        }

        @Test
        @DisplayName("Should not be usable when not held")
        void testNotUsableWhenNotHeld() {
            assertFalse(item.isUsable());
        }
    }

    @Nested
    @DisplayName("DisplayName Tests")
    class DisplayNameTests {

        @Test
        @DisplayName("Should get correct display name for AMMO")
        void testAmmoDisplayName() {
            assertEquals("Ammo", item.getDisplayName());
        }

        @Test
        @DisplayName("Should get correct display name for HEALTH")
        void testHealthDisplayName() {
            item.setType(ItemType.HEALTH);
            assertEquals("Health", item.getDisplayName());
        }
    }

    @Nested
    @DisplayName("Icon Tests")
    class IconTests {

        @Test
        @DisplayName("Should get correct icon for AMMO")
        void testAmmoIcon() {
            assertEquals("🔫", item.getIcon());
        }

        @Test
        @DisplayName("Should get correct icon for HEALTH")
        void testHealthIcon() {
            item.setType(ItemType.HEALTH);
            assertEquals("❤️", item.getIcon());
        }
    }

    @Nested
    @DisplayName("Category Tests")
    class CategoryTests {

        @Test
        @DisplayName("Should get correct category for AMMO")
        void testAmmoCategory() {
            assertEquals("Weapon", item.getCategory());
        }

        @Test
        @DisplayName("Should get correct category for HEALTH")
        void testHealthCategory() {
            item.setType(ItemType.HEALTH);
            assertEquals("Consumable", item.getCategory());
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should get correct description for AMMO")
        void testAmmoDescription() {
            assertEquals("Ammunition", item.getDescription());
        }

        @Test
        @DisplayName("Should get correct description for HEALTH")
        void testHealthDescription() {
            item.setType(ItemType.HEALTH);
            assertEquals("Health restoration", item.getDescription());
        }
    }

    @Nested
    @DisplayName("Position Tests")
    class PositionTests {

        @Test
        @DisplayName("Should have correct x position")
        void testGetX() {
            assertEquals(100f, item.getX(), 0.01);
        }

        @Test
        @DisplayName("Should have correct y position")
        void testGetY() {
            assertEquals(100f, item.getY(), 0.01);
        }
    }

    @Nested
    @DisplayName("Size Tests")
    class SizeTests {

        @Test
        @DisplayName("Should have correct width")
        void testGetWidth() {
            assertEquals(32, item.getWidth());
        }

        @Test
        @DisplayName("Should have correct height")
        void testGetHeight() {
            assertEquals(32, item.getHeight());
        }
    }

    @Nested
    @DisplayName("ID Tests")
    class IDTests {

        @Test
        @DisplayName("Should have correct ID")
        void testGetId() {
            assertEquals(1, item.getId());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should produce correct toString")
        void testToString() {
            String actual = item.toString();
            assertTrue(actual.contains("id="));
            assertTrue(actual.contains("type="));
            assertTrue(actual.contains("quantity="));
        }
    }

    @Nested
    @DisplayName("Static Factory Methods Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create ammo item")
        void testCreateAmmo() {
            Item ammo = Item.createAmmo(1, 50f, 50f, 5);
            assertNotNull(ammo);
            assertEquals(ItemType.AMMO, ammo.getType());
            assertEquals(5, ammo.getQuantity());
        }

        @Test
        @DisplayName("Should create health item")
        void testCreateHealth() {
            Item health = Item.createHealth(1, 50f, 50f, 5);
            assertNotNull(health);
            assertEquals(ItemType.HEALTH, health.getType());
            assertEquals(5, health.getQuantity());
        }

        @Test
        @DisplayName("Should create armor item")
        void testCreateArmor() {
            Item armor = Item.createArmor(1, 50f, 50f, 5);
            assertNotNull(armor);
            assertEquals(ItemType.ARMOR, armor.getType());
            assertEquals(5, armor.getQuantity());
        }

        @Test
        @DisplayName("Should create keycard item")
        void testCreateKeycard() {
            Item keycard = Item.createKeycard(1, 50f, 50f, 1);
            assertNotNull(keycard);
            assertEquals(ItemType.KEYCARD, keycard.getType());
            assertEquals(1, keycard.getQuantity());
        }

        @Test
        @DisplayName("Should create secret item")
        void testCreateSecret() {
            Item secret = Item.createSecret(1, 50f, 50f);
            assertNotNull(secret);
            assertEquals(ItemType.SECRET, secret.getType());
        }

        @Test
        @DisplayName("Should create monster item")
        void testCreateMonster() {
            Item monster = Item.createMonster(1, 50f, 50f, 3);
            assertNotNull(monster);
            assertEquals(ItemType.MONSTER, monster.getType());
            assertEquals(3, monster.getQuantity());
        }

        @Test
        @DisplayName("Should create medkit item")
        void testCreateMedkit() {
            Item medkit = Item.createMedkit(1, 50f, 50f);
            assertNotNull(medkit);
            assertEquals(ItemType.MEDKIT, medkit.getType());
        }

        @Test
        @DisplayName("Should create weapon item")
        void testCreateWeapon() {
            Item weapon = Item.createWeapon(1, 50f, 50f);
            assertNotNull(weapon);
            assertEquals(ItemType.WEAPON, weapon.getType());
        }
    }
}
