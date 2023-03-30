package chess.domain.game;

import chess.domain.board.BlackScore;
import chess.domain.board.WhiteScore;

public enum GameResult {
    BLACK_WIN("검정색 우승"),
    WHITE_WIN("흰색 우승"),
    DRAW("무승부");

    private final String message;

    GameResult(String message) {
        this.message = message;
    }

    public static GameResult getGameResultOf(WhiteScore white, BlackScore black) {
        if (black.getScore() > white.getScore()) {
            return BLACK_WIN;
        }

        if (black.getScore() < white.getScore()) {
            return WHITE_WIN;
        }

        return DRAW;
    }

    public String getMessage() {
        return message;
    }
}
