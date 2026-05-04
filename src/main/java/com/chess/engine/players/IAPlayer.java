package com.chess.engine.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.actions.Move;
import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Position;
import com.chess.engine.rules.Validation;

public class IAPlayer extends AbstractPlayer {
    private final Validation validation;
    private final Movement movement;
    private final Random random;

    public IAPlayer(boolean isWhite, Validation validation, Movement movement) {
        super(isWhite);
        this.validation = validation;
        this.movement = movement;
        this.random = new Random();
    }

    @Override
    public Move selectPiece(Board board) {
        List<Move> legalMoves = getLegalMoves(board);
        
        if (legalMoves.isEmpty()) return null;

        // 1. Prioridade: Se estiver em xeque, buscar qualquer movimento legal que saia do xeque
        // (O movement.validateMove já garante que o movimento não termina em xeque)
        if (validation.isUnderCheck(board, this.isWhite)) {
            System.out.println("IA em XEQUE! Buscando movimento de fuga...");
            return legalMoves.get(0); // Apenas retorna o primeiro movimento legal disponível
        }

        // 2. IA Gulosa: Se houver captura disponível, realiza a captura
        Move greedyMove = calculateGreedyMove(legalMoves, board);
        if (greedyMove != null) {
            System.out.println("IA encontrou uma captura!");
            return greedyMove;
        }

        // 3. Fallback: Movimento aleatório
        return calculateRandomMove(legalMoves);
    }

    /**
     * Varre todas as peças do jogador e testa todos os movimentos possíveis no tabuleiro.
     */
    private List<Move> getLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        // Criamos uma cópia para evitar erros de concorrência e garantir integridade
        List<Piece> piecesToProcess = new ArrayList<>(this.pieces);

        for (Piece piece : piecesToProcess) {
            // VERIFICAÇÃO CRÍTICA: Se a peça na posição informada não for a própria peça,
            // significa que ela foi capturada ou movida indevidamente.
            Piece pieceOnBoard = board.getPiece(piece.getPosition().x(), piece.getPosition().y());
            if (pieceOnBoard != piece) {
                continue; 
            }

            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    Position target = new Position(x, y);
                    if (movement.validateMove(board, piece, target)) {
                        legalMoves.add(new Move(piece.getPosition(), target));
                    }
                }
            }
        }
        return legalMoves;
    }

    /**
     * Procura por movimentos que terminam em uma casa ocupada por um inimigo.
     */
    private Move calculateGreedyMove(List<Move> legalMoves, Board board) {
        for (Move move : legalMoves) {
            Piece targetPiece = board.getPiece(move.target().x(), move.target().y());
            if (targetPiece != null && targetPiece.isWhite() != this.isWhite) {
                return move; // Retorna o primeiro movimento de captura encontrado
            }
        }
        return null;
    }

    private Move calculateRandomMove(List<Move> legalMoves) {
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }
    
    @Override
    public Player copy(Board newBoard) {
        IAPlayer copy = new IAPlayer(this.isWhite, this.validation, this.movement);
        copy.getPieces().clear();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = newBoard.getPiece(x, y);
                if (piece != null && piece.isWhite() == this.isWhite) {
                    copy.getPieces().add(piece);
                }
            }
        }
        return copy;
    }
}