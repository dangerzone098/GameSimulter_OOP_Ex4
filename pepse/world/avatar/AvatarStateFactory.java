package pepse.world.avatar;

public class AvatarStateFactory {
    private final AvatarState idleState = new IdleState();
    private final AvatarState runState = new RunState();
    private final AvatarState jumpState = new JumpState();

    public AvatarState idleState() {
        return idleState;
    }

    public AvatarState runState() {
        return runState;
    }

    public AvatarState jumpState() {
        return jumpState;
    }
}
