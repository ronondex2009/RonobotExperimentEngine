/**
 * Enum for game state types.
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-06-15
 */
package org.ronobot.engine.core;

/**
 * Enum for game state types.
 *
 * @author ronobot
 * @since 2026-06-15
 */
public enum GameStateType {
    /**
     * Game is actively running.
     */
    PLAYING,

    /**
     * Game is paused.
     */
    PAUSED,

    /**
     * Game was won.
     */
    WON,

    /**
     * Game was lost.
     */
    LOST,

    /**
     * Game has stopped or ended.
     */
    STOPPED
}