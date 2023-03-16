package chess.domain.state;

import chess.domain.ColorCompareResult;

public class BishopState implements MoveState {
    private static final BishopState instance = new BishopState();

    private BishopState() {
    }

    public static BishopState getInstance() {
        return instance;
    }

    @Override
    public boolean canMove(int x, int y, ColorCompareResult colorCompareResult) {
        if (Math.abs(x) == Math.abs(y) && colorCompareResult != ColorCompareResult.SAME_COLOR) {
            return true;
        }
        return false;
    }

    @Override
    public MoveState getNextState() {
        return this;
    }

    @Override
    public boolean canJump() {
        return false;
    }
}
