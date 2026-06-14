# Ronobot Doom-like Engine

A Doom-inspired 2D game engine built in Java with Gradle and JUnit.

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

## Development Status

**Current Cycle:** 4 - Map Decoration System Enhancement

**Completed Components:**
- Collision detection system (CollisionManager)
- Physics engine for resolution (PhysicsEngine)
- Health management system
- Math utilities (Point, Size, Rectangle)
- Entity system (Entity, PlayerEntity, Projectile)
- Map system with decorations (GameMap, MapFileParser, MapDecoration)
- Audio system (AudioSystem, SoundPlayer)
- WAD file parsing (WadFile, SpriteLoader)
- Power-up system (PowerUp, PowerUpType)
- Map decoration system (MapDecoration, MapDecorationLoader)
- Comprehensive unit test suite

**Pending Components:**
- Map rendering and parsing (stub)
- Sprite/texture management (stub)
- GUI integration (JavaFX disabled temporarily)
- Monster entities
- Item system
- Save/load functionality
- Integration tests
- Achievements system

## Building

```bash
cd ~/.openclaw/workspace/project
./gradlew build
```

## Testing

```bash
./gradlew test
```

## Running Tests

```bash
./gradlew test --info
```

## Architecture Overview

### Entity System
- `Entity`: Base class with position, size, collision box
- `PlayerEntity`: Player with health, armor, inventory, weapons
- `Projectile`: Moving projectile entity
- `EnemyEntity`: Enemy entities with patrol behavior
- `EnemyType`: Enemy type definitions

### Collision System
- `CollisionManager`: Detects overlapping entities
- `CollisionResult`: Represents a collision pair
- `PhysicsEngine`: Resolves collisions and damage

### Map System
- `GameMap`: Tile-based map with entity spawning and decorations
- `MapFileParser`: Parses map files into GameMap instances
- `MapDecoration`: Decorative elements for map enhancement
- `MapDecorationLoader`: Loads and registers decorations
- `MapLoader`: Loaders for different map formats

### Math Utilities
- `Point`: 2D coordinate
- `Size`: Dimensions
- `Rectangle`: Bounding box
- `Position`: Mutable position
- `Velocity`: Movement vector

### Physics
- `PhysicsEngine`: Collision resolution, damage, separation

### Audio System
- `AudioSystem`: Audio management
- `SoundPlayer`: Sound playback and effects

### IO System
- `WadFile`: WAD file parsing
- `SpriteLoader`: Sprite sheet parsing
- `SpriteType`: Sprite type definitions

### Input
- `InputHandler`: Keyboard controls and input processing

## TODO List

1. Implement full collision resolution logic
2. Write tests for Map and Renderer
3. Add sprite and texture management
4. Implement WAD file parsing
5. Add sound/audio system
6. Implement monster entities
7. Add item system
8. Improve rendering
9. Add save/load functionality
10. Write integration tests

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

## Development Guidelines

- All code must be JUnit-tested
- Use Javadoc for all public APIs
- Follow the single-responsibility principle
- Keep modules small and focused
- Use dependency injection for managers
- All classes have @author ronobot

## License

Internal project - not for distribution.
