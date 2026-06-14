# MEMORY.md
# Engine Development Log

## Current State - Cycle 30

### Build Status: BUILD SUCCESSFUL - 428/428 Tests Passing

---

## Cycle 30 In Progress (2026-06-03)

### Goals for This Cycle
1. **Item System Implementation**
   - Create Item.java entity class for inventory items
   - Create ItemType.java enum for item categories
   - Implement pickup/drop mechanics
   - Support ammo, health, armor, keycards, secrets, monsters, medkits, weapons
   - Add comprehensive unit tests

2. **Item Types**
   - AMMO: Weapon ammunition
   - HEALTH: Health restoration
   - ARMOR: Armor protection
   - KEYCARD: Door access keys
   - SECRET: Achievement unlocks
   - MONSTER: Enemy spawns
   - MEDKIT: Emergency medical
   - WEAPON: Weapon pickups

---

## Architecture

### Item System
```
Item
├── id, name, position, size
├── type: ItemType
├── quantity: int
├── held: boolean
├── isHeld(), setHeld()
├── hold(), release()
├── addQuantity(), removeQuantity()
├── clear()
├── getDisplayName(), getIcon()
├── isUsable(), getDescription()
├── update() - Lifecycle
├── getCategory()
└── Static factory methods:
    ├── createAmmo()
    ├── createHealth()
    ├── createArmor()
    ├── createKeycard()
    ├── createSecret()
    ├── createMonster()
    ├── createMedkit()
    └── createWeapon()

ItemType
├── AMMO, HEALTH, ARMOR
├── KEYCARD, SECRET, MONSTER
├── MEDKIT, WEAPON
├── displayName, icon, category
├── description
├── getName(), getIcon(), getCategory()
└── getDescription()
```

### Core Engine
```
Entity
├── id, name, position, size
├── health, maxHealth, velocity
├── isActive(), isDead()
├── takeDamage(), heal()
├── die(), resurrect()
└── move(), update()

PlayerEntity
├── extends Entity
├── weapon, healthRegenRate
├── ammunition
├── reload(), fire()

EnemyEntity
├── extends Entity
├── EnemyType type
├── health, attackCooldown
├── target, patrol
└── Sound reactions

Projectile
├── extends Entity
├── velocity, lifeTime
└── Moving projectile
```

### Map System
```
GameMap
├── tiles[x][y][z] - Tile grid
├── decorations: Map<Position, DecorationType>
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

MapDecoration
├── row, col, type, name
├── visual, priority
├── MapDecorationType enum
├── isValid()
└── toString()

MapDecorationLoader
├── register(decoration) - Register
├── registerAll(decorations) - Batch register
├── loadFromDefinition() - Load from string
├── getMapDecorations(map) - Get decorations
├── getDecoration(name) - Get by name
├── getAllDecorations() - Get all
├── clearAll() - Clear all
└── hasDecoration(name), getDecorationCount()

MapFileParser
├── parseFile(path) - Parse from file
├── parseContent(content) - Parse from string
├── getGrid() - Get tile grid
├── getSpawnPositions() - Get spawns
└── isValid() - Validate

LevelLoader
├── loadLevel(path) - Load from file
├── loadFromContent(content, path) - Load from string
├── getLevelMetadata() - Get metadata map
├── getDifficulty() - Get difficulty
├── setDifficulty(difficulty) - Set difficulty
├── getMapName() - Get map name
├── isLevelValid() - Validate level
├── clear() - Clear state
└── getSpawnPosition(type) - Get spawn
    └── registerSpawn(type, spawn) - Register spawn
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
- Item System: Complete item types and entity support

### Planned Features
1. **Level Loader**: Create map file parser with format specification
2. **UI Components**: Add keyboard controls and HUD rendering
3. **Network Support**: Multiplayer capabilities
4. **Save/Load System**: Game state persistence
5. **Achievement System**: Unlockable goals and rewards
6. **Monster Entities**: Full enemy AI and behavior
7. **Item System**: Inventory and item management (in progress)

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
- Item.java created
- ItemType.java created
- All tests passing
- Build successful
- Ready for git commit
