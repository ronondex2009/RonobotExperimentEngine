package org.ronobot.engine.textures;

import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Texture class for managing sprite textures and graphics.
 * Handles loading and caching of image textures for game sprites.
 *
 * @author ronobot
 */
public class Texture {
    
    private final String name;
    private final UUID id;
    public BufferedImage image;
    private final String sourcePath;
    public Size dimensions;
    private final Map<Integer, Integer> palette;
    private boolean isTransparent;
    
    /**
     * Creates a new Texture from an image file.
     *
     * @param name unique name for the texture
     * @param sourcePath path to the image file
     */
    public Texture(String name, String sourcePath) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.sourcePath = sourcePath;
        this.palette = new HashMap<>();
        this.isTransparent = true;
        
        try {
            BufferedImage loadedImage = ImageIO.read(Path.of(sourcePath).toFile());
            if (loadedImage != null) {
                this.image = loadedImage;
                this.dimensions = new Size(loadedImage.getWidth(null), loadedImage.getHeight(null));
            } else {
                this.image = null;
                this.dimensions = Size.unit();
            }
        } catch (IOException e) {
            this.image = null;
            this.dimensions = Size.unit();
        }
    }
    
    /**
     * Creates a blank texture with specified dimensions.
     *
     * @param name unique name for the texture
     * @param width width in pixels
     * @param height height in pixels
     */
    public Texture(String name, int width, int height) {
        this(name, width, height, 1);
    }
    
    /**
     * Creates a blank texture with specified dimensions.
     *
     * @param name unique name for the texture
     * @param width width in pixels
     * @param height height in pixels
     * @param frames number of animation frames (1 = static)
     */
    public Texture(String name, int width, int height, int frames) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.sourcePath = null;
        this.palette = new HashMap<>();
        this.isTransparent = true;
        
        // Store original width/height, compute frame dimensions later
        // For blank textures, we want the actual image dimensions, not multiplied by frames
        int actualWidth = Math.max(1, width);
        int actualHeight = Math.max(1, height);
        this.dimensions = new Size(actualWidth, actualHeight);
        
        // Create blank image with the actual dimensions
        BufferedImage blankImage = new BufferedImage(actualWidth, actualHeight, BufferedImage.TYPE_INT_ARGB);
        this.image = blankImage;
    }
    
    /**
     * Gets the texture name.
     *
     * @return the texture name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the texture ID.
     *
     * @return the unique texture ID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the image data.
     *
     * @return the BufferedImage, null if not loaded
     */
    public BufferedImage getImage() {
        return image;
    }
    
    /**
     * Gets the source path if applicable.
     *
     * @return the source path or null
     */
    public String getSourcePath() {
        return sourcePath;
    }
    
    /**
     * Gets the texture dimensions.
     *
     * @return the size of the texture
     */
    public Size getDimensions() {
        return dimensions;
    }
    
    /**
     * Gets the width of the texture.
     *
     * @return the width
     */
    public int getWidth() {
        return dimensions != null ? dimensions.getWidth() : 0;
    }
    
    /**
     * Gets the height of the texture.
     *
     * @return the height
     */
    public int getHeight() {
        return dimensions != null ? dimensions.getHeight() : 0;
    }
    
    /**
     * Gets the pixel at the specified position.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the color at the position, or null if out of bounds
     */
    public java.awt.Color getPixel(int x, int y) {
        if (image == null || x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return null;
        }
        return new java.awt.Color(image.getRGB(x, y));
    }
    
    /**
     * Creates a new texture at the specified position on this texture.
     *
     * @param x x-position to draw
     * @param y y-position to draw
     * @param sourceTexture source texture to copy from
     * @param offsetX horizontal offset
     * @param offsetY vertical offset
     */
    public void drawAt(int x, int y, Texture sourceTexture, int offsetX, int offsetY) {
        if (image == null || sourceTexture.getImage() == null) {
            return;
        }
        
        int sourceWidth = sourceTexture.getWidth();
        int sourceHeight = sourceTexture.getHeight();
        
        for (int sy = 0; sy < sourceHeight; sy++) {
            for (int sx = 0; sx < sourceWidth; sx++) {
                int dx = x + offsetX + sx;
                int dy = y + offsetY + sy;
                
                if (dx >= 0 && dx < getWidth() && dy >= 0 && dy < getHeight()) {
                    image.setRGB(dx, dy, sourceTexture.getImage().getRGB(sx, sy));
                }
            }
        }
    }
    
    /**
     * Creates a transparent copy of this texture.
     *
     * @return new transparent texture with same content
     */
    public Texture createCopy() {
        Texture copy = new Texture(name + "_copy", getWidth(), getHeight());
        copy.image = createCopyImage(image);
        copy.dimensions = dimensions;
        return copy;
    }
    
    /**
     * Creates an inverted version of this texture.
     *
     * @return inverted texture
     */
    public Texture createInverted() {
        Texture inverted = new Texture(name + "_inverted", getWidth(), getHeight());
        if (image != null) {
            inverted.image = createInvertedImage(image);
        }
        inverted.dimensions = dimensions;
        return inverted;
    }
    
    /**
     * Checks if this texture has the specified pixel color.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param color expected color
     * @return true if pixel matches
     */
    public boolean hasPixel(int x, int y, java.awt.Color color) {
        if (image == null) {
            return false;
        }
        return color.equals(getPixel(x, y));
    }
    
    /**
     * Converts an opaque BufferedImage to a transparent one.
     *
     * @param opaqueImage the opaque image
     * @return transparent version
     */
    private BufferedImage createCopyImage(BufferedImage opaqueImage) {
        if (opaqueImage == null) {
            return null;
        }
        BufferedImage transparent = new BufferedImage(opaqueImage.getWidth(null), 
                opaqueImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        
        for (int y = 0; y < opaqueImage.getHeight(null); y++) {
            for (int x = 0; x < opaqueImage.getWidth(null); x++) {
                int pixel = opaqueImage.getRGB(x, y);
                transparent.setRGB(x, y, pixel);
            }
        }
        return transparent;
    }
    
    /**
     * Creates an inverted copy of the image.
     *
     * @param image the image to invert
     * @return inverted image
     */
    private BufferedImage createInvertedImage(BufferedImage image) {
        if (image == null) {
            return null;
        }
        BufferedImage inverted = new BufferedImage(image.getWidth(null), 
                image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < image.getHeight(null); y++) {
            for (int x = 0; x < image.getWidth(null); x++) {
                int r = image.getRGB(x, y);
                int red = (r >> 16) & 0xFF;
                int green = (r >> 8) & 0xFF;
                int blue = r & 0xFF;
                int invertedPixel = (255 - red) << 16 | (255 - green) << 8 | (255 - blue);
                inverted.setRGB(x, y, invertedPixel);
            }
        }
        return inverted;
    }
    
    @Override
    public String toString() {
        return "Texture{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", dimensions=" + dimensions +
                ", loaded=" + (image != null) +
                '}';
    }
}
