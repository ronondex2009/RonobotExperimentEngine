package org.ronobot.engine.powerups;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PowerUpType enum.
 * <p>
 * Tests power-up type enumeration values, category classification,
 * upgrade detection, and property accessors.
 * </p>
 * 
 * @author ronobot
 * @since 1.0
 */
class PowerUpTypeTest {

    /**
     * Test that all expected power-up types exist.
     */
    @Test
    void testAllTypesExist() {
        assertEquals(12, PowerUpType.values().length);
        
        PowerUpType[] types = PowerUpType.values();
        assertTrue(types.length >= 3);
    }

    /**
     * Test enum value descriptions.
     */
    @Test
    void testEnumDescriptions() {
        assertEquals("Health Pack", PowerUpType.HEALTH.getName());
        assertEquals("Armor Plate", PowerUpType.ARMOR.getName());
        assertEquals("Ammo Box", PowerUpType.AMMO.getName());
        assertEquals("Rocket Launcher", PowerUpType.ROCKET.getName());
        assertEquals("Shotgun", PowerUpType.SHOTGUN.getName());
        assertEquals("Chain Gun", PowerUpType.CHAIN.getName());
        assertEquals("BFG", PowerUpType.BFG.getName());
        assertEquals("Speed Boost", PowerUpType.SPEED.getName());
        assertEquals("Invisibility", PowerUpType.INVISIBILITY.getName());
        assertEquals("Mega Medkit", PowerUpType.MEGAMEDKIT.getName());
        assertEquals("Super Shot", PowerUpType.SUPERSHOT.getName());
        assertEquals("Mystery", PowerUpType.MYSTERY.getName());
    }

    /**
     * Test enum visual representations.
     */
    @Test
    void testEnumVisuals() {
        assertEquals("🛡️❤️", PowerUpType.HEALTH.getVisual());
        assertEquals("🛡️", PowerUpType.ARMOR.getVisual());
        assertEquals("🔫", PowerUpType.AMMO.getVisual());
        assertEquals("🚀", PowerUpType.ROCKET.getVisual());
        assertEquals("💥", PowerUpType.SHOTGUN.getVisual());
        assertEquals("🔫⚡", PowerUpType.CHAIN.getVisual());
        assertEquals("☢️", PowerUpType.BFG.getVisual());
        assertEquals("⚡", PowerUpType.SPEED.getVisual());
        assertEquals("👻", PowerUpType.INVISIBILITY.getVisual());
        assertEquals("❇️", PowerUpType.MEGAMEDKIT.getVisual());
        assertEquals("➕", PowerUpType.SUPERSHOT.getVisual());
        assertEquals("❔", PowerUpType.MYSTERY.getVisual());
    }

    /**
     * Test health-related power-up detection.
     */
    @Test
    void testHealthRelatedTypes() {
        assertTrue(PowerUpType.HEALTH.isHealthRelated());
        assertTrue(PowerUpType.MEGAMEDKIT.isHealthRelated());
        
        assertFalse(PowerUpType.ARMOR.isHealthRelated());
        assertFalse(PowerUpType.AMMO.isHealthRelated());
        assertFalse(PowerUpType.ROCKET.isHealthRelated());
        assertFalse(PowerUpType.SHOTGUN.isHealthRelated());
        assertFalse(PowerUpType.CHAIN.isHealthRelated());
        assertFalse(PowerUpType.BFG.isHealthRelated());
        assertFalse(PowerUpType.SPEED.isHealthRelated());
        assertFalse(PowerUpType.INVISIBILITY.isHealthRelated());
        assertFalse(PowerUpType.SUPERSHOT.isHealthRelated());
        assertFalse(PowerUpType.MYSTERY.isHealthRelated());
    }

    /**
     * Test armor-related power-up detection.
     */
    @Test
    void testArmorRelatedTypes() {
        assertTrue(PowerUpType.ARMOR.isArmorRelated());
        
        assertFalse(PowerUpType.HEALTH.isArmorRelated());
        assertFalse(PowerUpType.AMMO.isArmorRelated());
        assertFalse(PowerUpType.MEGAMEDKIT.isArmorRelated());
        assertFalse(PowerUpType.SUPERSHOT.isArmorRelated());
        assertFalse(PowerUpType.MYSTERY.isArmorRelated());
    }

