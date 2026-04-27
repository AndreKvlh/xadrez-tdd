package com.chess.engine.pieces;

public class Pawn extends AbstractPiece {
    private boolean hasMoved = false;

    public Pawn(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    protected char getSymbol() {
        return 'P';
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = targetX - this.position.x();
        int dy = targetY - this.position.y();
        int direction = isWhite ? -1 : 1;

        if (dx == 0 && dy == direction) return true;
        if (!hasMoved && dx == 0 && dy == 2 * direction) return true;
        if (Math.abs(dx) == 1 && dy == direction) return true;

        return false;
    }

    @Override
    public Piece copy() {
        Pawn pawnCopy = new Pawn(this.position, this.isWhite);
        pawnCopy.setHasMoved(this.hasMoved);
        return pawnCopy;
    }

    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
    public boolean hasMoved() { return hasMoved; }
}