package com.chess.engine.pieces;

public class Bishop extends AbstractPiece {

    public Bishop(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = Math.abs(targetX - this.position.x());
        int dy = Math.abs(targetY - this.position.y());

        // Para o Bispo:
        // 1. Deve haver movimento (dx > 0)
        // 2. A variação em X deve ser igual à variação em Y para ser diagonal
        return dx > 0 && dx == dy;
    }

    @Override
    public Piece copy() {
        return new Bishop(this.position, this.isWhite);
    }
}