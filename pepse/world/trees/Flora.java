package pepse.world.trees;

import danogl.GameObject;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Flora {
    private static final double TREE_PROBABILITY = 0.1;

    private final Function<Float, Float> groundHeightAt;
    private final int seed;
    private final Runnable onFruitEaten;

    public Flora(Function<Float, Float> groundHeightAt, int seed, Runnable onFruitEaten) {
        this.groundHeightAt = groundHeightAt;
        this.seed = seed;
        this.onFruitEaten = onFruitEaten;
    }

    public List<GameObject> createInRange(int minX, int maxX) {
        List<GameObject> floraObjects = new ArrayList<>();
        int alignedMinX = alignToBlockGrid(minX);
        int alignedMaxX = alignToBlockGrid(maxX) + Block.SIZE;
        for (int x = alignedMinX; x < alignedMaxX; x += Block.SIZE) {
            Random treeRandom = new Random(Objects.hash(x, seed));
            if (treeRandom.nextDouble() <= TREE_PROBABILITY) {
                Tree tree = new Tree(x, groundHeightAt.apply((float) x), treeRandom, onFruitEaten);
                floraObjects.addAll(tree.createGameObjects());
            }
        }
        return floraObjects;
    }

    private int alignToBlockGrid(int x) {
        return (int) Math.floor((float) x / Block.SIZE) * Block.SIZE;
    }
}
