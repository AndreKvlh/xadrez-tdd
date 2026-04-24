package com.chess.engine.pieces;

public class Rook extends AbstractPiece {

    public Rook(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = Math.abs(targetX - this.position.x());
        int dy = Math.abs(targetY - this.position.y());

        // Para a Torre ser válida:
        // 1. Deve haver movimento (dx > 0 ou dy > 0)
        // 2. Deve ser puramente horizontal (dy == 0) OU puramente vertical (dx == 0)
        boolean isHorizontal = (dy == 0 && dx > 0);
        boolean isVertical = (dx == 0 && dy > 0);

        return isHorizontal || isVertical;
    }

    @Override
    public Piece copy() {
        return new Rook(this.position, this.isWhite);
    }
}