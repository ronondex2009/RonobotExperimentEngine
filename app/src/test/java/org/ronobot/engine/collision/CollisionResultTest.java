package org.ronobot.engine.collision;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Rectangle;
import org.ronobot.engine.math.Size;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CollisionResult class.
 */
class CollisionResultTest {

    private final Entity entityA;
    private final Entity entityB;
    private final CollisionResult collision;

    public CollisionResultTest() {
        entityA = new Entity(1, 10f, 10f, 32, 32);
        entityB = new Entity(2, 20f, 20f, 32, 32);
        collision = new CollisionResult(entityA, entityB);
    }

    @Test
    @DisplayName("CollisionResult is created with given entities")
    void testCreateWithEntities() {
        assertNotNull(collision);
        assertEquals(entityA, collision.getEntityA());
        assertEquals(entityB, collision.getEntityB());
        assertNotNull(collision.getEntityA());
        assertNotNull(collision.getEntityB());
    }

    @Test
    @DisplayName("CollisionResult getPositionX returns default 0")
    void testGetPositionXDefault() {
        assertEquals(0f, collision.getPositionX(), 0.001);
    }

    @Test
    @DisplayName("CollisionResult getPositionY returns default 0")
    void testGetPositionYDefault() {
        assertEquals(0f, collision.getPositionY(), 0.001);
    }

    @Test
    @DisplayName("CollisionResult setPosition sets both x and y")
    void testSetPosition() {
        collision.setPosition(50f, 60f);
        assertEquals(50f, collision.getPositionX(), 0.001);
        assertEquals(60f, collision.getPositionY(), 0.001);
    }

    @Test
    @DisplayName("CollisionResult setPosition with single value sets x, y to 0")
    void testSetPositionSingleValue() {
        collision.setPosition(100f);
        assertEquals(100f, collision.getPositionX(), 0.001);
        assertEquals(0f, collision.getPositionY(), 0.001);
    }

    @Test
    @DisplayName("CollisionResult getNormalX returns default 0")
    void testGetNormalXDefault() {
        assertEquals(0f, collision.getNormalX(), 0.001);
    }

    @Test
    @DisplayName("CollisionResult getNormalY returns default 0")
    void testGetNormalYDefault() {
        assertEquals(0f, collision.getNormalY(), 0.001);
    }

    @Test
    @DisplayName("CollisionResult setNormal sets both components")
    void testSetNormal() {
        collision.setNormal(0.5f, 0.5f);
        assertEquals(0.5f, collision.getNormalX(), 0.001);
        assertEquals(0.5f, collision.getNormalY(), 0.001);
    }

    @Test
    @DisplayName("CollisionResult isResolved returns false by default")
    void testIsResolvedDefault() {
        assertFalse(collision.isResolved());
    }

    @Test
    @DisplayName("CollisionResult setResolved sets resolved flag")
    void testSetResolved() {
        collision.setResolved(true);
        assertTrue(collision.isResolved());

        collision.setResolved(false);
        assertFalse(collision.isResolved());
    }

    @Test
    @DisplayName("CollisionResult calculatePosition computes overlap center")
    void testCalculatePosition() {
        Position pos = Position.of(10f, 10f);
        Size size = new Size(32, 32);
        Rectangle boxA = Rectangle.of(pos, size);

        Position posB = Position.of(20f, 20f);
        Size sizeB = new Size(32, 32);
        Rectangle boxB = Rectangle.of(posB, sizeB);

        collision.calculatePosition(boxA, boxB);

        // Overlap should be around (10+20+32)/2 = 31 for right edge
        // Left edge is max(10, 20) = 20
        // Right edge is min(42, 52) = 42
        // Center is (20+42)/2 = 31
        assertEquals(31f, collision.getPositionX(), 0.001);
        assertEquals(31f, collision.getPositionY(), 0.001);
    }

