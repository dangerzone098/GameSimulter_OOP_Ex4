package pepse.world.avatar;

public class RunState implements AvatarState {
    @Override
    public boolean shouldEnter(Avatar avatar, AvatarInput input) {
        return avatar.isOnGround() &&
                input.hasSingleHorizontalDirection() &&
                avatar.hasEnoughEnergy(Avatar.RUN_ENERGY_COST);
    }

    @Override
    public void enter(Avatar avatar) {
        avatar.setRunAnimation();
    }

    @Override
    public void update(Avatar avatar, AvatarInput input, float deltaTime) {
        avatar.spendRunEnergy();
        avatar.moveHorizontally(input.horizontalDirection());
        if (input.jump() && avatar.trySpendEnergy(Avatar.JUMP_ENERGY_COST)) {
            avatar.jump();
        }
    }

    @Override
    public void exit(Avatar avatar) {
    }
}
