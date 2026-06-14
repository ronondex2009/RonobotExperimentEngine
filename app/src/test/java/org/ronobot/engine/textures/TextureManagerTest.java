package org.ronobot.engine.textures;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for TextureManager class.
 *
 * @author ronobot
 */
class TextureManagerTest {
    
    private static final String TEST_TEXTURE = "/tmp/test_texture.png";
    
    @Test
    void testManagerCreation() {
        TextureManager manager = new TextureManager();
        
        assertNotNull(manager);
        assertEquals(0, manager.getTextureCount());
    }
    
    @Test
    void testManagerWithCustomRoot() {
        TextureManager manager = new TextureManager(Path.of("custom_textures"));
        
        assertNotNull(manager.getRoot());
        assertTrue(manager.getRoot().toString().contains("custom_textures"));
    }
    
    @Test
    void testRegisterTextureProcedural() {
        TextureManager manager = new TextureManager();
        Texture texture = manager.register("test", 64, 64);
        
        assertNotNull(texture);
        assertEquals("test", texture.getName());
        assertTrue(manager.has("test"));
        assertEquals(1, manager.getTextureCount());
    }
    
    @Test
    void testGetTexture() {
        TextureManager manager = new TextureManager();
        manager.register("test", 64, 64);
        
        Texture texture = manager.get("test");
        
        assertNotNull(texture);
        assertEquals("test", texture.getName());
    }
    
    @Test
    void testGetNonexistentTexture() {
        TextureManager manager = new TextureManager();
        
        Texture texture = manager.get("nonexistent");
        
        assertNull(texture);
    }
    
    @Test
    void testHasTexture() {
        TextureManager manager = new TextureManager();
        manager.register("test", 64, 64);
        
        assertTrue(manager.has("test"));
        assertFalse(manager.has("nonexistent"));
    }
    
    @Test
    void testGetAllTextureNames() {
        TextureManager manager = new TextureManager();
        manager.register("red", 32, 32);
        manager.register("blue", 64, 64);
        manager.register("green", 128, 128);
        
        List<String> names = manager.getAllTextureNames();
        
        assertEquals(3, names.size());
        assertTrue(names.contains("red"));
        assertTrue(names.contains("blue"));
        assertTrue(names.contains("green"));
    }
    
    @Test
    void testRemoveTexture() {
        TextureManager manager = new TextureManager();
        manager.register("test", 64, 64);
        
        Texture removed = manager.remove("test");
        
        assertNotNull(removed);
        assertEquals(0, manager.getTextureCount());
        assertFalse(manager.has("test"));
    }
    
    @Test
    void testClearTextures() {
        TextureManager manager = new TextureManager();
        manager.register("test1", 32, 32);
        manager.register("test2", 64, 64);
        
        manager.clear();
        
        assertEquals(0, manager.getTextureCount());
        assertFalse(manager.has("test1"));
        assertFalse(manager.has("test2"));
    }
    
    @Test
    void testCreateGradientTexture() {
        TextureManager manager = new TextureManager();
        Texture texture = manager.createGradientTexture("gradient", 64, 64, 128, 128, 128);
        
        assertNotNull(texture);
        assertEquals("gradient", texture.getName());
        assertEquals(64, texture.getWidth());
        assertEquals(64, texture.getHeight());
    }
    
    @Test
    void testCreateCheckerTexture() {
        TextureManager manager = new TextureManager();
        Texture texture = manager.createCheckerTexture("checker", 128, 128, 16);
        
        assertNotNull(texture);
        assertEquals("checker", texture.getName());
        assertEquals(128, texture.getWidth());
        assertEquals(128, texture.getHeight());
    }
    
    @Test
    void testManagerToString() {
        TextureManager manager = new TextureManager();
        
        assertNotNull(manager.toString());
        assertTrue(manager.toString().contains("textureCount"));
    }
    
    @Test
    void testManagerTextureCount() {
        TextureManager manager = new TextureManager();
        
        manager.register("t1", 32, 32);
        assertEquals(1, manager.getTextureCount());
        
        manager.register("t2", 32, 32);
        assertEquals(2, manager.getTextureCount());
        
        manager.register("t3", 32, 32);
        assertEquals(3, manager.getTextureCount());
    }
    
    @Test
    void testClearAndReRegister() {
        TextureManager manager = new TextureManager();
        manager.register("t1", 32, 32);
        
        assertEquals(1, manager.getTextureCount());
        
        manager.clear();
        assertEquals(0, manager.getTextureCount());
        
        manager.register("t2", 32, 32);
        assertEquals(1, manager.getTextureCount());
    }
    
    private static BufferedImage createTestImage(int width, int height) {
        try {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    img.setRGB(x, y, (x * 3 + y * 2) % 256);
                }
            }
            
            return img;
        } catch (Exception e) {
            fail("Failed to create test image: " + e.getMessage());
            return null;
        }
    }
}
