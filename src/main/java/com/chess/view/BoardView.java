package com.chess.view;

import com.chess.engine.actions.Move;
import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.game.Game;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Position;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.players.Player;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class BoardView extends StackPane {
    private final GridPane chessBoard;
    private final Game game;
    private final Board board;
    private final Movement movement;
    private final Player whitePlayer;
    private final Player blackPlayer;
    
    private final int TILE_SIZE = 70;
    private Position selectedPosition = null;

    public BoardView(Game game, Board board, Movement movement, Player white, Player black) {
        this.game = game;
    		this.board = board;    
        this.movement = movement;
        this.whitePlayer = white;
        this.blackPlayer = black;
        
        this.chessBoard = new GridPane();
        this.chessBoard.setAlignment(Pos.CENTER);
        
        initializeBoard();
        renderPieces();
        
        this.getChildren().add(chessBoard);
    }

    private void initializeBoard() {
        String lightColor = "#ebecd0";
        String darkColor = "#779556";

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (isCorner(row, col)) {
                    addEmptySpace(col, row);
                } else if (isRowLabel(col)) {
                    addRowLabel(col, row);
                } else if (isColLabel(row)) {
                    addColLabel(col, row);
                } else {
                    int boardX = col - 1;
                    int boardY = row - 1;
                    boolean isLight = (boardX + boardY) % 2 == 0;
                    addTile(col, row, isLight ? lightColor : darkColor, boardX, boardY);
                }
            }
        }
    }

    private void addTile(int col, int row, String color, int x, int y) {
        StackPane tile = new StackPane();
        Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
        rect.setFill(Color.web(color));
        
        tile.getChildren().add(rect);
        tile.setOnMouseClicked(event -> handleTileClick(x, y));
        
        chessBoard.add(tile, col, row);
    }

    private void handleTileClick(int x, int y) {
        if (selectedPosition == null) {
            Piece piece = board.getPiece(x, y);
            // Só permite selecionar se o jogo estiver ativo e for o turno das brancas
            if (game.isStarted() && piece != null && piece.isWhite()) {
                selectedPosition = new Position(x, y);
                renderPieces(); 
                highlightLegalMoves(piece);
            }
        } else {
            Position target = new Position(x, y);
            Piece movingPiece = board.getPiece(selectedPosition.x(), selectedPosition.y());

            if (movingPiece != null && movement.validateMove(board, movingPiece, target)) {
                // 1. Executa o movimento logicamente
                movement.executeMove(board, movingPiece, target);
                Move lastMove = new Move(selectedPosition, target);
                
                // 2. Confirma o turno e verifica condições de vitória/empate
                String gameStatus = game.confirmTurn(lastMove);
                
                selectedPosition = null;
                renderPieces();

                if (gameStatus != null) {
                    showGameOverAlert(gameStatus);
                } else {
                    // 3. Se o jogo continua, dispara o turno da IA
                    triggerAITurn();
                }
            } else {
                // Lógica de troca de seleção ou cancelamento
                Piece otherPiece = board.getPiece(x, y);
                if (otherPiece != null && otherPiece.isWhite()) {
                    selectedPosition = new Position(x, y);
                    renderPieces();
                    highlightLegalMoves(otherPiece);
                } else {
                    selectedPosition = null;
                    renderPieces();
                }
            }
        }
    }

    private void triggerAITurn() {
        if (!game.isStarted()) return;

        this.setDisable(true); // Bloqueia interação do usuário

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(event -> {
            Move aiMove = blackPlayer.selectPiece(board);
            
            if (aiMove != null) {
                Piece aiPiece = board.getPiece(aiMove.source().x(), aiMove.source().y());
                movement.executeMove(board, aiPiece, aiMove.target());
                
                // Processa o fim do turno da IA
                String gameStatus = game.confirmTurn(aiMove);
                renderPieces();

                if (gameStatus != null) {
                    showGameOverAlert(gameStatus);
                }
            }
            
            this.setDisable(false); // Libera o tabuleiro para o humano
        });
        pause.play();
    }
    
    private void showGameOverAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void renderPieces() {
        // 1. Limpa tudo (peças e destaques)
        chessBoard.getChildren().clear();
        
        // 2. Reconstrói o grid base (retângulos e coordenadas)
        initializeBoard();

        // 3. Renderiza as peças por cima
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = board.getPiece(x, y);
                if (piece != null) {
                    Label pieceLabel = new Label(getPieceUnicode(piece));
                    pieceLabel.setFont(Font.font("DejaVu Sans", 50));
                    pieceLabel.setMouseTransparent(true);
                    
                    chessBoard.add(pieceLabel, x + 1, y + 1);
                    GridPane.setHalignment(pieceLabel, javafx.geometry.HPos.CENTER);
                }
            }
        }
    }

    // --- Métodos de UI mantidos conforme seu original ---
    private void addRowLabel(int col, int row) {
        String text = String.valueOf(9 - row);
        chessBoard.add(createLabel(text), col, row);
    }

    private void addColLabel(int col, int row) {
        String text = String.valueOf((char) ('a' + col - 1));
        chessBoard.add(createLabel(text), col, row);
    }

    private Label createLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        l.setMinWidth(25);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    private void addEmptySpace(int col, int row) {
        chessBoard.add(new StackPane(), col, row);
    }

    private String getPieceUnicode(Piece piece) {
        if (piece instanceof King) return piece.isWhite() ? "♔" : "♚";
        if (piece instanceof Queen) return piece.isWhite() ? "♕" : "♛";
        if (piece instanceof Rook) return piece.isWhite() ? "♖" : "♜";
        if (piece instanceof Bishop) return piece.isWhite() ? "♗" : "♝";
        if (piece instanceof Knight) return piece.isWhite() ? "♘" : "♞";
        if (piece instanceof Pawn) return piece.isWhite() ? "♙" : "♟";
        return "";
    }
    
    private void highlightLegalMoves(Piece piece) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position target = new Position(col, row);
                
                if (movement.validateMove(board, piece, target)) {
                    // Busca o StackPane (Tile) na coordenada certa (col+1, row+1)
                    StackPane tile = getTileAt(col + 1, row + 1);
                    if (tile != null) {
                        Rectangle rect = (Rectangle) tile.getChildren().get(0);
                        
                        Piece targetPiece = board.getPiece(col, row);
                        if (targetPiece != null && targetPiece.isWhite() != piece.isWhite()) {
                            rect.setFill(Color.web("#ff5e5e")); // Vermelho para captura
                        } else {
                            rect.setFill(Color.web("#f7f76e")); // Amarelo para movimento
                        }
                    }
                }
            }
        }
    }

    /**
     * Método auxiliar para encontrar um nó específico dentro do GridPane pelas coordenadas.
     */
    private StackPane getTileAt(int col, int row) {
        for (Node node : chessBoard.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node instanceof StackPane) {
                return (StackPane) node;
            }
        }
        return null;
    }

    private boolean isCorner(int r, int c) { return (r == 0 || r == 9) && (c == 0 || c == 9); }
    private boolean isRowLabel(int c) { return c == 0 || c == 9; }
    private boolean isColLabel(int r) { return r == 0 || r == 9; }
}