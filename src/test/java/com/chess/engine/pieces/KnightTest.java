package com.chess.engine.pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KnightTest {

    private Knight knight;

    @BeforeEach
    void setUp() {
        knight = new Knight(new Position(0, 0), true);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro para um movimento válido em L")
    void testValidLMove() {
        assertTrue(knight.isValidMove(1, 2));
    }

    @Test
    @DisplayName("Deve retornar falso para um movimento inválido")
    void testInvalidMove() {
        assertFalse(knight.isValidMove(0, 1));
    }
}