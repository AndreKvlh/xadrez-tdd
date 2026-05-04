package com.chess.engine.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chess.engine.actions.Move;
import com.chess.engine.board.Board;
import com.chess.engine.history.Historic;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Position;
import com.chess.engine.players.HumanPlayer;
import com.chess.engine.players.Player;

public class EnPassantTest {
    private Board board;
    private Validation validation;
    private Historic historic;

    @BeforeEach
    public void setup() {
        board = new Board();
        validation = new Validation();
        historic = new Historic();
    }

    @Test
    public void testEnPassantValidation() {
        // 1. Setup: Peão Branco na posição (3, 3) - Linha 5 do tabuleiro real
        Pawn whitePawn = new Pawn(new Position(3, 3), true);
        whitePawn.setHasMoved(true);
        board.setPiece(3, 3, whitePawn);

        // 2. Simular Peão Preto movendo de (4, 1) para (4, 3) - Avanço Duplo
        Pawn blackPawn = new Pawn(new Position(4, 3), false);
        blackPawn.setHasMoved(true);
        board.setPiece(4, 3, blackPawn);

        // 3. Registrar o movimento duplo no histórico
        // No xadrez, se moveu de (4,1) para (4,3), o last-play deve refletir isso
        Move lastMove = new Move(new Position(4, 1), new Position(4, 3));
        Player[] dummyPlayers = {new HumanPlayer(true), new HumanPlayer(false)};
        historic.addEntry(1, board, dummyPlayers, lastMove);

        // 4. Alvo do En Passant (casa atrás do peão preto: 4, 2)
        Position target = new Position(4, 2);

        // Act
        boolean canCapture = validation.isEnPassant(board, whitePawn, target, historic);

        // Assert
        assertTrue(canCapture, "Deveria ser possível realizar a captura En Passant.");
    }
}