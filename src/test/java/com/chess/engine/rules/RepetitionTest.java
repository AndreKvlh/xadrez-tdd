package com.chess.engine.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chess.engine.board.Board;
import com.chess.engine.history.Historic;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Position;
import com.chess.engine.players.HumanPlayer;
import com.chess.engine.players.Player;

public class RepetitionTest {
    private Board board;
    private Validation validation;
    private Historic historic;
    private Player[] players;

    @BeforeEach
    public void setup() {
        board = new Board();
        validation = new Validation();
        historic = new Historic();
        players = new Player[]{new HumanPlayer(true), new HumanPlayer(false)};
        
        // Colocamos peças básicas para o tabuleiro não ficar vazio
        board.setPiece(0, 0, new King(new Position(0, 0), true));
        board.setPiece(7, 7, new King(new Position(7, 7), false));
    }

    @Test
    public void testThreefoldRepetition() {
        // 1. Estado Inicial (1ª ocorrência)
        historic.addEntry(1, board, players, null);

        // --- Ciclo de movimento ---
        
        // 2. Mover Rei Branco para (0,1)
        board.setPiece(0, 1, board.getPiece(0, 0));
        board.removePiece(0, 0, null); // Nota: ajuste conforme sua lógica de removePiece
        historic.addEntry(2, board, players, null);

        // 3. Voltar Rei Branco para (0,0) (2ª ocorrência da posição inicial)
        board.setPiece(0, 0, board.getPiece(0, 1));
        board.removePiece(0, 1, null);
        historic.addEntry(3, board, players, null);

        // 4. Mover Rei Branco para (0,1) novamente
        board.setPiece(0, 1, board.getPiece(0, 0));
        board.removePiece(0, 0, null);
        historic.addEntry(4, board, players, null);

        // 5. Voltar Rei Branco para (0,0) (3ª ocorrência da posição inicial)
        board.setPiece(0, 0, board.getPiece(0, 1));
        board.removePiece(0, 1, null);
        historic.addEntry(5, board, players, null);

        // Act & Assert
        // Agora o histórico contém o estado inicial 3 vezes.
        assertTrue(validation.isRepetition(board, historic.getHistory()), 
            "Deveria detectar repetição tripla (a mesma posição ocorreu 3 vezes).");
    }
}