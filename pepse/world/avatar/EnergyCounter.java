package pepse.world.avatar;

import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import danogl.GameObject;

import java.awt.Color;

public class EnergyCounter extends GameObject {
    private static final float INITIAL_ENERGY = 100;
    private static final int MIN_ENERGY = 0;
    private static final int MAX_ENERGY = 100;
    private static final int COLOR_STEP = 10;
    private static final int FULL_COLOR_VALUE = 255;

    private final TextRenderable textRenderable;

    public EnergyCounter(Vector2 topLeftCorner, Vector2 dimensions) {
        this(topLeftCorner, dimensions, new TextRenderable(""));
    }

    private EnergyCounter(
            Vector2 topLeftCorner,
            Vector2 dimensions,
            TextRenderable textRenderable) {
        super(topLeftCorner, dimensions, textRenderable);
        this.textRenderable = textRenderable;
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        setTag("energyCounter");
        setEnergy(INITIAL_ENERGY);
    }

    public void setEnergy(float energy) {
        int roundedEnergy = Math.round(energy);
        int clampedEnergy = Math.max(MIN_ENERGY, Math.min(MAX_ENERGY, roundedEnergy));
        textRenderable.setString(String.valueOf(clampedEnergy));
        textRenderable.setColor(colorForEnergy(clampedEnergy));
    }

    private Color colorForEnergy(int energy) {
        int steppedEnergy = (energy / COLOR_STEP) * COLOR_STEP;
        float greenRatio = (float) steppedEnergy / MAX_ENERGY;
        int red = Math.round(FULL_COLOR_VALUE * (1 - greenRatio));
        int green = Math.round(FULL_COLOR_VALUE * greenRatio);
        return new Color(red, green, 0);
    }
}
