# Save/Load System Documentation

## Overview

The save/load system allows persisting game state to disk and restoring it later. This enables:
- Resuming game progress
- Creating game checkpoints
- Backing up game saves
- Transferring saves between machines

## Components

### SaveGame.java

The `SaveGame` class handles all save/load operations.

**Key Features:**
- Automatic save directory creation at `~/.ronobot_engine/saves/`
- Timestamped save filenames to track changes
- Custom save paths for special saves
- Automatic cleanup of old saves (via deletion)
- Thread-safe file operations

### File Format

Saves are stored as Java serialized objects (.sav files). This ensures complete game state persistence.

**File Structure:**
```
~/.ronobot_engine/saves/
└── [save_name]_[timestamp].sav
```

## Usage

### Saving a Game

```java
import org.ronobot.engine.io.SaveGame;
import org.ronobot.engine.core.Game;

public class SaveManager {
    private final SaveGame saveGame = new SaveGame();
    
    public boolean saveGame(Game game, String name) {
        return saveGame.saveGame(game, name);
    }
    
    public boolean saveGameAtPath(Game game, String path) {
        return saveGame.saveGame(game, path);
    }
}
```

### Loading a Game

```java
import org.ronobot.engine.io.SaveGame;

public class LoadManager {
    private final SaveGame saveGame = new SaveGame();
    
    public Game loadLatestSave(String name) {
        return saveGame.loadGameByName(name);
    }
    
    public Game loadGame(String path) {
        return saveGame.loadGame(path);
    }
}
```

## API Reference

### SaveGame Class

#### Constructor

```java
public SaveGame()
// Uses default save directory

public SaveGame(String saveDir)
// Custom save directory
```

#### Save Methods

```java
public boolean saveGame(Game game, String name)
// Saves game with timestamped name

public boolean saveGame(Game game, String path)
// Saves game to specific path
```

#### Load Methods

```java
public Game loadGame(String path)
// Loads game from path, returns null if fails

public Game loadGameByName(String name)
// Loads most recent save matching name
```

#### Delete Methods

```java
public boolean deleteSave(String name)
// Deletes oldest save matching name

public boolean deleteSave(String path)
// Deletes save at specific path
```

#### List Methods

```java
public String[] listSaves()
// Lists all save filenames (sorted newest first)
```

#### Configuration

```java
public String getSaveDir()
// Gets save directory path

public void setSaveDir(String saveDir)
// Sets save directory path
```

## Best Practices

1. **Save Naming**: Use descriptive names like "level1_checkpoint", "boss_fight", etc.
2. **Timestamps**: Automatic timestamps prevent overwrites
3. **Cleanup**: Old saves can be deleted using `deleteSave()`
4. **Paths**: Use custom paths for special saves (e.g., cloud backups)
5. **Error Handling**: Always check return values for success/failure

## Error Handling

### Common Errors

| Error | Cause | Solution |
|-------|-------|----------|
| `null` from loadGame | File not found or invalid format | Verify file exists and is valid |
| `false` from saveGame | Permission denied or disk full | Check write permissions and disk space |
| `false` from deleteSave | File not found or permission denied | Verify file exists and is writable |

### Exception Handling

The `SaveGame` class handles exceptions internally and returns boolean/null values:

```java
if (!saveGame.saveGame(game, "checkpoints/level1")) {
    // Handle save failure (e.g., show warning message)
}

Game loaded = saveGame.loadGame("mygame_1234567890.sav");
if (loaded != null) {
    game.setState(loaded.getState());
} else {
    // Handle load failure
}
```

## Future Enhancements

- **Encryption**: Encrypt save files for security
- **Compression**: Compress save files for smaller size
- **Validation**: Validate game state before saving
- **Versioning**: Track save file versions
- **Cloud Sync**: Sync with cloud storage services
- **Automatic Backup**: Create backups before dangerous operations

## Implementation Details

### Serialization

The `SaveGame` class uses Java's built-in serialization:

```java
ObjectOutputStream out = new ObjectOutputStream(
    new FileOutputStream(file));
out.writeObject(game);
```

### Timestamped Filenames

Saves use timestamps to avoid overwrites:

```
mygame_1234567890.sav
mygame_1234567950.sav
```

### Directory Creation

The save directory is automatically created:

```java
File dir = new File(saveDir);
if (!dir.exists()) {
    dir.mkdirs();
}
```

## Testing

Unit tests are provided in `SaveGameTest.java`:

- Save/load functionality
- File creation/deletion
- Error handling
- Timestamped filenames
- Multiple save management

## License

This code is part of the org.ronobot.engine project.
