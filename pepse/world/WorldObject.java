package pepse.world;

import danogl.GameObject;

public class WorldObject {
    private final GameObject gameObject;
    private final int layer;

    public WorldObject(GameObject gameObject, int layer) {
        this.gameObject = gameObject;
        this.layer = layer;
    }

    public GameObject gameObject() {
        return gameObject;
    }

    public int layer() {
        return layer;
    }
}
