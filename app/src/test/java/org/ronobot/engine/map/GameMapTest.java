package org.ronobot.engine.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ronobot.engine.entity.PlayerEntity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the GameMap class.
 *
 * @author ronobot
 * @since 1.0
 */
@DisplayName("GameMap Tests")
class GameMapTest {

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("creates GameMap with dimensions")
        void testCreation() {
            GameMap map = new GameMap(10, 10);
            assertEquals(10, map.getWidth());
            assertEquals(10, map.getHeight());
            assertTrue(map.isInBounds(0, 0));
            assertTrue(map.isInBounds(9, 9));
            assertFalse(map.isInBounds(10, 0));
            assertFalse(map.isInBounds(0, 10));
        }

        @Test
        @DisplayName("creates arena GameMap with perimeter walls")
        void testArenaCreation() {
            GameMap map = GameMap.createArenaMap(10, 10);
            
            // Top and bottom walls
            for (int x = 0; x < 10; x++) {
                assertTrue(map.isWall(x, 0));
                assertTrue(map.isWall(x, 9));
            }
            
            // Left and right walls
            for (int y = 0; y < 10; y++) {
                assertTrue(map.isWall(0, y));
                assertTrue(map.isWall(9, y));
            }
        }

        @Test
        @DisplayName("creates room GameMap")
        void testRoomMapCreation() {
            GameMap map = GameMap.createRoomMap(20, 16, List.of(new int[]{2, 2, 4, 4}));
            
            // Room should be floor
            for (int x = 2; x < 6; x++) {
                for (int y = 2; y < 6; y++) {
                    assertEquals(GameMap.TILE_FLOOR, map.getTile(x, y));
                }
            }
        }
    }

    @Nested
    @DisplayName("Tile Operations")
    class TileTests {

        @Test
        @DisplayName("gets correct tile value")
        void testGetTile() {
            GameMap map = new GameMap(10, 10);
            map.setTile(5, 5, GameMap.TILE_WALL);
            assertEquals(GameMap.TILE_WALL, map.getTile(5, 5));
            assertEquals(GameMap.TILE_FLOOR, map.getTile(0, 0));
        }

        @Test
        @DisplayName("sets tile and returns change status")
        void testSetTile() {
            GameMap map = new GameMap(10, 10);
            assertTrue(map.setTile(5, 5, GameMap.TILE_WALL));
            assertEquals(GameMap.TILE_WALL, map.getTile(5, 5));
            
            // Setting same value returns false
            assertFalse(map.setTile(5, 5, GameMap.TILE_WALL));
        }

        @Test
        @DisplayName("returns false for out of bounds tiles")
        void testOutOfBoundsTiles() {
            GameMap map = new GameMap(10, 10);
            assertFalse(map.setTile(20, 0, GameMap.TILE_WALL));
            assertEquals(GameMap.TILE_EMPTY, map.getTile(20, 0));
        }

        @Test
        @DisplayName("adds wall at position")
        void testAddWall() {
            GameMap map = new GameMap(10, 10);
            map.addWall(5, 5);
            assertTrue(map.isWall(5, 5));
            assertEquals(GameMap.TILE_WALL, map.getTile(5, 5));
        }

        @Test
        @DisplayName("adds floor at position")
        void testAddFloor() {
            GameMap map = new GameMap(10, 10);
            map.addFloor(5, 5);
            assertEquals(GameMap.TILE_FLOOR, map.getTile(5, 5));
        }

        @Test
        @DisplayName("adds door at position")
        void testAddDoor() {
            GameMap map = new GameMap(10, 10);
            map.addDoor(5, 5);
            assertTrue(map.isDoor(5, 5));
            assertEquals(GameMap.TILE_DOOR, map.getTile(5, 5));
        }
    }

    @Nested
    @DisplayName("Position Conversions")
    class PositionTests {

        @Test
        @DisplayName("converts tile to world position")
        void testTileToWorldPosition() {
            GameMap map = new GameMap(10, 10);
            Position pos = map.toWorldPosition(0, 0);
            assertEquals(0, pos.getX(), 0.0f);
            assertEquals(0, pos.getY(), 0.0f);

            Position pos2 = map.toWorldPosition(1, 1);
            assertEquals(GameMap.getTileWidth(), pos2.getX(), 0.0f);
            assertEquals(GameMap.getTileHeight(), pos2.getY(), 0.0f);
        }

        @Test
        @DisplayName("converts world position to tile")
        void testWorldToTilePosition() {
            GameMap map = new GameMap(10, 10);
            // World position (32, 32) is at center of tile (1, 1)
            Position pos = map.toTilePosition(32, 32);
            assertNotNull(pos);
            assertEquals(1, pos.getX());
            assertEquals(1, pos.getY());
        }

        @Test
        @DisplayName("returns null for out of bounds world position")
        void testOutOfBoundsWorldPosition() {
            GameMap map = new GameMap(10, 10);
            Position pos = map.toTilePosition(1000, 1000);
            assertNull(pos);
        }
    }

    @Nested
    @DisplayName("Spawn Management")
    class SpawnTests {

        @Test
        @DisplayName("spawns entity at position")
        void testSpawnEntity() {
            GameMap map = new GameMap(10, 10);
            Position pos = new Position(5f, 5f);
            Size size = new Size(16, 16);
            PlayerEntity player = new PlayerEntity(0, pos.getX(), pos.getY(), 16, 16);
            assertTrue(map.spawnEntity(5, 5, player));
            
            org.ronobot.engine.core.Entity entity = map.getSpawnedEntity(5, 5);
            assertNotNull(entity);
            assertEquals(player, entity);
        }

        @Test
        @DisplayName("spawns projectile at position")
        void testSpawnProjectile() {
            GameMap map = new GameMap(10, 10);
            Position pos = new Position(5f, 5f);
            Size size = new Size(8, 8);
            org.ronobot.engine.entity.Projectile projectile = 
                new org.ronobot.engine.entity.Projectile(pos, size, 10);
            assertTrue(map.spawnProjectile(5, 5, projectile));
            
            org.ronobot.engine.entity.Projectile proj = map.getSpawnedProjectile(5, 5);
            assertNotNull(proj);
            assertEquals(projectile, proj);
        }

        @Test
        @DisplayName("returns false for out of bounds spawn")
        void testOutOfBoundsSpawn() {
            GameMap map = new GameMap(10, 10);
            Position pos = new Position(0, 5f);
            Size size = new Size(16, 16);
            PlayerEntity player = new PlayerEntity(0, pos.getX(), pos.getY(), 16, 16);
            assertFalse(map.spawnEntity(100, 100, player));
            assertNull(map.getSpawnedEntity(100, 100));
        }

        @Test
        @DisplayName("removes spawned entity")
        void testRemoveEntity() {
            GameMap map = new GameMap(10, 10);
            Position pos = new Position(5f, 5f);
            Size size = new Size(16, 16);
            PlayerEntity player = new PlayerEntity(0, pos.getX(), pos.getY(), 16, 16);
            map.spawnEntity(5, 5, player);
            assertTrue(map.removeEntity(player));
            assertNull(map.getSpawnedEntity(5, 5));
        }

        @Test
        @DisplayName("removes spawned projectile")
        void testRemoveProjectile() {
            GameMap map = new GameMap(10, 10);
            Position pos = new Position(5f, 5f);
            Size size = new Size(8, 8);
            org.ronobot.engine.entity.Projectile projectile = 
                new org.ronobot.engine.entity.Projectile(pos, size, 10);
            map.spawnProjectile(5, 5, projectile);
            assertTrue(map.removeProjectile(projectile));
            assertNull(map.getSpawnedProjectile(5, 5));
        }

        @Test
        @DisplayName("clears all spawned entities")
        void testClearSpawnedEntities() {
            GameMap map = new GameMap(10, 10);
            Position pos1 = new Position(5f, 5f);
            Size size1 = new Size(16, 16);
            PlayerEntity p1 = new PlayerEntity(0, pos1.getX(), pos1.getY(), 16, 16);
            Position pos2 = new Position(6f, 6f);
            Size size2 = new Size(16, 16);
            PlayerEntity p2 = new PlayerEntity(1, pos2.getX(), pos2.getY(), 16, 16);
            map.spawnEntity(5, 5, p1);
            map.spawnEntity(6, 6, p2);
            
            map.clearSpawnedEntities();
            assertEquals(0, map.getSpawnedEntities().size());
        }

        @Test
        @DisplayName("clears all spawned projectiles")
        void testClearSpawnedProjectiles() {
            GameMap map = new GameMap(10, 10);
            Position pos1 = new Position(5f, 5f);
            Size size1 = new Size(8, 8);
            org.ronobot.engine.entity.Projectile p1 = 
                new org.ronobot.engine.entity.Projectile(pos1, size1, 10);
            Position pos2 = new Position(6f, 6f);
            Size size2 = new Size(8, 8);
            org.ronobot.engine.entity.Projectile p2 = 
                new org.ronobot.engine.entity.Projectile(pos2, size2, 10);
            map.spawnProjectile(5, 5, p1);
            map.spawnProjectile(6, 6, p2);
            
            map.clearSpawnedProjectiles();
            assertEquals(0, map.getSpawnedProjectiles().size());
        }

        @Test
        @DisplayName("gets spawned entities list")
        void testGetSpawnedEntities() {
            GameMap map = new GameMap(10, 10);
            Position pos = new Position(5f, 5f);
            Size size = new Size(16, 16);
            PlayerEntity p1 = new PlayerEntity(0, pos.getX(), pos.getY(), 16, 16);
            map.spawnEntity(5, 5, p1);
            
            java.util.List<org.ronobot.engine.core.Entity> entities = map.getSpawnedEntities();
            assertEquals(1, entities.size());
        }
    }

    @Nested
    @DisplayName("GameMap State")
    class StateTests {

        @Test
        @DisplayName("is initially not loaded")
        void testInitialLoadedState() {
            GameMap map = new GameMap(10, 10);
            assertFalse(map.isLoaded());
        }

        @Test
        @DisplayName("loads GameMap after load call")
        void testLoad() {
            GameMap map = new GameMap(10, 10);
            map.load();
            assertTrue(map.isLoaded());
            assertNotNull(map.getCollisionManager());
        }

        @Test
        @DisplayName("can enable and disable GameMap")
        void testEnableDisable() {
            GameMap map = new GameMap(10, 10);
            map.load();
            assertTrue(map.isEnabled());
            
            map.disable();
            assertFalse(map.isEnabled());
            
            map.enable();
            assertTrue(map.isEnabled());
        }

        @Test
        @DisplayName("clears GameMap tiles and entities")
        void testClear() {
            GameMap map = new GameMap(10, 10);
            map.addWall(5, 5);
            map.addWall(6, 6);
            map.clear();
            
            assertFalse(map.isWall(5, 5));
            assertEquals(GameMap.TILE_FLOOR, map.getTile(5, 5));
        }

        @Test
        @DisplayName("creates copy of GameMap")
        void testCopy() {
            GameMap map1 = new GameMap(10, 10);
            map1.addWall(5, 5);
            
            GameMap map2 = map1.copy();
            assertEquals(map1.getWidth(), map2.getWidth());
            assertEquals(map1.getHeight(), map2.getHeight());
            assertEquals(map1.isWall(5, 5), map2.isWall(5, 5));
        }

        @Test
        @DisplayName("toString contains GameMap info")
        void testToString() {
            GameMap map = new GameMap(10, 10);
            String str = map.toString();
            assertTrue(str.contains("width=10"));
            assertTrue(str.contains("height=10"));
        }
    }

    @Nested
    @DisplayName("Boundary Checks")
    class BoundaryTests {

        @Test
        @DisplayName("isInBounds returns false for negative coordinates")
        void testNegativeCoordinates() {
            GameMap map = new GameMap(10, 10);
            assertFalse(map.isInBounds(-1, 0));
            assertFalse(map.isInBounds(0, -1));
        }

        @Test
        @DisplayName("isInBounds returns false for out of range")
        void testOutOfRangeCoordinates() {
            GameMap map = new GameMap(10, 10);
            assertFalse(map.isInBounds(10, 5));
            assertFalse(map.isInBounds(5, 10));
        }

        @Test
        @DisplayName("containsWall returns false for out of bounds")
        void testContainsWallOutOfBounds() {
            GameMap map = new GameMap(10, 10);
            map.addWall(5, 5);
            assertFalse(map.containsWall(100, 100));
        }

        @Test
        @DisplayName("containsWall detects wall correctly")
        void testContainsWall() {
            GameMap map = new GameMap(10, 10);
            // Wall is at tile (5,5), which in world coords is center at (160, 160)
            map.addWall(5, 5);
            assertTrue(map.containsWall(160, 160));
        }

        @Test
        @DisplayName("returns null for negative world position")
        void testNegativeWorldPosition() {
            GameMap map = new GameMap(10, 10);
            assertNull(map.toTilePosition(-10, -10));
        }
    }

    @Nested
    @DisplayName("Floor Tiles")
    class FloorTileTests {

        @Test
        @DisplayName("tiles are initially floor")
        void testInitiallyFloorTiles() {
            GameMap map = new GameMap(10, 10);
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    assertEquals(GameMap.TILE_FLOOR, map.getTile(x, y));
                }
            }
        }

        @Test
        @DisplayName("can check for floor tiles")
        void testIsFloor() {
            GameMap map = new GameMap(10, 10);
            // Tiles are initially floor
            assertEquals(GameMap.TILE_FLOOR, map.getTile(0, 0));
            
            map.addWall(5, 5);
            assertFalse(map.getTile(5, 5) == GameMap.TILE_FLOOR);
        }
    }

    @Nested
    @DisplayName("Tile Types")
    class TileTypesTests {

        @Test
        @DisplayName("TILE_WALL constant")
        void testWallTileConstant() {
            GameMap map = new GameMap(10, 10);
            map.addWall(5, 5);
            assertTrue(map.isWall(5, 5));
        }

        @Test
        @DisplayName("TILE_FLOOR constant")
        void testFloorTileConstant() {
            GameMap map = new GameMap(10, 10);
            map.addFloor(5, 5);
            assertEquals(GameMap.TILE_FLOOR, map.getTile(5, 5));
        }

        @Test
        @DisplayName("TILE_DOOR constant")
        void testDoorTileConstant() {
            GameMap map = new GameMap(10, 10);
            map.addDoor(5, 5);
            assertTrue(map.isDoor(5, 5));
        }
    }
}
