package com.chess.engine.pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    private Rook rook;

    @BeforeEach
    void setUp() {
        rook = new Rook(new Position(0, 0), true);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro para movimento vertical")
    void testValidVerticalMove() {
        assertTrue(rook.isValidMove(0, 5), "Torre deve poder se mover verticalmente");
    }

    @Test
    @DisplayName("Deve retornar verdadeiro para movimento horizontal")
    void testValidHorizontalMove() {
        assertTrue(rook.isValidMove(5, 0), "Torre deve poder se mover horizontalmente");
    }

    @Test
    @DisplayName("Deve retornar falso para movimento diagonal")
    void testInvalidDiagonalMove() {
        assertFalse(rook.isValidMove(1, 1), "Torre NÃO deve poder se mover na diagonal");
    }
    
    @Test
    @DisplayName("Deve retornar a instância correta no copy")
    void testCopy() {
        Piece copy = rook.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof Rook);
        assertNotSame(rook, copy);
        assertEquals(rook.getPosition(), copy.getPosition());
    }
}