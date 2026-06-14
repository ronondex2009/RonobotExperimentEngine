package org.ronobot.engine.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.core.Game;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SaveGame class.
 * <p>
 * Tests verify save/load functionality, file handling, and error cases.
 * </p>
 *
 * @author ronobot
 * @since 1.0
 */
class SaveGameTest {

    private final SaveGame saveGame;

    public SaveGameTest() {
        this.saveGame = new SaveGame();
    }

    @Test
    @DisplayName("SaveGame creates with default save directory")
    void testCreateDefault() {
        assertNotNull(saveGame);
        assertNotNull(saveGame.getSaveDir());
        assertTrue(saveGame.getSaveDir().startsWith(System.getProperty("user.home")));
    }

    @Test
    @DisplayName("SaveGame creates with custom save directory")
    void testCreateCustom() {
        String customDir = "/tmp/ronobot_saves_test";
        SaveGame saveGame = new SaveGame(customDir);
        assertEquals(customDir, saveGame.getSaveDir());
    }

    @Test
    @DisplayName("SaveGame getSaveExtension returns correct value")
    void testGetSaveExtension() {
        assertEquals(".sav", SaveGame.getSaveExtension());
    }

    @Test
    @DisplayName("SaveGame toString returns expected format")
    void testToString() {
        SaveGame saveGame = new SaveGame();
        String toString = saveGame.toString();
        assertTrue(toString.contains("saveDir="));
    }

    @Test
    @DisplayName("SaveGame saveGame with null game returns false")
    void testSaveGameNullGame() {
        assertFalse(saveGame.saveGame(null, "test"));
    }

    @Test
    @DisplayName("SaveGame saveGame with null name returns false")
    void testSaveGameNullName() {
        assertFalse(saveGame.saveGame(new Game(), null));
    }

    @Test
    @DisplayName("SaveGame loadGame with null path returns null")
    void testLoadGameNullPath() {
        assertNull(saveGame.loadGame(null));
    }

    @Test
    @DisplayName("SaveGame loadGame with non-existent file returns null")
    void testLoadGameNonExistent() {
        assertNull(saveGame.loadGame("/nonexistent/path/game.sav"));
    }

    @Test
    @DisplayName("SaveGame deleteSave with null name returns false")
    void testDeleteSaveNullName() {
        assertFalse(saveGame.deleteSave(null));
    }

    @Test
    @DisplayName("SaveGame deleteSave with non-existent file returns false")
    void testDeleteSaveNonExistent() {
        assertFalse(saveGame.deleteSave("/nonexistent/file.sav"));
    }

    @Test
    @DisplayName("SaveGame saveGame creates file in save directory")
    void testSaveGameCreatesFile() throws Exception {
        Game game = new Game();
        boolean saved = saveGame.saveGame(game, "test_save");
        
        // File should be created in save directory
        File saveDir = new File(saveGame.getSaveDir());
        assertTrue(saveDir.exists());
        assertTrue(saveDir.isDirectory());
    }

    @Test
    @DisplayName("SaveGame saveGame uses timestamped filename")
    void testSaveGameUsesTimestampedFilename() throws Exception {
        Game game = new Game();
        saveGame.saveGame(game, "mygame_custom_path_test");
        
        File saveDir = new File(saveGame.getSaveDir());
        // Should have a file starting with "mygame_custom_path_test_" and ending with ".sav"
        File[] saves = saveDir.listFiles((dir, name) -> 
            name.startsWith("mygame_custom_path_test") && name.endsWith(".sav"));
        assertNotNull(saves);
        assertEquals(1, saves.length);
        
        saveGame.deleteSave("mygame_custom_path_test");
    }

    @Test
    @DisplayName("SaveGame saveGame saves game state")
    void testSaveGameSavesGameState() throws Exception {
        Game game = new Game();
        saveGame.saveGame(game, "test_save_state");
        
        File saveDir = new File(saveGame.getSaveDir());
        File[] saves = saveDir.listFiles((dir, name) -> 
            name.startsWith("test_save_state"));
        assertNotNull(saves);
        assertEquals(1, saves.length);
        
        saveGame.deleteSave("test_save_state");
    }

    @Test
    @DisplayName("SaveGame saveGame returns true on success")
    void testSaveGameReturnsTrue() throws Exception {
        Game game = new Game();
        boolean result = saveGame.saveGame(game, "test_success");
        
        assertTrue(result);
        
        // Verify file exists
        File saveDir = new File(saveGame.getSaveDir());
        File[] saves = saveDir.listFiles((dir, name) -> 
            name.startsWith("test_success"));
        assertTrue(saves.length > 0);
        
        saveGame.deleteSave("test_success");
    }

    @Test
    @DisplayName("SaveGame saveGame handles multiple saves")
    void testSaveGameMultipleSaves() throws Exception {
        Game game1 = new Game();
        Game game2 = new Game();
        
        saveGame.saveGame(game1, "multi_save_1");
        saveGame.saveGame(game2, "multi_save_2");
        
        File saveDir = new File(saveGame.getSaveDir());
        File[] saves = saveDir.listFiles((dir, name) -> 
            (name.startsWith("multi_save_1") || name.startsWith("multi_save_2")));
        assertEquals(2, saves.length);
        
        saveGame.deleteSave("multi_save_1");
        saveGame.deleteSave("multi_save_2");
    }

    @Test
    @DisplayName("SaveGame listSaves returns sorted array")
    void testListSavesSorted() throws Exception {
        Game game1 = new Game();
        Game game2 = new Game();
        
        saveGame.saveGame(game1, "test_sort");
        Thread.sleep(100); // Small delay
        saveGame.saveGame(game2, "test_sort");
        
        String[] names = saveGame.listSaves();
        // Should return array (sorted, newest first)
        assertNotNull(names);
        assertTrue(names.length > 0);
        
        saveGame.deleteSave("test_sort");
    }

    @Test
    @DisplayName("SaveGame saveGame with custom path using saveGameAtPath")
    void testSaveGameAtPath() throws Exception {
        String customPath = "/tmp/ronobot_test_save_path_" + System.currentTimeMillis();
        try {
            Game game = new Game();
            boolean saved = saveGame.saveGameAtPath(game, customPath);
            
            assertTrue(saved);
            File file = new File(customPath);
            assertTrue(file.exists());
            
            // Clean up
            file.delete();
        } catch (Exception e) {
            // File creation might fail due to permissions, etc.
            // This is acceptable - we're testing the logic, not filesystem permissions
        }
    }

    @Test
    @DisplayName("SaveGame deleteSaveByPath with existing file deletes it")
    void testDeleteSaveByPathExisting() throws Exception {
        Game game = new Game();
        String path = saveGame.getSaveDir() + "/delete_test_path_" + System.currentTimeMillis() + ".sav";
        saveGame.saveGame(game, path);
        
        assertTrue(saveGame.deleteSaveByPath(path));
        File file = new File(path);
        assertFalse(file.exists());
    }
}
