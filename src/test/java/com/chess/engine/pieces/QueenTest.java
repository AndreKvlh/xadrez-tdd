package com.chess.engine.pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QueenTest {

    private Queen queen;

    @BeforeEach
    void setUp() {
        // Rainha no centro para ter liberdade de movimento
        queen = new Queen(new Position(4, 4), true);
    }

    @Test
    @DisplayName("Deve permitir movimento como Torre (Vertical/Horizontal)")
    void testRookLikeMovement() {
        assertTrue(queen.isValidMove(4, 7), "Rainha deve mover-se verticalmente");
        assertTrue(queen.isValidMove(7, 4), "Rainha deve mover-se horizontalmente");
    }

    @Test
    @DisplayName("Deve permitir movimento como Bispo (Diagonal)")
    void testBishopLikeMovement() {
        assertTrue(queen.isValidMove(6, 6), "Rainha deve mover-se diagonalmente");
    }

    @Test
    @DisplayName("Deve proibir movimentos inválidos (estilo Cavalo)")
    void testInvalidKnightMove() {
        assertFalse(queen.isValidMove(5, 6), "Rainha NÃO deve mover-se como um Cavalo (2,1)");
    }

    @Test
    @DisplayName("Deve retornar a instância correta no copy")
    void testCopy() {
        Piece copy = queen.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof Queen);
        assertNotSame(queen, copy);
        assertEquals(queen.getPosition(), copy.getPosition());
    }
}