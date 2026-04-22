package com.chess.engine.pieces;

public class Knight extends AbstractPiece {

    public Knight(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = Math.abs(targetX - this.position.x());
        int dy = Math.abs(targetY - this.position.y());

        return (dx == 1 && dy == 2) || (dx == 2 && dy == 1);
    }

    @Override
    public Piece copy() {
        return new Knight(this.position, this.isWhite);
    }
}