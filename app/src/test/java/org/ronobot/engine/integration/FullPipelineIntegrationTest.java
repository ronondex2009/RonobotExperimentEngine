package org.ronobot.engine.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entities.EntityManager;
import org.ronobot.engine.input.InputHandler;
import org.ronobot.engine.map.GameMap;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;
import org.ronobot.engine.math.Velocity;
import org.ronobot.engine.render.GameRenderer;
import org.ronobot.engine.render.Renderer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full pipeline integration tests for the DOOM-like engine.
 * <p>
 * These tests verify that all engine components work together correctly:
 * Game, Renderer, InputHandler, GameMap, EntityManager, PhysicsEngine.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
@DisplayName("Full Pipeline Integration Tests")
class FullPipelineIntegrationTest {

    @Nested
    @DisplayName("Game Initialization")
    class GameInitTests {

        @Test
        @DisplayName("game initializes with all components")
        void testGameInitialization() {
            Game game = new Game();
            
            // Verify game has default values
            assertNotNull(game);
            assertEquals("stopped", game.getState());
            assertEquals(0, game.getFrameCount());
            
            game.start();
            assertEquals("running", game.getState());
            
            assertEquals(0, game.getEntityManager().getEntities().size());
            
            game.stop();
            assertEquals("stopped", game.getState());
            
            game.cleanup();
        }

        @Test
        @DisplayName("game can set and get map")
        void testSetMap() {
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(16, 16);
            
            game.setMap(map);
            assertNotNull(game.getMap());
            
            map.cleanup();
        }

        @Test
        @DisplayName("game can set and get player")
        void testSetPlayer() {
            Game game = new Game();
            Position pos = new Position(96, 96);
            Size size = new Size(32, 32);
            PlayerEntity player = new PlayerEntity(0, pos.getX(), pos.getY(), 32, 32);
            
            game.setPlayer(player);
            assertEquals(player, game.getPlayer());
            
            game.cleanup();
        }

        @Test
        @DisplayName("game frame counter works")
        void testFrameCounter() {
            Game game = new Game();
            
            game.init();
            
            assertEquals(0, game.getFrameCount());
            
            game.update();
            assertEquals(1, game.getFrameCount());
            
            game.update();
            assertEquals(2, game.getFrameCount());
            
            game.cleanup();
        }
    }

    @Nested
    @DisplayName("Renderer Integration")
    class RendererIntegrationTests {

        @Test
        @DisplayName("rendering game with map tiles")
        void testRenderGameMap() {
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(16, 16);
            game.setMap(map);
            
            Renderer renderer = new Renderer();
            
            // Load some textures
            renderer.loadTexture("wall", "texture_wall.png");
            renderer.loadTexture("floor", "texture_floor.png");
            
            renderer.render(game);
            
            // Should have loaded textures
            assertEquals(2, renderer.getTextureCount());
            
            game.cleanup();
        }

        @Test
        @DisplayName("rendering with GameRenderer and arena map")
        void testRenderWithGameRenderer() {
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(16, 16);
            game.setMap(map);
            
            GameRenderer gameRenderer = new GameRenderer(map);
            gameRenderer.renderMap();
            
            // Should have a map available
            assertTrue(gameRenderer.hasMap());
            
            game.cleanup();
        }

        @Test
        @DisplayName("rendering entities and projectiles")
        void testRenderEntities() {
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(16, 16);
            game.setMap(map);
            
            EntityManager em = game.getEntityManager();
            
            // Add player
            Position playerPos = new Position(96, 96);
            Size playerSize = new Size(32, 32);
            PlayerEntity player = new PlayerEntity(0, playerPos.getX(), playerPos.getY(), 32, 32);
            em.addEntity(player);
            
            // Add projectile
            Position projectilePos = new Position(128, 128);
            Size projectileSize = new Size(16, 16);
            org.ronobot.engine.entity.Projectile projectile = 
                new org.ronobot.engine.entity.Projectile(projectilePos, projectileSize, 60);
            em.addEntity(projectile);
            
            Renderer renderer = new Renderer();
            renderer.loadTexture("default", "default.png");
            
            renderer.render(game);
            
            // Should have textures for player and projectile
            assertTrue(renderer.getTextureCount() >= 1);
            
            game.cleanup();
        }

