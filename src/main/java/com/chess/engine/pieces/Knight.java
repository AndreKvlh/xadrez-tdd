package com.chess.engine.pieces;

public class Knight extends AbstractPiece {
    public Knight(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = Math.abs(targetX - this.x);
        int dy = Math.abs(targetY - this.y);

        return (dx == 1 && dy == 2) || (dx == 2 && dy == 1);
    }
}