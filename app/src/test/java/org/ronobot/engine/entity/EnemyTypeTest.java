package org.ronobot.engine.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EnemyType enum.
 * <p>
 * Tests verify enemy type characteristics including movement speed,
 * damage, cooldown, health, patrol range, and sound sensitivity.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
@DisplayName("EnemyType Tests")
class EnemyTypeTest {

    @Nested
    @DisplayName("Enum Values")
    class EnumValuesTests {

        @Test
        @DisplayName("Zombie type exists with default characteristics")
        void zombieTypeExists() {
            EnemyType zombie = EnemyType.ZOMBIE;
            assertEquals(2f, zombie.getBaseMoveSpeed(), 0.01f);
            assertEquals(25f, zombie.getBaseDamage(), 0.01f);
            assertEquals(60, zombie.getCooldownFrames());
            assertEquals(100f, zombie.getBaseHealth(), 0.01f);
            assertEquals(100, zombie.getPatrolRange());
            assertEquals(0f, zombie.getSoundSensitivity(), 0.01f);
            assertEquals("🧟", zombie.getVisualName());
        }

        @Test
        @DisplayName("Demon type exists with aggressive characteristics")
        void demonTypeExists() {
            EnemyType demon = EnemyType.DEMON;
            assertEquals(3.5f, demon.getBaseMoveSpeed(), 0.01f);
            assertEquals(40f, demon.getBaseDamage(), 0.01f);
            assertEquals(45, demon.getCooldownFrames());
            assertEquals(70f, demon.getBaseHealth(), 0.01f);
            assertEquals(0, demon.getPatrolRange());
            assertEquals(1.5f, demon.getSoundSensitivity(), 0.01f);
            assertEquals("👹", demon.getVisualName());
        }

        @Test
        @DisplayName("Knight type exists with defensive characteristics")
        void knightTypeExists() {
            EnemyType knight = EnemyType.KNIGHT;
            assertEquals(1.5f, knight.getBaseMoveSpeed(), 0.01f);
            assertEquals(30f, knight.getBaseDamage(), 0.01f);
            assertEquals(80, knight.getCooldownFrames());
            assertEquals(180f, knight.getBaseHealth(), 0.01f);
            assertEquals(80, knight.getPatrolRange());
            assertEquals(0.5f, knight.getSoundSensitivity(), 0.01f);
            assertEquals("⚔️", knight.getVisualName());
        }

        @Test
        @DisplayName("Imp type exists with fast fragile characteristics")
        void impTypeExists() {
            EnemyType imp = EnemyType.IMP;
            assertEquals(2.5f, imp.getBaseMoveSpeed(), 0.01f);
            assertEquals(20f, imp.getBaseDamage(), 0.01f);
            assertEquals(40, imp.getCooldownFrames());
            assertEquals(60f, imp.getBaseHealth(), 0.01f);
            assertEquals(0, imp.getPatrolRange());
            assertEquals(0.5f, imp.getSoundSensitivity(), 0.01f);
            assertEquals("👺", imp.getVisualName());
        }

        @Test
        @DisplayName("Baron type exists with boss characteristics")
        void baronTypeExists() {
            EnemyType baron = EnemyType.BARON;
            assertEquals(2.2f, baron.getBaseMoveSpeed(), 0.01f);
            assertEquals(100f, baron.getBaseDamage(), 0.01f);
            assertEquals(90, baron.getCooldownFrames());
            assertEquals(500f, baron.getBaseHealth(), 0.01f);
            assertEquals(150, baron.getPatrolRange());
            assertEquals(0f, baron.getSoundSensitivity(), 0.01f);
            assertEquals("👺👑", baron.getVisualName());
        }
    }

    @Nested
    @DisplayName("Base Value Getters")
    class BaseValueTests {

        @Test
        @DisplayName("All types return correct base values")
        void baseValuesCorrect() {
            // Zombie: 2f speed, 25 damage, 60 cooldown, 100 health
            assertEquals(2f, EnemyType.ZOMBIE.getBaseMoveSpeed(), 0.01f);
            assertEquals(25f, EnemyType.ZOMBIE.getBaseDamage(), 0.01f);
            assertEquals(60, EnemyType.ZOMBIE.getCooldownFrames());
            assertEquals(100f, EnemyType.ZOMBIE.getBaseHealth(), 0.01f);

            // Demon: 3.5x speed, 1.6x damage, 75% cooldown, 70% health
            assertEquals(3.5f, EnemyType.DEMON.getBaseMoveSpeed(), 0.01f); // 3.5 * 1
            assertEquals(40f, EnemyType.DEMON.getBaseDamage(), 0.01f); // 25 * 1.6
            assertEquals(45, EnemyType.DEMON.getCooldownFrames()); // 60 * 0.75
            assertEquals(70f, EnemyType.DEMON.getBaseHealth(), 0.01f); // 100 * 0.7

            // Baron: 1.1x speed, 4x damage, 1.5x cooldown, 5x health
            assertEquals(2.2f, EnemyType.BARON.getBaseMoveSpeed(), 0.01f);
            assertEquals(100f, EnemyType.BARON.getBaseDamage(), 0.01f);
            assertEquals(90, EnemyType.BARON.getCooldownFrames()); // 60 * 1.5
            assertEquals(500f, EnemyType.BARON.getBaseHealth(), 0.01f);
        }
    }

    @Nested
    @DisplayName("Patrol Behavior")
    class PatrolTests {

