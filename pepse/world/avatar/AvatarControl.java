package pepse.world.avatar;

import java.util.List;

public class AvatarControl {
    private final List<AvatarState> states;
    private AvatarState currentState;

    public AvatarControl(AvatarStateFactory stateFactory) {
        states = List.of(
                stateFactory.jumpState(),
                stateFactory.runState(),
                stateFactory.idleState());
    }

    public void initialize(Avatar avatar) {
        changeState(avatar, states.get(states.size() - 1));
    }

    public void update(Avatar avatar, AvatarInput input, float deltaTime) {
        updateState(avatar, input);
        currentState.update(avatar, input, deltaTime);
        updateState(avatar, input);
    }

    private void updateState(Avatar avatar, AvatarInput input) {
        for (AvatarState state : states) {
            if (state.shouldEnter(avatar, input)) {
                changeStateIfNeeded(avatar, state);
                return;
            }
        }
    }

    private void changeStateIfNeeded(Avatar avatar, AvatarState nextState) {
        if (currentState != nextState) {
            changeState(avatar, nextState);
        }
    }

    private void changeState(Avatar avatar, AvatarState nextState) {
        if (currentState != null) {
            currentState.exit(avatar);
        }
        currentState = nextState;
        currentState.enter(avatar);
    }
}
