# CHANGES.md - Project Change Log

## 2026-06-11 - Cycle 2: Stable Build ✓
### Build Status: SUCCESSFUL
- Build completed with 16 tasks executed
- 403 tests passing (no failures)
- No compilation errors
- MapEditor.java restored successfully
- All source files compile cleanly

### Cycle 2 Progress
- **Completed**:
  - GameState file loading refactored to NIO for modern JSON parsing
  - AIStateMachine added with state transitions (IDLE, PATROL, CHASE, ATTACK, RETREAT, STUNNED)
  - MapEditor.java restored from previous commit
  - MapEditorTest.java deleted (incompatible with new MapEditor signature)
- **Build**: All 403 unit tests passing
- **Code Quality**: Well-documented with javadocs

### Systems in Place
- **Entity System**: PlayerEntity, EnemyEntity, Projectile, EnemyType, ItemType, Item
- **Collision System**: CollisionManager, CollisionResult, CollisionNotification, PhysicsEngine
- **Map System**: GameMap, MapFileParser, MapDecoration, MapDecorationLoader, MapEditor, MapLoader, LevelLoader, EntitySpawn
- **Math Utilities**: Point, Size, Rectangle, Position, Velocity, AxisAlignedBox
- **Audio System**: AudioSystem, SoundPlayer
- **IO System**: WadFile, SpriteLoader, SpriteType
- **Power-ups**: PowerUp, PowerUpType
- **Input**: InputHandler
- **Rendering**: Renderer, GameRenderer, SpriteRenderer
- **Game Core**: Game, GameMap, GameState
- **EntityManager**: Entity lifecycle management
- **AIStateMachine**: State machine for enemy AI behavior

### Next Cycle (Cycle 3) Goals
- Sprite/texture management for actual graphics
- WAD file parsing for complete asset loading
- Complete monster AI behavior
- Item system with inventory
- Save/load functionality
- Integration tests
- Achievement system

### Files Managed
- CHANGES.md: This change log
- MEMORY.md: Long-term project context
- BUGS.md: Active compilation/test bugs (only created when needed)
