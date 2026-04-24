package com.chess.engine.pieces;

public class Queen extends AbstractPiece {

    public Queen(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = Math.abs(targetX - this.position.x());
        int dy = Math.abs(targetY - this.position.y());

        // A lógica combinada:
        // 1. (dx == 0 || dy == 0) -> Movimento de Torre (Horizontal/Vertical)
        // 2. (dx == dy) -> Movimento de Bispo (Diagonal)
        // 3. (dx > 0 || dy > 0) -> Garante que ela se mova (não fique parada)
        return (dx == 0 || dy == 0 || dx == dy) && (dx > 0 || dy > 0);
    }

    @Override
    public Piece copy() {
        return new Queen(this.position, this.isWhite);
    }
}