package org.ronobot.engine.powerups;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PowerUp class.
 * <p>
 * Tests power-up creation, type handling, pickup logic,
 * lifespan management, and collection behavior.
 * </p>
 * 
 * @author ronobot
 * @since 1.0
 */
class PowerUpTest {

    /**
     * Test PowerUp creation with default type.
     */
    @Test
    void testCreationWithDefaultType() {
        PowerUp powerUp = new PowerUp(1, 10f, 10f, PowerUpType.HEALTH);
        
        assertNotNull(powerUp);
        assertEquals(1, powerUp.getId());
        assertEquals(10f, powerUp.getPosition().getX(), 0.001f);
        assertEquals(10f, powerUp.getPosition().getY(), 0.001f);
        assertEquals(PowerUpType.HEALTH, powerUp.getType());
        assertTrue(powerUp.canPickup());
    }

    /**
     * Test PowerUp creation with custom size and type.
     */
    @Test
    void testCreationWithCustomSize() {
        PowerUp powerUp = new PowerUp(2, 20f, 20f, 32, 32, PowerUpType.AMMO);
        
        assertNotNull(powerUp);
        assertEquals(32, powerUp.getSize().getWidth());
        assertEquals(32, powerUp.getSize().getHeight());
        assertEquals(PowerUpType.AMMO, powerUp.getType());
    }

    /**
     * Test PowerUp type getter and setter.
     */
    @Test
    void testTypeGetterAndSetter() {
        PowerUp powerUp = new PowerUp(3, 30f, 30f, PowerUpType.ROCKET);
        
        assertEquals(PowerUpType.ROCKET, powerUp.getType());
        assertEquals("PowerUp: Rocket Launcher", powerUp.getName());
        
        powerUp.setType(PowerUpType.HEALTH);
        assertEquals(PowerUpType.HEALTH, powerUp.getType());
        assertEquals("PowerUp: Health Pack", powerUp.getName());
    }

    /**
     * Test that setting null type throws exception.
     */
    @Test
    void testSetTypeWithNull() {
        PowerUp powerUp = new PowerUp(4, 40f, 40f);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> powerUp.setType(null)
        );
        
