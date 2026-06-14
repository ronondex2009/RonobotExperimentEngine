# Architecture Documentation

## Project Overview

**org.ronobot.engine** is a DOOM-like game engine built in Java 17 using Gradle with Kotlin DSL for build configuration. The engine provides a foundation for 2D action games with tile-based maps, entity management, collision detection, physics, and rendering.

## Directory Structure

```
app/src/main/java/org/ronobot/engine/
├── App.java                           # Application entry point
├── core/
│   ├── Entity.java                   # Base entity class
│   ├── Game.java                     # Game state management
│   └── GameException.java            # Custom exception
├── collision/
│   ├── CollisionManager.java         # AABB collision detection
│   ├── CollisionResult.java          # Collision pair data
│   └── AxisAlignedBox.java           # Deprecated alias
├── entity/
│   ├── PlayerEntity.java             # Player entity with health/armor
│   └── Projectile.java               # Moving projectile entity
├── input/
│   └── InputHandler.java             # Keyboard input processing
├── map/
│   ├── GameMap.java                  # Tile-based map system
│   ├── GameMapTest.java              # Map system tests
│   ├── MapLoader.java                # Map file parsing
│   └── MapFileParser.java            # Simple map parser (NEW)
├── math/
│   ├── Point.java                    # 2D coordinate
│   ├── Position.java                 # Mutable position
│   ├── Size.java                     # Width/height dimensions
│   ├── Velocity.java                 # Movement vector
│   ├── Rectangle.java                # AABB collision box
│   └── AxisAlignedBox.java           # Deprecated alias
├── physics/
│   └── PhysicsEngine.java            # Collision resolution
├── render/
│   ├── Renderer.java                 # Rendering interface
│   └── RendererTest.java             # Rendering tests
├── entities/
│   └── EntityManager.java            # Entity lifecycle management
└── utils/                            # Utility classes

app/src/test/java/org/ronobot/engine/
├── AppTest.java
├── EntityTest.java
├── PlayerEntityTest.java
├── ProjectileTest.java
├── CollisionManagerTest.java
├── CollisionResultTest.java
├── RectangleTest.java
├── AxisAlignedBoxTest.java
├── core/
│   ├── GameTest.java
│   └── GameExceptionTest.java
├── entity/
│   ├── PlayerEntityTest.java
│   └── ProjectileTest.java
├── input/
│   └── InputHandlerTest.java
├── map/
│   ├── GameMapTest.java
│   └── MapFileParserTest.java        # Map parser tests (NEW)
├── math/
│   ├── PositionTest.java
│   ├── SizeTest.java
│   ├── VelocityTest.java
│   ├── RectangleTest.java
│   └── AxisAlignedBoxTest.java
├── physics/
│   └── PhysicsEngineTest.java
├── render/
│   └── RendererTest.java
├── core/
│   └── GameIntegrationTest.java      # Integration tests (NEW)
└── utils/                           # Utility tests
```

## Component Architecture

### Core Components

#### Game (core/Game.java)
The central game state container managing:
- Game loop state (running/ended)
- Entity lifecycle
- Collision detection
- Map and player management

#### Entity (core/Entity.java)
Base class for all game objects with:
- Position (float x, y)
- Size (width, height)
- Velocity (optional)
- Lifecycle methods (spawn, active, die)
- Health/armor properties

#### Renderer (render/Renderer.java)
Handles rendering of:
- Map tiles
- Entities
- Projectiles
- Texture management with HashMap-based caching

#### InputHandler (input/InputHandler.java)
Processes keyboard input:
- WASD/Arrow key movement
- Action triggers (space bar)
- Boundary checking
- Float-precision position updates

#### GameMap (map/GameMap.java)
Tile-based map system:
- Grid storage (2D array)
- Tile types (wall, floor, empty)
- Entity bounds enforcement
- Collision callbacks
- Spawn position tracking

#### CollisionManager (collision/CollisionManager.java)
AABB collision detection:
- Entity registration/unregistration
- Collision pair detection
- Collision result storage

#### PhysicsEngine (physics/PhysicsEngine.java)
Collision resolution:
- Position adjustments
- Velocity adjustments
- Separation distance calculation

