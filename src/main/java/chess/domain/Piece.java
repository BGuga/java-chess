package chess.domain;

public class Piece {
    private final String name;

    public Piece(String name) {
        this.name = name;
    }

    public Piece() {
        this(".");
    }

    public String getName() {
        return name;
    }
}
