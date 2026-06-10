package pepse.world.avatar;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class Avatar extends GameObject {
    public static final float MAX_ENERGY = 100;
    public static final float IDLE_ENERGY_GAIN = 1;
    public static final float RUN_ENERGY_COST = 2;
    public static final float JUMP_ENERGY_COST = 20;
    public static final float DOUBLE_JUMP_ENERGY_COST = 50;

    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final float AVATAR_SIZE = 50;
    private static final float ANIMATION_FRAME_DURATION = 0.15f;
    private static final String ASSETS_DIRECTORY = "assests/";

    private final UserInputListener inputListener;
    private final AnimationRenderable idleAnimation;
    private final AnimationRenderable runAnimation;
    private final AnimationRenderable jumpAnimation;
    private final AvatarControl avatarControl;

    private float energy = MAX_ENERGY;
    private boolean onGround;
    private Consumer<Float> energyCallback;

    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner,
                Vector2.ONES.mult(AVATAR_SIZE),
                imageReader.readImage(ASSETS_DIRECTORY + "idle_0.png", true));
        this.inputListener = inputListener;
        avatarControl = new AvatarControl(new AvatarStateFactory());
        idleAnimation = createAnimation(imageReader, "idle_", 4);
        runAnimation = createAnimation(imageReader, "run_", 6);
        jumpAnimation = createAnimation(imageReader, "jump_", 4);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        avatarControl.initialize(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        handleInput(readInput(), deltaTime);
    }

    public void handleInput(AvatarInput input, float deltaTime) {
        avatarControl.update(this, input, deltaTime);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        updateGroundStatus();
    }

    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);
        updateGroundStatus();
    }

    private void updateGroundStatus() {
        if (getVelocity().y() >= 0) {
            onGround = true;
            transform().setVelocityY(0);
        }
    }

    public boolean trySpendEnergy(float amount) {
        if (!hasEnoughEnergy(amount)) {
            return false;
        }
        setEnergy(energy - amount);
        return true;
    }

    public boolean hasEnoughEnergy(float amount) {
        return energy >= amount;
    }

    public void addEnergy(float amount) {
        setEnergy(energy + amount);
    }

    public void spendRunEnergy() {
        trySpendEnergy(RUN_ENERGY_COST);
    }

    public void setEnergyCallback(Consumer<Float> energyCallback) {
        this.energyCallback = energyCallback;
        this.energyCallback.accept(energy);
    }

    public void moveHorizontally(int direction) {
        transform().setVelocityX(direction * VELOCITY_X);
        renderer().setIsFlippedHorizontally(direction < 0);
    }

    public void stopHorizontalMovement() {
        transform().setVelocityX(0);
    }

    public void jump() {
        onGround = false;
        transform().setVelocityY(VELOCITY_Y);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isFalling() {
        return getVelocity().y() > 0;
    }

    public void setIdleAnimation() {
        renderer().setRenderable(idleAnimation);
    }

    public void setRunAnimation() {
        renderer().setRenderable(runAnimation);
    }

    public void setJumpAnimation() {
        renderer().setRenderable(jumpAnimation);
    }

    private AvatarInput readInput() {
        return new AvatarInput(
                inputListener.isKeyPressed(KeyEvent.VK_LEFT),
                inputListener.isKeyPressed(KeyEvent.VK_RIGHT),
                inputListener.isKeyPressed(KeyEvent.VK_SPACE));
    }

    private AnimationRenderable createAnimation(
            ImageReader imageReader,
            String prefix,
            int frameCount) {
        Renderable[] frames = new Renderable[frameCount];
        for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
            frames[frameIndex] =
                    imageReader.readImage(ASSETS_DIRECTORY + prefix + frameIndex + ".png", true);
        }
        return new AnimationRenderable(frames, ANIMATION_FRAME_DURATION);
    }

    private void setEnergy(float nextEnergy) {
        float previousEnergy = energy;
        energy = Math.max(0, Math.min(MAX_ENERGY, nextEnergy));
        if (energyCallback != null && energy != previousEnergy) {
            energyCallback.accept(energy);
        }
    }
}
