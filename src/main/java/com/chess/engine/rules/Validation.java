package com.chess.engine.rules;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.*;

public class Validation {

    public boolean isAlliedPiece(Board board, Piece movingPiece, Position target) {
        Piece targetPiece = board.getPiece(target.x(), target.y());
        if (targetPiece == null) return false;
        return targetPiece.isWhite() == movingPiece.isWhite();
    }

    public boolean isCovered(Board board, Piece movingPiece, Position target) {
        // Cavalo pula, então não precisa verificar cobertura
        if (movingPiece instanceof Knight) return false;

        int startX = movingPiece.getPosition().x();
        int startY = movingPiece.getPosition().y();
        int targetX = target.x();
        int targetY = target.y();

        int dx = Integer.compare(targetX, startX);
        int dy = Integer.compare(targetY, startY);

        int currentX = startX + dx;
        int currentY = startY + dy;

        // Itera pelo caminho, parando antes da casa de destino
        while (currentX != targetX || currentY != targetY) {
            if (board.getPiece(currentX, currentY) != null) {
                return true; // Existe uma peça bloqueando o caminho
            }
            currentX += dx;
            currentY += dy;
        }
        return false;
    }

    public boolean pawnCanAttack(Board board, Pawn pawn, Position target) {
        int dx = Math.abs(target.x() - pawn.getPosition().x());
        int dy = target.y() - pawn.getPosition().y();
        int direction = pawn.isWhite() ? -1 : 1;

        // Se for movimento diagonal (ataque)
        if (dx == 1 && dy == direction) {
            Piece targetPiece = board.getPiece(target.x(), target.y());
            // Só pode atacar se for inimigo
            return targetPiece != null && targetPiece.isWhite() != pawn.isWhite();
        }

        // Se for movimento reto (avanço)
        if (dx == 0 && dy == direction) {
            // Só pode avançar se a casa estiver vazia
            return board.getPiece(target.x(), target.y()) == null;
        }

        return false;
    }

    public boolean isPromoted(Piece piece) {
        if (!(piece instanceof Pawn)) return false;
        // Se branca, atinge linha 0; se preta, atinge linha 7
        return piece.isWhite() ? piece.getPosition().y() == 0 : piece.getPosition().y() == 7;
    }

    public boolean isUnderCheck(Board board, boolean isWhiteKing) {
        Position kingPos = null;

        // 1. Localizar o Rei
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece p = board.getPiece(x, y);
                if (p instanceof King && p.isWhite() == isWhiteKing) {
                    kingPos = p.getPosition();
                    break;
                }
            }
        }

        if (kingPos == null) return false; // Rei não encontrado (situação de erro)

        // 2. Verificar se alguma peça inimiga ataca o Rei
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece enemy = board.getPiece(x, y);
                if (enemy != null && enemy.isWhite() != isWhiteKing) {
                    // Verifica se a peça inimiga alcança o rei (considerando regras de movimento e bloqueios)
                    if (enemy.isValidMove(kingPos.x(), kingPos.y()) && 
                        !isCovered(board, enemy, kingPos)) {
                        return true; 
                    }
                }
            }
        }
        return false;
    }
}