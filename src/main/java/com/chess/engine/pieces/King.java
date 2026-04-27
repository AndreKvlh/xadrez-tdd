package com.chess.engine.pieces;

public class King extends AbstractPiece {

    public King(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    protected char getSymbol() {
        return 'K';
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = Math.abs(targetX - this.position.x());
        int dy = Math.abs(targetY - this.position.y());
        return (dx <= 1 && dy <= 1) && (dx > 0 || dy > 0);
    }

    @Override
    public Piece copy() {
        return new King(this.position, this.isWhite);
    }
}