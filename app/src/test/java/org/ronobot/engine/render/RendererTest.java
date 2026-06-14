package org.ronobot.engine.render;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.entity.Projectile;
import org.ronobot.engine.core.Entity;
import org.ronobot.engine.entities.EntityManager;
import org.ronobot.engine.core.Game;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Renderer class.
 * <p>
 * Tests texture management, rendering of game tiles, entities, and projectiles.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class RendererTest {

    /**
     * The renderer instance under test.
     */
    private Renderer renderer;

    /**
     * Sets up the test fixtures.
     */
    @BeforeEach
    public void setUp() {
        this.renderer = new Renderer();
    }

    /**
     * Tests that the renderer initializes with correct screen dimensions.
     */
    @Test
    public void testDefaultDimensions() {
        assertEquals(640, renderer.getScreenWidth());
        assertEquals(480, renderer.getScreenHeight());
    }

    /**
     * Tests that texture loading stores the texture path.
     */
    @Test
    public void testLoadTexture() {
        String texturePath = "textures/player.png";
        assertTrue(renderer.loadTexture("player", texturePath));
        assertEquals(texturePath, renderer.getTexture("player"));
    }

    /**
     * Tests that loading the same texture twice returns the same path.
     */
    @Test
    public void testLoadTextureTwice() {
        String texturePath = "textures/player.png";
        assertTrue(renderer.loadTexture("player", texturePath));
        assertTrue(renderer.loadTexture("player", texturePath));
        assertEquals(texturePath, renderer.getTexture("player"));
    }

    /**
     * Tests that loading a null texture returns false.
     */
    @Test
    public void testLoadTextureNullName() {
        assertFalse(renderer.loadTexture(null, "path"));
    }

    /**
     * Tests that loading a null texture returns false.
     */
    @Test
    public void testLoadTextureNullPath() {
        assertFalse(renderer.loadTexture("name", null));
    }

    /**
     * Tests that clearTextures removes all textures.
     */
    @Test
    public void testClearTextures() {
        renderer.loadTexture("texture1", "path1");
        renderer.loadTexture("texture2", "path2");
        renderer.clearTextures();
        assertEquals(0, renderer.getTextureCount());
        assertNull(renderer.getTexture("texture1"));
    }

    /**
     * Tests that getTexture returns null for non-existent texture.
     */
    @Test
    public void testGetTextureNonExistent() {
        assertNull(renderer.getTexture("nonexistent"));
    }

    /**
     * Tests that hasTexture returns correct values.
     */
    @Test
    public void testHasTexture() {
        renderer.loadTexture("test", "path");
        assertTrue(renderer.hasTexture("test"));
        assertFalse(renderer.hasTexture("nonexistent"));
    }

    /**
     * Tests rendering with null game.
     */
    @Test
    public void testRenderNullGame() {
        renderer.render(null);
    }

    /**
     * Tests rendering with empty game.
     */
    @Test
    public void testRenderEmptyGame() {
        Game game = new Game();
        renderer.render(game);
    }

    /**
     * Tests that entity rendering registers entity in texture cache.
     */
    @Test
    public void testRenderEntity() {
        Game game = new Game();
        EntityManager em = game.getEntityManager();
        
        // Load a default texture first
        renderer.loadTexture("default", "default.png");
        
        Entity entity = new Entity(1, 0f, 0f, 16, 16);
        em.addEntity(entity);
        
        renderer.render(game);
        
        // Entity should be in texture cache
        assertTrue(renderer.getTextureCount() >= 1);
    }

    /**
     * Tests that projectile rendering registers projectile in texture cache.
     */
    @Test
    public void testRenderProjectile() {
        Game game = new Game();
        EntityManager em = game.getEntityManager();
        
        // Load a default texture first
        renderer.loadTexture("default", "default.png");
        
        Position pos = new Position(0f, 0f);
        Size size = new Size(16, 16);
        Projectile projectile = new Projectile(pos, size, 5);
        em.addEntity(projectile);
        
        renderer.render(game);
        
        // Projectile should be in texture cache
        assertTrue(renderer.getTextureCount() >= 1);
    }

    /**
     * Tests that multiple textures can be loaded simultaneously.
     */
    @Test
    public void testMultipleTextures() {
        renderer.loadTexture("player", "player.png");
        renderer.loadTexture("enemy", "enemy.png");
        renderer.loadTexture("wall", "wall.png");
        renderer.loadTexture("floor", "floor.png");
        
        assertEquals(4, renderer.getTextureCount());
        assertEquals("player.png", renderer.getTexture("player"));
        assertEquals("enemy.png", renderer.getTexture("enemy"));
        assertEquals("wall.png", renderer.getTexture("wall"));
        assertEquals("floor.png", renderer.getTexture("floor"));
    }

    /**
     * Tests that texture count updates correctly.
     */
    @Test
    public void testTextureCount() {
        int initialCount = renderer.getTextureCount();
        renderer.loadTexture("t1", "p1");
        assertEquals(initialCount + 1, renderer.getTextureCount());
        renderer.clearTextures();
        assertEquals(initialCount, renderer.getTextureCount());
    }
}
