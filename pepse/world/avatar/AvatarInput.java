package pepse.world.avatar;

public class AvatarInput {
    private final boolean left;
    private final boolean right;
    private final boolean jump;

    public AvatarInput(boolean left, boolean right, boolean jump) {
        this.left = left;
        this.right = right;
        this.jump = jump;
    }

    public boolean left() {
        return left;
    }

    public boolean right() {
        return right;
    }

    public boolean jump() {
        return jump;
    }

    public boolean hasSingleHorizontalDirection() {
        return left != right;
    }

    public int horizontalDirection() {
        if (left == right) {
            return 0;
        }
        return right ? 1 : -1;
    }
}
