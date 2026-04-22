package com.chess.engine.pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {

    private Knight knight;

    @BeforeEach
    void setUp() {
    		// Inicializa o cavalo em uma posição qualquer (ex: 0,0)
        knight = new Knight(0, 0, true);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro para um movimento válido em L")
    void testValidLMove() {
        // Movimento de (0,0) para (1,2) é um movimento válido de Cavalo
        assertTrue(knight.isValidMove(1, 2), 
            "O movimento (0,0) para (1,2) deveria ser válido para o Cavalo.");
    }

    @Test
    @DisplayName("Deve retornar falso para um movimento inválido")
    void testInvalidMove() {
        // Movimento de (0,0) para (0,1) é um movimento inválido (horizontal/vertical simples)
        assertFalse(knight.isValidMove(0, 1), 
            "O movimento (0,0) para (0,1) não deveria ser válido para o Cavalo.");
    }
}