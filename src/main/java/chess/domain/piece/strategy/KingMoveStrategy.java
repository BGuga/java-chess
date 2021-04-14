package chess.domain.piece.strategy;

import chess.domain.board.Position;
import chess.domain.exceptions.InvalidMoveException;
import chess.domain.piece.Piece;

public class KingMoveStrategy extends BasicMoveStrategy {

    private static final int KING_DISTANCE_LIMIT = 1;

    @Override
    void checkValidMove(final Position source, final Position target) {
        checkValidDistance(source, target);
    }

    private void checkValidDistance(final Position source, final Position target) {
        if (Math.abs(source.computeHorizontalDistance(target)) > KING_DISTANCE_LIMIT ||
            Math.abs(source.computeVerticalDistance(target)) > KING_DISTANCE_LIMIT) {
            throw new InvalidMoveException(Piece.OVER_DISTANCE_MESSAGE);
        }
    }
}