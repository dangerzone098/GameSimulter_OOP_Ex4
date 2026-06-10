package pepse.world;


import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class block
 */
public class Block extends GameObject {
    public static final int SIZE = 30;

    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        // stop colliding Game objects
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        // make the block immovable
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}