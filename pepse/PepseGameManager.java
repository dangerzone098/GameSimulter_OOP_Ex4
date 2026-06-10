package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.WorldWindow;
import pepse.world.WorldObject;
import pepse.world.avatar.Avatar;
import pepse.world.avatar.EnergyCounter;
import pepse.world.trees.Flora;

import java.util.ArrayList;
import java.util.List;

public class PepseGameManager extends GameManager {
    private static final int SEED = 1;
    private static final float AVATAR_SIZE = 50;
    private static final Vector2 ENERGY_COUNTER_TOP_LEFT = new Vector2(20, 20);
    private static final Vector2 ENERGY_COUNTER_DIMENSIONS = new Vector2(70, 35);
    private Avatar avatar;
    private WorldWindow worldWindow;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        // creating the sky object
        GameObject sky = Sky.create(windowDimensions);
        // we set the sky to the background layer
        int skyLayer = Layer.BACKGROUND;
        gameObjects().addGameObject(sky, skyLayer);

        Terrain t = new Terrain(windowDimensions, SEED);
        float avatarX = windowDimensions.x() / 2;
        float avatarY = t.groundHeightAt(avatarX) - AVATAR_SIZE;
        avatar = new Avatar(new Vector2(avatarX, avatarY), inputListener, imageReader);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        Flora flora = new Flora(t::groundHeightAt, SEED, () -> avatar.addEnergy(10));
        worldWindow = new WorldWindow(
                gameObjects(),
                windowDimensions.x(),
                List.of(
                        (minX, maxX) -> createTerrainWorldObjects(t, minX, maxX),
                        flora::createWorldObjectsInRange));
        worldWindow.update(avatar.getCenter().x());

        EnergyCounter energyCounter =
                new EnergyCounter(ENERGY_COUNTER_TOP_LEFT, ENERGY_COUNTER_DIMENSIONS);
        avatar.setEnergyCallback(energyCounter::setEnergy);
        gameObjects().addGameObject(energyCounter, Layer.UI);

        setCamera(new Camera(avatar, Vector2.ZERO, windowDimensions, windowDimensions));

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        worldWindow.update(avatar.getCenter().x());
    }

    private List<WorldObject> createTerrainWorldObjects(Terrain terrain, int minX, int maxX) {
        List<WorldObject> worldObjects = new ArrayList<>();
        for (Block block : terrain.createInRange(minX, maxX)) {
            worldObjects.add(new WorldObject(block, Layer.STATIC_OBJECTS));
        }
        return worldObjects;
    }
}
