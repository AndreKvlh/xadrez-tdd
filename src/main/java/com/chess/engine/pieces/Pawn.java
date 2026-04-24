package com.chess.engine.pieces;

public class Pawn extends AbstractPiece {

    private boolean hasMoved = false;

    public Pawn(Position position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    public boolean isValidMove(int targetX, int targetY) {
        int dx = targetX - this.position.x();
        int dy = targetY - this.position.y();
        
        // Direção: -1 para Branco (sobe), 1 para Preto (desce)
        int direction = isWhite ? -1 : 1;

        // 1. Movimento padrão (uma casa à frente)
        if (dx == 0 && dy == direction) {
            return true;
        }

        // 2. Primeiro movimento (duas casas à frente)
        if (!hasMoved && dx == 0 && dy == 2 * direction) {
            return true;
        }

        // 3. Captura (diagonal à frente)
        // Nota: Geometricamente é válido, a verificação se há peça inimiga 
        // ficará na lógica do Board posteriormente.
        if (Math.abs(dx) == 1 && dy == direction) {
            return true;
        }

        return false;
    }

    @Override
    public Piece copy() {
        Pawn pawnCopy = new Pawn(this.position, this.isWhite);
        pawnCopy.setHasMoved(this.hasMoved); // Importante copiar o estado!
        return pawnCopy;
    }

    // Método necessário para atualizar o estado quando o movimento for efetivado
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean hasMoved() {
        return hasMoved;
    }
}