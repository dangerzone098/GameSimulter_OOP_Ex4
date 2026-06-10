package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.Color;

public class Leaf extends GameObject {
    public static final String TAG = "leaf";
    private static final float MIN_ANGLE = -8;
    private static final float MAX_ANGLE = 8;
    private static final float ANGLE_TRANSITION_TIME = 1.5f;
    private static final float DIMENSION_TRANSITION_TIME = 2f;
    private static final float SIZE_CHANGE_FACTOR = 0.9f;

    private final Vector2 originalDimensions;

    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Color color, float movementDelay) {
        super(topLeftCorner, dimensions, new RectangleRenderable(color));
        originalDimensions = dimensions;
        setTag(TAG);
        new ScheduledTask(this, movementDelay, false, this::startMovement);
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    private void startMovement() {
        new Transition<>(
                this,
                renderer()::setRenderableAngle,
                MIN_ANGLE,
                MAX_ANGLE,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                ANGLE_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        new Transition<>(
                this,
                this::setDimensions,
                originalDimensions,
                originalDimensions.mult(SIZE_CHANGE_FACTOR),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                DIMENSION_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }
}