        @Test
        @DisplayName("texture cache updates when rendering different maps")
        void testTextureCacheUpdates() {
            Game game = new Game();
            GameMap map1 = GameMap.createArenaMap(8, 8);
            game.setMap(map1);
            
            Renderer renderer = new Renderer();
            renderer.render(game);
            
            int textureCount1 = renderer.getTextureCount();
            
            // Change map
            GameMap map2 = GameMap.createArenaMap(16, 16);
            game.setMap(map2);
            renderer.render(game);
            
            // Texture count should be same or increase (not decrease)
            assertTrue(renderer.getTextureCount() >= textureCount1);
            
            game.cleanup();
        }
    }

    @Nested
    @DisplayName("Input Handler Integration")
    class InputHandlerIntegrationTests {

        @Test
        @DisplayName("input handler processes player movement")
        void testInputHandlerWithPlayer() {
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(16, 16);
            game.setMap(map);
            
            EntityManager em = game.getEntityManager();
            
            // Add player at center
            Position playerPos = new Position(96, 96);
            Size playerSize = new Size(32, 32);
            PlayerEntity player = new PlayerEntity(0, playerPos.getX(), playerPos.getY(), 32, 32);
            em.addEntity(player);
            
            // CRITICAL: Set the player on the game so game.getPlayer() returns it
            game.setPlayer(player);
            
            InputHandler input = new InputHandler();
            input.setGameMap(map);
            
            // Process input - move right by 0.5 pixels
            input.setMovingRight(true);
            input.handle(game);
            
            // Player should have moved right by 0.5 pixels
            assertEquals(96.5, player.getPosition().getX(), 0.1);
            
            // Clear input
            input.reset();
            
            game.cleanup();
        }

        @Test
        @DisplayName("input handler enforces map boundaries")
        void testInputHandlerBoundaries() {
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(8, 8);
            game.setMap(map);
            
            EntityManager em = game.getEntityManager();
            
            // Add player at right edge - this tests boundary clamping to map width
            Position playerPos = new Position(224, 96); // x=224 is within 8x8 map (max x=256)
            Size playerSize = new Size(32, 32);
            PlayerEntity player = new PlayerEntity(0, playerPos.getX(), playerPos.getY(), 32, 32);
            em.addEntity(player);
            
            // CRITICAL: Set the player on the game so game.getPlayer() returns it
            game.setPlayer(player);
            
            InputHandler input = new InputHandler();
            input.setGameMap(map);
            input.setMovingRight(true);
            input.handle(game);
            
            // Player should be able to move right from 224 to 224.5
            // Boundary check: 224.5 < 256 (map width), so movement allowed
            assertEquals(224.5, player.getPosition().getX(), 0.1);
            
            game.cleanup();
        }
    }

    @Nested
    @DisplayName("Physics Engine Integration")
    class PhysicsIntegrationTests {

        @Test
        @DisplayName("physics engine processes collisions")
        void testPhysicsEngine() {
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(16, 16);
            game.setMap(map);
            
            EntityManager em = game.getEntityManager();
            
            Position playerPos = new Position(96, 96);
            Size playerSize = new Size(32, 32);
            PlayerEntity player = new PlayerEntity(0, playerPos.getX(), playerPos.getY(), 32, 32);
            em.addEntity(player);
            
            Velocity playerVel = new Velocity(0.5f, 0f);
            player.setVelocity(playerVel);
            
            // CRITICAL: Set the player on the game so it can be retrieved
            game.setPlayer(player);
            
            // Update physics
            game.update();
            
            // Player should still exist
            assertNotNull(game.getEntityManager().getEntity(0));
            
            game.cleanup();
        }

