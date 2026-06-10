package pepse.world.avatar;

public class RunState implements AvatarState {
    @Override
    public void enter(Avatar avatar) {
        avatar.setRunAnimation();
    }

    @Override
    public void update(Avatar avatar, float deltaTime) {
        avatar.spendRunEnergy();
    }

    @Override
    public void exit(Avatar avatar) {
    }
}
