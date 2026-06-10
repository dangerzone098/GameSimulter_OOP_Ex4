package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;


import java.util.List;

public class PepseGameManager extends GameManager {
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

        Terrain t = new Terrain(windowDimensions, 1);
        List<Block> tb =  t.createInRange(0, (int) windowDimensions.x());
        for (Block b : tb){
            gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
        }



    }
}