    /**
     * Test ammo-related power-up detection.
     */
    @Test
    void testAmmoRelatedTypes() {
        assertTrue(PowerUpType.AMMO.isAmmoRelated());
        
        assertFalse(PowerUpType.HEALTH.isAmmoRelated());
        assertFalse(PowerUpType.ARMOR.isAmmoRelated());
        assertFalse(PowerUpType.MEGAMEDKIT.isAmmoRelated());
    }

    /**
     * Test effect value getters for value-based and upgrade power-ups.
     */
    @Test
    void testEffectValues() {
        assertEquals(50, PowerUpType.HEALTH.getEffectValue());
        assertEquals(25, PowerUpType.ARMOR.getEffectValue());
        assertEquals(50, PowerUpType.AMMO.getEffectValue());
        assertEquals(0, PowerUpType.ROCKET.getEffectValue());
        assertEquals(0, PowerUpType.SHOTGUN.getEffectValue());
        assertEquals(0, PowerUpType.CHAIN.getEffectValue());
        assertEquals(0, PowerUpType.BFG.getEffectValue());
        assertEquals(0, PowerUpType.SPEED.getEffectValue());
        assertEquals(0, PowerUpType.INVISIBILITY.getEffectValue());
        assertEquals(100, PowerUpType.MEGAMEDKIT.getEffectValue());
        assertEquals(10, PowerUpType.SUPERSHOT.getEffectValue());
        assertEquals(0, PowerUpType.MYSTERY.getEffectValue());
    }

    /**
     * Test isValuePowerUp detection.
     */
    @Test
    void testIsValuePowerUp() {
        assertTrue(PowerUpType.HEALTH.isValuePowerUp());
        assertTrue(PowerUpType.ARMOR.isValuePowerUp());
        assertTrue(PowerUpType.AMMO.isValuePowerUp());
        assertTrue(PowerUpType.MEGAMEDKIT.isValuePowerUp());
        assertTrue(PowerUpType.SUPERSHOT.isValuePowerUp());
        assertTrue(PowerUpType.MYSTERY.isValuePowerUp());
        
        assertFalse(PowerUpType.ROCKET.isValuePowerUp());
        assertFalse(PowerUpType.SHOTGUN.isValuePowerUp());
        assertFalse(PowerUpType.CHAIN.isValuePowerUp());
        assertFalse(PowerUpType.BFG.isValuePowerUp());
        assertFalse(PowerUpType.SPEED.isValuePowerUp());
        assertFalse(PowerUpType.INVISIBILITY.isValuePowerUp());
    }

    /**
     * Test isUpgradePowerUp detection.
     */
    @Test
    void testIsUpgradePowerUp() {
        assertTrue(PowerUpType.ROCKET.isUpgradePowerUp());
        assertTrue(PowerUpType.SHOTGUN.isUpgradePowerUp());
        assertTrue(PowerUpType.CHAIN.isUpgradePowerUp());
        assertTrue(PowerUpType.BFG.isUpgradePowerUp());
        
        assertFalse(PowerUpType.HEALTH.isUpgradePowerUp());
        assertFalse(PowerUpType.ARMOR.isUpgradePowerUp());
        assertFalse(PowerUpType.AMMO.isUpgradePowerUp());
        assertFalse(PowerUpType.SUPERSHOT.isUpgradePowerUp());
        assertFalse(PowerUpType.MEGAMEDKIT.isUpgradePowerUp());
        assertFalse(PowerUpType.SPEED.isUpgradePowerUp());
        assertFalse(PowerUpType.INVISIBILITY.isUpgradePowerUp());
        assertFalse(PowerUpType.MYSTERY.isUpgradePowerUp());
    }

