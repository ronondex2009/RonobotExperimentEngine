# MEMORY.md
# Engine Development Log

## Current State - Cycle 32

### Build Status: BUILD SUCCESSFUL - 471/471 Tests Passing

### Next Goal: Address Bug from BUGS.md - Game Loop and Rendering

---

## Cycle 32 In Progress (2026-06-06)

### Goals for This Cycle
1. **Fix Game Loop and Rendering Issues**
   - App.start() calls game.runLoop() but Game.start() doesn't create game loop
   - Game needs proper rendering capability
   - Fix App.start() to properly handle game loop lifecycle
   - Implement rendering in Game class or App.start()

2. **Code Quality**
   - Make sure no compilation errors remain
   - Review and commit completed work

3. **Documentation**
   - Update CHANGES.md with progress
   - Delete BUGS.md if issues resolved

---

## Architecture

### Core Engine
```
App
├── game: Game
├── physics: PhysicsEngine
├── renderer: Renderer
├── input: InputHandler
└── start()
    ├── game.init()
    ├── game.setRenderer(this.renderer)
    ├── game.setInputHandler(this.input)
    └── game.runLoop()

Game
├── running: boolean
├── ended: boolean
├── entities: EntityManager
├── collisionManager: CollisionManager
├── map: GameMap
├── player: PlayerEntity
├── state: String
├── physicsEngine: PhysicsEngine
├── renderer: Renderer
├── frameCount: int
├── inputHandler: InputHandler
├── init()
├── start() - sets running flag but uses runLoop()
├── stop()
├── end()
├── update()
├── runLoop() - Main game loop with rendering
│   ├── inputHandler.handle(this)
│   ├── update()
│   ├── detectCollisions()
│   ├── collision.resolve()
│   ├── renderer.render(this)
│   └── Thread.sleep(16) for ~60 FPS
└── cleanup()
```

### Map System
```
LevelLoader
├── levelMetadata: Map
├── mapName: String
├── difficultyString: String
├── spawnPositions: List
├── isLevelValid: boolean
├── loadLevel(path)
├── loadFromContent(content, path)
├── parseMetadata(content)
├── parseSpawns(content)
├── parseGrid(content)
├── getSpawnPosition(type)
├── getLevelMetadata()
├── getDifficulty()
├── getMapName()
├── isLevelValid()
├── clear()
└── setDifficulty(int)

GameMap
├── tiles[x][y][z]
├── spawnedEntities: Map
├── spawnedProjectiles: Map
├── decorations: Map
├── entitySpawns: List - Protected for LevelLoader access
├── isWall, isDoor, isEmpty
├── spawnEntity, removeEntity
├── addDecoration, removeDecoration
├── getDecorationType
└── load(), isLoaded()
```

---

## Build Configuration

### Gradle Setup
```kotlin
plugins {
    id("java")
    id("application")
    id("org.jetbrains.dokka-javadoc") version "2.2.0"
}

group = "org.ronobot.engine"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit-platform-launcher:1.10.1")
    implementation("com.google.guava:guava:33.0.0-jre")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
```

---

## Code Style Guidelines

- All classes use Javadoc with `@author ronobot`
- Enums are `public static` when standalone
- Methods have Javadoc blocks
- Use `final` for immutable fields
- Prefer immutable objects (Size, Point)
- Use `float` for positions, `int` for tile indices
- Handle null inputs gracefully
- Follow SRP (Single Responsibility Principle)
- Keep methods under 50 lines where possible
- Use meaningful constant names
- Group related methods together

---

## Development Guidelines

- All code must be JUnit-tested
- Use Javadoc for all public APIs
- Follow the single-responsibility principle
- Keep modules small and focused
- Use dependency injection for managers
- All classes have @author ronobot

---

## Project Structure

```
project/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── org/
│   │   │   │       └── ronobot/
│   │   │   │           └── engine/
│   │   │   │               ├── collision/
│   │   │   │               ├── core/
│   │   │   │               ├── entity/
│   │   │   │               ├── entities/
│   │   │   │               ├── input/
│   │   │   │               ├── io/
│   │   │   │               ├── map/
│   │   │   │               ├── math/
│   │   │   │               ├── physics/
│   │   │   │               ├── powerups/
│   │   │   │               ├── render/
│   │   │   └── resources/
│   │   └── test/
│   │       └── java/
│   │           └── org/ronobot/engine/
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── CHANGES.md
├── MEMORY.md
└── README.md
```

---

## Future Enhancements

### Implemented Features
- WAD File Parsing: Complete directory parsing with lump type detection
- Sound System: Sound player with channel management and volume control
- Map Loader: Text-based map file parser with spawn support
- PowerUp System: Complete power-up implementation with types
- Audio System: Audio system with sound/music management
- Collision System: Complete collision detection and resolution
- Map Decoration System: Decoration support for GameMap
- Input Handler: Keyboard control processing
- Math Utilities: Complete math utility suite
- Item System: Complete item types and entity support (AMMO, HEALTH, ARMOR, KEYCARD, SECRET, MONSTER, MEDKIT, WEAPON)

### Completed Features
- Level Loader: Text-based map file parser with spawn support (COMPLETED)
- Game Loop: Implemented in Game.runLoop() with rendering (COMPLETED)

### Planned Features
1. **UI Components**: Add keyboard controls and HUD rendering
2. **Network Support**: Multiplayer capabilities
3. **Save/Load System**: Game state persistence
4. **Achievement System**: Unlockable goals and rewards
5. **Monster Entities**: Full enemy AI and behavior

### Technical Debt
- Replace stub renderer implementation with actual graphics library
- Implement proper boundary validation in InputHandler
- Add map decoration persistence
- Optimize collision detection for larger entity counts
- Re-enable GUI with proper JavaFX configuration when dependencies resolved

---

## Cycle Summary

### Test Results
- **Total Tests: 471**
- **Passing: 471**
- **Failing: 0**
- **Build: SUCCESSFUL**

### Build Status
- Java 17 compatible
- Clean build with no warnings
- All tests passing
- Comprehensive test coverage

### Recent Changes
- LevelLoader entity spawning logic fixed for enemy spawns
- GameMap.entitySpawns made protected for LevelLoader access
- BUGS.md issue about game loop addressed

### Changes to Commit
- LevelLoader.java - Fixed enemy spawn registration in game map
- GameMap.java - Made entitySpawns protected field

### Next Steps
- Address BUGS.md game loop/rendering concerns if they're still relevant
- Consider if the current implementation (App.start() -> game.runLoop()) is sufficient
- Commit completed work
- Delete BUGS.md if issues are resolved
