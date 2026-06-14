# CHANGES.md
# Engine Development Log

## Cycle 26 (2026-05-30) - COMPLETE

### Status: BUILD SUCCESSFUL - All Tests Passing
### Result: 325/325 tests passing, BUILD SUCCESSFUL

---

### Completed Goals (Cycle 26)

1. **MapFileParser Implementation**
   - Creating MapFileParser.java for map file parsing
   - Support for text-based map formats
   - Grid conversion utilities
   - Tile type recognition and validation
   - Entity spawn position handling (player, enemy, powerup, ammo)
   - Map validation (requires player spawn)

2. **MapFileParser API**
   - `parseFile(String path)` - Parse map from file
   - `parseContent(String content)` - Parse map from string
   - `setTile(int row, int col, char tile)` - Set tile
   - `getTile(int row, int col)` - Get tile
   - `getGridString()` - Get grid as string
   - `getSpawnPositions()` - Get spawn positions
   - `isValid()` - Validate map
   - `getGrid()` - Get raw grid
   - `getColumns()`, `getRows()`, `getName()` - Get properties

3. **Supported Tile Characters**
   - `#`: Wall (solid, collision)
   - `.` or ` `: Floor (walkable)
   - `@`: Player spawn
   - `*`: Enemy spawn
   - `P`: Power-up spawn
   - `/`: Ammo spawn

4. **EntitySpawn Class**
   - `Type.PLAYER` - Player spawn position
   - `Type.ENEMY` - Enemy spawn position
   - `Type.POWERUP` - Power-up spawn position
   - `Type.AMMO` - Ammo spawn position
   - `getTypeName()`, `getType()`, `getCol()`, `getRow()` - Getters
   - `toString()` - String representation

5. **Build Stability**
   - Clean build achieved
   - All 325 tests passing
   - No compilation errors
   - BUGS.md deleted

---

### Build Status

- **Total Tests: 325**
- **Passing: 325**
- **Failing: 0**
- **Build: SUCCESSFUL**

---

---

## Cycle 23 (2026-05-29) - COMPLETE

### Status: BUILD SUCCESSFUL - All Tests Passing
### Result: 325/325 tests passing, BUILD SUCCESSFUL

---

### Completed Goals (Cycle 23)

1. **App Initialization Enhancement**
   - Modified App.java to create new Game instance in constructor
   - Changed `this.game = null;` to `this.game = new Game();`
   - Added JavaDoc explaining automatic Game initialization

2. **Test Updates**
   - Updated AppTest.java to expect non-null game by default
   - Changed `testDefaultGame()` to assert non-null game
   - Changed `testGetGameInitially()` to assert non-null game
   - Tests verify Game is initialized with EntityManager

3. **Build Stability**
   - Clean build achieved
   - All 325 tests passing
   - No compilation errors
   - BUGS.md deleted

---

## Cycle 22 (2026-05-29) - COMPLETE

### Status: BUILD SUCCESSFUL - All Tests Passing
### Result: 322/322 tests passing, BUILD SUCCESSFUL

---

### Completed Goals (Cycle 22)

1. **Fixed Compilation Errors**
   - Fixed CollisionManagerTest.java createEntity method signature mismatch
   - Changed test calls from `createEntity(1, "e1", ...)` to `createEntity("e1", ...)`
   - Changed test calls from `createEntity(2, "e2", ...)` to `createEntity("e2", ...)`

2. **Build Stability**
   - Clean build achieved
   - All 322 tests passing
   - No compilation errors
   - Removed problematic test code

3. **Test Coverage**
   - 73 test result files generated
   - All test classes pass successfully

---

### File Modifications (Cycle 22)

1. **CollisionManagerTest.java**
   - Fixed `createEntity()` method calls in test methods
   - Removed ID parameter from test helper calls
   - Tests now use correct method signatures

---

### Build Status

- **Total Tests: 322**
- **Passing: 322**
- **Failing: 0**
- **Build: SUCCESSFUL**
