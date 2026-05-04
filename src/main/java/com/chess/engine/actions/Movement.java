package com.chess.engine.actions;

import com.chess.engine.board.Board;
import com.chess.engine.history.Historic;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Position;
import com.chess.engine.pieces.Rook;
import com.chess.engine.rules.Validation;

public class Movement {

	private final Validation validation;
    private final Historic historic; // Novo campo

    // Atualizamos o construtor para receber ambos
    public Movement(Validation validation, Historic historic) {
        this.validation = validation;
        this.historic = historic;
    }

    /**
     * Executa o movimento de uma peça no tabuleiro, atualizando sua posição 
     * e retornando a peça capturada, caso exista.
     */
    public Piece executeMove(Board board, Piece piece, Position target) {
    		if (piece instanceof King && Math.abs(target.x() - piece.getPosition().x()) == 2) {
            executeCastling(board, (King) piece, target.x(), target.y());
            return null; // Roque não captura peça adversária
        }
    		
    		// Verifica se é En Passant antes de executar o movimento padrão
        if (piece instanceof Pawn && validation.isEnPassant(board, (Pawn) piece, target, historic)) {
            return executeEnPassant(board, (Pawn) piece, target);
        }
    		
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
     * Valida se um movimento é permitido, verificando regras de peça, 
     * colisões e xeque.
     */
    public boolean validateMove(Board board, Piece piece, Position target) {
        // Se a peça passada for nula, o movimento é logicamente impossível
        if (piece == null) return false; 
        
        if (!piece.isValidMove(target.x(), target.y())) return false;
        if (validation.isAlliedPiece(board, piece, target)) return false;
        if (validation.isCovered(board, piece, target)) return false;
        if (piece instanceof Pawn && !validation.pawnCanAttack(board, (Pawn) piece, target)) return false;

        boolean isCastling = (piece instanceof King && Math.abs(target.x() - piece.getPosition().x()) == 2);

        if (isCastling) {
            if (validation.isUnderCheck(board, piece.isWhite())) return false;
            if (!validation.isCastling(board, (King) piece, target.x(), target.y())) return false;
        }

        Board tempBoard = new Board(board);
        Piece tempPiece = tempBoard.getPiece(piece.getPosition().x(), piece.getPosition().y());

        // Verificação extra antes de simular o movimento no tabuleiro temporário
        if (tempPiece == null) return false;

        executeMove(tempBoard, tempPiece, target);

        return !validation.isUnderCheck(tempBoard, piece.isWhite());
    }
    
    public void executeCastling(Board board, King king, int targetX, int targetY) {
        int kingX = king.getPosition().x();
        int y = king.getPosition().y();

        // 1. Move o Rei
        board.removePiece(kingX, y, null);
        king.setPosition(new Position(targetX, y)); // Certifique-se de que esse método exista em AbstractPiece
        board.setPiece(targetX, y, king);
        king.setHasMoved(true);

        // 2. Calcula posição da Torre e a move
        // Se targetX > kingX (Roque Curto/Direita), Torre sai de 7 e vai para 5
        // Se targetX < kingX (Roque Longo/Esquerda), Torre sai de 0 e vai para 3
        int oldRookX = (targetX > kingX) ? 7 : 0;
        int newRookX = (targetX > kingX) ? 5 : 3;

        Piece rook = board.getPiece(oldRookX, y);
        
        board.removePiece(oldRookX, y, null);
        rook.setPosition(new Position(newRookX, y));
        board.setPiece(newRookX, y, rook);
        ((Rook) rook).setHasMoved(true);
    }
    
    /**
     * Executa o movimento de En Passant, removendo o peão adversário que 
     * está na lateral e movendo o peão atacante para o destino.
     */
    public Piece executeEnPassant(Board board, Pawn pawn, Position target) {
        // No En Passant, o peão capturado está na mesma coluna do target,
        // mas na linha de origem do peão atacante (y original).
        int capturedPawnX = target.x();
        int capturedPawnY = pawn.getPosition().y();
        
        // 1. Identifica e remove a peça capturada (o peão adversário ao lado)
        Piece capturedPiece = board.getPiece(capturedPawnX, capturedPawnY);
        board.removePiece(capturedPawnX, capturedPawnY, capturedPiece);
        
        // 2. Remove o peão atacante da posição atual
        board.removePiece(pawn.getPosition().x(), pawn.getPosition().y(), pawn);
        
        // 3. Move o peão atacante para a casa de destino (atrás de onde estava o adversário)
        pawn.setPosition(target);
        board.setPiece(target.x(), target.y(), pawn);
        pawn.setHasMoved(true);
        
        return capturedPiece;
    }
}