### Math Utilities

All mathematical operations use a consistent API:

- **Position**: Mutable 2D position with get/set methods
- **Size**: Immutable width/height dimensions
- **Velocity**: Movement vector (dx, dy)
- **Rectangle**: Static AABB with immutable properties
- **Point**: Immutable 2D point coordinate

### Map System

Maps are represented as grids where each character represents a tile:
- `#`: Wall (solid, collision)
- `.`: Floor (walkable)
- ` `: Empty floor
- `@`: Player spawn position

Maps can be:
1. Loaded from files (future WAD support)
2. Created from raw grid data
3. Created programmatically

## Design Decisions

### Float vs Double for Positions

**Decision**: Use `float` for all position coordinates.

**Reasoning**:
- Matches Entity API which returns `float` from `getPosition()`
- InputHandler uses float for dx/dy calculations
- Consistent with Java game development practices
- Sufficient precision for game positions

### Texture Cache Design

**Implementation**: `HashMap<String, String>` textures
- Keys: Generated per entity/projectile (e.g., "entity_player1")
- Values: Texture paths or default names

**Methods**:
- `loadTexture(name, path)` - Caches texture
- `getTexture(name)` - Retrieves texture or null
- `hasTexture(name)` - Boolean check
- `clearTextures()` - Clear cache
- `getTextureCount()` - Count loaded textures

### Input Handling Design

**Two-level processing**:
1. `handle(PlayerEntity)` - Direct player movement
2. `handle(Game)` - Game-level, delegates to player

**Benefits**:
- Allows both direct and indirect input handling
- Supports null game gracefully
- Clean separation of concerns

### Collision Detection

Uses AABB (Axis-Aligned Bounding Box) collision:
- Efficient O(n²) detection for entity pairs
- Rectangle intersection testing
- Returns CollisionResult pairs for overlapping entities

### Test Coverage Goals

- **Core entities**: ~90%
- **Collision**: ~95%
- **Map system**: ~85%
- **Renderer**: ~85%
- **InputHandler**: ~90%
- **Game**: ~90%
- **Overall target**: 85%+

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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
```

### Run Tests

```bash
./gradlew test
./gradlew clean build
```

## API Usage Examples

### Creating a Game

```java
Game game = new Game();
game.start();

// Create player
Position spawn = new Position(10f, 10f);
Size playerSize = new Size(2f, 2f);
PlayerEntity player = new PlayerEntity(spawn, playerSize, 100);

// Set player in game
game.setPlayer(player);
game.getEntityManager().registerEntity(player);
game.registerEntity(player);
```

### Rendering

```java
Renderer renderer = new Renderer();
renderer.render(game);
```

### Input Handling

```java
InputHandler input = new InputHandler();

// Set movement keys
input.setMovingRight(true);
input.setMovingUp(true);
input.setActionPressed(true);

// Process input
input.handle(game);
```

### Collision Detection

```java
List<CollisionResult> collisions = game.detectCollisions();
for (CollisionResult result : collisions) {
    System.out.println("Collision: " + result.getEntity1().getId() + 
                        " with " + result.getEntity2().getId());
}
```

### Map Loading

```java
// Parse map from file
GameMap map = MapFileParser.parseFile("maps/level1.map");

// Or create map programmatically
GameMap map = MapFileParser.createMap(50, 20, 50, 100);
map.setTile(0, 0, GameMap.TILE_WALL);
map.setTile(1, 0, GameMap.TILE_WALL);
// ... etc
```

## Future Enhancements

### Planned Features

1. **WAD File Parsing**: Parse DOOM WAD files for sprite and music loading
2. **Sound System**: Audio effects and music management
3. **Level Loader**: Create map file parser with format specification
4. **UI Components**: Add keyboard controls and HUD rendering
5. **Network Support**: Multiplayer capabilities
6. **Save/Load System**: Game state persistence
7. **Achievement System**: Unlockable goals and rewards

### Technical Debt

- Replace stub renderer implementation with actual graphics library
- Implement proper boundary validation in InputHandler
- Add map decoration system
- Optimize collision detection for larger entity counts

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

## License

This project is maintained by ronobot.
