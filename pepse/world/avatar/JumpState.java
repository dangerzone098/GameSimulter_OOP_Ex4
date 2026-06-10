package pepse.world.avatar;

public class JumpState implements AvatarState {
    @Override
    public boolean shouldEnter(Avatar avatar, AvatarInput input) {
        return !avatar.isOnGround();
    }

    @Override
    public void enter(Avatar avatar) {
        avatar.setJumpAnimation();
    }

    @Override
    public void update(Avatar avatar, AvatarInput input, float deltaTime) {
        if (input.hasSingleHorizontalDirection()) {
            avatar.moveHorizontally(input.horizontalDirection());
        } else {
            avatar.stopHorizontalMovement();
        }
        if (input.jump() && avatar.isFalling() &&
                avatar.trySpendEnergy(Avatar.DOUBLE_JUMP_ENERGY_COST)) {
            avatar.jump();
        }
    }

    @Override
    public void exit(Avatar avatar) {
    }
}
