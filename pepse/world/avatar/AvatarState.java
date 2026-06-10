package pepse.world.avatar;

public interface AvatarState {
    void enter(Avatar avatar);

    void update(Avatar avatar, float deltaTime);

    void exit(Avatar avatar);
}
