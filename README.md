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
│   │   │   │               │   ├── CollisionManager.java
│   │   │   │               │   └── CollisionResult.java
│   │   │   │               ├── entity/
│   │   │   │               │   ├── Entity.java
│   │   │   │               │   ├── PlayerEntity.java
│   │   │   │               │   └── Projectile.java
│   │   │   │               ├── math/
│   │   │   │               │   ├── Point.java
│   │   │   │               │   ├── Size.java
│   │   │   │               │   ├── Rectangle.java
│   │   │   │               │   └── AxisAlignedBox.java
│   │   │   │               ├── physics/
│   │   │   │               │   └── PhysicsEngine.java
│   │   │   │               └── (future) map/, renderer/, resources/
│   │   │   └── resources/
│   │   └── test/
│   │       └── java/
│   │           └── org/ronobot/engine/
│   │               └── (test classes for each package)
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── CHANGES.md
├── MEMORY.md
└── README.md
```

## Development Status

**Current Cycle:** 4 - Collision Resolution Implementation

**Completed Components:**
- Collision detection system (CollisionManager)
- Physics engine for resolution (PhysicsEngine)
- Health management system
- Math utilities (Point, Size, Rectangle)
- Entity system (Entity, PlayerEntity, Projectile)
- Comprehensive unit test suite

**Pending Components:**
- Map rendering and parsing
- Sprite/texture management
- WAD file parsing
- Sound/audio system
- Monster entities
- Item system
- Save/load functionality
- Integration tests

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

## Development Guidelines

- All code must be JUnit-tested
- Use Javadoc for all public APIs
- Follow the single-responsibility principle
- Keep modules small and focused
- Use dependency injection for managers
- All classes have @author ronobot

## Architecture Overview

### Entity System
- `Entity`: Base class with position, size, collision box
- `PlayerEntity`: Player with health, armor, inventory, weapons
- `Projectile`: Moving projectile entity

### Collision System
- `CollisionManager`: Detects overlapping entities
- `CollisionResult`: Represents a collision pair
- `PhysicsEngine`: Resolves collisions

### Math Utilities
- `Point`: 2D coordinate
- `Size`: Dimensions
- `Rectangle`: Bounding box

### Physics
- `PhysicsEngine`: Collision resolution, damage, separation

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

## License

Internal project - not for distribution.
