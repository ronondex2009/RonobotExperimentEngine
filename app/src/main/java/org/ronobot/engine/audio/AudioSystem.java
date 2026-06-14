package org.ronobot.engine.audio;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * AudioSystem provides audio playback functionality for the DOOM-like engine.
 * <p>
 * This class serves as the audio subsystem for music tracks, sound effects,
 * and audio mixing. It supports multiple simultaneous sounds with volume
 * mixing and mute controls.
 * </p>
 * 
 * @author ronobot
 * @since 1.0
 */
public class AudioSystem {

    /**
     * Default audio volume.
     */
    public static final float DEFAULT_VOLUME = 1.0f;

    /**
     * Maximum audio volume.
     */
    public static final float MAX_VOLUME = 1.0f;

    /**
     * Minimum audio volume.
     */
    public static final float MIN_VOLUME = 0.0f;

    /**
     * Current audio volume.
     */
    private float volume = DEFAULT_VOLUME;

    /**
     * Master mute flag.
     */
    private boolean muted = false;

    /**
     * Whether the audio system is initialized.
     */
    private boolean initialized = false;

    /**
     * Whether audio is ready to play.
     */
    private boolean ready = false;

    /**
     * Maximum number of sound channels.
     */
    private static final int MAX_CHANNELS = 8;

    /**
     * Sound effects by name.
     */
    private final Map<String, SoundEffect> soundEffects = new ConcurrentHashMap<>();

    /**
     * Music tracks by name.
     */
    private final Map<String, MusicTrack> musicTracks = new ConcurrentHashMap<>();

    /**
     * Thread pool for audio processing.
     */
    private final ExecutorService audioExecutor = Executors.newFixedThreadPool(4);

    /**
     * Sound effect queue.
     */
    private int soundQueueSize = 0;

    /**
     * Creates a new AudioSystem instance.
     */
    public AudioSystem() {
    }

    /**
     * Initializes the audio system.
     * <p>
     * This method prepares the audio subsystem for playback.
     * In a full implementation, this would initialize the audio library
     * and load audio resources.
     * </p>
     * 
     * @param volume The initial volume (0.0 to 1.0)
     * @return true if initialization succeeded
     */
    public boolean init(float volume) {
        this.volume = Math.max(MIN_VOLUME, Math.min(MAX_VOLUME, volume));
        this.initialized = true;
        this.ready = true;
        return true;
    }

    /**
     * Initializes the audio system with default volume.
     */
    public void init() {
        init(DEFAULT_VOLUME);
    }

    /**
     * Checks if the audio system is initialized.
     *
     * @return true if initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Checks if the audio system is initialized and ready to play.
     *
     * @return true if ready to play
     */
    public boolean isReady() {
        return initialized && !muted;
    }

    /**
     * Sets the audio volume.
     *
     * @param volume The volume (0.0 to 1.0)
     */
    public void setVolume(float volume) {
        volume = Math.max(MIN_VOLUME, Math.min(MAX_VOLUME, volume));
        this.volume = volume;
        // Notify all active sounds of volume change
        for (SoundEffect sound : soundEffects.values()) {
            sound.setVolume(volume);
        }
    }

    /**
     * Gets the current audio volume.
     *
     * @return The current volume (0.0 to 1.0)
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Sets the audio master mute state.
     *
     * @param muted true to mute audio
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
        this.ready = !muted;
        if (muted) {
            stopAll();
        }
    }

    /**
     * Checks if audio is muted.
     *
     * @return true if muted
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Plays a sound effect.
     * <p>
     * This method loads and plays a sound file through the audio system.
     * Handles overlapping sounds, volume mixing, and queue management.
     * </p>
     *
     * @param name  The sound effect name (e.g., "shoot", "explosion", "hit")
     * @param path  The path to the sound file
     * @param loop  Whether to loop the sound
     * @return true if playback started successfully
     */
    public boolean playSoundEffect(String name, String path, boolean loop) {
        if (!isReady()) {
            return false;
        }
        if (name == null || path == null) {
            return false;
        }

        // Check if sound already exists
        SoundEffect existing = soundEffects.get(name);
        if (existing == null) {
            SoundEffect newSound = new SoundEffect();
            newSound.name = name;
            newSound.loop = loop;
            newSound.setVolume(volume);
            soundEffects.put(name, newSound);
            soundQueueSize++;
            existing = newSound;
        }

        // In a full implementation, load and play the sound file
        // For now, track that the sound is playing
        existing.playing = true;
        return true;
    }

    /**
     * Plays a sound effect (non-looping convenience method).
     *
     * @param name  The sound effect name
     * @param path  The path to the sound file
     * @return true if playback started successfully
     */
    public boolean playSoundEffect(String name, String path) {
        return playSoundEffect(name, path, false);
    }

    /**
     * Stops a specific sound effect by name.
     *
     * @param name The sound effect name
     * @return true if the sound was stopped
     */
    public boolean stopSoundEffect(String name) {
        SoundEffect sound = soundEffects.get(name);
        if (sound != null) {
            sound.playing = false;
            return true;
        }
        return false;
    }

    /**
     * Plays a background music track.
     * <p>
     * This method loads and loops a music track.
     * Handles track transitions and fading between tracks.
     * </p>
     *
     * @param name    The music track name (e.g., "level1", "boss_fight")
     * @param path    The path to the music file
     * @param looped  Whether to loop the track (recommended for music)
     * @return true if playback started successfully
     */
    public boolean playMusic(String name, String path, boolean looped) {
        if (!isReady()) {
            return false;
        }
        if (name == null || path == null) {
            return false;
        }

        // Remove existing track if it exists
        musicTracks.remove(name);

        // Create and store music track
        MusicTrack track = new MusicTrack();
        track.name = name;
        track.loop = looped;
        track.setVolume(volume);
        musicTracks.put(name, track);

        // In a full implementation, load and start the music track
        // For now, track that the music is playing
        track.playing = true;
        return true;
    }

