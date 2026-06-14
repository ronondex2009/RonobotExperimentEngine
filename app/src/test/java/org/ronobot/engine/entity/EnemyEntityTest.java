package org.ronobot.engine.entity;

import org.ronobot.engine.core.Entity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EnemyEntity.
 * <p>
 * Tests enemy behavior including movement, targeting, attacking,
 * and damage handling.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
@DisplayName("EnemyEntity Tests")
class EnemyEntityTest {

    private static final Position TEST_POSITION = new Position(100f, 100f);
    private static final Size TEST_SIZE = new Size(32, 32);

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("EnemyEntity created with default position")
        void shouldCreateEnemyWithDefaultPosition() {
            EnemyEntity enemy = new EnemyEntity(1, 10f, 10f, "Test");
            assertEquals(10f, enemy.getX(), 0.01);
            assertEquals(10f, enemy.getY(), 0.01);
            assertEquals(32, enemy.getWidth());
            assertEquals(32, enemy.getHeight());
            assertEquals("Enemy", enemy.getName());
        }

        @Test
        @DisplayName("EnemyEntity created with custom size")
        void shouldCreateEnemyWithCustomSize() {
            EnemyEntity enemy = new EnemyEntity(1, 10f, 10f, 64, 64);
            assertEquals(64, enemy.getWidth());
            assertEquals(64, enemy.getHeight());
        }

