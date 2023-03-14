package chess.domain;

import java.util.List;

public class PieceFixture {
    public static final Piece pawn = new Piece("p", Color.BLACK);
    public static final Piece rook = new Piece("r", Color.BLACK);
    public static final Piece knight = new Piece("n", Color.BLACK);
    public static final Piece bishop = new Piece("b", Color.BLACK);
    public static final Piece queen = new Piece("q", Color.BLACK);
    public static final Piece king = new Piece("k", Color.BLACK);
    public static final Piece empty = new Piece();

    public static final List<Piece> emptyPieces = List.of(empty, empty, empty, empty, empty, empty, empty, empty);
    public static final List<Piece> queenLine = List.of(rook, knight, bishop, queen, king, bishop, knight, rook);
    public static final List<Piece> pawnLine = List.of(pawn, pawn, pawn, pawn, pawn, pawn, pawn, pawn);

}