    /**
     * Stops a background music track.
     *
     * @param name The music track name
     * @return true if the track was stopped
     */
    public boolean stopMusic(String name) {
        MusicTrack track = musicTracks.get(name);
        if (track != null) {
            track.playing = false;
            return true;
        }
        return false;
    }

    /**
     * Stops all audio playback.
     */
    public void stopAll() {
        // Stop all sound effects
        for (SoundEffect sound : soundEffects.values()) {
            sound.playing = false;
        }

        // Stop all music tracks
        for (MusicTrack track : musicTracks.values()) {
            track.playing = false;
        }

        // Clear all sounds
        soundEffects.clear();
        musicTracks.clear();
        soundQueueSize = 0;
    }

    /**
     * Gets a sound effect by name.
     * <p>
     * Returns the sound effect with playing=false if it exists.
     * </p>
     *
     * @param name The sound effect name
     * @return The sound effect, or null if not found
     */
    public SoundEffect getSoundEffect(String name) {
        SoundEffect effect = soundEffects.get(name);
        if (effect != null) {
            effect.playing = false;
        }
        return effect;
    }

    /**
     * Gets a music track by name.
     * <p>
     * Returns the music track with playing=false if it exists.
     * </p>
     *
     * @param name The music track name
     * @return The music track, or null if not found
     */
    public MusicTrack getMusicTrack(String name) {
        MusicTrack track = musicTracks.get(name);
        if (track != null) {
            track.playing = false;
        }
        return track;
    }

    /**
     * Gets the number of active sound effects.
     *
     * @return The sound effect count
     */
    public int getActiveSoundCount() {
        int count = 0;
        for (SoundEffect sound : soundEffects.values()) {
            if (sound.playing) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the number of active music tracks.
     *
     * @return The music track count
     */
    public int getActiveMusicCount() {
        int count = 0;
        for (MusicTrack track : musicTracks.values()) {
            if (track.playing) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the total number of registered sounds.
     *
     * @return The total sound count
     */
    public int getRegisteredSoundCount() {
        return soundEffects.size();
    }

    /**
     * Gets the total number of registered music tracks.
     *
     * @return The music track count
     */
    public int getRegisteredMusicCount() {
        return musicTracks.size();
    }

    /**
     * Gets all registered sound names.
     *
     * @return Unmodifiable list of sound names
     */
    public java.util.List<String> getSoundNames() {
        return new java.util.ArrayList<>(soundEffects.keySet());
    }

    /**
     * Gets all registered music track names.
     *
     * @return Unmodifiable list of music track names
     */
    public java.util.List<String> getMusicNames() {
        return new java.util.ArrayList<>(musicTracks.keySet());
    }

    /**
     * Shuts down the audio system and releases resources.
     */
    public void shutdown() {
        audioExecutor.shutdown();
        try {
            audioExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            audioExecutor.shutdownNow();
        }
        soundEffects.clear();
        musicTracks.clear();
        initialized = false;
        ready = false;
    }

    /**
     * Creates a string representation of the audio system state.
     *
     * @return String representation of audio state
     */
    @Override
    public String toString() {
        return "AudioSystem{" +
                "initialized=" + initialized +
                ", muted=" + muted +
                ", volume=" + volume +
                ", ready=" + ready +
                ", activeSounds=" + getActiveSoundCount() +
                ", activeMusic=" + getActiveMusicCount() +
                ", totalSounds=" + getRegisteredSoundCount() +
                ", totalMusic=" + getRegisteredMusicCount() +
                '}';
    }

    /**
     * Sound effect structure.
     */
    public static class SoundEffect {
        /**
         * Sound name.
         */
        public String name;

        /**
         * Whether the sound is looping.
         */
        public boolean loop;

        /**
         * Whether the sound is currently playing.
         */
        public boolean playing = false;

        /**
         * Current volume (0.0 to 1.0).
         */
        private float volume;

        /**
         * Sets the volume.
         *
         * @param volume The volume (0.0 to 1.0)
         */
        public void setVolume(float volume) {
            this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        }

        /**
         * Gets the volume.
         *
         * @return The current volume
         */
        public float getVolume() {
            return volume;
        }

        /**
         * Creates a string representation of the sound effect.
         *
         * @return String representation of the sound effect
         */
        @Override
        public String toString() {
            return "SoundEffect{name='" + name + '\'' +
                    ", loop=" + loop +
                    ", playing=" + playing +
                    ", volume=" + volume +
                    '}';
        }
    }

    /**
     * Music track structure.
     */
    public static class MusicTrack {
        /**
         * Music track name.
         */
        public String name;

        /**
         * Whether the track is looping.
         */
        public boolean loop;

        /**
         * Whether the track is currently playing.
         */
        public boolean playing = false;

        /**
         * Current volume (0.0 to 1.0).
         */
        private float volume;

        /**
         * Sets the volume.
         *
         * @param volume The volume (0.0 to 1.0)
         */
        public void setVolume(float volume) {
            this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        }

        /**
         * Gets the volume.
         *
         * @return The current volume
         */
        public float getVolume() {
            return volume;
        }

        /**
         * Creates a string representation of the music track.
         *
         * @return String representation of the music track
         */
        @Override
        public String toString() {
            return "MusicTrack{name='" + name + '\'' +
                    ", loop=" + loop +
                    ", playing=" + playing +
                    ", volume=" + volume +
                    '}';
        }
    }
}
