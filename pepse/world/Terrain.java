package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.utils.ColorSupplier;
import pepse.utils.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {

    // TODO: define this constant
    private final float groundHeightAtX0;

    private static final int NOISE_DEVIATION = 8;
    private static final String GROUND_TAG = "ground";
    private static final Color BASE_GROUND_COLOR = new Color(212,
            123, 74);
    private final Vector2 windowDimensions;
    private final int seed;


    public Terrain(Vector2 windowDimensions, int seed){
        // TODO: implement
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        float GROUND_HEIGHT_AT_X0_BY_WINDOWS_SIZE = (float) (2.0 / 3.0);

        this.groundHeightAtX0 = windowDimensions.y() * GROUND_HEIGHT_AT_X0_BY_WINDOWS_SIZE;
    }

    public float groundHeightAt(float x) {
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * NOISE_DEVIATION);

        // we approximate the closest smaller value that is divisible by Block.SIZE
        return (float) Math.floor((groundHeightAtX0 + noise) / Block.SIZE) *  Block.SIZE;
    }


    public List<Block> createInRange(int minX, int maxX) {
        // defining a blockList to hold all blocks
        ArrayList<Block> blockList = new ArrayList<>();

        float left_ptr = (float) Math.floor( (float) (minX) / Block.SIZE) *  Block.SIZE;
        // The instructions specify to reach past maxX, so we do + Block.SIZE
        float right_ptr = (float) Math.floor( (float) (maxX) / Block.SIZE) *  Block.SIZE + Block.SIZE;
        float topHeight;
        while (left_ptr < right_ptr){

            topHeight = groundHeightAt(left_ptr);
            // keep in mind topHeight is actually the smallest number
            // because the higher the coordinate the lower it is on the screen

            for (float currHeight = topHeight; currHeight <= windowDimensions.y(); currHeight += Block.SIZE) {
                // Each block gets its own Renderable for unique color variation
                Renderable renderable = new RectangleRenderable(
                        ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block groundBlock = new Block(new Vector2(left_ptr, currHeight), renderable);
                groundBlock.setTag(GROUND_TAG);
                blockList.add(groundBlock);
            }

            // incrementing the left_ptr
            left_ptr += Block.SIZE;
        }

        return blockList;
    }



}
