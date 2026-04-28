package com.chess.engine.rules;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InsufficientMaterialTest {
    private Board board;
    private Validation validation;

    @BeforeEach
    public void setup() {
        board = new Board();
        validation = new Validation();
    }

    @Test
    public void testKingVsKingIsInsufficientMaterial() {
        // Cenário: Apenas reis no tabuleiro
        board.setPiece(4, 0, new King(new Position(4, 0), false)); // Rei Preto
        board.setPiece(4, 7, new King(new Position(4, 7), true));  // Rei Branco

        assertTrue(validation.isInsufficientMaterial(board), 
            "Deveria ser empate por material insuficiente (Rei vs Rei).");
    }
}