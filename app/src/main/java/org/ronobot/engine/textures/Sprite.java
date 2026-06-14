package org.ronobot.engine.textures;

import org.ronobot.engine.math.Position;

import java.awt.image.BufferedImage;

/**
 * Sprite class for representing textured entities in the game.
 * Wraps a texture and provides sprite operations.
 *
 * @author ronobot
 */
public class Sprite {
    
    private final String name;
    private final Texture texture;
    private Position position;
    private Position velocity;
    private Position size;
    private int frameIndex;
    private final int totalFrames;
    private int frameWidth;
    private int frameHeight;
    public boolean flipHorizontal;
    public boolean flipVertical;
    private int rotationDegrees;
    
    /**
     * Creates a new sprite from a texture.
     *
     * @param name sprite name
     * @param texture the texture
     * @param position initial position
     */
    public Sprite(String name, Texture texture, Position position) {
        this.name = name;
        this.texture = texture;
        this.position = position;
        this.velocity = new Position(0, 0);
        this.size = new Position(texture.getWidth(), texture.getHeight());
        this.frameIndex = 0;
        this.totalFrames = 1;
        this.frameWidth = texture.getWidth();
        this.frameHeight = texture.getHeight();
        this.flipHorizontal = false;
        this.flipVertical = false;
        this.rotationDegrees = 0;
    }
    
    /**
     * Creates a sprite with animation frames.
     *
     * @param name sprite name
     * @param texture the texture atlas
     * @param position initial position
     * @param totalFrames animation frame count
     */
    public Sprite(String name, Texture texture, Position position, int totalFrames) {
        this.name = name;
        this.texture = texture;
        this.position = position;
        this.velocity = new Position(0, 0);
        this.size = new Position(texture.getWidth(), texture.getHeight());
        this.frameIndex = 0;
        this.totalFrames = Math.max(1, totalFrames);
        this.frameWidth = texture.getWidth() / totalFrames;
        this.frameHeight = texture.getHeight() / totalFrames;
        this.flipHorizontal = false;
        this.flipVertical = false;
        this.rotationDegrees = 0;
    }
    
    /**
     * Gets the sprite name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the texture.
     *
     * @return the texture
     */
    public Texture getTexture() {
        return texture;
    }
    
    /**
     * Gets the sprite position.
     *
     * @return current position
     */
    public Position getPosition() {
        return position != null ? position.copy() : new Position(0, 0);
    }
    
    /**
     * Gets the sprite velocity.
     *
     * @return current velocity
     */
    public Position getVelocity() {
        return velocity != null ? velocity.copy() : new Position(0, 0);
    }
    
    /**
     * Gets the sprite size.
     *
     * @return the size
     */
    public Position getSize() {
        return size != null ? size.copy() : new Position(0, 0);
    }
    
    /**
     * Gets the width.
     *
     * @return width
     */
    public int getWidth() {
        return size != null ? (int) size.getX() : 0;
    }
    
    /**
     * Gets the height.
     *
     * @return height
     */
    public int getHeight() {
        return size != null ? (int) size.getY() : 0;
    }
    
    /**
     * Gets the current frame index.
     *
     * @return frame index
     */
    public int getFrameIndex() {
        return frameIndex;
    }
    
    /**
     * Gets the total number of frames.
     *
     * @return total frames
     */
    public int getTotalFrames() {
        return totalFrames;
    }
    
    /**
     * Gets the frame width for animated sprites.
     *
     * @return frame width
     */
    public int getFrameWidth() {
        return frameWidth;
    }
    
    /**
     * Gets the frame height for animated sprites.
     *
     * @return frame height
     */
    public int getFrameHeight() {
        return frameHeight;
    }
    
    /**
     * Sets the frame index.
     *
     * @param frameIndex the frame index
     */
    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex % totalFrames;
    }
    
    /**
     * Increments the frame index.
     */
    public void nextFrame() {
        frameIndex = (frameIndex + 1) % totalFrames;
    }
    
    /**
     * Sets the sprite position.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setPosition(int x, int y) {
        if (this.position == null) {
            this.position = new Position(x, y);
        } else {
            this.position.set(x, y);
        }
    }
    
    /**
     * Sets the sprite velocity.
     *
     * @param vx x-velocity
     * @param vy y-velocity
     */
    public void setVelocity(float vx, float vy) {
        if (this.velocity == null) {
            this.velocity = new Position(vx, vy);
        } else {
            this.velocity.set(vx, vy);
        }
    }
    
    /**
     * Sets the sprite size.
     *
     * @param width new width
     * @param height new height
     */
    public void setSize(int width, int height) {
        this.size = new Position(width, height);
    }
    
    /**
     * Toggles horizontal flipping.
     *
     * @param flip whether to flip
     */
    public void setFlipHorizontal(boolean flip) {
        this.flipHorizontal = flip;
    }
    
    /**
     * Toggles vertical flipping.
     *
     * @param flip whether to flip
     */
    public void setFlipVertical(boolean flip) {
        this.flipVertical = flip;
    }
    
    /**
     * Sets the rotation angle in degrees.
     *
     * @param degrees rotation angle
     */
    public void setRotationDegrees(float degrees) {
        this.rotationDegrees = (int) (degrees % 360);
    }
    
    /**
     * Gets the rotation in degrees.
     *
     * @return rotation
     */
    public float getRotationDegrees() {
        return rotationDegrees;
    }
    
    /**
     * Gets the rotation in radians.
     *
     * @return rotation radians
     */
    public float getRotationRadians() {
        return (float) Math.toRadians(rotationDegrees);
    }
    
    /**
     * Resets the sprite to default state.
     */
    public void reset() {
        this.position = new Position(0, 0);
        this.velocity = new Position(0, 0);
        this.size = new Position(0, 0);
        frameIndex = 0;
        flipHorizontal = false;
        flipVertical = false;
        rotationDegrees = 0;
    }
    
    @Override
    public String toString() {
        return "Sprite{" +
                "name='" + name + '\'' +
                ", position=" + (position != null ? position : "null") +
                ", velocity=" + (velocity != null ? velocity : "null") +
                ", size=" + (size != null ? size : "null") +
                ", frame=" + frameIndex + "/" + totalFrames +
                ", rotation=" + rotationDegrees + "°" +
                '}';
    }
}