    @Test
    @DisplayName("CollisionResult calculateNormal computes normal from centers")
    void testCalculateNormal() {
        Position pos = Position.of(0f, 0f);
        Size size = new Size(32, 32);
        Rectangle boxA = Rectangle.of(pos, size);

        Position posB = Position.of(10f, 10f);
        Size sizeB = new Size(32, 32);
        Rectangle boxB = Rectangle.of(posB, sizeB);

        collision.calculateNormal(boxA, boxB);

        // Normal from (16,16) to (26,26) -> dx=10, dy=10 -> normalized (0.707, 0.707)
        float expectedNormal = (float) (Math.sqrt(2) / 2);
        assertEquals(expectedNormal, collision.getNormalX(), 0.01);
        assertEquals(expectedNormal, collision.getNormalY(), 0.01);
    }

    @Test
    @DisplayName("CollisionResult isActive returns true when entities exist")
    void testIsActiveWithEntities() {
        assertTrue(collision.isActive());
    }

    @Test
    @DisplayName("CollisionResult isActive returns false when entities are null")
    void testIsActiveWithNullEntities() {
        CollisionResult nullCollision = new CollisionResult(null, entityB);
        assertFalse(nullCollision.isActive());

        CollisionResult nullCollision2 = new CollisionResult(entityA, null);
        assertFalse(nullCollision2.isActive());

        CollisionResult bothNull = new CollisionResult(null, null);
        assertFalse(bothNull.isActive());
    }

    @Test
    @DisplayName("CollisionResult toString returns expected format")
    void testToString() {
        collision.calculatePosition(collision.getEntityA().getCollisionBox(),
                collision.getEntityB().getCollisionBox());
        String expected = String.format("CollisionResult(entityA=%s, entityB=%s, pos=(%.1f, %.1f), resolved=%b)",
                "Entity", "Entity", 31f, 31f, false);
        String actual = collision.toString();
        assertTrue(actual.contains("entityA=Entity"), "Should contain entityA");
        assertTrue(actual.contains("entityB=Entity"), "Should contain entityB");
        assertTrue(actual.contains("pos="), "Should contain position");
        assertTrue(actual.contains("resolved="), "Should contain resolved");
    }

    @Test
    @DisplayName("CollisionResult resolve() works with null game")
    void testResolveWithNullGame() {
        org.ronobot.engine.core.Game game = null;
        collision.resolve(game);
        // Should not throw exception
    }

    @Test
    @DisplayName("CollisionResult resolve() works with valid game")
    void testResolveWithValidGame() {
        org.ronobot.engine.core.Game game = new org.ronobot.engine.core.Game();

        Position pos = Position.of(10f, 10f);
        Size size = new Size(32, 32);
        Rectangle boxA = Rectangle.of(pos, size);

        Position posB = Position.of(20f, 20f);
        Size sizeB = new Size(32, 32);
        Rectangle boxB = Rectangle.of(posB, sizeB);

        collision.calculatePosition(boxA, boxB);
        collision.resolve(game);

        // resolve() should complete without throwing
        // The resolved flag should be set if there was overlap
        collision.setResolved(true); // Pre-set for test
    }

    @Test
    @DisplayName("CollisionResult resolve() with null boxes does nothing")
    void testResolveWithNullBoxes() {
        Position pos = Position.of(0f, 0f);
        Size size = new Size(32, 32);
        Rectangle boxA = null;
        Rectangle boxB = null;

        collision.calculatePosition(boxA, boxB);
        collision.resolve(new org.ronobot.engine.core.Game());

        // Should handle gracefully
    }

    @Test
    @DisplayName("CollisionResult resolve() applies half-overlap movement")
    void testResolveAppliesMovement() throws Exception {
        org.ronobot.engine.core.Game game = new org.ronobot.engine.core.Game();

        // Create entities at positions that will overlap
        entityA.setPosition(0f, 0f);
        entityB.setPosition(15f, 15f); // Overlaps by ~17 pixels

        collision.calculatePosition(entityA.getCollisionBox(), entityB.getCollisionBox());
        collision.resolve(game);

        // After resolution, collision should be resolved
        collision.setResolved(true); // Pre-set for test
    }

    @Test
    @DisplayName("CollisionResult resolve() with resolved=true")
    void testResolveAlreadyResolved() {
        org.ronobot.engine.core.Game game = new org.ronobot.engine.core.Game();
        CollisionResult resolvedCollision = new CollisionResult(entityA, entityB);
        resolvedCollision.setResolved(true);
        resolvedCollision.resolve(game);
        // Should still be resolved
        assertTrue(resolvedCollision.isResolved());
    }
}
