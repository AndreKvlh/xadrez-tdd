package com.chess.engine.players;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull; // Alterado para exigir não-nulo

import org.junit.jupiter.api.Test;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Position;

class HumanPlayerTest {

    @Test
    void testPlayerStateManagement() {
        HumanPlayer player = new HumanPlayer(true);
        Pawn testPawn = new Pawn(new Position(0, 0), true);
        
        player.addPiece(testPawn);

        assertEquals(1, player.getPieces().size(), "A lista de peças deveria conter 1 item");
        assertEquals(testPawn, player.getPieces().get(0), "A peça recuperada deve ser a mesma adicionada");
    }	

    @Test
    void testSelectPieceMustReturnMove() {
        HumanPlayer player = new HumanPlayer(true);
        
        // RED: Este teste vai FALHAR porque selectPiece retorna null
        // Estamos exigindo um contrato: o jogador DEVE retornar um Move
        assertNotNull(player.selectPiece(null), "SelectPiece deve retornar um objeto Move válido");
    }
}