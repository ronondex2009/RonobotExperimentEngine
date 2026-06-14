/**
 * AI State - Represents an enemy's behavioral state in the AI state machine.
 * <p>
 * Each state has associated behavior that determines the enemy's actions:
 * - IDLE: Enemy is waiting, not actively pursuing anything
 * - PATROL: Enemy is moving along a patrol route
 * - CHASE: Enemy is pursuing a target
 * - ATTACK: Enemy is attacking a target
 * - RETREAT: Enemy is retreating when low on health
 * - STUNNED: Enemy is stunned and unable to act
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
package org.ronobot.engine.entity;

/**
 * AI State enum for enemy AI behavior management.
 *
 * @author ronobot
 * @since 1.0
 */
public enum AIState {

    /**
     * Enemy is idle, waiting for stimuli.
     */
    IDLE(0),

    /**
     * Enemy is patrolling a route.
     */
    PATROL(1),

    /**
     * Enemy is chasing a target.
     */
    CHASE(2),

    /**
     * Enemy is attacking a target.
     */
    ATTACK(3),

    /**
     * Enemy is retreating.
     */
    RETREAT(4),

    /**
     * Enemy is stunned.
     */
    STUNNED(5);

    /**
     * Patrol position enum for patrol state.
     */
    public enum PatrolPosition {
        /**
         * Patrol along the x-axis.
         */
        X_AXIS,

        /**
         * Patrol along the y-axis.
         */
        Y_AXIS,

        /**
         * Patrol along the diagonal.
         */
        DIAGONAL
    };

    /**
     * The priority value for this state.
     */
    private final int priority;

    /**
     * Creates a new AIState with the given priority.
     *
     * @param priority The priority for this state
     */
    AIState(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the priority value for this state.
     *
     * @return The priority value
     */
    public int getPriority() {
        return priority;
    }
}
