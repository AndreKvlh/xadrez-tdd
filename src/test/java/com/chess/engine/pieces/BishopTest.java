package com.chess.engine.pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BishopTest {

    private Bishop bishop;

    @BeforeEach
    void setUp() {
        // Bispo no centro do tabuleiro (4, 4) para permitir diagonais livres
        bishop = new Bishop(new Position(4, 4), true);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro para movimento diagonal válido")
    void testValidDiagonalMove() {
        // Movendo 2 casas na diagonal (de 4,4 para 6,6)
        assertTrue(bishop.isValidMove(6, 6), "Bispo deve poder mover-se diagonalmente");
    }

    @Test
    @DisplayName("Deve retornar falso para movimento horizontal ou vertical")
    void testInvalidStraightMove() {
        assertFalse(bishop.isValidMove(4, 6), "Bispo NÃO deve mover-se na vertical");
        assertFalse(bishop.isValidMove(6, 4), "Bispo NÃO deve mover-se na horizontal");
    }

    @Test
    @DisplayName("Deve retornar a instância correta no copy")
    void testCopy() {
        Piece copy = bishop.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof Bishop);
        assertNotSame(bishop, copy);
        assertEquals(bishop.getPosition(), copy.getPosition());
    }
}