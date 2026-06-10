package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree {
    public static final String TRUNK_TAG = "trunk";

    private static final Color BASE_TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
    private static final Color BASE_FRUIT_COLOR = new Color(220, 40, 40);
    private static final int MIN_TRUNK_BLOCKS = 4;
    private static final int TRUNK_HEIGHT_RANGE = 5;
    private static final int LEAF_RADIUS = 2;
    private static final double LEAF_PROBABILITY = 0.75;
    private static final double FRUIT_PROBABILITY = 0.12;
    private static final float FRUIT_SIZE = Block.SIZE * 0.65f;
    private static final float MAX_LEAF_DELAY = 2f;
    private static final int COLOR_DELTA = 10;

    private final int x;
    private final float groundHeight;
    private final Random random;
    private final Runnable onFruitEaten;

    public Tree(int x, float groundHeight, Random random, Runnable onFruitEaten) {
        this.x = x;
        this.groundHeight = groundHeight;
        this.random = random;
        this.onFruitEaten = onFruitEaten;
    }

    public List<GameObject> createGameObjects() {
        List<GameObject> treeObjects = new ArrayList<>();
        int trunkHeightInBlocks = MIN_TRUNK_BLOCKS + random.nextInt(TRUNK_HEIGHT_RANGE);
        float treetopY = groundHeight - trunkHeightInBlocks * Block.SIZE;
        addTrunk(treeObjects, trunkHeightInBlocks);
        addLeavesAndFruits(treeObjects, treetopY);
        return treeObjects;
    }

    private void addTrunk(List<GameObject> treeObjects, int trunkHeightInBlocks) {
        for (int blockIndex = 1; blockIndex <= trunkHeightInBlocks; blockIndex++) {
            Vector2 topLeftCorner =
                    new Vector2(x, groundHeight - blockIndex * Block.SIZE);
            Block trunkBlock = new Block(
                    topLeftCorner,
                    new RectangleRenderable(variedColor(BASE_TRUNK_COLOR)));
            trunkBlock.setTag(TRUNK_TAG);
            treeObjects.add(trunkBlock);
        }
    }

    private void addLeavesAndFruits(List<GameObject> treeObjects, float treetopY) {
        for (int xOffset = -LEAF_RADIUS; xOffset <= LEAF_RADIUS; xOffset++) {
            for (int yOffset = -LEAF_RADIUS; yOffset <= LEAF_RADIUS; yOffset++) {
                if (random.nextDouble() <= LEAF_PROBABILITY) {
                Vector2 leafTopLeft = new Vector2(
                        x + xOffset * Block.SIZE,
                        treetopY + yOffset * Block.SIZE);
                treeObjects.add(new Leaf(
                        leafTopLeft,
                        Vector2.ONES.mult(Block.SIZE),
                        variedColor(BASE_LEAF_COLOR),
                        random.nextFloat() * MAX_LEAF_DELAY));
                addFruitIfNeeded(treeObjects, leafTopLeft);
                }
            }
        }
    }

    private void addFruitIfNeeded(List<GameObject> treeObjects, Vector2 leafTopLeft) {
        if (random.nextDouble() <= FRUIT_PROBABILITY) {    
            Vector2 fruitTopLeft = leafTopLeft.add(
                    Vector2.ONES.mult((Block.SIZE - FRUIT_SIZE) / 2));
            treeObjects.add(new Fruit(
                    fruitTopLeft,
                    FRUIT_SIZE,
                    variedColor(BASE_FRUIT_COLOR),
                    onFruitEaten));
        }        
    }

    private Color variedColor(Color baseColor) {
        return new Color(
                variedChannel(baseColor.getRed()),
                variedChannel(baseColor.getGreen()),
                variedChannel(baseColor.getBlue()));
    }

    private int variedChannel(int baseChannel) {
        int min = Math.max(0, baseChannel - COLOR_DELTA);
        int max = Math.min(255, baseChannel + COLOR_DELTA);
        return min + random.nextInt(max - min + 1);
    }
}
