package pepse.world.avatar;

public class JumpState implements AvatarState {
    private boolean doubleJumpUsed;

    @Override
    public boolean shouldEnter(Avatar avatar, AvatarInput input) {
        return !avatar.isOnGround();
    }

    @Override
    public void enter(Avatar avatar) {
        doubleJumpUsed = false;
        avatar.setJumpAnimation();
    }

    @Override
    public void update(Avatar avatar, AvatarInput input, float deltaTime) {
        if (input.hasSingleHorizontalDirection()) {
            avatar.moveHorizontally(input.horizontalDirection());
        } else {
            avatar.stopHorizontalMovement();
        }
        if (input.jump() && avatar.isFalling() && !doubleJumpUsed &&
                avatar.trySpendEnergy(Avatar.DOUBLE_JUMP_ENERGY_COST)) {
            doubleJumpUsed = true;
            avatar.jump();
        }
    }

    @Override
    public void exit(Avatar avatar) {
    }
}
