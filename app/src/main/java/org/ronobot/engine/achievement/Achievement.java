package org.ronobot.engine.achievement;

/**
 * Base achievement definition.
 * <p>
 * Achievements represent unlockable goals and rewards in the game.
 * Each achievement has a name, description, and unlock condition.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class Achievement {

    /**
     * Achievement name.
     */
    private final String name;

    /**
     * Achievement description.
     */
    private final String description;

    /**
     * Whether the achievement is unlocked.
     */
    private boolean unlocked = false;

    /**
     * Whether the achievement is completed (cannot be re-unlocked).
     */
    private boolean completed = false;

    /**
     * Achievement point value.
     */
    private int points = 0;

    /**
     * Creates a new achievement with name and description.
     *
     * @param name The achievement name
     * @param description The achievement description
     */
    public Achievement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Creates a new achievement with name, description, and points.
     *
     * @param name The achievement name
     * @param description The achievement description
     * @param points The achievement point value
     */
    public Achievement(String name, String description, int points) {
        this.name = name;
        this.description = description;
        this.points = points;
    }

    /**
     * Gets the achievement name.
     *
     * @return The achievement name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the achievement description.
     *
     * @return The achievement description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the point value.
     *
     * @return The point value
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the point value.
     *
     * @param points The new point value
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets whether the achievement is unlocked.
     *
     * @return true if unlocked
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Gets whether the achievement is completed.
     *
     * @return true if completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Checks if the achievement is both unlocked and completed.
     *
     * @return true if unlocked and completed
     */
    public boolean isFullyCompleted() {
        return unlocked && completed;
    }

    /**
     * Unlocks the achievement if not already completed.
     *
     * @return true if unlocked, false if already completed
     */
    public boolean unlock() {
        if (completed) {
            return false;
        }
        unlocked = true;
        return true;
    }

    /**
     * Completes the achievement (prevents re-unlocking).
     */
    public void complete() {
        completed = true;
    }

    /**
     * Resets the achievement to unlocked but not completed state.
     */
    public void reset() {
        unlocked = false;
        completed = false;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unlocked=" + unlocked +
                ", completed=" + completed +
                ", points=" + points +
                '}';
    }
}