        @Test
        @DisplayName("EnemyEntity has correct initial health")
        void shouldInitializeWithMaxHealth() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            assertEquals(EnemyEntity.MAX_HEALTH, enemy.getHealth());
        }

        @Test
        @DisplayName("EnemyEntity health clamped to maximum")
        void shouldClampHealthToMaximum() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setHealth(200);
            assertEquals(EnemyEntity.MAX_HEALTH, enemy.getHealth());
        }

        @Test
        @DisplayName("EnemyEntity health clamped to minimum")
        void shouldClampHealthToMinimum() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setHealth(-50);
            assertEquals(0, enemy.getHealth());
        }
    }

    @Nested
    @DisplayName("Targeting Tests")
    class TargetingTests {

        @Test
        @DisplayName("Get target returns null initially")
        void shouldReturnNullTargetInitially() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            assertNull(enemy.getTarget());
        }

        @Test
        @DisplayName("Set target and get back same entity")
        void shouldStoreTargetCorrectly() {
            Entity testTarget = new Entity(2, TEST_POSITION.getX(), TEST_POSITION.getY(), TEST_SIZE.getWidth(), TEST_SIZE.getHeight());
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setTarget(testTarget);
            assertEquals(testTarget, enemy.getTarget());
        }

        @Test
        @DisplayName("Attack returns false when no target")
        void shouldNotAttackWithoutTarget() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            assertFalse(enemy.attack());
            assertEquals(EnemyEntity.MAX_HEALTH, enemy.getHealth()); // No damage taken
        }

        @Test
        @DisplayName("Attack returns false when target is null")
        void shouldNotAttackWithNullTarget() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setTarget(null);
            assertFalse(enemy.attack());
        }

        @Test
        @DisplayName("Attack returns false when target is dead")
        void shouldNotAttackDeadTarget() {
            Entity deadTarget = new Entity(999, TEST_POSITION.getX(), TEST_POSITION.getY(), TEST_SIZE.getWidth(), TEST_SIZE.getHeight());
            deadTarget.takeDamage(1000); // Kill it
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setTarget(deadTarget);
            assertFalse(enemy.attack());
        }

        @Test
        @DisplayName("Can attack when cooldown is ready")
        void shouldAttackWhenCooldownReady() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setTarget(new Entity(2, TEST_POSITION.getX(), TEST_POSITION.getY(), TEST_SIZE.getWidth(), TEST_SIZE.getHeight()));
            enemy.reduceAttackCooldown(100); // Ready
            assertTrue(enemy.canAttack());
        }

        @Test
        @DisplayName("Cooldown decrements correctly")
        void shouldDecrementCooldown() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setAttackCooldown(10);
            enemy.reduceAttackCooldown(3);
            assertEquals(7, enemy.getAttackCooldown());
        }

        @Test
        @DisplayName("Cooldown clamped to minimum")
        void shouldClampCooldownToMinimum() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.reduceAttackCooldown(100);
            assertEquals(0, enemy.getAttackCooldown());
        }
    }

    @Nested
    @DisplayName("Attack Tests")
    class AttackTests {

        @Test
        @DisplayName("Attack deals base damage")
        void shouldDealBaseDamage() {
            Entity victim = new Entity(2, TEST_POSITION.getX(), TEST_POSITION.getY(), TEST_SIZE.getWidth(), TEST_SIZE.getHeight());
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setTarget(victim);
            enemy.reduceAttackCooldown(100); // Ready

            boolean attacked = enemy.attack();
            assertTrue(attacked);
            assertEquals(75, victim.getHealth()); // 100 - 25 base damage
        }

        @Test
        @DisplayName("Attack deals zero damage when not in range")
        void shouldNotAttackOutOfRange() {
            Entity victim = new Entity(2, 500f, 500f, TEST_SIZE.getWidth(), TEST_SIZE.getHeight()); // Far away
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setTarget(victim);

            boolean attacked = enemy.attack();
            assertFalse(attacked);
            assertEquals(100, victim.getHealth()); // No damage
        }

        @Test
        @DisplayName("Attack sets cooldown after hitting")
        void shouldSetCooldownAfterAttack() {
            Entity victim = new Entity(2, TEST_POSITION.getX(), TEST_POSITION.getY(), TEST_SIZE.getWidth(), TEST_SIZE.getHeight());
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setTarget(victim);

            enemy.attack();
            assertEquals(EnemyEntity.ATTACK_COOLDOWN_MAX, enemy.getAttackCooldown());
        }

        @Test
        @DisplayName("Attack does nothing when cooldown not ready")
        void shouldNotAttackWhenCooldownNotReady() {
            Entity victim = new Entity(2, TEST_POSITION.getX(), TEST_POSITION.getY(), TEST_SIZE.getWidth(), TEST_SIZE.getHeight());
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setTarget(victim);
            // Cooldown at max, not ready
            assertEquals(EnemyEntity.ATTACK_COOLDOWN_MAX, enemy.getAttackCooldown());
            assertFalse(enemy.canAttack());

            enemy.reduceAttackCooldown(EnemyEntity.ATTACK_COOLDOWN_MAX); // Ready
            assertTrue(enemy.canAttack());

            enemy.attack();
            assertEquals(EnemyEntity.ATTACK_COOLDOWN_MAX, enemy.getAttackCooldown()); // Back to max
            assertEquals(75, victim.getHealth()); // Attacked with base damage
        }
    }

    @Nested
    @DisplayName("Damage Tests")
    class DamageTests {

        @Test
        @DisplayName("Take damage reduces health")
        void shouldTakeDamage() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setHealth(100);
            int remaining = enemy.takeDamage(25);
            assertEquals(75, remaining);
        }

        @Test
        @DisplayName("Take damage clamps health to minimum")
        void shouldClampDamageToMinimum() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.takeDamage(200);
            assertEquals(0, enemy.getHealth());
            assertEquals(false, enemy.isActive());
        }

        @Test
        @DisplayName("Take damage does nothing when dead")
        void shouldNotTakeDamageWhenDead() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.takeDamage(1000); // Kill
            int remaining = enemy.takeDamage(50);
            assertEquals(0, remaining);
        }

        @Test
        @DisplayName("Take damage returns remaining health")
        void shouldReturnRemainingHealth() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            int remaining = enemy.takeDamage(30);
            assertEquals(70, remaining);
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Update decrements cooldown")
        void shouldDecrementCooldownOnUpdate() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setAttackCooldown(10);
            enemy.update();
            assertEquals(9, enemy.getAttackCooldown());
        }

        @Test
        @DisplayName("Update with zero cooldown does nothing")
        void shouldNotChangeZeroCooldown() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setAttackCooldown(0);
            enemy.update();
            assertEquals(0, enemy.getAttackCooldown());
        }
    }

    @Nested
    @DisplayName("Movement Tests")
    class MovementTests {

        @Test
        @DisplayName("Move towards target within range")
        void shouldMoveTowardsTarget() {
            Entity target = new Entity(2, 200f, 200f, TEST_SIZE.getWidth(), TEST_SIZE.getHeight());
            EnemyEntity enemy = new EnemyEntity(1, 50f, 50f, "Test");
            enemy.setTarget(target);
            // Reduce distance to within attack range
            enemy.move(50f, 50f); // Move to (100, 100)
            assertEquals(100f, enemy.getX(), 0.01);
            assertEquals(100f, enemy.getY(), 0.01);
        }

        @Test
        @DisplayName("Move updates position")
        void shouldUpdatePositionOnMove() {
            EnemyEntity enemy = new EnemyEntity(1, 50f, 50f, "Test");
            enemy.move(10f, 20f);
            assertEquals(60f, enemy.getX(), 0.01);
            assertEquals(70f, enemy.getY(), 0.01);
        }

        @Test
        @DisplayName("Move does nothing when target dead")
        void shouldNotMoveTowardsDeadTarget() {
            Entity deadTarget = new Entity(999, 500f, 500f, TEST_SIZE.getWidth(), TEST_SIZE.getHeight());
            deadTarget.takeDamage(1000);
            EnemyEntity enemy = new EnemyEntity(1, 50f, 50f, "Test");
            enemy.setTarget(deadTarget);
            enemy.update();

            // Position should not change since target is dead
            assertEquals(50f, enemy.getX(), 0.01);
        }

        @Test
        @DisplayName("Move does nothing when target not in range")
        void shouldNotMoveWhenTargetOutOfRange() {
            Entity farTarget = new Entity(2, 1000f, 1000f, TEST_SIZE.getWidth(), TEST_SIZE.getHeight());
            EnemyEntity enemy = new EnemyEntity(1, 50f, 50f, "Test");
            enemy.setTarget(farTarget);
            enemy.update();

            // Position should not change since target is out of range
            assertEquals(50f, enemy.getX(), 0.01);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("ToString includes all relevant fields")
        void shouldIncludeFieldsInToString() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            String toString = enemy.toString();
            assertTrue(toString.contains("EnemyEntity"));
            assertTrue(toString.contains("health"));
            assertTrue(toString.contains("attackCooldown"));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Attack does nothing when enemy is dead")
        void shouldNotAttackWhenDead() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setHealth(0);
            enemy.setTarget(new Entity(2, TEST_POSITION.getX(), TEST_POSITION.getY(), TEST_SIZE.getWidth(), TEST_SIZE.getHeight()));

            boolean attacked = enemy.attack();
            assertEquals(false, attacked);
            assertEquals(false, enemy.isActive());
        }

        @Test
        @DisplayName("Update does nothing when dead")
        void shouldNotUpdateWhenDead() {
            EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
            enemy.setHealth(0);
            enemy.update();

            // Health should remain 0
            assertEquals(0, enemy.getHealth());
        }
    }
}
