package com.chess.engine.rules;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    private Board board;
    private Validation validation;

    @BeforeEach
    void setUp() {
        board = new Board();
        validation = new Validation();
    }

    @Test
    void testIsAlliedPiece() {
        // Peça branca em 0,0 e alvo branco em 0,1
        Piece whitePawn1 = new Pawn(new Position(0, 0), true);
        Piece whitePawn2 = new Pawn(new Position(0, 1), true);
        
        board.setPiece(0, 0, whitePawn1);
        board.setPiece(0, 1, whitePawn2);

        assertTrue(validation.isAlliedPiece(board, whitePawn1, new Position(0, 1)), 
            "Deve identificar peça aliada");
    }

    @Test
    void testIsCovered() {
        // Peça em 0,0, alvo em 0,2, obstáculo em 0,1
        Piece rook = new Rook(new Position(0, 0), true);
        Piece pawn = new Pawn(new Position(0, 1), false);
        
        board.setPiece(0, 0, rook);
        board.setPiece(0, 1, pawn);

        assertTrue(validation.isCovered(board, rook, new Position(0, 2)), 
            "Deve detectar obstáculo no caminho da torre");
    }

    @Test
    void testPawnFrontalBlock() {
        // Peão branco em 4,4, Peão preto em 4,3 (blocado)
        Pawn whitePawn = new Pawn(new Position(4, 4), true);
        Pawn blackPawn = new Pawn(new Position(4, 3), false);
        
        board.setPiece(4, 4, whitePawn);
        board.setPiece(4, 3, blackPawn);

        assertFalse(validation.pawnCanAttack(board, whitePawn, new Position(4, 3)), 
            "Peão não deve poder avançar se houver peça na frente");
    }
    
    @Test
    @DisplayName("Deve identificar promoção de peão")
    void testIsPromoted() {
        // Peão branco na linha 0 (promoção)
        Pawn whitePawn = new Pawn(new Position(0, 0), true);
        assertTrue(validation.isPromoted(whitePawn), "Peão branco na linha 0 deve ser promovido");

        // Peão preto na linha 7 (promoção)
        Pawn blackPawn = new Pawn(new Position(0, 7), false);
        assertTrue(validation.isPromoted(blackPawn), "Peão preto na linha 7 deve ser promovido");
        
        // Peão no meio do caminho
        Pawn middlePawn = new Pawn(new Position(4, 4), true);
        assertFalse(validation.isPromoted(middlePawn), "Peão no meio do tabuleiro não deve ser promovido");
    }

    @Test
    @DisplayName("Deve identificar xeque ao Rei")
    void testIsUnderCheck() {
        King king = new King(new Position(4, 4), true);
        board.setPiece(4, 4, king);

        // Coloca uma torre inimiga na mesma coluna
        Rook enemyRook = new Rook(new Position(4, 0), false);
        board.setPiece(4, 0, enemyRook);

        assertTrue(validation.isUnderCheck(board, true), "Rei deve estar em xeque pela torre inimiga");
    }

    @Test
    @DisplayName("Não deve identificar xeque se não houver ameaça")
    void testIsNotUnderCheck() {
        King king = new King(new Position(4, 4), true);
        board.setPiece(4, 4, king);

        // Torre inimiga em posição que não ataca o Rei
        Rook enemyRook = new Rook(new Position(0, 0), false);
        board.setPiece(0, 0, enemyRook);

        assertFalse(validation.isUnderCheck(board, true), "Rei não deve estar em xeque");
    }
}