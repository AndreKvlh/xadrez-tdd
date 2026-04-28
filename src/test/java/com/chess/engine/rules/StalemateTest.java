package com.chess.engine.rules;

import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.*;
import com.chess.engine.players.HumanPlayer;
import com.chess.engine.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StalemateTest {
    private Board board;
    private Validation validation;
    private Movement movement;
    private Player white;
    private Player black;

    @BeforeEach
    public void setup() {
        board = new Board();
        validation = new Validation();
        movement = new Movement(validation);
        white = new HumanPlayer(true);
        black = new HumanPlayer(false);
    }

    @Test
    public void testStalemateCondition() {
        // Cenário: Rei Branco em a8 (0,7), Rainha Preta em b6 (1,5), Rei Preto em c8 (2,7)
        // O Rei Branco não está em xeque, mas não tem para onde ir.
        
        King whiteKing = new King(new Position(0, 7), true);
        King blackKing = new King(new Position(2, 7), false);
        Queen blackQueen = new Queen(new Position(1, 5), false);
        
        board.setPiece(0, 7, whiteKing);
        board.setPiece(2, 7, blackKing);
        board.setPiece(1, 5, blackQueen);
        
        white.getPieces().add(whiteKing);
        black.getPieces().add(blackKing);
        black.getPieces().add(blackQueen);

        // O teste deve falhar aqui porque ainda não temos a lógica implementada
        assertTrue(validation.isStalemate(board, white, movement), 
            "Deveria ser afogamento: O rei não está em xeque e não tem movimentos legais.");
    }
}