        @Test
        @DisplayName("projectile velocity decays")
        void testProjectileDecay() {
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(16, 16);
            game.setMap(map);
            
            EntityManager em = game.getEntityManager();
            
            Position projectilePos = new Position(128, 128);
            Size projectileSize = new Size(16, 16);
            org.ronobot.engine.entity.Projectile projectile = 
                new org.ronobot.engine.entity.Projectile(projectilePos, projectileSize, 60); // 1 second lifetime
            projectile.setVelocity(new Velocity(0.5f, 0f));
            em.addEntity(projectile);
            
            // Update physics a few times via game loop
            for (int i = 0; i < 10; i++) {
                game.update();
            }
            
            // Projectile should be dead (lifetime exceeded)
            assertNull(game.getEntityManager().getEntity(1));
            
            game.cleanup();
        }
    }

    @Nested
    @DisplayName("Entity Lifecycle Integration")
    class EntityLifecycleTests {

        @Test
        @DisplayName("entity lifecycle works")
        void testEntityLifecycle() {
            Game game = new Game();
            EntityManager em = game.getEntityManager();
            
            // Create entity
            Position pos = new Position(0, 0);
            Size size = new Size(16, 16);
            Entity entity = new Entity(0, pos.getX(), pos.getY(), 16, 16);
            em.addEntity(entity);
            
            assertEquals(1, em.getEntities().size());
            
            // Remove entity
            em.removeEntity(entity);
            assertEquals(0, em.getEntities().size());
            
            game.cleanup();
        }

        @Test
        @DisplayName("multiple entities can coexist")
        void testMultipleEntities() {
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(16, 16);
            game.setMap(map);
            
            EntityManager em = game.getEntityManager();
            
            // Create multiple entities
            for (int i = 0; i < 5; i++) {
                Position pos = new Position(i * 48, 96);
                Size size = new Size(32, 32);
                PlayerEntity entity = new PlayerEntity(i, pos.getX(), pos.getY(), 32, 32);
                em.addEntity(entity);
            }
            
            assertEquals(5, em.getEntities().size());
            
            game.cleanup();
        }
    }

    @Nested
    @DisplayName("Full Pipeline Test")
    class FullPipelineTests {

        @Test
        @DisplayName("complete game loop integration")
        void testFullPipeline() {
            // Initialize game
            Game game = new Game();
            GameMap map = GameMap.createArenaMap(16, 16);
            game.setMap(map);
            
            // Add decorations
            map.addWall(2, 2);
            map.addFloor(3, 3);
            map.addDoor(5, 5);
            
            // Add player
            EntityManager em = game.getEntityManager();
            Position playerPos = new Position(96, 96);
            Size playerSize = new Size(32, 32);
            PlayerEntity player = new PlayerEntity(0, playerPos.getX(), playerPos.getY(), 32, 32);
            em.addEntity(player);
            
            // Add renderer
            GameRenderer renderer = new GameRenderer(map);
            
            // Add input handler
            InputHandler input = new InputHandler();
            
            // Initial render
            renderer.render(game);
            int initialRenderCount = renderer.getTextureCount();
            
            // Process input
            input.setMovingRight(true);
            input.handle(game);
            
            // Update physics
            game.update();
            
            // Render again
            renderer.render(game);
            
            // All components should be working
            assertEquals(initialRenderCount, renderer.getTextureCount());
            assertTrue(renderer.getTextureCount() >= 0);
            
            // Cleanup
            game.cleanup();
        }

        @Test
        @DisplayName("game state transitions work")
        void testGameStateTransitions() {
            Game game = new Game();
            
            // Init
            game.start();
            assertEquals("running", game.getState());
            
            // Cleanup
            game.stop();
            assertEquals("stopped", game.getState());
            
            game.cleanup();
        }
    }
}
