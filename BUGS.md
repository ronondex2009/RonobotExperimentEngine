# BUGS.md - Compilation Issues

## Cycle 39 (2026-06-10)

### Build Status: BUILD FAILED - Type Mismatch Errors

### Errors to Fix:
1. **GameMap.java:676** - getEntitySpawns() returns wrong type
2. **GameMap.java:949** - getDecorations() - type mismatch DecorationType vs MapDecoration
3. **MapFileParser.java:202** - EntitySpawn constructor expects 3 args, getting 4
4. **MapEditor.java:271** - EntitySpawn constructor expects (String, float, float), getting wrong types
5. **LevelLoader.java:263** - Type mismatch MapFileParser.EntitySpawn vs EntitySpawn
6. **EntityManager.java:210** - Projectile class not found, need to import
7. **Renderer.java** - Cannot find Game symbol (uses Game class reference)
8. **App.java, PhysicsEngine.java** - Cannot find Game symbol
9. **EnemyEntity.java** - Cannot find Game class
10. **GameRenderer.java** - Cannot find Game class

### Type System Issue:
The project uses two different EntitySpawn classes:
- `org.ronobot.engine.map.EntitySpawn` - the proper entity spawn class
- `org.ronobot.engine.map.MapFileParser.EntitySpawn` - an inner class in MapFileParser

Need to unify types or create adapter methods.
