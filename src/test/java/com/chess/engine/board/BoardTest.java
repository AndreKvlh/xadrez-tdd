package com.chess.engine.board;

import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    @DisplayName("Deve lançar exceção ao acessar coordenadas fora dos limites")
    void testOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> board.getPiece(8, 0), 
            "Deveria lançar exceção para coordenada X fora do limite (8)");
        
        assertThrows(IllegalArgumentException.class, () -> board.getPiece(0, -1), 
            "Deveria lançar exceção para coordenada Y fora do limite (-1)");
    }

    @Test
    @DisplayName("Deve garantir que o tabuleiro copiado é independente do original (Deep Copy)")
    void testDeepCopyIndependence() {
        Piece knight = new Knight(0, 0, true);
        board.setPiece(0, 0, knight);

        // Criando uma cópia profunda
        Board boardCopy = new Board(board);

        // Removemos a peça na cópia
        boardCopy.removePiece(0, 0, knight);

        // Verificações de isolamento
        assertFalse(board.isEmpty(0, 0), 
            "O tabuleiro original NÃO deve ter sido alterado pela remoção na cópia.");
        
        assertTrue(boardCopy.isEmpty(0, 0), 
            "O tabuleiro copiado deve estar vazio na posição (0,0).");
        
        assertNotNull(board.getPiece(0, 0), 
            "A peça ainda deve existir no tabuleiro original.");
    }
    
    @Test
    void testInitialBoardIsEmpty() {
        assertTrue(board.isEmpty(0, 0));
        assertTrue(board.isEmpty(7, 7));
    }

    @Test
    void testSetAndGetPiece() {
        Piece knight = new Knight(0, 0, true);
        board.setPiece(0, 0, knight);
        
        assertEquals(knight, board.getPiece(0, 0));
        assertFalse(board.isEmpty(0, 0));
    }

    @Test
    void testRemovePiece() {
        Piece knight = new Knight(0, 0, true);
        board.setPiece(0, 0, knight);
        
        board.removePiece(0, 0, knight);
        
        assertTrue(board.isEmpty(0, 0));
        assertNull(board.getPiece(0, 0));
    }
}
