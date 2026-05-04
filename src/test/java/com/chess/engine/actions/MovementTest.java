package com.chess.engine.actions;

import com.chess.engine.board.Board;
import com.chess.engine.history.Historic;
import com.chess.engine.pieces.*;
import com.chess.engine.rules.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovementTest {

	private Board board;
    private Validation validation;
    private Movement movement;
    private Historic historic;

    @BeforeEach
    public void setup() {
        board = new Board();
        validation = new Validation();
        historic = new Historic();
        movement = new Movement(validation, historic);
    }

    @Test
    void testExecuteMoveNoCapture() {
        Pawn pawn = new Pawn(new Position(0, 1), true);
        board.setPiece(0, 1, pawn);
        Position target = new Position(0, 2);

        // O método deve retornar null, pois não havia peça no destino
        Piece captured = movement.executeMove(board, pawn, target);

        assertNull(captured, "A captura deve ser nula se a casa de destino estiver vazia");
        assertNull(board.getPiece(0, 1), "Casa de origem vazia");
        assertEquals(pawn, board.getPiece(0, 2), "Peça na casa de destino");
    }

    @Test
    void testExecuteMoveWithCapture() {
        Pawn attacker = new Pawn(new Position(0, 1), true);
        Pawn victim = new Pawn(new Position(0, 2), false);
        
        board.setPiece(0, 1, attacker);
        board.setPiece(0, 2, victim);
        
        // O método deve retornar a peça capturada (victim)
        Piece captured = movement.executeMove(board, attacker, new Position(0, 2));

        assertEquals(victim, captured, "O método deve retornar a peça que estava na casa de destino");
        assertEquals(attacker, board.getPiece(0, 2), "Peça atacante deve ter ocupado a casa");
        assertNull(board.getPiece(0, 1), "Casa de origem vazia");
    }
}