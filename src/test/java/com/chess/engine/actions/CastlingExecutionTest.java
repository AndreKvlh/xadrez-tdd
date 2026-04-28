package com.chess.engine.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Position;
import com.chess.engine.pieces.Rook;
import com.chess.engine.rules.Validation;

public class CastlingExecutionTest {
    private Board board;
    private Validation validation;
    private Movement movement;

    @BeforeEach
    public void setup() {
        board = new Board();
        validation = new Validation();
        movement = new Movement(validation);
    }

    @Test
    public void testExecuteKingSideCastling() {
        // Setup: Rei em E1 (4,7) e Torre em H1 (7,7) - Brancas
        King king = new King(new Position(4, 7), true);
        Rook rook = new Rook(new Position(7, 7), true);
        
        board.setPiece(4, 7, king);
        board.setPiece(7, 7, rook);

        // Ação: Executar Roque curto (alvo do Rei é 6,7)
        movement.executeCastling(board, king, 6, 7);

        // Assert: Rei deve estar em (6,7) e Torre em (5,7)
        assertEquals(king, board.getPiece(6, 7), "O Rei deveria ter se movido para G1 (6,7).");
        assertTrue(board.getPiece(5, 7) instanceof Rook, "A Torre deveria ter se movido para F1 (5,7).");
        
        // Assert: As posições originais devem estar vazias
        assertNull(board.getPiece(4, 7), "A casa original do Rei (E1) deve estar vazia.");
        assertNull(board.getPiece(7, 7), "A casa original da Torre (H1) deve estar vazia.");
        
        // Assert: Estado hasMoved deve ser atualizado
        assertTrue(king.hasMoved(), "O Rei deveria estar marcado como 'já movido'.");
        assertTrue(((Rook) board.getPiece(5, 7)).hasMoved(), "A Torre deveria estar marcada como 'já movida'.");
    }
}