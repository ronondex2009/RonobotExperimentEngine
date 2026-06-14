package org.ronobot.engine.collision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CollisionNotification class.
 *
 * @author ronobot
 * @since 1.0
 */
public class CollisionNotificationTest {

    /**
     * Tests that wall collision notification is created correctly.
     */
    @Test
    @DisplayName("Wall collision notification is created correctly")
    public void testWallCollision() {
        CollisionNotification notification = new CollisionNotification(
                CollisionNotification.EventType.WALL_COLLISION,
                1,
                0,
                5.5,
                3.2,
                100
        );

        assertEquals(CollisionNotification.EventType.WALL_COLLISION, notification.getType());
        assertEquals(1, notification.getPrimaryId());
        assertEquals(0, notification.getSecondaryId());
        assertEquals(5.5, notification.getX());
        assertEquals(3.2, notification.getY());
        assertEquals(100, notification.getFrame());
        assertTrue(notification.isWallCollision());
    }

    /**
     * Tests that entity collision notification is created correctly.
     */
    @Test
    @DisplayName("Entity collision notification is created correctly")
    public void testEntityCollision() {
        CollisionNotification notification = new CollisionNotification(
                CollisionNotification.EventType.ENTITY_COLLISION,
                1,
                2,
                4.5,
                4.5,
                150
        );

        assertEquals(CollisionNotification.EventType.ENTITY_COLLISION, notification.getType());
        assertEquals(1, notification.getPrimaryId());
        assertEquals(2, notification.getSecondaryId());
        assertEquals(4.5, notification.getX());
        assertEquals(4.5, notification.getY());
        assertEquals(150, notification.getFrame());
        assertTrue(notification.isEntityCollision());
    }

    /**
     * Tests that projectile hit notification is created correctly.

    /**
     * Tests that toString returns correct format.
     */
    @Test
    @DisplayName("ToString returns correct format")
    public void testToString() {
        CollisionNotification notification = new CollisionNotification(
                CollisionNotification.EventType.WALL_COLLISION,
                1,
                0,
                5.5,
                3.2,
                100
        );

        String result = notification.toString();
        assertTrue(result.contains("type=WALL_COLLISION"));
        assertTrue(result.contains("primaryId=1"));
        assertTrue(result.contains("secondaryId=0"));
        assertTrue(result.contains("x=5.5"));
        assertTrue(result.contains("y=3.2"));
        assertTrue(result.contains("frame=100"));
    }

    /**
     * Tests that notifications with different secondary IDs work correctly.
     */
    @Test
    @DisplayName("Notifications with different secondary IDs work correctly")
    public void testSecondaryIds() {
        CollisionNotification noSecondary = new CollisionNotification(
                CollisionNotification.EventType.WALL_COLLISION,
                1,
                0,
                5.0,
                5.0,
                50
        );

        CollisionNotification withSecondary = new CollisionNotification(
                CollisionNotification.EventType.ENTITY_COLLISION,
                1,
                5,
                5.0,
                5.0,
                50
        );

        assertEquals(0, noSecondary.getSecondaryId());
        assertEquals(5, withSecondary.getSecondaryId());
    }

    /**
     * Tests collision notifications with zero positions.
     */
    @Test
    @DisplayName("Collision notifications with zero positions work correctly")
    public void testZeroPositions() {
        CollisionNotification notification = new CollisionNotification(
                CollisionNotification.EventType.BOUNDARY_REACHED,
                1,
                0,
                0.0,
                0.0,
                0
        );

        assertEquals(0.0, notification.getX());
        assertEquals(0.0, notification.getY());
        assertEquals(0, notification.getFrame());
    }

    /**
     * Tests collision notifications with large frame numbers.
     */
    @Test
    @DisplayName("Collision notifications with large frame numbers work correctly")
    public void testLargeFrameNumbers() {
        CollisionNotification notification = new CollisionNotification(
                CollisionNotification.EventType.PROJECTILE_HIT,
                100,
                50,
                10.0,
                10.0,
                Integer.MAX_VALUE
        );

        assertEquals(Integer.MAX_VALUE, notification.getFrame());
    }

    /**
     * Tests that all event types can be created.
     */
    @Test
    @DisplayName("All event types can be created")
    public void testAllEventTypes() {
        CollisionNotification wall = new CollisionNotification(
                CollisionNotification.EventType.WALL_COLLISION,
                1,
                0,
                1.0,
                1.0,
                1
        );

        CollisionNotification entity = new CollisionNotification(
                CollisionNotification.EventType.ENTITY_COLLISION,
                1,
                2,
                2.0,
                2.0,
                2
        );

        CollisionNotification projectile = new CollisionNotification(
                CollisionNotification.EventType.PROJECTILE_HIT,
                1,
                2,
                3.0,
                3.0,
                3
        );

        CollisionNotification decoration = new CollisionNotification(
                CollisionNotification.EventType.MAP_DECORATION,
                1,
                0,
                4.0,
                4.0,
                4
        );

        CollisionNotification boundary = new CollisionNotification(
                CollisionNotification.EventType.BOUNDARY_REACHED,
                1,
                0,
                5.0,
                5.0,
                5
        );

        CollisionNotification door = new CollisionNotification(
                CollisionNotification.EventType.DOOR_INTERACTION,
                1,
                0,
                6.0,
                6.0,
                6
        );

        assertNotNull(wall);
        assertNotNull(entity);
        assertNotNull(projectile);
        assertNotNull(decoration);
        assertNotNull(boundary);
        assertNotNull(door);
    }
}
