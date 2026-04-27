package com.chess.engine.pieces;

public class Rook extends AbstractPiece {

    public Rook(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    protected char getSymbol() {
        return 'R';
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = Math.abs(targetX - this.position.x());
        int dy = Math.abs(targetY - this.position.y());
        return (dy == 0 && dx > 0) || (dx == 0 && dy > 0);
    }

    @Override
    public Piece copy() {
        return new Rook(this.position, this.isWhite);
    }
}