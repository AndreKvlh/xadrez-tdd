package com.chess.engine.actions;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.*;
import com.chess.engine.rules.Validation;

public class Movement {

    private final Validation validation;

    public Movement(Validation validation) {
        this.validation = validation;
    }

    public void executeMove(Board board, Piece piece, Position target) {
        // 1. Remove a peça da posição atual
        // Usamos as coordenadas atuais da peça antes de atualizar sua posição
        int currentX = piece.getPosition().x();
        int currentY = piece.getPosition().y();
        
        board.removePiece(currentX, currentY, piece);
        
        // 2. Atualiza a posição da peça para o alvo
        piece.setPosition(target);
        
        // 3. Insere a peça na nova posição no tabuleiro
        // (Isso automaticamente substituirá qualquer peça inimiga, realizando a captura)
        board.setPiece(target.x(), target.y(), piece);

        // 4. Lógica de estado do Peão
        if (piece instanceof Pawn) {
            ((Pawn) piece).setHasMoved(true);
        }
    }

    public boolean validateMove(Board board, Piece piece, Position target) {
        // 1. Validação Geométrica
        if (!piece.isValidMove(target.x(), target.y())) {
            return false;
        }

        // 2. Validação de Regras (Allied, Covered, Pawn Attack)
        if (validation.isAlliedPiece(board, piece, target)) return false;
        if (validation.isCovered(board, piece, target)) return false;
        if (piece instanceof Pawn && !validation.pawnCanAttack(board, (Pawn) piece, target)) return false;

        // 3. Validação de Xeque (Simulação Virtual)
        // Usamos o Construtor de Cópia que você criou
        Board tempBoard = new Board(board); 
        
        // Buscamos a peça equivalente no tabuleiro temporário
        // A posição é a mesma, mas a instância do objeto é nova (deepcopy)
        Piece tempPiece = tempBoard.getPiece(piece.getPosition().x(), piece.getPosition().y());
        
        // Executamos o movimento virtual no tabuleiro temporário
        executeMove(tempBoard, tempPiece, target);

        // Retorna TRUE se, após o movimento, o Rei NÃO estiver em xeque
        return !validation.isUnderCheck(tempBoard, piece.isWhite());
    }
}