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
import pepse.world.avatar.Avatar;


import java.util.List;

public class PepseGameManager extends GameManager {
    private static final int SEED = 1;
    private static final float AVATAR_SIZE = 50;

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
        List<Block> tb =  t.createInRange((int) -windowDimensions.x(), (int) (2 * windowDimensions.x()));
        for (Block b : tb){
            gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
        }

        float avatarX = windowDimensions.x() / 2;
        float avatarY = t.groundHeightAt(avatarX) - AVATAR_SIZE;
        Avatar avatar = new Avatar(new Vector2(avatarX, avatarY), inputListener, imageReader);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        setCamera(new Camera(avatar, Vector2.ZERO, windowDimensions, windowDimensions));

    }
}
