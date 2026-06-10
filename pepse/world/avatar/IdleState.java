package pepse.world.avatar;

public class IdleState implements AvatarState {
    @Override
    public void enter(Avatar avatar) {
        avatar.setIdleAnimation();
        avatar.stopHorizontalMovement();
    }

    @Override
    public void update(Avatar avatar, float deltaTime) {
        avatar.addEnergy(Avatar.IDLE_ENERGY_GAIN);
    }

    @Override
    public void exit(Avatar avatar) {
    }
}
