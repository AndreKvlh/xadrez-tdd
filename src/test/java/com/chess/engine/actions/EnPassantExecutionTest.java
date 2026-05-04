package com.chess.engine.actions;

import com.chess.engine.board.Board;
import com.chess.engine.history.Historic;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Position;
import com.chess.engine.pieces.Piece;
import com.chess.engine.rules.Validation;
import com.chess.engine.players.Player;
import com.chess.engine.players.HumanPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnPassantExecutionTest {
    private Board board;
    private Movement movement;
    private Historic historic;

    @BeforeEach
    public void setup() {
        Validation validation = new Validation();
        board = new Board();
        historic = new Historic();
        movement = new Movement(validation, historic);
    }

    @Test
    public void testExecuteEnPassant() {
        // 1. Setup: Peão Branco em (3,3)
        Pawn whitePawn = new Pawn(new Position(3, 3), true);
        board.setPiece(3, 3, whitePawn);

        // 2. Setup: Peão Preto acaba de mover de (4,1) para (4,3)
        Pawn blackPawn = new Pawn(new Position(4, 3), false);
        board.setPiece(4, 3, blackPawn);
        Move lastMove = new Move(new Position(4, 1), new Position(4, 3));
        Player[] dummyPlayers = {new HumanPlayer(true), new HumanPlayer(false)};
        historic.addEntry(1, board, dummyPlayers, lastMove);

        // 3. Ação: Branco captura En Passant em (4,2)
        Position target = new Position(4, 2);
        Piece captured = movement.executeMove(board, whitePawn, target);

        // Assert: O peão capturado deve ser o peão preto
        assertEquals(blackPawn, captured, "A peça retornada deve ser o peão preto capturado.");
        assertNull(board.getPiece(4, 3), "O peão preto na casa (4,3) deve ter sido removido.");
        assertEquals(whitePawn, board.getPiece(4, 2), "O peão branco deve estar na casa (4,2).");
        assertTrue(whitePawn.hasMoved());
    }
}