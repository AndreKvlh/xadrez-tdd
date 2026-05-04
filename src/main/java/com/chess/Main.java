package com.chess;

import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.game.Game;
import com.chess.engine.history.Historic;
import com.chess.engine.players.HumanPlayer;
import com.chess.engine.players.IAPlayer;
import com.chess.engine.players.Player;
import com.chess.engine.rules.Validation;
import com.chess.view.BoardView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
    public void start(Stage primaryStage) {
		Board board = new Board();
		Validation validation = new Validation();
		Historic historic = new Historic();
		Movement  movement = new Movement(validation, historic);
		
		Player p1 = new HumanPlayer(true);
		Player p2 = new IAPlayer(false, validation, movement);
		
		Game game = new Game(board, validation, historic, movement, p1, p2);
		
		game.startGame();
		
        BoardView boardView = new BoardView(game, board, movement, p1, p2);
        
        // Criamos uma cena com um fundo levemente cinza
        Scene scene = new Scene(boardView, 600, 600);
        
        primaryStage.setTitle("Java Chess Engine - v1.0");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}