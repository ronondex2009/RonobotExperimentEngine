/**
 * AIStateMachine - Manages enemy AI state transitions and behavior.
 * <p>
 * Provides state machine functionality for enemy entities:
 * - Handles state transitions based on stimuli
 * - Manages state durations and cooldowns
 * - Processes enemy behavior based on current state
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
package org.ronobot.engine.entity;

import org.ronobot.engine.core.Entity;

/**
 * AIStateMachine for enemy AI behavior management.
 *
 * <p>This state machine handles enemy AI behavior including:
 * - State transitions (IDLE -> CHASE, PATROL -> ATTACK, etc.)
 * - State duration management
 * - Cooldown tracking
 * - Behavior processing based on current state
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class AIStateMachine {

    // ==================== Constants ==================

    /**
     * Default stun duration in frames.
     */
    private static final int DEFAULT_STUN_DURATION = 120;

    /**
     * Default chase duration in frames before switching to attack.
     */
    private static final int DEFAULT_CHASE_DURATION = 300;

    /**
     * Default patrol duration in frames.
     */
    private static final int DEFAULT_PATROL_DURATION = 600;

    // ==================== Fields ==================

    /**
     * The current AI state.
     */
    private AIState currentState;

    /**
     * The target entity for chase/attack states.
     */
    private Entity target;

    /**
     * The remaining duration for the current state in frames.
     */
    private int remainingDuration;

    /**
     * Whether the state machine is currently active.
     */
    private boolean isActive;

    /**
     * The cooldown for state changes.
     */
    private int stateChangeCooldown;

    // ==================== Constructors ==================

    /**
     * Creates a new AIStateMachine with default settings.
     */
    public AIStateMachine() {
        this(AIState.IDLE, 0);
    }

    /**
     * Creates a new AIStateMachine with a starting state.
     *
     * @param initialState The initial AI state
     * @param initialDuration The initial duration
     */
    public AIStateMachine(AIState initialState, int initialDuration) {
        this.currentState = initialState;
        this.remainingDuration = initialDuration;
        this.isActive = true;
        this.stateChangeCooldown = 0;
    }

    // ==================== Getters ==================

    /**
     * Gets whether the state machine is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Gets the current AI state.
     *
     * @return The current AI state
     */
    public AIState getCurrentState() {
        return currentState;
    }

    /**
     * Gets the target entity.
     *
     * @return The target entity, or null if none
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Gets the remaining duration for the current state.
     *
     * @return The remaining duration in frames
     */
    public int getRemainingDuration() {
        return remainingDuration;
    }

    /**
     * Gets the state change cooldown.
     *
     * @return The cooldown frames
     */
    public int getStateChangeCooldown() {
        return stateChangeCooldown;
    }

    // ==================== State Machine Methods ==================

    /**
     * Updates the state machine based on stimuli and conditions.
     * <p>
     * This method processes state transitions, decrements duration,
     * and handles state change cooldowns.
     * </p>
     *
     * @param deltaTime The time delta in frames (usually 1)
     */
    public void update(int deltaTime) {
        if (!isActive || currentState == null) {
            return;
        }

        // Decrement remaining duration
        remainingDuration -= deltaTime;

        // If state change is not on cooldown, evaluate new state
        if (stateChangeCooldown <= 0) {
            evaluateStateChange();
        }

        // If duration expired and not on cooldown, switch states
        if (remainingDuration <= 0) {
            switchStates();
        }
    }

    /**
     * Handles state transitions.
     */
    private void switchStates() {
        switch (currentState) {
            case PATROL -> switchToChase();
            case CHASE -> switchToAttack();
            case ATTACK -> switchToIdle();
            case IDLE -> {
                if (target != null) {
                    switchToChase();
                } else {
                    switchToPatrol();
                }
            }
            case RETREAT -> switchToIdle();
            case STUNNED -> switchToPatrol();
            default -> switchToPatrol();
        }
    }

    /**
     * Evaluates whether a state change should occur.
     */
    private void evaluateStateChange() {
        // This method can be extended for custom state change conditions
        // For now, state changes are handled by duration expiration
    }

    /**
     * Transitions to the IDLE state.
     */
    public void switchToIdle() {
        currentState = AIState.IDLE;
        remainingDuration = DEFAULT_PATROL_DURATION;
        target = null;
    }

    /**
     * Transitions to the PATROL state without a specific position.
     */
    public void switchToPatrol() {
        currentState = AIState.PATROL;
        remainingDuration = DEFAULT_PATROL_DURATION;
        target = null;
    }

    /**
     * Transitions to the CHASE state with a target.
     */
    public void switchToChase() {
        if (target == null) {
            switchToIdle();
            return;
        }

        currentState = AIState.CHASE;
        remainingDuration = DEFAULT_CHASE_DURATION;
    }

    /**
     * Sets a new target entity to chase.
     *
     * @param target The target entity
     */
    public void switchToChase(Entity target) {
        this.target = target;
        switchToChase();
    }

    /**
     * Transitions to the ATTACK state.
     */
    public void switchToAttack() {
        currentState = AIState.ATTACK;
        remainingDuration = 0; // Attack happens immediately
    }

    /**
     * Transitions to the RETREAT state.
     */
    public void switchToRetreat() {
        currentState = AIState.RETREAT;
        remainingDuration = DEFAULT_PATROL_DURATION;
        target = null;
    }

    /**
     * Transitions to the STUNNED state.
     *
     * @param duration The stun duration in frames
     */
    public void switchToStunned(int duration) {
        currentState = AIState.STUNNED;
        remainingDuration = duration;
        target = null; // Cannot target while stunned
    }

    /**
     * Sets the state change cooldown.
     *
     * @param cooldown The cooldown frames
     */
    public void setStateChangeCooldown(int cooldown) {
        this.stateChangeCooldown = Math.max(cooldown, 0);
    }

    /**
     * Processes a stimulus that might trigger a state change.
     *
     * @param entity The entity providing the stimulus
     * @param stimulusType The type of stimulus
     */
    public void processStimulus(Entity entity, String stimulusType) {
        if (currentState == AIState.STUNNED) {
            return;
        }

        switch (stimulusType.toLowerCase()) {
            case "enemy" -> switchToChase();
            case "sound" -> switchToIdle();
            case "damage" -> switchToRetreat();
            case "attack" -> switchToIdle();
            case "player" -> switchToChase();
            default -> switchToIdle();
        }
    }

    /**
     * Gets a description of the current state.
     *
     * @return A human-readable description
     */
    public String getDescription() {
        return switch (currentState) {
            case IDLE -> "Enemy is idle, waiting";
            case PATROL -> "Enemy is patrolling";
            case CHASE -> "Enemy is chasing target";
            case ATTACK -> "Enemy is attacking target";
            case RETREAT -> "Enemy is retreating";
            case STUNNED -> "Enemy is stunned";
        };
    }

    /**
     * Gets a visual representation of the current state.
     *
     * @return A string representing the state
     */
    public String getVisualRepresentation() {
        return switch (currentState) {
            case IDLE -> "[IDLE]";
            case PATROL -> "[PATROL]";
            case CHASE -> "[CHASE]";
            case ATTACK -> "[ATTACK]";
            case RETREAT -> "[RETREAT]";
            case STUNNED -> "[STUNNED]";
        };
    }
}
