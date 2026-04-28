package com.chess.engine.players;

import java.util.Scanner;

import com.chess.engine.actions.Move;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Position;

public class HumanPlayer extends AbstractPlayer {
    private final Scanner scanner;

    public HumanPlayer(boolean isWhite) {
        super(isWhite);
        this.scanner = new Scanner(System.in);
    }

    /**
     * ATENÇÃO: Esta implementação utiliza terminal (Scanner).
     * Quando integrarmos o JavaFX, este método será substituído para escutar 
     * eventos de clique do mouse na interface gráfica.
     */
    @Override
    public Move selectPiece(Board board) {
        while (true) {
            System.out.println("Jogador " + (isWhite ? "(Brancas)" : "(Pretas)") + ", insira seu movimento (Ex: A2 A4):");
            String input = scanner.nextLine().toUpperCase().trim();

            if (isValidFormat(input)) {
                try {
                    Position source = parsePosition(input.substring(0, 2));
                    Position target = parsePosition(input.substring(3, 5));
                    return new Move(source, target);
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            } else {
                System.out.println("Formato inválido! Use 'A2 A4'");
            }
        }
    }
    
    @Override
    public Player copy(Board newBoard) {
        HumanPlayer copy = new HumanPlayer(this.isWhite);
        copy.getPieces().clear();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = newBoard.getPiece(x, y);
                if (piece != null && piece.isWhite() == this.isWhite) {
                    copy.getPieces().add(piece);
                }
            }
        }
        return copy;
    }
}