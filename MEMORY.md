# MEMORY.md
# Engine Development Log

## Current State - Cycle 28 COMPLETE

### Build Status: BUILD SUCCESSFUL - 428/428 Tests Passing
### Status: Core engine built with map decoration support

---

## Cycle 28 Complete (2026-06-03)

### Goals for This Cycle
1. **Map Decoration System**
   - Created MapDecoration.java for decorative elements
   - Created MapDecorationLoader.java for decoration management
   - Extended GameMap.java with decoration support
   - All classes well-documented with Javadoc
   - Unit tests written for MapDecoration

2. **Test Failures Fixed**
   - Fixed GameRenderer constructor handling for null maps
   - Fixed test cases for renderDecorations with player/enemy entities
   - Fixed projectile texture key generation in tests
   - All 428 tests passing

3. **Build Achievements**
   - Clean build achieved
   - All 428 tests passing
   - Ready for decoration integration

---

## Architecture

### Decoration System
```
GameMap
├── decorations: Map<Position, DecorationType>
├── DecorationType enum (NONE, STATUE, PICTURE, TABLE, CHEST, CRATE, FLAG, FOUNTAIN)
├── addDecoration(x, y, "STATUE") - Add by name
├── addDecoration(x, y, type) - Add by enum
├── getDecorationType(x, y) - Get decoration
├── removeDecoration(x, y) - Remove decoration
├── getDecorationPositions() - Get all positions
├── clearDecorations() - Clear all
└── parseDecorationType(name) - Parse string to enum

MapDecoration
├── row, col, type, name, visual, priority
├── MapDecorationType enum (WALL, FLOOR, DOOR, STATUE, etc.)
└── toString(), isValid()

MapDecorationLoader
├── register(decoration) - Register
├── registerAll(decorations) - Batch register
├── loadFromDefinition() - Load from string
├── getMapDecorations(map) - Get decorations for map
├── getDecoration(name) - Get by name
├── getAllDecorations() - Get all
├── clearAll() - Clear all
├── hasDecoration(name) - Check existence
├── getDecorationCount() - Get count
└── splitLine(), parseLines()

GameRenderer
├── gameMap: GameMap
├── gameRendererTextures: Map<String, String>
├── renderMap() - Render tiles
├── renderDecorations() - Render decorations
├── renderEntities(game) - Render entities
├── renderPlayerEntity() - Render player
├── renderEnemyEntity() - Render enemy
├── renderProjectile() - Render projectile
└── clearTextures(), getTextureCount(), hasTexture()
```

### Map System
```
GameMap
├── tiles: int[][][] [x][y][z]
├── DecorationType enum
├── addWall(), addFloor(), addDoor()
├── addDecoration() - Multiple overloads
├── getDecorationType() - Multiple overloads
├── removeDecoration()
├── clearDecorations()
├── getDecorationPositions()
├── isWall(), isDoor(), isEmpty()
├── toWorldPosition(), toTilePosition()
├── isInBounds() - World and tile bounds
├── createArenaMap(), createRoomMap() - Factories
└── load(), isLoaded(), enable(), disable()
```

### Core Engine
```
Entity
├── id, name, position, size, velocity
├── health, maxHealth, armor, damageTaken
├── isActive(), isDead()
├── takeDamage(), heal()
├── die(), resurrect()
└── move(), update()

PlayerEntity
├── extends Entity
├── weapon, healthRegenRate, ammunition
├── getMaxAmmunition(), getAmmunition()
├── setAmmunition(), reload(), fire()
└── toString()

EnemyEntity
├── extends Entity
├── EnemyType type (ZOMBIE, DEMON, KNIGHT, IMP, BARON)
├── health, attackCooldown
├── target, patrol position
├── sound reactions
└── patrol behavior

Projectile
├── extends Entity
├── velocity, lifeTime
└── Moving projectile

EntityManager
├── entityMap: Map<String, Entity>
├── entityList: List<Entity>
├── addEntity(), removeEntity()
├── getEntity(), getEntityById(), getEntity(int)
├── getEntities(), getActiveEntities()
├── update()
└── clear()

CollisionManager
├── entities: Map<String, Entity>
├── notifications: Map<String, CollisionNotification>
├── registerEntity(entity) - Register entity
├── unregisterEntity(entity) - Unregister entity
├── isEntityRegistered(entity) - Check if registered
├── getEntityCount() - Entity count
├── findCollisions() - Find all collisions
├── findAndResolveCollisions(deltaTime) - Resolve
└── clear() - Clear all entities

PhysicsEngine
├── Collision resolution
├── Position adjustments
├── Velocity adjustments
└── Separation distance

Game
├── Game state management
├── Entity lifecycle
├── Collision detection
├── Map and player management
└── Game loop control
```

### Math Utilities
```
Point - Immutable 2D point
Position - Mutable 2D position
Size - Width/height dimensions
Velocity - Movement vector (dx, dy)
Rectangle - Static AABB box
AxisAlignedBox - Deprecated alias
```

### IO System
```
WadFile
├── WAD file parsing
├── Lump type detection
├── Directory parsing

SpriteLoader
├── Sprite sheet parsing
├── Texture caching
├── Frame extraction

SpriteType
├── Sprite types
├── Frame definitions
└── Animation support

AudioSystem
├── Audio management
├── Sound bank loading
└── Volume control

SoundPlayer
├── Sound playback
├── Sound effects
├── Volume control
└── Audio scheduling
```

---

## Build Configuration

### Gradle Setup
```kotlin
plugins {
    id("java")
    id("application")
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
│   │   │   │               ├── io/
│   │   │   │               ├── input/
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

### Planned Features
1. **Level Loader**: Create map file parser with format specification
2. **UI Components**: Add keyboard controls and HUD rendering
3. **Network Support**: Multiplayer capabilities
4. **Save/Load System**: Game state persistence
5. **Achievement System**: Unlockable goals and rewards
6. **Monster Entities**: Full enemy AI and behavior
7. **Item System**: Inventory and item management

### Technical Debt
- Replace stub renderer implementation with actual graphics library
- Implement proper boundary validation in InputHandler
- Add map decoration persistence
- Optimize collision detection for larger entity counts
- Re-enable GUI with proper JavaFX configuration when dependencies resolved

---

## Cycle Summary

### Test Results
- **Total Tests: 428**
- **Passing: 428**
- **Failing: 0**
- **Build: SUCCESSFUL**

### Build Status
- Java 17 compatible
- Clean build with no warnings
- All tests passing
- Comprehensive test coverage

### Recent Changes
- MapDecoration class created
- MapDecorationLoader class created
- GameMap extended with decoration support
- All renderer tests fixed
- 428 tests passing
- Build successful
- BUGS.md updated to document JavaFX dependency research needed
