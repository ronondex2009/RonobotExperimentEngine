package org.ronobot.engine.textures;

import org.ronobot.engine.math.Position;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Sprite class.
 *
 * @author ronobot
 */
class SpriteTest {
    
    @Test
    void testSpriteCreation() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(32, 32));
        
        assertNotNull(sprite);
        assertEquals("player", sprite.getName());
        assertEquals(64, sprite.getWidth());
        assertEquals(64, sprite.getHeight());
        assertEquals(new Position(32, 32), sprite.getPosition());
        assertEquals(new Position(0, 0), sprite.getVelocity());
    }
    
    @Test
    void testSpriteCreationWithAnimation() {
        Texture texture = new Texture("animation", 128, 64, 4);
        Sprite sprite = new Sprite("anim_sprite", texture, new Position(32, 32), 4);
        
        assertNotNull(sprite);
        assertEquals("anim_sprite", sprite.getName());
        assertEquals(32, sprite.getFrameWidth()); // 128/4 = 32
        assertEquals(16, sprite.getFrameHeight()); // 64/4 = 16
        assertEquals(4, sprite.getTotalFrames());
        assertEquals(0, sprite.getFrameIndex());
    }
    
    @Test
    void testSpritePosition() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setPosition(100, 150);
        
        assertEquals(new Position(100, 150), sprite.getPosition());
    }
    
    @Test
    void testSpriteVelocity() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setVelocity(2.5f, -1.0f);
        
        assertEquals(2.5f, sprite.getVelocity().x(), 0.001f);
        assertEquals(-1.0f, sprite.getVelocity().y(), 0.001f);
    }
    
    @Test
    void testSpriteSize() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setSize(48, 48);
        
        assertEquals(48, sprite.getWidth());
        assertEquals(48, sprite.getHeight());
    }
    
    @Test
    void testSpriteFrameIndex() {
        Texture texture = new Texture("animated", 128, 64, 4);
        Sprite sprite = new Sprite("anim", texture, new Position(10, 20), 4);
        
        assertEquals(0, sprite.getFrameIndex());
        
        sprite.nextFrame();
        assertEquals(1, sprite.getFrameIndex());
        
        sprite.nextFrame();
        assertEquals(2, sprite.getFrameIndex());
        
        sprite.nextFrame();
        assertEquals(3, sprite.getFrameIndex());
        
        sprite.nextFrame();
        assertEquals(0, sprite.getFrameIndex()); // Wrapped back to start
    }
    
    @Test
    void testSpriteFrameIndexWrapping() {
        Texture texture = new Texture("animated", 128, 64, 10);
        Sprite sprite = new Sprite("anim", texture, new Position(10, 20), 10);
        
        for (int i = 0; i < 100; i++) {
            sprite.nextFrame();
            assertTrue(sprite.getFrameIndex() >= 0 && sprite.getFrameIndex() < 10);
        }
    }
    
    @Test
    void testSpriteFlipHorizontal() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setFlipHorizontal(true);
        
        assertTrue(sprite.flipHorizontal);
    }
    
    @Test
    void testSpriteFlipVertical() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setFlipVertical(true);
        
        assertTrue(sprite.flipVertical);
    }
    
    @Test
    void testSpriteRotation() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setRotationDegrees(90);
        
        assertEquals(90, sprite.getRotationDegrees());
        assertEquals(Math.PI / 2.0f, sprite.getRotationRadians(), 0.001f);
        
        sprite.setRotationDegrees(180);
        assertEquals(180, sprite.getRotationDegrees());
    }
    
    @Test
    void testSpriteRotationWrapping() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setRotationDegrees(450);
        
        // 450 % 360 = 90
        assertEquals(90, sprite.getRotationDegrees());
    }
    
    @Test
    void testSpriteReset() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setPosition(100, 200);
        sprite.setVelocity(5.0f, -3.0f);
        sprite.setFrameIndex(5);
        sprite.setFlipHorizontal(true);
        sprite.setFlipVertical(true);
        sprite.setRotationDegrees(180);
        
        sprite.reset();
        
        assertEquals(new Position(0, 0), sprite.getPosition());
        assertEquals(new Position(0, 0), sprite.getVelocity());
        assertEquals(0, sprite.getFrameIndex());
        assertFalse(sprite.flipHorizontal);
        assertFalse(sprite.flipVertical);
        assertEquals(0, sprite.getRotationDegrees());
    }
    
    @Test
    void testSpriteToString() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        assertNotNull(sprite.toString());
        assertTrue(sprite.toString().contains("name"));
        assertTrue(sprite.toString().contains("position"));
    }
    
    @Test
    void testSpriteDimensionsConsistency() {
        Texture texture = new Texture("test", 128, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        assertEquals(128, sprite.getWidth());
        assertEquals(64, sprite.getHeight());
    }
    
    @Test
    void testSpriteCopyFromTexture() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        Texture copyTexture = texture.createCopy();
        Sprite sprite2 = new Sprite("player_copy", copyTexture, new Position(50, 50));
        
        assertNotNull(sprite2);
        assertEquals(64, sprite2.getWidth());
        assertEquals(64, sprite2.getHeight());
    }
    
    @Test
    void testSpriteNullTexture() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        assertNotNull(sprite);
        assertNotNull(sprite.getTexture());
    }
    
    @Test
    void testSpriteWithDifferentTextureSizes() {
        Texture texture = new Texture("test", 256, 128);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        assertEquals(256, sprite.getWidth());
        assertEquals(128, sprite.getHeight());
        
        sprite.setSize(64, 64);
        assertEquals(64, sprite.getWidth());
        assertEquals(64, sprite.getHeight());
    }
    
    @Test
    void testSpriteFrameWidthCalculation() {
        Texture texture = new Texture("animated", 128, 64, 4);
        Sprite sprite = new Sprite("anim", texture, new Position(10, 20), 4);
        
        assertEquals(32, sprite.getFrameWidth());
        assertEquals(16, sprite.getFrameHeight());
    }
    
    @Test
    void testSpriteFrameHeightCalculation() {
        Texture texture = new Texture("animated", 128, 128, 4);
        Sprite sprite = new Sprite("anim", texture, new Position(10, 20), 4);
        
        assertEquals(32, sprite.getFrameWidth());
        assertEquals(32, sprite.getFrameHeight()); // 128/4 = 32
    }
    
    @Test
    void testSpriteNegativeVelocity() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setVelocity(-3.0f, -2.0f);
        
        assertEquals(-3.0f, sprite.getVelocity().x(), 0.001f);
        assertEquals(-2.0f, sprite.getVelocity().y(), 0.001f);
    }
    
    @Test
    void testSpriteZeroVelocity() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setVelocity(0.0f, 0.0f);
        
        assertEquals(0.0f, sprite.getVelocity().x(), 0.001f);
        assertEquals(0.0f, sprite.getVelocity().y(), 0.001f);
    }
    
    @Test
    void testSpriteLargeRotation() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setRotationDegrees(720);
        
        assertEquals(0, sprite.getRotationDegrees());
    }
    
    @Test
    void testSpriteNegativeRotation() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setRotationDegrees(-45);
        
        assertEquals(-45, sprite.getRotationDegrees());
    }
    
    @Test
    void testSpriteFloatRotation() {
        Texture texture = new Texture("test", 64, 64);
        Sprite sprite = new Sprite("player", texture, new Position(10, 20));
        
        sprite.setRotationDegrees(45.5f);
        
        assertEquals(45, sprite.getRotationDegrees()); // Casts to int
    }
}
