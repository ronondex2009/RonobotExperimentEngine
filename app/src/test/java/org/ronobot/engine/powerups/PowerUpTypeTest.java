package org.ronobot.engine.powerups;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PowerUpType enum.
 * <p>
 * Tests verify power-up characteristics including effect values,
 * categories, and upgrade types.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
@DisplayName("PowerUpType Tests")
class PowerUpTypeTest {

    @Nested
    @DisplayName("Enum Values")
    class EnumValuesTests {

        @Test
        @DisplayName("Health power-up exists with correct values")
        void healthPowerUpExists() {
            PowerUpType health = PowerUpType.HEALTH;
            assertEquals("Health Pack", health.getName());
            assertEquals(50, health.getEffectValue());
            assertEquals("🛡️❤️", health.getVisual());
            assertTrue(health.isValuePowerUp());
            assertFalse(health.isUpgradePowerUp());
            assertEquals("Health", health.getCategory());
        }

        @Test
        @DisplayName("Armor power-up exists with correct values")
        void armorPowerUpExists() {
            PowerUpType armor = PowerUpType.ARMOR;
            assertEquals("Armor Plate", armor.getName());
            assertEquals(25, armor.getEffectValue());
            assertEquals("🛡️", armor.getVisual());
            assertTrue(armor.isValuePowerUp());
            assertFalse(armor.isUpgradePowerUp());
            assertEquals("Armor", armor.getCategory());
            assertTrue(armor.isArmorRelated());
        }

        @Test
        @DisplayName("Ammo power-up exists with correct values")
        void ammoPowerUpExists() {
            PowerUpType ammo = PowerUpType.AMMO;
            assertEquals("Ammo Box", ammo.getName());
            assertEquals(50, ammo.getEffectValue());
            assertEquals("🔫", ammo.getVisual());
            assertTrue(ammo.isValuePowerUp());
            assertFalse(ammo.isUpgradePowerUp());
            assertEquals("Ammo", ammo.getCategory());
            assertTrue(ammo.isAmmoRelated());
        }

        @Test
        @DisplayName("Rocket launcher weapon power-up exists")
        void rocketPowerUpExists() {
            PowerUpType rocket = PowerUpType.ROCKET;
            assertEquals("Rocket Launcher", rocket.getName());
            assertEquals(0, rocket.getEffectValue());
            assertEquals("🚀", rocket.getVisual());
            assertFalse(rocket.isValuePowerUp());
            assertTrue(rocket.isUpgradePowerUp());
            assertTrue(rocket.isWeaponUpgrade());
            assertEquals("Weapon", rocket.getCategory());
        }

        @Test
        @DisplayName("Shotgun weapon power-up exists")
        void shotgunPowerUpExists() {
            PowerUpType shotgun = PowerUpType.SHOTGUN;
            assertEquals("Shotgun", shotgun.getName());
            assertEquals(0, shotgun.getEffectValue());
            assertEquals("💥", shotgun.getVisual());
            assertFalse(shotgun.isValuePowerUp());
            assertTrue(shotgun.isUpgradePowerUp());
            assertTrue(shotgun.isWeaponUpgrade());
            assertEquals("Weapon", shotgun.getCategory());
        }

        @Test
        @DisplayName("Chain gun weapon power-up exists")
        void chainPowerUpExists() {
            PowerUpType chain = PowerUpType.CHAIN;
            assertEquals("Chain Gun", chain.getName());
            assertEquals(0, chain.getEffectValue());
            assertEquals("🔫⚡", chain.getVisual());
            assertFalse(chain.isValuePowerUp());
            assertTrue(chain.isUpgradePowerUp());
            assertTrue(chain.isWeaponUpgrade());
            assertEquals("Weapon", chain.getCategory());
        }

        @Test
        @DisplayName("BFG weapon power-up exists")
        void bfgPowerUpExists() {
            PowerUpType bfg = PowerUpType.BFG;
            assertEquals("BFG", bfg.getName());
            assertEquals(0, bfg.getEffectValue());
            assertEquals("☢️", bfg.getVisual());
            assertFalse(bfg.isValuePowerUp());
            assertTrue(bfg.isUpgradePowerUp());
            assertTrue(bfg.isWeaponUpgrade());
            assertEquals("Weapon", bfg.getCategory());
        }

        @Test
        @DisplayName("Speed power-up exists with correct values")
        void speedPowerUpExists() {
            PowerUpType speed = PowerUpType.SPEED;
            assertEquals("Speed Boost", speed.getName());
            assertEquals(0, speed.getEffectValue());
            assertEquals("⚡", speed.getVisual());
            assertFalse(speed.isValuePowerUp());
            assertEquals("Speed", speed.getCategory());
        }

