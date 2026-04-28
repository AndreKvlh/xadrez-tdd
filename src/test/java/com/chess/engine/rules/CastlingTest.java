package com.chess.engine.rules;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Position;
import com.chess.engine.pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CastlingTest {
    private Board board;
    private Validation validation;

    @BeforeEach
    public void setup() {
        board = new Board();
        validation = new Validation();
    }

    @Test
    public void testKingSideCastlingPossible() {
        // Setup: Rei em E1 (4,7) e Torre em H1 (7,7)
        King king = new King(new Position(4, 7), true);
        Rook rook = new Rook(new Position(7, 7), true);
        
        board.setPiece(4, 7, king);
        board.setPiece(7, 7, rook);

        // O alvo do roque para o rei é (6, 7)
        boolean result = validation.isCastling(board, king, 6, 7);

        assertTrue(result, "O Roque deveria ser possível com as peças na posição inicial.");
    }
}	