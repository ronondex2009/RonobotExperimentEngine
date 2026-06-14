package org.ronobot.engine.textures;

import org.ronobot.engine.math.Size;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * TextureManager class for managing texture loading, caching, and sprite handling.
 * Handles loading textures from files, caching, and providing sprite operations.
 *
 * @author ronobot
 */
public class TextureManager {
    
    private final Map<String, Texture> textureCache;
    private final Path textureRoot;
    private final List<Consumer<Texture>> textureListeners;
    
    /**
     * Creates a new TextureManager.
     *
     * @param textureRoot root directory for texture files
     */
    public TextureManager(Path textureRoot) {
        this.textureRoot = textureRoot;
        this.textureCache = new HashMap<>();
        this.textureListeners = new ArrayList<>();
    }
    
    /**
     * Creates a TextureManager with default texture root.
     */
    public TextureManager() {
        this(Paths.get("textures"));
    }
    
    /**
     * Registers a texture procedurally with a blank image.
     *
     * @param name texture name
     * @param width width in pixels
     * @param height height in pixels
     * @return the created texture
     */
    public Texture register(String name, int width, int height) {
        Texture texture = new Texture(name, width, height);
        textureCache.put(name, texture);
        notifyListeners(texture);
        return texture;
    }
    
    /**
     * Gets a texture by name, loading from cache if available.
     *
     * @param name the texture name
     * @return the texture, or null if not found
     */
    public Texture get(String name) {
        return textureCache.get(name);
    }
    
    /**
     * Checks if a texture exists in the cache.
     *
     * @param name the texture name
     * @return true if exists
     */
    public boolean has(String name) {
        return textureCache.containsKey(name);
    }
    
    /**
     * Gets all registered texture names.
     *
     * @return list of texture names
     */
    public List<String> getAllTextureNames() {
        return new ArrayList<>(textureCache.keySet());
    }
    
    /**
     * Gets the total number of cached textures.
     *
     * @return texture count
     */
    public int getTextureCount() {
        return textureCache.size();
    }
    
    /**
     * Removes a texture from the cache.
     *
     * @param name the texture name
     * @return the removed texture, or null if not found
     */
    public Texture remove(String name) {
        return textureCache.remove(name);
    }
    
    /**
     * Clears all cached textures.
     */
    public void clear() {
        textureCache.clear();
    }
    
    /**
     * Creates a procedural texture with a gradient background.
     *
     * @param name texture name
     * @param width width
     * @param height height
     * @param r base red component
     * @param g base green component
     * @param b base blue component
     * @return created texture
     */
    public Texture createGradientTexture(String name, int width, int height, int r, int g, int b) {
        Texture texture = new Texture(name, width, height);
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        texture.image = img;
        texture.dimensions = new Size(width, height);
        textureCache.put(name, texture);
        notifyListeners(texture);
        return texture;
    }
    
    /**
     * Creates a checkerboard texture.
     *
     * @param name texture name
     * @param width width
     * @param height height
     * @param tileSize tile size
     * @return checkerboard texture
     */
    public Texture createCheckerTexture(String name, int width, int height, int tileSize) {
        Texture texture = new Texture(name, width, height);
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int tileX = x / tileSize;
                int tileY = y / tileSize;
                int color = (tileX + tileY) % 2 == 0 ? 0xFFFFFF : 0x333333;
                img.setRGB(x, y, color);
            }
        }
        texture.image = img;
        texture.dimensions = new Size(width, height);
        textureCache.put(name, texture);
        notifyListeners(texture);
        return texture;
    }
    
    /**
     * Adds a listener for texture events.
     *
     * @param listener consumer to call when textures change
     */
    public void addTextureListener(Consumer<Texture> listener) {
        if (listener != null) {
            textureListeners.add(listener);
        }
    }
    
    /**
     * Removes a texture listener.
     *
     * @param listener to remove
     */
    public void removeTextureListener(Consumer<Texture> listener) {
        textureListeners.remove(listener);
    }
    
    /**
     * Notifies all listeners about a texture change.
     *
     * @param texture the changed texture
     */
    private void notifyListeners(Texture texture) {
        textureListeners.forEach(l -> l.accept(texture));
    }
    
    /**
     * Gets the texture root path.
     *
     * @return the root path
     */
    public Path getRoot() {
        return textureRoot;
    }
    
    @Override
    public String toString() {
        return "TextureManager{" +
                "textureCount=" + textureCache.size() +
                '}';
    }
}