        @Test
        @DisplayName("Invisibility power-up exists with correct values")
        void invisibilityPowerUpExists() {
            PowerUpType invisibility = PowerUpType.INVISIBILITY;
            assertEquals("Invisibility", invisibility.getName());
            assertEquals(0, invisibility.getEffectValue());
            assertEquals("👻", invisibility.getVisual());
            assertFalse(invisibility.isValuePowerUp());
            assertEquals("Special", invisibility.getCategory());
        }

        @Test
        @DisplayName("Mega medkit power-up exists with correct values")
        void megaMedkitPowerUpExists() {
            PowerUpType megaMedkit = PowerUpType.MEGAMEDKIT;
            assertEquals("Mega Medkit", megaMedkit.getName());
            assertEquals(100, megaMedkit.getEffectValue());
            assertEquals("❇️", megaMedkit.getVisual());
            assertTrue(megaMedkit.isValuePowerUp());
            assertEquals("Health", megaMedkit.getCategory());
            assertTrue(megaMedkit.isHealthRelated());
        }
    }

    @Nested
    @DisplayName("Total Power-Up Count")
    class CountTests {

        @Test
        @DisplayName("All 10 power-up types are defined")
        void allTenTypes() {
            assertEquals(10, PowerUpType.values().length);

            // Check each type name
            assertEquals(PowerUpType.HEALTH.name(), "HEALTH");
            assertEquals(PowerUpType.ARMOR.name(), "ARMOR");
            assertEquals(PowerUpType.AMMO.name(), "AMMO");
            assertEquals(PowerUpType.ROCKET.name(), "ROCKET");
            assertEquals(PowerUpType.SHOTGUN.name(), "SHOTGUN");
            assertEquals(PowerUpType.CHAIN.name(), "CHAIN");
            assertEquals(PowerUpType.BFG.name(), "BFG");
            assertEquals(PowerUpType.SPEED.name(), "SPEED");
            assertEquals(PowerUpType.INVISIBILITY.name(), "INVISIBILITY");
            assertEquals(PowerUpType.MEGAMEDKIT.name(), "MEGAMEDKIT");
        }
    }

    @Nested
    @DisplayName("Power-Up Classification")
    class ClassificationTests {

        @Test
        @DisplayName("Value-based power-ups are identified correctly")
        void valueBasedPowerUps() {
            // Value-based: HEALTH (50), ARMOR (25), AMMO (50), MEGAMEDKIT (100)
            assertTrue(PowerUpType.HEALTH.isValuePowerUp());
            assertTrue(PowerUpType.ARMOR.isValuePowerUp());
            assertTrue(PowerUpType.AMMO.isValuePowerUp());
            assertTrue(PowerUpType.MEGAMEDKIT.isValuePowerUp());

            // Upgrades: ROCKET, SHOTGUN, CHAIN, BFG, SPEED, INVISIBILITY
            assertFalse(PowerUpType.ROCKET.isValuePowerUp());
            assertFalse(PowerUpType.SHOTGUN.isValuePowerUp());
            assertFalse(PowerUpType.CHAIN.isValuePowerUp());
            assertFalse(PowerUpType.BFG.isValuePowerUp());
            assertFalse(PowerUpType.SPEED.isValuePowerUp());
            assertFalse(PowerUpType.INVISIBILITY.isValuePowerUp());
        }

        @Test
        @DisplayName("Upgrade-based power-ups are identified correctly")
        void upgradeBasedPowerUps() {
            // Upgrades: ROCKET, SHOTGUN, CHAIN, BFG, SPEED, INVISIBILITY
            assertTrue(PowerUpType.ROCKET.isUpgradePowerUp());
            assertTrue(PowerUpType.SHOTGUN.isUpgradePowerUp());
            assertTrue(PowerUpType.CHAIN.isUpgradePowerUp());
            assertTrue(PowerUpType.BFG.isUpgradePowerUp());
            assertTrue(PowerUpType.SPEED.isUpgradePowerUp());
            assertTrue(PowerUpType.INVISIBILITY.isUpgradePowerUp());

            // Not upgrades: HEALTH, ARMOR, AMMO, MEGAMEDKIT
            assertFalse(PowerUpType.HEALTH.isUpgradePowerUp());
            assertFalse(PowerUpType.ARMOR.isUpgradePowerUp());
            assertFalse(PowerUpType.AMMO.isUpgradePowerUp());
            assertFalse(PowerUpType.MEGAMEDKIT.isUpgradePowerUp());
        }

