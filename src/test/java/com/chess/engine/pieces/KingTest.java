package com.chess.engine.pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    private King king;

    @BeforeEach
    void setUp() {
        king = new King(new Position(4, 4), true);
    }

    @Test
    @DisplayName("Deve permitir movimento de uma casa em qualquer direção")
    void testValidShortMove() {
        assertTrue(king.isValidMove(4, 5), "Rei deve mover-se 1 casa verticalmente");
        assertTrue(king.isValidMove(5, 4), "Rei deve mover-se 1 casa horizontalmente");
        assertTrue(king.isValidMove(5, 5), "Rei deve mover-se 1 casa diagonalmente");
    }

    @Test
    @DisplayName("Deve proibir movimento maior que uma casa")
    void testInvalidLongMove() {
        assertFalse(king.isValidMove(4, 6), "Rei NÃO deve mover-se 2 casas verticalmente");
        assertFalse(king.isValidMove(6, 4), "Rei NÃO deve mover-se 2 casas horizontalmente");
        assertFalse(king.isValidMove(6, 6), "Rei NÃO deve mover-se 2 casas diagonalmente");
    }

    @Test
    @DisplayName("Deve retornar a instância correta no copy")
    void testCopy() {
        Piece copy = king.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof King);
        assertNotSame(king, copy);
        assertEquals(king.getPosition(), copy.getPosition());
    }
}