package com.chess.engine.pieces;

public class Bishop extends AbstractPiece {

    public Bishop(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    protected char getSymbol() {
        return 'B';
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = Math.abs(targetX - this.position.x());
        int dy = Math.abs(targetY - this.position.y());
        return dx > 0 && dx == dy;
    }

    @Override
    public Piece copy() {
        return new Bishop(this.position, this.isWhite);
    }
}