/**
 * GameState - Represents the complete state of a game for persistence.
 * Includes game map, entities, player position, and game state for save/load functionality.
 *
 * @author ronobot
 */
package org.ronobot.engine.core;

import org.ronobot.engine.map.EntitySpawn;
import org.ronobot.engine.map.MapDecoration;

import com.google.gson.*;

import java.io.*;
import java.util.*;

/**
 * Complete game state representation for serialization and persistence.
 *
 * @author ronobot
 */
public class GameState {

    private final String levelName;
    private final int difficulty;
    private final GameStateType gameState;
    private final int score;
    private final int frameCount;
    private final List<EntitySpawn> spawnPositions;
    private final List<MapDecoration> decorations;
    private final List<EntitySpawn> spawnedEntities;
    private final List<EntitySpawn> spawnedProjectiles;

    public GameState(String levelName, int difficulty, GameStateType gameState,
                     int score, int frameCount,
                     List<EntitySpawn> spawnPositions,
                     List<MapDecoration> decorations,
                     List<EntitySpawn> spawnedEntities,
                     List<EntitySpawn> spawnedProjectiles) {
        this.levelName = levelName;
        this.difficulty = difficulty;
        this.gameState = gameState;
        this.score = score;
        this.frameCount = frameCount;
        this.spawnPositions = new ArrayList<>(spawnPositions != null ? spawnPositions : Collections.emptyList());
        this.decorations = new ArrayList<>(decorations != null ? decorations : Collections.emptyList());
        this.spawnedEntities = new ArrayList<>(spawnedEntities != null ? spawnedEntities : Collections.emptyList());
        this.spawnedProjectiles = new ArrayList<>(spawnedProjectiles != null ? spawnedProjectiles : Collections.emptyList());
    }

    public static GameState fromJson(JsonObject json) {
        String levelName = json.has("levelName") ? json.get("levelName").getAsString() : "Level 1";
        int difficulty = json.has("difficulty") ? json.get("difficulty").getAsInt() : 1;
        GameStateType gameState = json.has("gameState")
                ? GameStateType.valueOf(json.get("gameState").getAsString())
                : GameStateType.PLAYING;
        int score = json.has("score") ? json.get("score").getAsInt() : 0;
        int frameCount = json.has("frameCount") ? json.get("frameCount").getAsInt() : 0;

        JsonArray spawns = json.has("spawnPositions") && json.get("spawnPositions").isJsonArray()
                ? json.get("spawnPositions").getAsJsonArray()
                : new JsonArray();

        List<EntitySpawn> spawnPositionsList = new ArrayList<>();
        for (JsonElement spawnJson : spawns) {
            if (spawnJson.isJsonObject()) {
                spawnPositionsList.add(EntitySpawn.fromJson(spawnJson.getAsJsonObject()));
            }
        }

        JsonArray decorationsArray = json.has("decorations") && json.get("decorations").isJsonArray()
                ? json.get("decorations").getAsJsonArray()
                : new JsonArray();

        List<MapDecoration> decorationsList = new ArrayList<>();
        for (JsonElement decorationJson : decorationsArray) {
            if (decorationJson.isJsonObject()) {
                decorationsList.add(MapDecoration.fromJson(decorationJson.getAsJsonObject()));
            }
        }

        JsonArray entitiesArray = json.has("spawnedEntities") && json.get("spawnedEntities").isJsonArray()
                ? json.get("spawnedEntities").getAsJsonArray()
                : new JsonArray();

        List<EntitySpawn> entitiesList = new ArrayList<>();
        for (JsonElement entityJson : entitiesArray) {
            if (entityJson.isJsonObject()) {
                entitiesList.add(EntitySpawn.fromJson(entityJson.getAsJsonObject()));
            }
        }

        JsonArray projectilesArray = json.has("spawnedProjectiles") && json.get("spawnedProjectiles").isJsonArray()
                ? json.get("spawnedProjectiles").getAsJsonArray()
                : new JsonArray();

        List<EntitySpawn> projectilesList = new ArrayList<>();
        for (JsonElement projectileJson : projectilesArray) {
            if (projectileJson.isJsonObject()) {
                projectilesList.add(EntitySpawn.fromJson(projectileJson.getAsJsonObject()));
            }
        }

        return new GameState(levelName, difficulty, gameState, score, frameCount,
                             spawnPositionsList, decorationsList, entitiesList, projectilesList);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("levelName", levelName);
        json.addProperty("difficulty", difficulty);
        json.addProperty("gameState", gameState.name());
        json.addProperty("score", score);
        json.addProperty("frameCount", frameCount);

        JsonArray spawnsArray = new JsonArray();
        for (EntitySpawn spawn : spawnPositions) {
            spawnsArray.add(spawn.toJson());
        }
        json.add("spawnPositions", spawnsArray);

        JsonArray decorationsArray = new JsonArray();
        for (MapDecoration decoration : decorations) {
            decorationsArray.add(decoration.toJson());
        }
        json.add("decorations", decorationsArray);

        JsonArray entitiesArray = new JsonArray();
        for (EntitySpawn entity : spawnedEntities) {
            entitiesArray.add(entity.toJson());
        }
        json.add("spawnedEntities", entitiesArray);

        JsonArray projectilesArray = new JsonArray();
        for (EntitySpawn projectile : spawnedProjectiles) {
            projectilesArray.add(projectile.toJson());
        }
        json.add("spawnedProjectiles", projectilesArray);

        return json;
    }

    public void saveToFile(String path) {
        if (path == null || path.isBlank()) {
            return;
        }
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(toJson().toString());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save game state", e);
            }
        }
    }

    public static GameState loadFromFile(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        try {
            // Read entire file as JSON string
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            if (content != null && !content.isBlank()) {
                try {
                    JsonElement jsonElement = JsonParser.parseString(content);
                    JsonObject json = jsonElement.getAsJsonObject();
                    return GameState.fromJson(json);
                } catch (JsonParseException e) {
                    throw new RuntimeException("Failed to parse game state JSON", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read game state file", e);
        }
        return null;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public GameStateType getGameState() {
        return gameState;
    }

    public int getScore() {
        return score;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public List<EntitySpawn> getSpawnPositions() {
        return Collections.unmodifiableList(spawnPositions);
    }

    public List<MapDecoration> getDecorations() {
        return Collections.unmodifiableList(decorations);
    }

    public List<EntitySpawn> getSpawnedEntities() {
        return Collections.unmodifiableList(spawnedEntities);
    }

    public List<EntitySpawn> getSpawnedProjectiles() {
        return Collections.unmodifiableList(spawnedProjectiles);
    }

    public enum GameStateType {
        PLAYING, PAUSED, WON, LOST
    }

    @Override
    public String toString() {
        return "GameState{" +
                "levelName='" + levelName + '\'' +
                ", difficulty=" + difficulty +
                ", gameState=" + gameState +
                ", score=" + score +
                ", frameCount=" + frameCount +
                ", spawnPositions=" + spawnPositions +
                ", decorations=" + decorations +
                ", spawnedEntities=" + spawnedEntities +
                ", spawnedProjectiles=" + spawnedProjectiles +
                '}';
    }
}
