package com.chess.engine.pieces;

public class Queen extends AbstractPiece {

    public Queen(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    protected char getSymbol() {
        return 'Q';
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = Math.abs(targetX - this.position.x());
        int dy = Math.abs(targetY - this.position.y());
        return (dx == 0 || dy == 0 || dx == dy) && (dx > 0 || dy > 0);
    }

    @Override
    public Piece copy() {
        return new Queen(this.position, this.isWhite);
    }
}