        assertEquals("Type cannot be null", exception.getMessage());
    }

    /**
     * Test getTimeRemaining returns default lifespan.
     */
    @Test
    void testGetTimeRemaining() {
        PowerUp powerUp = new PowerUp(5, 50f, 50f);
        
        assertEquals(300, powerUp.getTimeRemaining());
    }

    /**
     * Test setTimeRemaining clamps to valid range.
     */
    @Test
    void testSetTimeRemainingClamped() {
        PowerUp powerUp = new PowerUp(6, 60f, 60f);
        
        powerUp.setTimeRemaining(0);
        assertEquals(0, powerUp.getTimeRemaining());
        
        powerUp.setTimeRemaining(1000);
        assertEquals(300, powerUp.getTimeRemaining()); // Default lifespan
        
        powerUp.setLifespan(500);
        powerUp.setTimeRemaining(600);
        assertEquals(500, powerUp.getTimeRemaining()); // New lifespan
    }

    /**
     * Test reduceLifespan decreases time remaining.
     */
    @Test
    void testReduceLifespan() {
        PowerUp powerUp = new PowerUp(7, 70f, 70f, PowerUpType.AMMO);
        powerUp.setLifespan(600);
        
        assertEquals(600, powerUp.getTimeRemaining());
        
        powerUp.reduceLifespan(100);
        assertEquals(500, powerUp.getTimeRemaining());
        
        powerUp.reduceLifespan(500);
        assertEquals(0, powerUp.getTimeRemaining());
    }

    /**
     * Test canPickup returns true for active, uncollected power-ups.
     */
    @Test
    void testCanPickup() {
        PowerUp powerUp = new PowerUp(8, 80f, 80f);
        
        assertTrue(powerUp.canPickup());
    }

    /**
     * Test canPickup returns false when collected.
     */
    @Test
    void testCanPickupWhenCollected() {
        PowerUp powerUp = new PowerUp(9, 90f, 90f);
        powerUp.collected = true;
        
        assertFalse(powerUp.canPickup());
    }

    /**
     * Test canPickup returns false when inactive.
     */
    @Test
    void testCanPickupWhenInactive() {
        PowerUp powerUp = new PowerUp(10, 100f, 100f);
        powerUp.setActive(false);
        
        assertFalse(powerUp.canPickup());
    }

    /**
     * Test canPickup returns false when lifespan expired.
     */
    @Test
    void testCanPickupWhenExpired() {
        PowerUp powerUp = new PowerUp(11, 110f, 110f);
        powerUp.reduceLifespan(300);
        
        assertFalse(powerUp.canPickup());
    }

    /**
     * Test pickup collects the power-up.
     */
    @Test
    void testPickup() {
        PowerUp powerUp = new PowerUp(12, 120f, 120f);
        PowerUpType type = powerUp.pickup();
        
        assertEquals(PowerUpType.HEALTH, type);
        assertTrue(powerUp.collected);
        assertEquals(0, powerUp.getTimeRemaining());
    }

    /**
     * Test pickup returns null when cannot pick up.
     */
    @Test
    void testPickupWhenCannotPickup() {
        PowerUp powerUp = new PowerUp(13, 130f, 130f);
        powerUp.collected = true;
        
        PowerUpType type = powerUp.pickup();
        assertNull(type);
    }

    /**
     * Test getEffectValue returns type-specific value.
     */
    @Test
    void testGetEffectValue() {
        assertEquals(50, new PowerUp(14, 140f, 140f, PowerUpType.HEALTH).getEffectValue());
        assertEquals(25, new PowerUp(15, 150f, 150f, PowerUpType.ARMOR).getEffectValue());
        assertEquals(50, new PowerUp(16, 160f, 160f, PowerUpType.AMMO).getEffectValue());
        assertEquals(0, new PowerUp(17, 170f, 170f, PowerUpType.ROCKET).getEffectValue());
        assertEquals(0, new PowerUp(18, 180f, 180f, PowerUpType.SPEED).getEffectValue());
    }

    /**
     * Test getVisual returns type-specific visual.
     */
    @Test
    void testGetVisual() {
        assertEquals("🛡️❤️", new PowerUp(19, 190f, 190f, PowerUpType.HEALTH).getVisual());
        assertEquals("🛡️", new PowerUp(20, 200f, 200f, PowerUpType.ARMOR).getVisual());
        assertEquals("🔫", new PowerUp(21, 210f, 210f, PowerUpType.AMMO).getVisual());
        assertEquals("🚀", new PowerUp(22, 220f, 220f, PowerUpType.ROCKET).getVisual());
    }

    /**
     * Test getCategory returns type-specific category.
     */
    @Test
    void testGetCategory() {
        assertEquals("Health", new PowerUp(23, 230f, 230f, PowerUpType.HEALTH).getCategory());
        assertEquals("Armor", new PowerUp(24, 240f, 240f, PowerUpType.ARMOR).getCategory());
        assertEquals("Ammo", new PowerUp(25, 250f, 250f, PowerUpType.AMMO).getCategory());
        assertEquals("Weapon", new PowerUp(26, 260f, 260f, PowerUpType.ROCKET).getCategory());
        assertEquals("Speed", new PowerUp(27, 270f, 270f, PowerUpType.SPEED).getCategory());
        assertEquals("Special", new PowerUp(28, 280f, 280f, PowerUpType.INVISIBILITY).getCategory());
    }

    /**
     * Test getDescription returns type-specific description.
     */
    @Test
    void testGetDescription() {
        assertEquals("Health Pack - 🛡️❤️", new PowerUp(29, 290f, 290f, PowerUpType.HEALTH).getDescription());
        assertEquals("Rocket Launcher - 🚀", new PowerUp(30, 300f, 300f, PowerUpType.ROCKET).getDescription());
    }

    /**
     * Test update decreases time remaining.
     */
    @Test
    void testUpdateDecrementsLifespan() {
        PowerUp powerUp = new PowerUp(31, 310f, 310f, PowerUpType.HEALTH);
        powerUp.setLifespan(100);
        
        assertEquals(100, powerUp.getTimeRemaining());
        
        powerUp.update();
        assertEquals(99, powerUp.getTimeRemaining());
        
        for (int i = 0; i < 95; i++) {
            powerUp.update();
        }
        assertEquals(4, powerUp.getTimeRemaining());
    }

    /**
     * Test update deactivates when lifespan expires.
     */
    @Test
    void testUpdateExpires() {
        PowerUp powerUp = new PowerUp(32, 320f, 320f, PowerUpType.HEALTH);
        powerUp.setLifespan(3);
        
        powerUp.update();
        assertFalse(powerUp.canPickup());
        assertFalse(powerUp.isActive());
        assertTrue(powerUp.collected);
    }

    /**
     * Test update does nothing when dead.
     */
    @Test
    void testUpdateWhenDead() {
        PowerUp powerUp = new PowerUp(33, 330f, 330f);
        powerUp.die();
        
        for (int i = 0; i < 100; i++) {
            powerUp.update();
        }
        assertFalse(powerUp.isActive());
        assertFalse(powerUp.canPickup());
    }

    /**
     * Test isActive returns true for active power-ups.
     */
    @Test
    void testIsActive() {
        PowerUp powerUp = new PowerUp(34, 340f, 340f);
        
        assertTrue(powerUp.isActive());
    }

    /**
     * Test setActive toggles active state.
     */
    @Test
    void testSetActive() {
        PowerUp powerUp = new PowerUp(35, 350f, 350f);
        
        assertFalse(powerUp.isActive());
        
        powerUp.setActive(true);
        assertTrue(powerUp.isActive());
        
        powerUp.setActive(false);
        assertFalse(powerUp.isActive());
    }

    /**
     * Test deactivate sets power-up inactive.
     */
    @Test
    void testDeactivate() {
        PowerUp powerUp = new PowerUp(36, 360f, 360f);
        
        powerUp.deactivate();
        assertFalse(powerUp.isActive());
        assertFalse(powerUp.canPickup());
    }

    /**
     * Test resurrect restores power-up to active state.
     */
    @Test
    void testResurrect() {
        PowerUp powerUp = new PowerUp(37, 370f, 370f);
        powerUp.reduceLifespan(300);
        powerUp.setActive(false);
        
        assertEquals(0, powerUp.getTimeRemaining());
        assertFalse(powerUp.isActive());
        
        powerUp.resurrect();
        
        assertTrue(powerUp.isActive());
        assertEquals(300, powerUp.getTimeRemaining());
        assertFalse(powerUp.collected);
    }

    /**
     * Test getLifespan returns configured lifespan.
     */
    @Test
    void testGetLifespan() {
        PowerUp powerUp = new PowerUp(38, 380f, 380f);
        
        assertEquals(300, powerUp.getLifespan());
    }

    /**
     * Test setLifespan clamps to valid range.
     */
    @Test
    void testSetLifespanClamped() {
        PowerUp powerUp = new PowerUp(39, 390f, 390f);
        
        powerUp.setLifespan(60);
        assertEquals(60, powerUp.getLifespan());
        
        powerUp.setLifespan(999999);
        assertEquals(999999, powerUp.getLifespan());
        
        powerUp.setLifespan(50);
        assertEquals(60, powerUp.getLifespan()); // Minimum 60
        
        powerUp.setLifespan(-100);
        assertEquals(60, powerUp.getLifespan()); // Minimum 60
    }

    /**
     * Test toString returns expected format.
     */
    @Test
    void testToString() {
        PowerUp powerUp = new PowerUp(40, 400f, 400f, PowerUpType.AMMO);
        powerUp.reduceLifespan(100);
        
        String str = powerUp.toString();
        
        assertTrue(str.contains("id=40"));
        assertTrue(str.contains("type=AMMO"));
        assertTrue(str.contains("timeRemaining=200"));
        assertTrue(str.contains("active=true"));
        assertTrue(str.contains("collected=false"));
    }

    /**
     * Test PowerUp with custom width and height.
     */
    @Test
    void testCreationWithCustomDimensions() {
        PowerUp powerUp = new PowerUp(41, 410f, 410f, 16, 16, PowerUpType.HEALTH);
        
        assertEquals(16, powerUp.getSize().getWidth());
        assertEquals(16, powerUp.getSize().getHeight());
        assertEquals(PowerUpType.HEALTH, powerUp.getType());
    }
}
