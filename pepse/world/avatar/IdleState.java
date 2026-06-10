package pepse.world.avatar;

public class IdleState implements AvatarState {
    @Override
    public boolean shouldEnter(Avatar avatar, AvatarInput input) {
        return true;
    }

    @Override
    public void enter(Avatar avatar) {
        avatar.setIdleAnimation();
        avatar.stopHorizontalMovement();
    }

    @Override
    public void update(Avatar avatar, AvatarInput input, float deltaTime) {
        avatar.addEnergy(Avatar.IDLE_ENERGY_GAIN);
        if (input.jump() && avatar.trySpendEnergy(Avatar.JUMP_ENERGY_COST)) {
            avatar.jump();
        }
    }

    @Override
    public void exit(Avatar avatar) {
    }
}
