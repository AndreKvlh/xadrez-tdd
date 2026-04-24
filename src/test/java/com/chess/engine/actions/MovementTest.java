package com.chess.engine.actions;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.*;
import com.chess.engine.rules.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovementTest {

    private Board board;
    private Validation validation;
    private Movement movement;

    @BeforeEach
    void setUp() {
        board = new Board();
        validation = new Validation();
        movement = new Movement(validation);
    }

    @Test
    void testExecuteMove() {
        Pawn pawn = new Pawn(new Position(0, 1), true);
        board.setPiece(0, 1, pawn);
        Position target = new Position(0, 2);

        movement.executeMove(board, pawn, target);

        assertNull(board.getPiece(0, 1), "A casa de origem deve estar vazia após o movimento");
        assertEquals(pawn, board.getPiece(0, 2), "A peça deve estar na nova posição");
        assertEquals(target, pawn.getPosition(), "A peça deve ter atualizado seu estado interno de posição");
    }

    @Test
    void testValidateMoveLogic() {
        // Setup: Rei branco em 4,4, Torre preta em 4,0 (bloqueada pelo próprio peão em 4,2)
        King king = new King(new Position(4, 4), true);
        Rook enemyRook = new Rook(new Position(4, 0), false);
        Pawn blocker = new Pawn(new Position(4, 2), true);

        board.setPiece(4, 4, king);
        board.setPiece(4, 0, enemyRook);
        board.setPiece(4, 2, blocker);

        // Movimento do bloco que abriria o xeque para o rei
        assertFalse(movement.validateMove(board, blocker, new Position(4, 3)), 
            "Mover o peão que protege o rei deveria ser um movimento ilegal (se resultar em xeque)");
    }
}