    /**
     * Test weapon upgrade detection.
     */
    @Test
    void testIsWeaponUpgrade() {
        assertTrue(PowerUpType.ROCKET.isWeaponUpgrade());
        assertTrue(PowerUpType.SHOTGUN.isWeaponUpgrade());
        assertTrue(PowerUpType.CHAIN.isWeaponUpgrade());
        assertTrue(PowerUpType.BFG.isWeaponUpgrade());
        
        assertFalse(PowerUpType.HEALTH.isWeaponUpgrade());
        assertFalse(PowerUpType.ARMOR.isWeaponUpgrade());
        assertFalse(PowerUpType.AMMO.isWeaponUpgrade());
        assertFalse(PowerUpType.SUPERSHOT.isWeaponUpgrade());
        assertFalse(PowerUpType.MEGAMEDKIT.isWeaponUpgrade());
        assertFalse(PowerUpType.SPEED.isWeaponUpgrade());
        assertFalse(PowerUpType.INVISIBILITY.isWeaponUpgrade());
        assertFalse(PowerUpType.MYSTERY.isWeaponUpgrade());
    }

    /**
     * Test category getters.
     */
    @Test
    void testGetCategories() {
        assertEquals("Health", PowerUpType.HEALTH.getCategory());
        assertEquals("Health", PowerUpType.MEGAMEDKIT.getCategory());
        assertEquals("Armor", PowerUpType.ARMOR.getCategory());
        assertEquals("Ammo", PowerUpType.AMMO.getCategory());
        assertEquals("Weapon", PowerUpType.ROCKET.getCategory());
        assertEquals("Weapon", PowerUpType.SHOTGUN.getCategory());
        assertEquals("Weapon", PowerUpType.CHAIN.getCategory());
        assertEquals("Weapon", PowerUpType.BFG.getCategory());
        assertEquals("Speed", PowerUpType.SPEED.getCategory());
        assertEquals("Special", PowerUpType.INVISIBILITY.getCategory());
        assertEquals("Upgrade", PowerUpType.SUPERSHOT.getCategory());
        assertEquals("Special", PowerUpType.MYSTERY.getCategory());
    }

    /**
     * Test getDescription returns meaningful descriptions.
     */
    @Test
    void testGetDescriptions() {
        assertEquals("Health Pack - 🛡️❤️", PowerUpType.HEALTH.getDescription());
        assertEquals("Armor Plate - 🛡️", PowerUpType.ARMOR.getDescription());
        assertEquals("Ammo Box - 🔫", PowerUpType.AMMO.getDescription());
        assertEquals("Mega Medkit - ❇️", PowerUpType.MEGAMEDKIT.getDescription());
        assertEquals("Rocket Launcher - 🚀", PowerUpType.ROCKET.getDescription());
        assertEquals("Super Shot - ➕", PowerUpType.SUPERSHOT.getDescription());
        assertEquals("Speed Boost - ⚡", PowerUpType.SPEED.getDescription());
        assertEquals("Invisibility - 👻", PowerUpType.INVISIBILITY.getDescription());
        assertEquals("Mystery - ❔", PowerUpType.MYSTERY.getDescription());
    }

    /**
     * Test toString returns enum name.
     */
    @Test
    void testToString() {
        assertEquals("HEALTH", PowerUpType.HEALTH.toString());
        assertEquals("ARMOR", PowerUpType.ARMOR.toString());
        assertEquals("AMMO", PowerUpType.AMMO.toString());
        assertEquals("SPEED", PowerUpType.SPEED.toString());
        assertEquals("SUPERSHOT", PowerUpType.SUPERSHOT.toString());
        assertEquals("MYSTERY", PowerUpType.MYSTERY.toString());
    }

    /**
     * Test PowerUpType enum values.
     */
    @Test
    void testEnumValues() {
        PowerUpType[] types = PowerUpType.values();
        
        assertEquals(12, types.length);
        assertEquals(PowerUpType.HEALTH, types[0]);
        assertEquals(PowerUpType.ARMOR, types[1]);
        assertEquals(PowerUpType.AMMO, types[2]);
        assertEquals(PowerUpType.ROCKET, types[3]);
        assertEquals(PowerUpType.SHOTGUN, types[4]);
        assertEquals(PowerUpType.CHAIN, types[5]);
        assertEquals(PowerUpType.BFG, types[6]);
        assertEquals(PowerUpType.SPEED, types[7]);
        assertEquals(PowerUpType.INVISIBILITY, types[8]);
        assertEquals(PowerUpType.MEGAMEDKIT, types[9]);
        assertEquals(PowerUpType.SUPERSHOT, types[10]);
        assertEquals(PowerUpType.MYSTERY, types[11]);
    }
}
