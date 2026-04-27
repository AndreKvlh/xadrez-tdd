package com.chess.engine.game;

import com.chess.engine.actions.Move;
import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.*;
import com.chess.engine.players.HumanPlayer;
import com.chess.engine.players.Player;
import com.chess.engine.rules.Validation; // Adicionado para acessar isPromoted

import java.util.Scanner;

public class Game {
    private final Board board;
    private final Movement movement;
    private final Validation validation; // Referência adicionada
    private final Player[] players;
    private boolean isStarted;
    private int currentTurn;

    public Game(Board board, Movement movement, Validation validation, Player white, Player black) {
        this.board = board;
        this.movement = movement;
        this.validation = validation; // Injeção da validação
        this.players = new Player[]{white, black};
        this.isStarted = false;
        this.currentTurn = 0;
    }

    /**
     * Inicia o jogo, configurando o tabuleiro e definindo o estado como iniciado.
     */
    public void startGame() {
        if (isStarted) return;
        
        initializePieces();
        
        this.isStarted = true;
        this.currentTurn = 0;
        System.out.println("Jogo iniciado! Vez das Brancas.");
    }

    /**
     * Posiciona as peças no tabuleiro nas coordenadas iniciais e 
     * registra cada peça na lista de peças do jogador correspondente.
     */
    private void initializePieces() {
        // Posicionamento das peças (Y=0,7 back row; Y=1,6 pawns)
        // Índice 0: Brancas (players[0]), Índice 1: Pretas (players[1])
        
        // --- Peças Pretas (Linhas 0 e 1) ---
        setupBackRow(0, false);
        setupPawnRow(1, false);

        // --- Peças Brancas (Linhas 7 e 6) ---
        setupBackRow(7, true);
        setupPawnRow(6, true);
    }

    private void setupBackRow(int y, boolean isWhite) {
        Player player = isWhite ? players[0] : players[1];
        
        // Ordem: Torre, Cavalo, Bispo, Rainha, Rei, Bispo, Cavalo, Torre
        // Nota: Assumindo que seu construtor de Peça aceita (Position, isWhite)
        // Se a lógica de posição for tratada via setPosition no setPiece, ajustamos aqui.
        
        Piece[] pieces = {
            new Rook(null, isWhite), new Knight(null, isWhite), new Bishop(null, isWhite),
            new Queen(null, isWhite), new King(null, isWhite), new Bishop(null, isWhite),
            new Knight(null, isWhite), new Rook(null, isWhite)
        };

        for (int x = 0; x < 8; x++) {
            board.setPiece(x, y, pieces[x]);
            player.getPieces().add(pieces[x]);
        }
    }

    private void setupPawnRow(int y, boolean isWhite) {
        Player player = isWhite ? players[0] : players[1];
        for (int x = 0; x < 8; x++) {
            Pawn pawn = new Pawn(null, isWhite);
            board.setPiece(x, y, pawn);
            player.getPieces().add(pawn);
        }
    }
    /**
     * Reinicia o jogo, limpando o tabuleiro e as listas de peças dos jogadores.
     */
    public void restartGame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.setPiece(i, j, null);
            }
        }
        for (Player p : players) {
            p.getPieces().clear();
            p.getCapturedPieces().clear();
        }
        this.isStarted = false;
        System.out.println("Jogo reiniciado.");
    }

    /**
     * Remove a peça capturada do jogador vitimado e a adiciona à lista de capturadas do capturador.
     */
    public void capturePiece(Piece piece, Player capturer, Player victim) {
        if (victim.getPieces().remove(piece)) {
            capturer.getCapturedPieces().add(piece);
        }
    }

    /**
     * Promove um peão: humano escolhe a peça, IA promove automaticamente para Rainha.
     * Atualiza o tabuleiro e a lista de peças do jogador.
     */
    public void promotePawn(Pawn pawn, Player player) {
        Piece promotedPiece;
        boolean isWhite = pawn.isWhite();
        Position pos = pawn.getPosition();

        if (player instanceof HumanPlayer) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Promoção! Escolha: (Q)ueen, (R)ook, (B)ishop, (K)night");
            String choice = scanner.nextLine().toUpperCase();
            
            promotedPiece = switch (choice) {
                case "R" -> new Rook(pos, isWhite);
                case "B" -> new Bishop(pos, isWhite);
                case "K" -> new Knight(pos, isWhite);
                default -> new Queen(pos, isWhite); // Padrão Rainha
            };
        } else {
            System.out.println("IA promoveu para Rainha.");
            promotedPiece = new Queen(pos, isWhite);
        }

        // Atualiza a peça no Board e no Player
        board.setPiece(pos.x(), pos.y(), promotedPiece);
        player.getPieces().remove(pawn);
        player.getPieces().add(promotedPiece);
    }

    /**
     * Controla o fluxo de turnos, validação, execução e checagem de promoção.
     */
    public void gameLoop() {
        if (!isStarted) throw new IllegalStateException("O jogo não foi iniciado.");

        while (isStarted) {
            generateBoard();
            Player currentPlayer = players[currentTurn];

            // Trava temporária: Apenas brancas se movem
            if (!currentPlayer.isWhite()) {
                System.out.println("Aguardando IA (Turno das Pretas)...");
                break;
            }

            Move move = currentPlayer.selectPiece(board);
            Piece piece = board.getPiece(move.source().x(), move.source().y());

            if (piece != null && piece.isWhite() == currentPlayer.isWhite()) {
                if (movement.validateMove(board, piece, move.target())) {
                    
                    Piece captured = movement.executeMove(board, piece, move.target());
                    if (captured != null) {
                        capturePiece(captured, currentPlayer, players[1 - currentTurn]);
                    }

                    // Verifica promoção usando a lógica da classe Validation
                    if (validation.isPromoted(piece)) {
                        promotePawn((Pawn) piece, currentPlayer);
                    }

                    currentTurn = 1 - currentTurn;
                } else {
                    System.out.println("Movimento ilegal!");
                }
            } else {
                System.out.println("Selecione uma peça válida sua.");
            }
        }
    }

    /**
     * Gera a representação visual do tabuleiro no console.
     * AVISO: Esta visualização será ajustada quando integrarmos a GUI.
     */
    public void generateBoard() {
        System.out.println("\n  A B C D E F G H");
        for (int y = 0; y < 8; y++) {
            System.out.print((8 - y) + " ");
            for (int x = 0; x < 8; x++) {
                Piece p = board.getPiece(x, y);
                // Agora o polimorfismo resolve a exibição para qualquer peça
                System.out.print((p == null ? "." : p.getDisplay()) + " ");
            }
            System.out.println();
        }
    }
    
    public boolean isStarted() {
        return isStarted;
    }
}