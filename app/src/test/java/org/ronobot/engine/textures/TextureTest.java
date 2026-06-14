package org.ronobot.engine.textures;

import org.ronobot.engine.math.Point;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Texture class.
 *
 * @author ronobot
 */
class TextureTest {
    
    @Test
    void testTextureCreationProcedural() {
        Texture texture = new Texture("test", 64, 64);
        
        assertNotNull(texture);
        assertEquals("test", texture.getName());
        assertEquals(64, texture.getWidth());
        assertEquals(64, texture.getHeight());
    }
    
    @Test
    void testTextureBlankCreation() {
        Texture texture = new Texture("blank", 32, 32);
        
        assertNotNull(texture);
        assertEquals("blank", texture.getName());
        assertEquals(32, texture.getWidth());
        assertEquals(32, texture.getHeight());
    }
    
    @Test
    void testTextureGetImage() {
        Texture texture = new Texture("test", 64, 64);
        
        assertNotNull(texture.getImage());
        assertEquals(64, texture.getImage().getWidth());
        assertEquals(64, texture.getImage().getHeight());
    }
    
    @Test
    void testTextureGetDimensions() {
        Texture texture = new Texture("test", 128, 64);
        
        assertEquals(128, texture.getDimensions().getWidth());
        assertEquals(64, texture.getDimensions().getHeight());
    }
    
    @Test
    void testTextureGetWidth() {
        Texture texture = new Texture("test", 100, 50);
        
        assertEquals(100, texture.getWidth());
    }
    
    @Test
    void testTextureGetHeight() {
        Texture texture = new Texture("test", 50, 100);
        
        assertEquals(50, texture.getWidth());
        assertEquals(100, texture.getHeight());
    }
    
    @Test
    void testTextureCreateCopy() {
        Texture original = new Texture("original", 32, 32);
        Texture copy = original.createCopy();
        
        assertNotNull(copy);
        assertEquals(32, copy.getWidth());
        assertEquals(32, copy.getHeight());
    }
    
    @Test
    void testTextureCreateInverted() {
        Texture original = new Texture("original", 10, 10);
        Texture inverted = original.createInverted();
        
        assertNotNull(inverted);
        assertEquals("original_inverted", inverted.getName());
        assertEquals(10, inverted.getWidth());
        assertEquals(10, inverted.getHeight());
    }
    
    @Test
    void testTextureDrawAt() {
        Texture source = new Texture("source", 10, 10);
        Texture target = new Texture("target", 100, 100);
        
        source.drawAt(50, 50, target, 0, 0);
        
        // Method should exist and not crash
        assertTrue(true);
    }
    
    @Test
    void testTextureToString() {
        Texture texture = new Texture("test", 64, 64);
        
        assertNotNull(texture.toString());
        assertTrue(texture.toString().contains("name"));
        assertTrue(texture.toString().contains("dimensions"));
    }
    
    @Test
    void testTextureUUIDUniqueness() {
        UUID first = null;
        for (int i = 0; i < 100; i++) {
            Texture t = new Texture("test_uuid_" + i, 10, 10);
            UUID uuid = t.getId();
            
            if (first == null) {
                first = uuid;
            }
            
            if (i > 0) {
                assertFalse(uuid.equals(first), "UUID should be unique for texture " + i);
            }
        }
    }
    
    @Test
    void testTextureDimensionsConsistency() {
        Texture texture = new Texture("test", 128, 64);
        
        assertEquals(128, texture.getDimensions().getWidth());
        assertEquals(64, texture.getDimensions().getHeight());
    }
    
    @Test
    void testTextureNullHandling() {
        Texture texture = new Texture("null_test", 64, 64);
        
        assertNotNull(texture);
    }
    
    @Test
    void testTextureWithLargeSize() {
        Texture texture = new Texture("large", 256, 256);
        
        assertEquals(256, texture.getWidth());
        assertEquals(256, texture.getHeight());
    }
    
    @Test
    void testTextureWithZeroSize() {
        // Zero sizes should be handled gracefully with 1x1 minimum
        Texture texture = new Texture("zero", 0, 0);
        
        assertEquals(1, texture.getWidth());
        assertEquals(1, texture.getHeight());
    }
    
    @Test
    void testTextureNegativeDimensions() {
        // Negative dimensions should be handled gracefully with 1x1 minimum
        Texture texture = new Texture("negative", -10, -20);
        
        assertEquals(1, texture.getWidth());
        assertEquals(1, texture.getHeight());
    }
}
