package com.chess.engine.game;

import com.chess.engine.actions.Move;
import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.history.Historic;
import com.chess.engine.pieces.*;
import com.chess.engine.players.Player;
import com.chess.engine.rules.Validation;

public class Game {
    private final Board board;
    private final Movement movement;
    private final Validation validation;
    private final Historic historic;
    private final Player[] players;
    private boolean isStarted;
    private int currentTurn; // 0 para Brancas, 1 para Pretas
    private int turnCount;

    public Game(Board board, Validation validation, Historic historic, Movement movement, Player white, Player black) {
        this.board = board;
        this.validation = validation;
        this.historic = historic;
        this.movement = movement;
        this.players = new Player[]{white, black};
        this.isStarted = false;
        this.currentTurn = 0;
        this.turnCount = 1;
    }

    public void startGame() {
        if (isStarted) return;
        initializePieces();
        // O primeiro registro no histórico não tem movimento
        historic.addEntry(turnCount, board, players, null);
        this.isStarted = true;
    }

    private void initializePieces() {
        setupBackRow(0, false);
        setupPawnRow(1, false);
        setupBackRow(7, true);
        setupPawnRow(6, true);
    }

    private void setupBackRow(int y, boolean isWhite) {
        Player player = isWhite ? players[0] : players[1];
        Piece[] pieces = {
            new Rook(new Position(0, y), isWhite), new Knight(new Position(1, y), isWhite), 
            new Bishop(new Position(2, y), isWhite), new Queen(new Position(3, y), isWhite), 
            new King(new Position(4, y), isWhite), new Bishop(new Position(5, y), isWhite),
            new Knight(new Position(6, y), isWhite), new Rook(new Position(7, y), isWhite)
        };

        for (int x = 0; x < 8; x++) {
            board.setPiece(x, y, pieces[x]);
            player.getPieces().add(pieces[x]);
        }
    }

    private void setupPawnRow(int y, boolean isWhite) {
        Player player = isWhite ? players[0] : players[1];
        for (int x = 0; x < 8; x++) {
            Pawn pawn = new Pawn(new Position(x, y), isWhite);
            board.setPiece(x, y, pawn);
            player.getPieces().add(pawn);
        }
    }

    /**
     * Este método agora é chamado pela BoardView após cada movimento executado.
     * Ele centraliza a lógica de fim de jogo e atualização de turno.
     */
    public String confirmTurn(Move lastMove) {
        Player currentPlayer = players[currentTurn];
        Piece movedPiece = board.getPiece(lastMove.target().x(), lastMove.target().y());

        // 1. Verifica Promoção
        if (validation.isPromoted(movedPiece)) {
            promotePawn((Pawn) movedPiece, currentPlayer);
        }

        // 2. Atualiza Histórico
        turnCount++;
        historic.addEntry(turnCount, board, players, lastMove);
        
        // 3. Troca o turno
        currentTurn = 1 - currentTurn;
        Player nextPlayer = players[currentTurn];

        // 4. Verificações de Fim de Jogo (Otimizadas)
        if (validation.isCheckmate(board, nextPlayer, movement)) {
            isStarted = false;
            return "XEQUE-MATE! Vitória das " + (currentPlayer.isWhite() ? "Brancas" : "Pretas");
        }
        
        if (validation.isStalemate(board, nextPlayer, movement)) {
            isStarted = false;
            return "EMPATE por Afogamento!";
        }

        if (validation.isInsufficientMaterial(board)) {
            isStarted = false;
            return "EMPATE por Material Insuficiente!";
        }

        // A verificação de repetição pode ser pesada, use com cautela
        if (turnCount > 6 && validation.isRepetition(board, historic.getHistory())) {
            isStarted = false;
            return "EMPATE por Repetição!";
        }

        return null; // Jogo continua
    }

    public void promotePawn(Pawn pawn, Player player) {
        // No JavaFX, para simplificar por enquanto, promovemos sempre para Queen
        // Para evitar o uso de Scanner que trava a Thread da interface.
        Position pos = pawn.getPosition();
        Queen queen = new Queen(pos, pawn.isWhite());
        board.setPiece(pos.x(), pos.y(), queen);
        player.getPieces().remove(pawn);
        player.getPieces().add(queen);
    }

    // Getters para a BoardView consultar
    public Player getCurrentPlayer() { return players[currentTurn]; }
    public boolean isStarted() { return isStarted; }
    public Board getBoard() { return board; }
}