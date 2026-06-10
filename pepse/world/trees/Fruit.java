package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.avatar.Avatar;

import java.awt.Color;

public class Fruit extends GameObject {
    public static final String TAG = "fruit";
    private static final float RESPAWN_TIME = 30;

    private final Runnable onEaten;
    private boolean active = true;

    public Fruit(Vector2 topLeftCorner, float size, Color color, Runnable onEaten) {
        super(topLeftCorner, Vector2.ONES.mult(size), new OvalRenderable(color));
        this.onEaten = onEaten;
        setTag(TAG);
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return active && Avatar.TAG.equals(other.getTag());
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (active && Avatar.TAG.equals(other.getTag())) {
            eat();
        }
    }

    private void eat() {
        active = false;
        renderer().setOpaqueness(0);
        onEaten.run();
        new ScheduledTask(this, RESPAWN_TIME, false, this::reappear);
    }

    private void reappear() {
        active = true;
        renderer().setOpaqueness(1);
    }
}
