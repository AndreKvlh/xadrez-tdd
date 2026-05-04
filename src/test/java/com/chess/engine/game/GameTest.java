package com.chess.engine.game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.history.Historic;
import com.chess.engine.players.HumanPlayer;
import com.chess.engine.rules.Validation;

class GameTest {

    @Test
    void testGameStartShouldChangeStatusToStarted() {
        Board board = new Board();
        Validation validation = new Validation();
        Historic historic = new Historic();
        Movement movement = new Movement(validation, historic);
        
        Game game = new Game(board, validation, historic, movement,  new HumanPlayer(true), new HumanPlayer(false));

        // Estado inicial
        assertFalse(game.isStarted(), "O jogo não deveria iniciar como Started");

        // Execução
        game.startGame();

        // Verificação (Irá falhar pois o método está vazio)
        assertTrue(game.isStarted(), "O jogo deveria estar marcado como iniciado após startGame");
    }
}