        @Test
        @DisplayName("Weapon upgrades are identified correctly")
        void weaponUpgrades() {
            assertTrue(PowerUpType.ROCKET.isWeaponUpgrade());
            assertTrue(PowerUpType.SHOTGUN.isWeaponUpgrade());
            assertTrue(PowerUpType.CHAIN.isWeaponUpgrade());
            assertTrue(PowerUpType.BFG.isWeaponUpgrade());
            assertFalse(PowerUpType.HEALTH.isWeaponUpgrade());
            assertFalse(PowerUpType.ARMOR.isWeaponUpgrade());
            assertFalse(PowerUpType.AMMO.isWeaponUpgrade());
            assertFalse(PowerUpType.SPEED.isWeaponUpgrade());
            assertFalse(PowerUpType.INVISIBILITY.isWeaponUpgrade());
        }

        @Test
        @DisplayName("Health-related power-ups are identified correctly")
        void healthRelated() {
            assertTrue(PowerUpType.HEALTH.isHealthRelated());
            assertTrue(PowerUpType.MEGAMEDKIT.isHealthRelated());
            assertFalse(PowerUpType.ARMOR.isHealthRelated());
            assertFalse(PowerUpType.AMMO.isHealthRelated());
        }

        @Test
        @DisplayName("Armor-related power-ups are identified correctly")
        void armorRelated() {
            assertFalse(PowerUpType.HEALTH.isArmorRelated());
            assertTrue(PowerUpType.ARMOR.isArmorRelated());
            assertFalse(PowerUpType.AMMO.isArmorRelated());
        }

        @Test
        @DisplayName("Ammo-related power-ups are identified correctly")
        void ammoRelated() {
            assertFalse(PowerUpType.HEALTH.isAmmoRelated());
            assertFalse(PowerUpType.ARMOR.isAmmoRelated());
            assertTrue(PowerUpType.AMMO.isAmmoRelated());
        }
    }

    @Nested
    @DisplayName("Category Tests")
    class CategoryTests {

        @Test
        @DisplayName("Health category contains health power-ups")
        void healthCategory() {
            assertEquals("Health", PowerUpType.HEALTH.getCategory());
            assertEquals("Health", PowerUpType.MEGAMEDKIT.getCategory());
        }

        @Test
        @DisplayName("Armor category contains armor power-ups")
        void armorCategory() {
            assertEquals("Armor", PowerUpType.ARMOR.getCategory());
        }

        @Test
        @DisplayName("Ammo category contains ammo power-ups")
        void ammoCategory() {
            assertEquals("Ammo", PowerUpType.AMMO.getCategory());
        }

        @Test
        @DisplayName("Weapon category contains weapon upgrades")
        void weaponCategory() {
            assertEquals("Weapon", PowerUpType.ROCKET.getCategory());
            assertEquals("Weapon", PowerUpType.SHOTGUN.getCategory());
            assertEquals("Weapon", PowerUpType.CHAIN.getCategory());
            assertEquals("Weapon", PowerUpType.BFG.getCategory());
        }

        @Test
        @DisplayName("Speed category contains speed power-ups")
        void speedCategory() {
            assertEquals("Speed", PowerUpType.SPEED.getCategory());
        }

        @Test
        @DisplayName("Special category contains invisibility")
        void specialCategory() {
            assertEquals("Special", PowerUpType.INVISIBILITY.getCategory());
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Health power-up description is correct")
        void healthDescription() {
            String expected = "Health Pack - 🛡️❤️ Provides 50 units of benefit.";
            assertEquals(expected, PowerUpType.HEALTH.getDescription());
        }

        @Test
        @DisplayName("Armor power-up description is correct")
        void armorDescription() {
            String expected = "Armor Plate - 🛡️ Provides 25 units of benefit.";
            assertEquals(expected, PowerUpType.ARMOR.getDescription());
        }

        @Test
        @DisplayName("Ammo power-up description is correct")
        void ammoDescription() {
            String expected = "Ammo Box - 🔫 Provides 50 units of benefit.";
            assertEquals(expected, PowerUpType.AMMO.getDescription());
        }

        @Test
        @DisplayName("Rocket launcher description is correct")
        void rocketDescription() {
            String expected = "Rocket Launcher - 🚀 Upgrades current weapon.";
            assertEquals(expected, PowerUpType.ROCKET.getDescription());
        }

        @Test
        @DisplayName("BFG description is correct")
        void bfgDescription() {
            String expected = "BFG - ☢️ Upgrades current weapon.";
            assertEquals(expected, PowerUpType.BFG.getDescription());
        }
    }
}
