package com.chess.engine.pieces;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    @Test
    @DisplayName("Peão branco deve mover-se apenas para cima")
    void testWhitePawnForward() {
        Pawn whitePawn = new Pawn(new Position(4, 4), true);
        assertTrue(whitePawn.isValidMove(4, 3), "Peão branco deve subir (y-1)");
        assertFalse(whitePawn.isValidMove(4, 5), "Peão branco não deve descer");
    }

    @Test
    @DisplayName("Peão preto deve mover-se apenas para baixo")
    void testBlackPawnForward() {
        Pawn blackPawn = new Pawn(new Position(4, 4), false);
        assertTrue(blackPawn.isValidMove(4, 5), "Peão preto deve descer (y+1)");
        assertFalse(blackPawn.isValidMove(4, 3), "Peão preto não deve subir");
    }

    @Test
    @DisplayName("Peão deve mover-se duas casas apenas no primeiro movimento")
    void testDoubleMove() {
        // Nota: Precisaremos gerenciar estado interno 'hasMoved' futuramente
        Pawn whitePawn = new Pawn(new Position(4, 6), true); // Y=6 é linha inicial
        assertTrue(whitePawn.isValidMove(4, 4), "Peão branco deve poder mover 2 casas no início");
    }

    @Test
    @DisplayName("Peão deve capturar apenas na diagonal")
    void testCaptureMovement() {
        Pawn whitePawn = new Pawn(new Position(4, 4), true);
        assertTrue(whitePawn.isValidMove(3, 3), "Peão branco deve capturar na diagonal esquerda");
        assertTrue(whitePawn.isValidMove(5, 3), "Peão branco deve capturar na diagonal direita");
    }

    @Test
    @DisplayName("Deve retornar a instância correta no copy")
    void testCopy() {
        Pawn pawn = new Pawn(new Position(4, 4), true);
        Piece copy = pawn.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof Pawn);
        assertNotSame(pawn, copy);
        assertEquals(pawn.getPosition(), copy.getPosition());
    }
}