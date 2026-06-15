package org.ronobot.engine.io;

import org.ronobot.engine.core.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * SaveGame handles saving and loading game state for a DOOM-like engine.
 * <p>
 * This class provides methods to persist game state to files and restore
 * it later. Supports saving game progress, entity positions, player stats,
 * and world state.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
public class SaveGame {

    /**
     * Save file extension.
     */
    public static final String SAVE_EXTENSION = ".sav";

    /**
     * Temporary directory for save files.
     */
    private final String saveDir;

    /**
     * Creates a new SaveGame with default save directory.
     */
    public SaveGame() {
        this.saveDir = System.getProperty("user.home") + "/.ronobot_engine/saves/";
    }

    /**
     * Creates a new SaveGame with a custom save directory.
     *
     * @param saveDir The custom save directory
     */
    public SaveGame(String saveDir) {
        this.saveDir = saveDir;
    }

    /**
     * Saves the game to a file.
     * <p>
     * This method serializes the entire game state including player position,
     * health, ammo, entity positions, and world state. The file is stored
     * in the save directory with a timestamped filename.
     * </p>
     *
     * @param game The game to save
     * @param name The save name (without extension)
     * @return true if save succeeded, false otherwise
     */
    public boolean saveGame(Game game, String name) {
        if (game == null || name == null) {
            return false;
        }

        // Create save directory if it doesn't exist
        File dir = new File(saveDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("SaveGame: Could not create save directory");
                return false;
            }
        }

        // Create unique save filename with timestamp
        long timestamp = System.currentTimeMillis();
        String filename = name + "_" + timestamp + SAVE_EXTENSION;
        File saveFile = new File(dir, filename);

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(saveFile))) {

            // Write game state
            out.writeObject(game);

            return true;
        } catch (IOException e) {
            System.err.println("SaveGame: Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves the game to a file with a specific path.
     *
     * @param game The game to save
     * @param path The full path including filename
     * @return true if save succeeded, false otherwise
     */
    public boolean saveGameAtPath(Game game, String path) {
        if (game == null || path == null) {
            return false;
        }

        File file = new File(path);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                System.err.println("SaveGame: Could not create parent directory");
                return false;
            }
        }

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(file))) {
            out.writeObject(game);
            return true;
        } catch (IOException e) {
            System.err.println("SaveGame: Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads a game from a file.
     *
     * @param path The path to the save file
     * @return The loaded game, or null if load failed
     */
    public Game loadGame(String path) {
        if (path == null) {
            return null;
        }

        File file = new File(path);
        if (!file.exists()) {
            System.err.println("SaveGame: Save file does not exist: " + path);
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(file))) {
            Object obj = in.readObject();
            if (!(obj instanceof Game)) {
                System.err.println("SaveGame: Save file contains invalid game data");
                return null;
            }
            return (Game) obj;
        } catch (IOException e) {
            System.err.println("SaveGame: Error loading game: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("SaveGame: Class not found during game load: " + e.getMessage());
            return null;
        }
    }

    /**
     * Loads a game by name from the save directory.
     * <p>
     * Searches for the latest save file matching the name.
     * </p>
     *
     * @param name The save name (without extension)
     * @return The loaded game, or null if not found
     */
    public Game loadGameByName(String name) {
        if (name == null) {
            return null;
        }

        File dir = new File(saveDir);
        if (!dir.exists()) {
            return null;
        }

        File[] saveFiles = dir.listFiles((dir1, name1) ->
                name1.startsWith(name) && name1.endsWith(SAVE_EXTENSION));

        if (saveFiles == null || saveFiles.length == 0) {
            System.err.println("SaveGame: No save files found for name: " + name);
            return null;
        }

        // Find the most recent save file
        File latestSave = null;
        long latestTime = 0;
        for (File saveFile : saveFiles) {
            long time = saveFile.lastModified();
            if (time > latestTime) {
                latestTime = time;
                latestSave = saveFile;
            }
        }

        return loadGame(latestSave.getAbsolutePath());
    }

    /**
     * Lists all save files in the save directory.
     *
     * @return An array of save file names, or empty array if none exist
     */
    public String[] listSaves() {
        File dir = new File(saveDir);
        if (!dir.exists()) {
            return new String[0];
        }

        File[] files = dir.listFiles((dir1, name1) ->
                name1.endsWith(SAVE_EXTENSION));

        if (files == null) {
            return new String[0];
        }

        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }

        // Sort by modification time (newest first)
        Integer[] indices = new Integer[names.length];
        for (int i = 0; i < names.length; i++) {
            indices[i] = i;
        }

        java.util.Arrays.sort(indices, (i1, i2) -> {
            File f1 = files[i1];
            File f2 = files[i2];
            return Long.compare(f2.lastModified(), f1.lastModified());
        });

        // Reorder names based on sorted indices
        String[] sortedNames = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            sortedNames[i] = names[indices[i]];
        }

        return sortedNames;
    }

    /**
     * Deletes a save file by name.
     *
     * @param name The save name (without extension)
     * @return true if deleted, false if not found
     */
    public boolean deleteSave(String name) {
        if (name == null) {
            return false;
        }

        File[] saveFiles = listSavesByName(name);
        if (saveFiles == null || saveFiles.length == 0) {
            return false;
        }

        File fileToDelete = saveFiles[saveFiles.length - 1]; // Delete oldest
        if (!fileToDelete.exists()) {
            return false;
        }

        if (!fileToDelete.delete()) {
            System.err.println("SaveGame: Could not delete save file: " + fileToDelete.getAbsolutePath());
            return false;
        }

        System.out.println("SaveGame: Deleted save: " + fileToDelete.getName());
        return true;
    }

    /**
     * Deletes a save file by path.
     *
     * @param path The path to the save file
     * @return true if deleted, false otherwise
     */
    public boolean deleteSaveByPath(String path) {
        if (path == null) {
            return false;
        }

        File file = new File(path);
        if (!file.exists()) {
            return false;
        }

        if (!file.delete()) {
            System.err.println("SaveGame: Could not delete save file: " + path);
            return false;
        }

        System.out.println("SaveGame: Deleted save: " + path);
        return true;
    }

    /**
     * Lists all save files by name prefix.
     *
     * @param name The name prefix
     * @return Array of File objects matching the name
     */
    private File[] listSavesByName(String name) {
        File dir = new File(saveDir);
        if (!dir.exists()) {
            return null;
        }

        File[] files = dir.listFiles((dir1, name1) ->
                name1.startsWith(name) && name1.endsWith(SAVE_EXTENSION));

        return files;
    }

    /**
     * Gets the save directory.
     *
     * @return The save directory path
     */
    public String getSaveDir() {
        return saveDir;
    }

    /**
     * Gets the save file extension.
     *
     * @return The save file extension
     */
    public static String getSaveExtension() {
        return SAVE_EXTENSION;
    }

    @Override
    public String toString() {
        return "SaveGame{" +
                "saveDir='" + saveDir + '\'' +
                '}';
    }
}
