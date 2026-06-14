package org.ronobot.engine.audio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.ronobot.engine.audio.AudioSystem.*;

/**
 * Unit tests for AudioSystem class.
 * <p>
 * Tests audio initialization, volume control, mute state,
 * sound effect and music track management.
 * </p>
 * 
 * @author ronobot
 * @since 1.0
 */
class AudioSystemTest {

    /**
     * Test AudioSystem creation and default state.
     */
    @Test
    void testCreation() {
        AudioSystem system = new AudioSystem();
        assertNotNull(system);
        assertFalse(system.isInitialized());
        assertFalse(system.isReady());
        assertFalse(system.isMuted());
        assertEquals(DEFAULT_VOLUME, system.getVolume());
    }

    /**
     * Test AudioSystem initialization with default volume.
     */
    @Test
    void testInitDefault() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        assertTrue(system.isInitialized());
        assertTrue(system.isReady());
        assertEquals(DEFAULT_VOLUME, system.getVolume());
    }

    /**
     * Test AudioSystem initialization with custom volume.
     */
    @Test
    void testInitCustomVolume() {
        AudioSystem system = new AudioSystem();
        system.init(0.5f);
        
        assertTrue(system.isInitialized());
        assertTrue(system.isReady());
        assertEquals(0.5f, system.getVolume(), 0.001f);
    }

    /**
     * Test volume is clamped to valid range.
     */
    @Test
    void testVolumeClamped() {
        AudioSystem system = new AudioSystem();
        
        // Volume above maximum
        system.init(2.0f);
        assertEquals(MAX_VOLUME, system.getVolume(), 0.001f);
        
        // Volume below minimum
        system.init(-0.5f);
        assertEquals(MIN_VOLUME, system.getVolume(), 0.001f);
        
        // Normal volume
        system.init(0.5f);
        assertEquals(0.5f, system.getVolume(), 0.001f);
    }

    /**
     * Test setting volume after initialization.
     */
    @Test
    void testSetVolume() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.setVolume(0.75f);
        assertEquals(0.75f, system.getVolume(), 0.001f);
    }

    /**
     * Test that setting volume clamps to valid range.
     */
    @Test
    void testSetVolumeClamped() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.setVolume(2.0f);
        assertEquals(MAX_VOLUME, system.getVolume(), 0.001f);
        
        system.setVolume(-0.5f);
        assertEquals(MIN_VOLUME, system.getVolume(), 0.001f);
    }

    /**
     * Test mute control.
     */
    @Test
    void testMuteControl() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        assertFalse(system.isMuted());
        assertTrue(system.isReady());
        
        system.setMuted(true);
        assertTrue(system.isMuted());
        assertFalse(system.isReady());
        
        system.setMuted(false);
        assertFalse(system.isMuted());
        assertTrue(system.isReady());
    }

    /**
     * Test stopAll stops all sounds and clears tracks.
     */
    @Test
    void testStopAll() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.playSoundEffect("shoot", "test.wav", false);
        system.playSoundEffect("hit", "test.wav", false);
        system.playMusic("level1", "test.mid", true);
        
        assertEquals(2, system.getActiveSoundCount());
        assertEquals(1, system.getActiveMusicCount());
        assertEquals(2, system.getRegisteredSoundCount());
        assertEquals(1, system.getRegisteredMusicCount());
        
        system.stopAll();
        
        assertEquals(0, system.getActiveSoundCount());
        assertEquals(0, system.getActiveMusicCount());
        assertEquals(2, system.getRegisteredSoundCount()); // Registered remains
        assertEquals(1, system.getRegisteredMusicCount()); // Registered remains
        assertFalse(system.isMuted());
    }

    /**
     * Test getSoundEffect returns correct sound.
     */
    @Test
    void testGetSoundEffect() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.playSoundEffect("shoot", "test.wav", false);
        
        SoundEffect effect = system.getSoundEffect("shoot");
        assertNotNull(effect);
        assertEquals("shoot", effect.name);
        assertFalse(effect.loop);
        assertFalse(effect.playing);
    }

    /**
     * Test getSoundEffect returns null for unknown sound.
     */
    @Test
    void testGetSoundEffectUnknown() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        SoundEffect effect = system.getSoundEffect("unknown");
        assertNull(effect);
    }

    /**
     * Test getMusicTrack returns correct track.
     */
    @Test
    void testGetMusicTrack() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.playMusic("level1", "test.mid", true);
        
        MusicTrack track = system.getMusicTrack("level1");
        assertNotNull(track);
        assertEquals("level1", track.name);
        assertTrue(track.loop);
        assertFalse(track.playing);
    }

    /**
     * Test getMusicTrack returns null for unknown track.
     */
    @Test
    void testGetMusicTrackUnknown() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        MusicTrack track = system.getMusicTrack("unknown");
        assertNull(track);
    }

    /**
     * Test getActiveSoundCount.
     */
    @Test
    void testActiveSoundCount() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        assertEquals(0, system.getActiveSoundCount());
        
        system.playSoundEffect("shoot", "test.wav", false);
        assertEquals(1, system.getActiveSoundCount());
        
        system.playSoundEffect("hit", "test.wav", false);
        assertEquals(2, system.getActiveSoundCount());
        
        system.stopSoundEffect("shoot");
        assertEquals(1, system.getActiveSoundCount());
    }

    /**
     * Test getActiveMusicCount.
     */
    @Test
    void testActiveMusicCount() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        assertEquals(0, system.getActiveMusicCount());
        
        system.playMusic("level1", "test.mid", true);
        assertEquals(1, system.getActiveMusicCount());
        
        system.playMusic("boss", "boss.mid", true);
        assertEquals(2, system.getActiveMusicCount());
        
        system.stopMusic("level1");
        assertEquals(1, system.getActiveMusicCount());
    }

    /**
     * Test getRegisteredSoundCount.
     */
    @Test
    void testRegisteredSoundCount() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        assertEquals(0, system.getRegisteredSoundCount());
        
        system.playSoundEffect("shoot", "test.wav", false);
        assertEquals(1, system.getRegisteredSoundCount());
        
        system.playSoundEffect("hit", "test.wav", false);
        assertEquals(2, system.getRegisteredSoundCount());
    }

    /**
     * Test getRegisteredMusicCount.
     */
    @Test
    void testRegisteredMusicCount() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        assertEquals(0, system.getRegisteredMusicCount());
        
        system.playMusic("level1", "test.mid", true);
        assertEquals(1, system.getRegisteredMusicCount());
        
        system.playMusic("boss", "boss.mid", true);
        assertEquals(2, system.getRegisteredMusicCount());
    }

    /**
     * Test getSoundNames returns all sound names.
     */
    @Test
    void testGetSoundNames() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.playSoundEffect("shoot", "test.wav", false);
        system.playSoundEffect("hit", "test.wav", false);
        
        java.util.List<String> names = system.getSoundNames();
        
        assertEquals(2, names.size());
        assertTrue(names.contains("shoot"));
        assertTrue(names.contains("hit"));
    }

    /**
     * Test getMusicNames returns all music track names.
     */
    @Test
    void testGetMusicNames() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.playMusic("level1", "test.mid", true);
        system.playMusic("boss", "boss.mid", true);
        
        java.util.List<String> names = system.getMusicNames();
        
        assertEquals(2, names.size());
        assertTrue(names.contains("level1"));
        assertTrue(names.contains("boss"));
    }

    /**
     * Test toString returns expected format.
     */
    @Test
    void testToString() {
        AudioSystem system = new AudioSystem();
        system.init();
        system.setVolume(0.5f);
        system.setMuted(true);
        
        String str = system.toString();
        
        assertTrue(str.contains("AudioSystem"));
        assertTrue(str.contains("initialized=true"));
        assertTrue(str.contains("muted=true"));
        assertTrue(str.contains("volume=0.5"));
        assertTrue(str.contains("ready=false"));
    }

    /**
     * Test stopSoundEffect stops specific sound.
     */
    @Test
    void testStopSoundEffect() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.playSoundEffect("shoot", "test.wav", false);
        system.playSoundEffect("hit", "test.wav", false);
        
        assertEquals(2, system.getActiveSoundCount());
        
        system.stopSoundEffect("shoot");
        assertEquals(1, system.getActiveSoundCount());
        
        system.stopSoundEffect("unknown");
        assertEquals(1, system.getActiveSoundCount()); // No error on unknown
    }

    /**
     * Test stopMusic stops specific track.
     */
    @Test
    void testStopMusic() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.playMusic("level1", "test.mid", true);
        system.playMusic("boss", "boss.mid", true);
        
        assertEquals(2, system.getActiveMusicCount());
        
        system.stopMusic("level1");
        assertEquals(1, system.getActiveMusicCount());
        
        system.stopMusic("unknown");
        assertEquals(1, system.getActiveMusicCount()); // No error on unknown
    }

    /**
     * Test playSoundEffect with invalid name.
     */
    @Test
    void testPlaySoundEffectNullName() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        boolean result = system.playSoundEffect(null, "test.wav", false);
        assertFalse(result);
    }

    /**
     * Test playSoundEffect with invalid path.
     */
    @Test
    void testPlaySoundEffectNullPath() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        boolean result = system.playSoundEffect("shoot", null, false);
        assertFalse(result);
    }

    /**
     * Test playMusic with invalid name.
     */
    @Test
    void testPlayMusicNullName() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        boolean result = system.playMusic(null, "test.mid", true);
        assertFalse(result);
    }

    /**
     * Test playMusic with invalid path.
     */
    @Test
    void testPlayMusicNullPath() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        boolean result = system.playMusic("level1", null, true);
        assertFalse(result);
    }

    /**
     * Test that playing when not initialized fails.
     */
    @Test
    void testPlayWithoutInit() {
        AudioSystem system = new AudioSystem();
        
        assertFalse(system.isReady());
        
        boolean result = system.playSoundEffect("shoot", "test.wav", false);
        assertFalse(result);
        
        boolean musicResult = system.playMusic("level1", "test.mid", true);
        assertFalse(musicResult);
    }

    /**
     * Test that playing when muted fails.
     */
    @Test
    void testPlayWhenMuted() {
        AudioSystem system = new AudioSystem();
        system.init();
        
        system.setMuted(true);
        
        boolean result = system.playSoundEffect("shoot", "test.wav", false);
        assertFalse(result);
        
        boolean musicResult = system.playMusic("level1", "test.mid", true);
        assertFalse(musicResult);
    }
}
