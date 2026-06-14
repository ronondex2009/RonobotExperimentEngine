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

**Current Cycle:** 1 - COMPLETE

**Build Status:** SUCCESSFUL - 403 tests passing

**Completed Components:**
- Collision detection system (CollisionManager)
- Physics engine for resolution (PhysicsEngine)
- Health management system
- Math utilities (Point, Size, Rectangle, Position, Velocity)
- Entity system (Entity, PlayerEntity, Projectile, EnemyEntity)
- Map system with decorations (GameMap, MapFileParser, MapDecoration, MapDecorationLoader)
- Audio system (AudioSystem, SoundPlayer)
- WAD file parsing (WadFile, SpriteLoader)
- Power-up system (PowerUp, PowerUpType)
- Map decoration system (MapDecoration, MapDecorationLoader)
- Input handler (InputHandler)
- Renderer (Renderer, GameRenderer, SpriteRenderer)
- Game core (Game, GameMap, GameState)
- EntityManager and entity lifecycle management

**Pending Components:**
- Map rendering and GUI integration (JavaFX - dependency issue pending)
- Sprite/texture management for actual graphics
- Monster entity AI behavior
- Item system and inventory management
- Save/load functionality
- Level loading from files
- Network support for multiplayer
- Achievement system
- Integration tests
- Fix GameState.java deprecated API usage

## Building

```bash
cd ~/.openclaw/workspace/project
./gradlew build
```

## Testing

```bash
./gradlew test
```

## Architecture Overview

### Entity System
- `Entity`: Base class with position, size, collision box, health, armor
- `PlayerEntity`: Player with health, armor, inventory, weapons, ammo
- `Projectile`: Moving projectile entity with velocity and lifetime
- `EnemyEntity`: Enemy entities with patrol behavior and target tracking
- `EnemyType`: Enemy type definitions (ZOMBIE, DEMON, KNIGHT, IMP, BARON)

### Collision System
- `CollisionManager`: Detects overlapping entities, registers/unregisters
- `CollisionResult`: Represents a collision pair
- `CollisionNotification`: Collision event notifications
- `PhysicsEngine`: Resolves collisions, damage, and separation

### Map System
- `GameMap`: Tile-based map with entity spawning, decorations, bounds checking
- `MapFileParser`: Parses map files into GameMap instances
- `MapDecoration`: Decorative elements (statues, pictures, tables, etc.)
- `MapDecorationLoader`: Loads and registers decoration definitions
- `MapLoader`: Loaders for different map formats

### Math Utilities
- `Point`: Immutable 2D coordinate
- `Size`: Width/height dimensions
- `Rectangle`: Static bounding box
- `Position`: Mutable position
- `Velocity`: Movement vector (dx, dy)

### Physics
- `PhysicsEngine`: Collision resolution, damage calculation, entity separation

### Audio System
- `AudioSystem`: Audio management and sound bank loading
- `SoundPlayer`: Sound playback with channel management and volume control

### IO System
- `WadFile`: WAD file parsing with lump type detection
- `SpriteLoader`: Sprite sheet parsing with texture caching
- `SpriteType`: Sprite type definitions for animation support

### Input
- `InputHandler`: Keyboard controls (WASD/Arrows), action triggers, boundary checking

### Renderer
- `Renderer`: Base renderer interface with texture management
- `GameRenderer`: Game-specific rendering with map tiles, entities, decorations

### Game Core
- `Game`: Game state management, entity lifecycle, collision detection
- `App`: Main application entry point

## TODO List

1. Implement full collision resolution with sliding
2. Write integration tests for end-to-end game flow
3. Add sprite and texture management for actual graphics rendering
4. Implement WAD file parsing for complete asset loading
5. Add sound/audio system with multiple channels
6. Implement complete monster entity AI behavior
7. Add item system with inventory management
8. Improve renderer with actual graphics library (JavaFX pending)
9. Add save/load functionality for game state persistence
10. Add network support for multiplayer capabilities
11. Write comprehensive integration tests
12. Implement achievement system
13. **Fix GameState.java deprecated API usage** (cycle 2 priority)

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

## Memory Files

- `CHANGES.md`: Track changes in this build cycle
- `MEMORY.md`: Long-term project context and decisions
- `BUGS.md`: Active compilation/test bugs (created when needed)

## License

Internal project - not for distribution.
