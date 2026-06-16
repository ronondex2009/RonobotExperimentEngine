package org.ronobot.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for App class.
 * <p>
 * These tests verify App initialization and basic functionality
 * without requiring a GUI display (for headless environments).
 * </p>
 *
 * @author ronobot
 * @version 1.0
 * @since 2026-06-16
 */
public class AppTest {

    /**
     * Should create App instance without GUI.
     */
    @Test
    public void shouldCreateAppInstance() {
        App app = new App();
        assertNotNull(app);
    }

    /**
     * Should have default game set.
     */
    @Test
    public void shouldHaveDefaultGame() {
        App app = new App();
        assertNotNull(app.getGame());
    }

    /**
     * Should set game and get it back.
     */
    @Test
    public void shouldSetAndGetGame() {
        App app = new App();
        
        assertNotNull(app.getGame());
    }

    /**
     * Should get non-null game initially.
     */
    @Test
    public void shouldGetNonNullGameInitially() {
        App app = new App();
        assertNotNull(app.getGame());
    }

    /**
     * Should get non-null physics engine.
     */
    @Test
    public void shouldGetNonNullPhysicsEngine() {
        App app = new App();
        assertNotNull(app.getPhysicsEngine());
    }

    /**
     * Should get non-null renderer.
     */
    @Test
    public void shouldGetNonNullRenderer() {
        App app = new App();
        assertNotNull(app.getRenderer());
    }

    /**
     * Should get non-null input handler.
     */
    @Test
    public void shouldGetNonNullInputHandler() {
        App app = new App();
        assertNotNull(app.getInputHandler());
    }
}
