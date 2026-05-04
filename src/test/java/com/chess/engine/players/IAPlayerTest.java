package com.chess.engine.players;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.chess.engine.actions.Move;
import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.history.Historic;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Position;
import com.chess.engine.rules.Validation;

public class IAPlayerTest {

    @Test
    public void testGreedyStrategyShouldPreferCapture() {
        Board board = new Board();
        // Precisamos instanciar as dependências para o construtor da IA
        Validation validation = new Validation();
        Historic historic = new Historic();
        Movement movement = new Movement(validation, historic);
        
        IAPlayer ia = new IAPlayer(true, validation, movement);
        
        // Setup: IA tem um peão em E4, inimigo tem um peão em D5 (capturável)
        Pawn myPawn = new Pawn(new Position(4, 4), true); // E4
        Pawn enemyPawn = new Pawn(new Position(3, 3), false); // D5
        
        board.setPiece(4, 4, myPawn);
        board.setPiece(3, 3, enemyPawn);
        
        ia.addPiece(myPawn);
        
        // Executa a escolha
        Move selectedMove = ia.selectPiece(board);
        
        // Verificação: O destino deve ser D5 (a posição do inimigo)
        assertNotNull(selectedMove, "IA deveria retornar um movimento");
        assertEquals(3, selectedMove.target().x(), "IA deveria escolher a captura na coluna D");
        assertEquals(3, selectedMove.target().y(), "IA deveria escolher a captura na linha 5");
    }
}