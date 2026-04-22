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
    
    @Override
    public Piece copy() {
        return new Knight(this.x, this.y, this.isWhite);
    }
}