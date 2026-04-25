package com.chess.engine.actions;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.*;
import com.chess.engine.rules.Validation;

public class Movement {

    private final Validation validation;

    public Movement(Validation validation) {
        this.validation = validation;
    }

    /**
     * Executa o movimento de uma peça no tabuleiro, atualizando sua posição 
     * e retornando a peça capturada, caso exista.
     */
    public Piece executeMove(Board board, Piece piece, Position target) {
        Piece capturedPiece = board.getPiece(target.x(), target.y());

        board.removePiece(piece.getPosition().x(), piece.getPosition().y(), piece);
        
        piece.setPosition(target);
        board.setPiece(target.x(), target.y(), piece);

        if (piece instanceof Pawn) {
            ((Pawn) piece).setHasMoved(true);
        }

        return capturedPiece;
    }

    /**
     * Valida se um movimento é legal, verificando regras geométricas, 
     * restrições de jogo e se o movimento coloca o rei em xeque.
     */
    public boolean validateMove(Board board, Piece piece, Position target) {
        if (!piece.isValidMove(target.x(), target.y())) {
            return false;
        }

        if (validation.isAlliedPiece(board, piece, target)) return false;
        if (validation.isCovered(board, piece, target)) return false;
        if (piece instanceof Pawn && !validation.pawnCanAttack(board, (Pawn) piece, target)) return false;

        Board tempBoard = new Board(board); 
        Piece tempPiece = tempBoard.getPiece(piece.getPosition().x(), piece.getPosition().y());
        
        executeMove(tempBoard, tempPiece, target);

        return !validation.isUnderCheck(tempBoard, piece.isWhite());
    }
}