        @Test
        @DisplayName("Zombie has patrol enabled")
        void zombieHasPatrol() {
            assertEquals(100, EnemyType.ZOMBIE.getPatrolRange());
            assertTrue(EnemyType.ZOMBIE.getPatrolRange() > 0);
        }

        @Test
        @DisplayName("Demon has no patrol (aggressive)")
        void demonHasNoPatrol() {
            assertEquals(0, EnemyType.DEMON.getPatrolRange());
        }

        @Test
        @DisplayName("Imp has no patrol (aggressive)")
        void impHasNoPatrol() {
            assertEquals(0, EnemyType.IMP.getPatrolRange());
        }

        @Test
        @DisplayName("Baron has long patrol range")
        void baronHasLongPatrol() {
            assertEquals(150, EnemyType.BARON.getPatrolRange());
        }
    }

    @Nested
    @DisplayName("Sound Sensitivity")
    class SoundSensitivityTests {

        @Test
        @DisplayName("Demon has high sound sensitivity")
        void demonHasHighSensitivity() {
            assertEquals(1.5f, EnemyType.DEMON.getSoundSensitivity(), 0.01f);
            assertTrue(EnemyType.DEMON.getSoundSensitivity() > 1f);
        }

        @Test
        @DisplayName("Zombie has low sound sensitivity (immune)")
        void zombieHasLowSensitivity() {
            assertEquals(0f, EnemyType.ZOMBIE.getSoundSensitivity(), 0.01f);
            assertTrue(EnemyType.ZOMBIE.getSoundSensitivity() <= 0.01f);
        }

        @Test
        @DisplayName("Knight has medium sound sensitivity")
        void knightHasMediumSensitivity() {
            assertEquals(0.5f, EnemyType.KNIGHT.getSoundSensitivity(), 0.01f);
            assertTrue(EnemyType.KNIGHT.getSoundSensitivity() >= 0f);
            assertTrue(EnemyType.KNIGHT.getSoundSensitivity() <= 1f);
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Zombie has correct description")
        void zombieDescription() {
            assertEquals("Basic zombie, slow but persistent hunter", EnemyType.ZOMBIE.getDescription());
        }

        @Test
        @DisplayName("Demon has correct description")
        void demonDescription() {
            assertEquals("Fast demon with high damage output", EnemyType.DEMON.getDescription());
        }

        @Test
        @DisplayName("Knight has correct description")
        void knightDescription() {
            assertEquals("Tanky knight with defensive abilities", EnemyType.KNIGHT.getDescription());
        }

        @Test
        @DisplayName("Imp has correct description")
        void impDescription() {
            assertEquals("Fast, fragile imps that attack quickly", EnemyType.IMP.getDescription());
        }

        @Test
        @DisplayName("Baron has correct description")
        void baronDescription() {
            assertEquals("Boss-type baron with overwhelming power", EnemyType.BARON.getDescription());
        }
    }

    @Nested
    @DisplayName("Visual Name Tests")
    class VisualTests {

        @Test
        @DisplayName("Zombie has zombie emoji")
        void zombieVisual() {
            assertEquals("🧟", EnemyType.ZOMBIE.getVisualName());
        }

        @Test
        @DisplayName("Demon has demon emoji")
        void demonVisual() {
            assertEquals("👹", EnemyType.DEMON.getVisualName());
        }

        @Test
        @DisplayName("Knight has sword emoji")
        void knightVisual() {
            assertEquals("⚔️", EnemyType.KNIGHT.getVisualName());
        }

        @Test
        @DisplayName("Imp has imp emoji")
        void impVisual() {
            assertEquals("👺", EnemyType.IMP.getVisualName());
        }

        @Test
        @DisplayName("Baron has baron emoji with crown")
        void baronVisual() {
            assertEquals("👺👑", EnemyType.BARON.getVisualName());
        }
    }

    @Nested
    @DisplayName("Getters")
    class GetterTests {

        @Test
        @DisplayName("All getter methods exist and work correctly")
        void allGettersWork() {
            EnemyType testType = EnemyType.KNIGHT;

            // Multiplier getters
            assertNotNull(testType.getBaseMoveSpeed());
            assertNotNull(testType.getBaseDamage());
            assertNotNull(testType.getHealthMultiplier());

            // Frame getters
            assertNotNull(testType.getCooldownFrames());

            // Patrol getter
            assertNotNull(testType.getPatrolRange());

            // Sensitivity getter
            assertNotNull(testType.getSoundSensitivity());

            // Size getter
            assertNotNull(testType.getSizeMultiplier());

            // Base value getters
            assertNotNull(testType.getBaseMoveSpeed());
            assertNotNull(testType.getBaseDamage());
            assertNotNull(testType.getCooldownFrames());
            assertNotNull(testType.getBaseHealth());

            // Description getter
            assertNotNull(testType.getDescription());

            // Visual name getter
            assertNotNull(testType.getVisualName());
        }
    }

    @Nested
    @DisplayName("Complete Enemy Types List")
    class CompleteTypes {

        @Test
        @DisplayName("All 5 enemy types are defined")
        void allFiveTypes() {
            assertEquals(5, EnemyType.values().length);

            // Check each type name
            assertEquals(EnemyType.ZOMBIE.name(), "ZOMBIE");
            assertEquals(EnemyType.DEMON.name(), "DEMON");
            assertEquals(EnemyType.KNIGHT.name(), "KNIGHT");
            assertEquals(EnemyType.IMP.name(), "IMP");
            assertEquals(EnemyType.BARON.name(), "BARON");
        }
    }
}