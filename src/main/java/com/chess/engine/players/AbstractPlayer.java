package com.chess.engine.players;

import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Position;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayer implements Player {
    protected final List<Piece> pieces = new ArrayList<>();
    protected final List<Piece> capturedPieces = new ArrayList<>();
    protected final boolean isWhite;

    protected AbstractPlayer(boolean isWhite) {
        this.isWhite = isWhite;
    }

    // Métodos de utilidade comuns para convergir coordenadas
    protected Position parsePosition(String pos) {
        char colChar = pos.toUpperCase().charAt(0);
        char rowChar = pos.charAt(1);

        if (colChar < 'A' || colChar > 'H' || rowChar < '1' || rowChar > '8') {
            throw new IllegalArgumentException("Posição fora do tabuleiro: " + pos);
        }

        int x = colChar - 'A';
        int y = 7 - (rowChar - '1'); // A1 -> x=0, y=7
        return new Position(x, y);
    }

    protected boolean isValidFormat(String input) {
        return input != null && input.length() == 5 && input.charAt(2) == ' ';
    }

    // Getters mantidos...
    @Override public List<Piece> getPieces() { return pieces; }
    @Override public List<Piece> getCapturedPieces() { return capturedPieces; }
    @Override public boolean isWhite() { return isWhite; }
    public void addPiece(Piece piece) { this.pieces.add(piece); }
}