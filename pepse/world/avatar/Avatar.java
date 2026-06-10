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
    private final AvatarStateFactory stateFactory;

    private AvatarState currentState;
    private float energy = MAX_ENERGY;
    private boolean onGround;
    private boolean usedDoubleJump;
    private Consumer<Float> energyCallback;

    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner,
                Vector2.ONES.mult(AVATAR_SIZE),
                imageReader.readImage(ASSETS_DIRECTORY + "idle_0.png", true));
        this.inputListener = inputListener;
        stateFactory = new AvatarStateFactory();
        idleAnimation = createAnimation(imageReader, "idle_", 4);
        runAnimation = createAnimation(imageReader, "run_", 6);
        jumpAnimation = createAnimation(imageReader, "jump_", 4);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        changeState(stateFactory.idleState());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        handleInput(readInput(), deltaTime);
    }

    public void handleInput(AvatarInput input, float deltaTime) {
        updateMovement(input);
        updateState();
        currentState.update(this, deltaTime);
        if (onGround && getVelocity().y() != 0) {
            onGround = false;
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (getVelocity().y() >= 0) {
            onGround = true;
            usedDoubleJump = false;
            transform().setVelocityY(0);
        }
    }

    public void changeState(AvatarState nextState) {
        if (currentState != null) {
            currentState.exit(this);
        }
        currentState = nextState;
        currentState.enter(this);
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

    private void updateMovement(AvatarInput input) {
        updateHorizontalMovement(input);
        updateJumpMovement(input);
    }

    private void updateHorizontalMovement(AvatarInput input) {
        if (!input.hasSingleHorizontalDirection()) {
            stopHorizontalMovement();
            return;
        }
        if (onGround && !hasEnoughEnergy(RUN_ENERGY_COST)) {
            stopHorizontalMovement();
            return;
        }
        moveHorizontally(input.horizontalDirection());
    }

    private void updateJumpMovement(AvatarInput input) {
        if (!input.jump()) {
            return;
        }
        if (onGround && trySpendEnergy(JUMP_ENERGY_COST)) {
            usedDoubleJump = false;
            jump();
            return;
        }
        if (!onGround && isFalling() && !usedDoubleJump &&
                trySpendEnergy(DOUBLE_JUMP_ENERGY_COST)) {
            usedDoubleJump = true;
            jump();
        }
    }

    private void updateState() {
        if (!onGround) {
            changeStateIfNeeded(stateFactory.jumpState());
            return;
        }
        if (getVelocity().x() != 0) {
            changeStateIfNeeded(stateFactory.runState());
            return;
        }
        changeStateIfNeeded(stateFactory.idleState());
    }

    private void changeStateIfNeeded(AvatarState nextState) {
        if (currentState != nextState) {
            changeState(nextState);
        }
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
