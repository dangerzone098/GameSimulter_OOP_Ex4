package pepse.world.avatar;

public interface AvatarState {
    boolean shouldEnter(Avatar avatar, AvatarInput input);

    void enter(Avatar avatar);

    void update(Avatar avatar, AvatarInput input, float deltaTime);

    void exit(Avatar avatar);
}
