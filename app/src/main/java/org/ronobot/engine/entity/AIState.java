package org.ronobot.engine.entity;

/**
 * AI states for enemy entity behavior.
 * <p>
 * Represents different behavioral states an enemy can be in:
 * - PATROL: Moving around looking for player
 * - CHASE: Actively pursuing the player
 * - ATTACK: Performing an attack animation
 * - RETREAT: Running away from danger
 * - STUNNED: Unable to act temporarily
 * - IDLE: Waiting in a neutral state
 * </p>
 *
 * @author ronobot
 */
public enum AIState {

    /**
     * Enemy is idle/neutral.
     */
    IDLE,

    /**
     * Enemy is patrolling the map.
     */
    PATROL("patrol"),

    /**
     * Enemy is chasing the player.
     */
    CHASE("chase"),

    /**
     * Enemy is attacking.
     */
    ATTACK("attack"),

    /**
     * Enemy is retreating.
     */
    RETREAT("retreat"),

    /**
     * Enemy is stunned.
     */
    STUNNED("stunned");

    /**
     * The state name used for serialization or logging.
     */
    private final String name;

    /**
     * Creates an AIState with the given name.
     *
     * @param name The state name
     */
    AIState(String name) {
        this.name = name;
    }

    /**
     * Creates an AIState with no name (for IDLE).
     */
    AIState() {
        this.name = "idle";
    }

    /**
     * Gets the state name.
     *
     * @return The state name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Represents a patrol position.
     */
    public enum PatrolPosition {
        X_AXIS("x"),
        Y_AXIS("y"),
        RANDOM("random");

        private final String name;

        PatrolPosition(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
