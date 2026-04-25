package com.chess;

import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.game.Game;
import com.chess.engine.players.HumanPlayer;
import com.chess.engine.rules.Validation;

public class Main {
    public static void main(String[] args) {
        // 1. Instancia as dependências básicas
        Board board = new Board();
        Validation validation = new Validation();
        Movement movement = new Movement(validation);
        
        // 2. Instancia os jogadores
        // Por enquanto, apenas o Player branco terá o input real do terminal
        HumanPlayer white = new HumanPlayer(true);
        HumanPlayer black = new HumanPlayer(false);
        
        // 3. Cria e inicia o jogo
        Game game = new Game(board, movement, validation, white, black);
        
        // Inicializa o jogo (aqui você faria o setup das peças no tabuleiro)
        game.startGame();
        
        // 4. Inicia o loop
        game.gameLoop();
    }
}