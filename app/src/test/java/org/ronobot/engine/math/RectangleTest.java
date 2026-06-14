package org.ronobot.engine.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Rectangle class.
 * <p>
 * Tests include basic operations, intersection detection, containment checks,
 * and boundary checks.
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-05-28
 */
@DisplayName("Rectangle Tests")
class RectangleTest {

    @Nested
    @DisplayName("Intersection Detection")
    class IntersectionTests {

        private final Rectangle rect1 = Rectangle.of(0, 0, 100, 100);
        private final Rectangle rect2 = Rectangle.of(50, 50, 100, 100);

        @DisplayName("Rectangles that overlap return true for intersects")
        @Test
        void testOverlappingRectangles() {
            assertTrue(rect1.intersects(rect2));
        }

        @DisplayName("Non-overlapping rectangles return false for intersects")
        @Test
        void testNonOverlappingRectangles() {
            Rectangle rect3 = Rectangle.of(150, 150, 100, 100);
            assertFalse(rect1.intersects(rect3));
        }

        @DisplayName("One rectangle completely inside another")
        @Test
        void testContainment() {
            Rectangle largeRect = Rectangle.of(0, 0, 500, 500);
            Rectangle smallRect = Rectangle.of(100, 100, 100, 100);

            assertTrue(largeRect.intersects(smallRect));
            assertTrue(smallRect.intersects(largeRect));
        }

        @DisplayName("Edge-touching rectangles return false")
        @Test
        void testEdgeTouching() {
            Rectangle rectTouch1 = Rectangle.of(0, 0, 100, 100);
            Rectangle rectTouch2 = Rectangle.of(100, 0, 100, 100);

            assertFalse(rectTouch1.intersects(rectTouch2));
        }
    }

    @Nested
    @DisplayName("Containment Checks")
    class ContainmentTests {

        private final Rectangle container = Rectangle.of(0, 0, 200, 200);
        private final Rectangle contained = Rectangle.of(10, 10, 50, 50);

        @DisplayName("Small rectangle is contained within larger rectangle")
        @Test
        void testSmallContainedInLarge() {
            assertTrue(container.contains(contained));
            assertFalse(contained.contains(container));
        }

        @DisplayName("Non-contained rectangle returns false")
        @Test
        void testNonContained() {
            Rectangle nonContained = Rectangle.of(10, 10, 300, 300);
            assertFalse(container.contains(nonContained));
        }

        @DisplayName("Rectangle containing itself")
        @Test
        void testSelfContained() {
            assertTrue(contained.contains(contained));
        }
    }

    @Nested
    @DisplayName("Boundary Checks")
    class BoundaryTests {

        private final Rectangle rect = Rectangle.of(100, 100, 100, 100);

        @DisplayName("Top edge is at boundary")
        @Test
        void testTopBoundary() {
            assertTrue(rect.isAtBoundary(100, 100));
        }

        @DisplayName("Left edge is at boundary")
        @Test
        void testLeftBoundary() {
            assertTrue(rect.isAtBoundary(100, 150));
        }
    }

    @Nested
    @DisplayName("Expansion Operations")
    class ExpansionTests {

        private final Rectangle rect = Rectangle.of(100, 100, 100, 100);

        @DisplayName("Expand by positive amount")
        @Test
        void testExpandPositive() {
            Rectangle expanded = rect.expand(50);
            assertEquals(100, expanded.getX(), 0.01);
            assertEquals(100, expanded.getY(), 0.01);
            assertTrue(expanded.getWidth() > rect.getWidth());
            assertTrue(expanded.getHeight() > rect.getHeight());
        }

        @DisplayName("Expand by negative amount (contract)")
        @Test
        void testExpandNegative() {
            Rectangle contracted = rect.expand(-25);
            assertEquals(100, contracted.getX(), 0.01);
            assertEquals(100, contracted.getY(), 0.01);
            assertTrue(contracted.getWidth() < rect.getWidth());
            assertTrue(contracted.getHeight() < rect.getHeight());
        }

        @DisplayName("Zero expansion returns same rectangle")
        @Test
        void testZeroExpansion() {
            Rectangle result = rect.expand(0);
            assertEquals(rect, result);
        }
    }
}
