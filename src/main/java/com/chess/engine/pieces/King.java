package com.chess.engine.pieces;

public class King extends AbstractPiece {
    private boolean hasMoved = false;

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

        // Movimento padrão: 1 casa em qualquer direção
        boolean standardMove = (dx <= 1 && dy <= 1) && (dx > 0 || dy > 0);
        
        // Movimento de Roque: 2 casas na horizontal (dy == 0)
        boolean castlingMove = (dx == 2 && dy == 0);

        return standardMove || castlingMove;
    }

    @Override
    public Piece copy() {
        King copy = new King(this.position, this.isWhite);
        copy.setHasMoved(this.hasMoved);
        return copy;
    }

    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
    public boolean hasMoved() { return hasMoved; }
}