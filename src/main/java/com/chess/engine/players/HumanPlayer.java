package com.chess.engine.players;

import com.chess.engine.actions.Move;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Position;
import java.util.Scanner;

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
}