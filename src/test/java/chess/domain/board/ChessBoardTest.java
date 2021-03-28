package chess.domain.board;

import chess.domain.piece.*;
import chess.domain.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ChessBoardTest {

    private Map<Coordinate, Cell> cells;
    private ChessBoard chessBoard;

    @BeforeEach
    void setup() {
        cells = Arrays.stream(File.values())
                .flatMap(file -> Arrays.stream(Rank.values()).map(rank -> new Coordinate(file, rank)))
                .collect(Collectors.toMap(coordinate -> coordinate, value -> new Cell()));
        chessBoard = new ChessBoard(cells);
    }

    @DisplayName("체스 보드의 크기가 8*8이 아니면 예외가 발생한다.")
    @Test
    void cannotMakeChessBoard() {
        assertThatCode(() -> new ChessBoard(new HashMap<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 크기의 체스보드입니다.");
    }

    @DisplayName("점수 계산")
    @Test
    void calculateScores() {
        cells.get(Coordinate.from("b8")).put(new King(TeamType.BLACK));
        cells.get(Coordinate.from("c8")).put(new Rook(TeamType.BLACK));
        cells.get(Coordinate.from("a7")).put(new Pawn(TeamType.BLACK));
        cells.get(Coordinate.from("c7")).put(new Pawn(TeamType.BLACK));
        cells.get(Coordinate.from("d7")).put(new Bishop(TeamType.BLACK));
        cells.get(Coordinate.from("b6")).put(new Pawn(TeamType.BLACK));
        cells.get(Coordinate.from("e6")).put(new Queen(TeamType.BLACK));
        cells.get(Coordinate.from("f4")).put(new Knight(TeamType.WHITE));
        cells.get(Coordinate.from("g4")).put(new Queen(TeamType.WHITE));
        cells.get(Coordinate.from("f3")).put(new Pawn(TeamType.WHITE));
        cells.get(Coordinate.from("h3")).put(new Pawn(TeamType.WHITE));
        cells.get(Coordinate.from("f2")).put(new Pawn(TeamType.WHITE));
        cells.get(Coordinate.from("g2")).put(new Pawn(TeamType.WHITE));
        cells.get(Coordinate.from("e1")).put(new Rook(TeamType.WHITE));
        cells.get(Coordinate.from("f1")).put(new King(TeamType.WHITE));

        Result result = chessBoard.calculateScores();

        assertThat(result.getBlackTeamScore()).isEqualTo(20);
        assertThat(result.getWhiteTeamScore()).isEqualTo(19.5);
    }

    @DisplayName("체크메이트 상태가 아니라 승리한 팀을 찾을 수 없다.")
    @Test
    void cannotFindWinnerTeam() {
        assertThatCode(() -> chessBoard.findWinnerTeam())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("승리한 팀을 찾을 수 없습니다.");
    }

    @DisplayName("체크메이트 상태인 경우 승리한 팀을 반환한다.")
    @Test
    void findWinnerTeam() {
        cells.get(Coordinate.from("c1")).put(new King(TeamType.WHITE));

        TeamType winnerTeam = chessBoard.findWinnerTeam();

        assertThat(winnerTeam).isEqualTo(TeamType.WHITE);
    }

    @DisplayName("현재 위치에서 도착 위치 직전까지의 경로에 기물이 존재하는 경우 true를 반환한다.")
    @Test
    void hasPieceOnRoute_True() {
        Coordinate current = Coordinate.from("c1");
        Coordinate route = Coordinate.from("c2");
        Coordinate destination = Coordinate.from("c3");

        Direction direction = Direction.UP;
        cells.get(route).put(new Bishop(TeamType.WHITE));
        boolean hasPieceOnRoute = chessBoard.hasPieceOnRouteBeforeDestination(current, destination);

        assertThat(hasPieceOnRoute).isTrue();
    }

    @DisplayName("현재 위치에서 도착 위치 직전까지의 경로에 기물이 존재하지 않는 경우 false를 반환한다.")
    @Test
    void hasPieceOnRoute_False() {
        Coordinate current = Coordinate.from("c1");
        Coordinate destination = Coordinate.from("c2");

        boolean hasPieceOnRoute = chessBoard.hasPieceOnRouteBeforeDestination(current, destination);

        assertThat(hasPieceOnRoute).isFalse();
    }

    @DisplayName("move 메서드는")
    @Nested
    class Describe_Move {

        private Coordinate current = Coordinate.from("d5");
        private Coordinate destination = Coordinate.from("e6");

        @BeforeEach
        void setup() {
            cells.get(current)
                    .put(new King(TeamType.WHITE));
        }

        @DisplayName("현재 위치에 기물이 없으면 예외를 발생시킨다.")
        @Test
        void cannotMoveWhenNoPiece() {
            Coordinate invalidCurrent = Coordinate.from("c1");

            assertThatCode(() -> chessBoard.move(invalidCurrent, destination, TeamType.WHITE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("조작할 수 있는 기물이 없습니다.");
        }

        @DisplayName("현재 위치에 기물이 있더라도 팀이 다르면 예외를 발생시킨다.")
        @Test
        void cannotMoveWhenInvalidTeam() {
            assertThatCode(() -> chessBoard.move(current, destination, TeamType.BLACK))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("조작할 수 있는 기물이 없습니다.");
        }

        @DisplayName("도착 지점이 현재 위치와 동일하면 예외가 발생한다.")
        @Test
        void cannotMoveWhenCurrentAndDestinationSame() {
            assertThatCode(() -> chessBoard.move(current, current, TeamType.WHITE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("현재 위치와 도착 위치가 동일합니다.");
        }

        @DisplayName("현재의 기물이 도착 위치로 이동할 수 없는 경우 예외가 발생한다.")
        @Test
        void cannotMoveWhenPieceIsNotMovable() {
            Coordinate wrongDestination = Coordinate.from("d7");

            assertThatCode(() -> chessBoard.move(current, wrongDestination, TeamType.WHITE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("이동할 수 없는 도착 위치 입니다.");
        }

        @DisplayName("정상적으로 이동하면 ChessBoard가 갱신된다.")
        @Test
        void completeMovement() {
            Piece pieceOnCurrentCell = cells.get(current).getPiece();

            chessBoard.move(current, destination, TeamType.WHITE);
            Piece pieceOnDestinationCell = cells.get(destination).getPiece();

            assertThat(pieceOnDestinationCell).isEqualTo(pieceOnCurrentCell);
        }
    }

    @DisplayName("CheckMate 메서드는")
    @Nested
    class Describe_isKingCheckMate {

        private Piece whiteKing = new King(TeamType.WHITE);
        private Piece blackKing = new King(TeamType.BLACK);

        @DisplayName("양 팀의 킹이 모두 살아 있으면 false를 반환한다.")
        @Test
        void allKingsAlive() {
            cells.get(Coordinate.from("c1")).put(blackKing);
            cells.get(Coordinate.from("c2")).put(whiteKing);

            assertThat(chessBoard.isKingCheckmate()).isFalse();
        }

        @DisplayName("킹이 한 명만 존재하는 경우 true를 반환한다.")
        @Test
        void oneKingAlive() {
            cells.get(Coordinate.from("c1")).put(blackKing);

            assertThat(chessBoard.isKingCheckmate()).isTrue();
        }
    }
}