package pepse.world;

import danogl.collisions.GameObjectCollection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class WorldWindow {
    private static final int CHUNKS_AROUND_AVATAR = 2;

    private final GameObjectCollection gameObjects;
    private final int chunkSize;
    private final List<BiFunction<Integer, Integer, List<WorldObject>>> objectCreators;
    private final Map<Integer, List<WorldObject>> visibleChunks = new HashMap<>();

    public WorldWindow(
            GameObjectCollection gameObjects,
            float windowWidth,
            List<BiFunction<Integer, Integer, List<WorldObject>>> objectCreators) {
        this.gameObjects = gameObjects;
        this.chunkSize = Math.max(Block.SIZE, alignToBlockGrid((int) windowWidth));
        this.objectCreators = objectCreators;
    }

    public void update(float centerX) {
        int centerChunk = chunkOf(centerX);
        addMissingChunks(centerChunk);
        removeFarChunks(centerChunk);
    }

    private void addMissingChunks(int centerChunk) {
        for (int chunk = centerChunk - CHUNKS_AROUND_AVATAR;
             chunk <= centerChunk + CHUNKS_AROUND_AVATAR;
             chunk++) {
            if (!visibleChunks.containsKey(chunk)) {
                createChunk(chunk);
            }
        }
    }

    private void createChunk(int chunk) {
        int minX = chunk * chunkSize;
        int maxX = minX + chunkSize;
        List<WorldObject> chunkObjects = new java.util.ArrayList<>();
        for (BiFunction<Integer, Integer, List<WorldObject>> creator : objectCreators) {
            chunkObjects.addAll(creator.apply(minX, maxX));
        }
        for (WorldObject worldObject : chunkObjects) {
            gameObjects.addGameObject(worldObject.gameObject(), worldObject.layer());
        }
        visibleChunks.put(chunk, chunkObjects);
    }

    private void removeFarChunks(int centerChunk) {
        Iterator<Map.Entry<Integer, List<WorldObject>>> iterator =
                visibleChunks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<WorldObject>> entry = iterator.next();
            if (Math.abs(entry.getKey() - centerChunk) > CHUNKS_AROUND_AVATAR) {
                removeChunkObjects(entry.getValue());
                iterator.remove();
            }
        }
    }

    private void removeChunkObjects(List<WorldObject> chunkObjects) {
        for (WorldObject worldObject : chunkObjects) {
            gameObjects.removeGameObject(worldObject.gameObject(), worldObject.layer());
        }
    }

    private int chunkOf(float x) {
        return (int) Math.floor(x / chunkSize);
    }

    private int alignToBlockGrid(int x) {
        return (int) Math.floor((float) x / Block.SIZE) * Block.SIZE;
    }
}
