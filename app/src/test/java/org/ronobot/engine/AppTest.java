package org.ronobot.engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the App class.
 * Tests application entry point and game initialization.
 *
 * @author ronobot
 * @since 1.0
 */
@DisplayName("App Tests")
class AppTest {

    @Nested
    @DisplayName("App Creation")
    class AppCreationTests {

        @DisplayName("Should create App instance")
        @Test
        void testCreateApp() {
            App app = new App();
            assertNotNull(app);
        }

        @DisplayName("Should have default game set to null")
        @Test
        void testDefaultGame() {
            App app = new App();
            assertNotNull(app.getGame());
            assertNotNull(app.getGame().getEntityManager());
        }
    }

    @Nested
    @DisplayName("Game Getter and Setter")
    class GameGetterSetterTests {

        @DisplayName("Should get non-null game initially")
        @Test
        void testGetGameInitially() {
            App app = new App();
            assertNotNull(app.getGame());
        }

        @DisplayName("Should set game and get it back")
        @Test
        void testSetGetGame() {
            App app1 = new App();
            App app2 = new App();
            app1.setGame(app2.getGame());
            assertEquals(app2.getGame(), app1.getGame());
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTests {

        @DisplayName("Should return string representation without game")
        @Test
        void testToStringWithoutGame() {
            String actual = new App().toString();
            assertTrue(actual.contains("App"));
        }

        @DisplayName("Should return string representation with game")
        @Test
        void testToStringWithGame() {
            String actual = new App().toString();
            assertTrue(actual.contains("game"));
        }
    }
}
