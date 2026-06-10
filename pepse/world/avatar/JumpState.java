package pepse.world.avatar;

public class JumpState implements AvatarState {
    @Override
    public void enter(Avatar avatar) {
        avatar.setJumpAnimation();
    }

    @Override
    public void update(Avatar avatar, float deltaTime) {
    }

    @Override
    public void exit(Avatar avatar) {
    }
}
