package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Position;

public class Board {
	private final Piece[][] grid;

    public Board() {
        this.grid = new Piece[8][8];
    }

    // Deep Copy Constructor
    public Board(Board other) {
        this.grid = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (other.grid[i][j] != null) {
                    // Clona a peça para garantir independência
                    this.grid[i][j] = other.grid[i][j].copy();
                }
            }
        }
    }

    public boolean isEmpty(int x, int y) {
        validateCoordinates(x, y);
        return this.grid[x][y] == null;
    }

    public Piece getPiece(int x, int y) {
        validateCoordinates(x, y);
        return this.grid[x][y];
    }

    public void setPiece(int x, int y, Piece piece) {
        validateCoordinates(x, y);
        this.grid[x][y] = piece;
        // Opcional: Atualizar a peça para saber onde ela está
        if (piece != null) {
            piece.setPosition(new Position(x, y));
        }
    }

    public void removePiece(int x, int y, Piece piece) {
        validateCoordinates(x, y);
        this.grid[x][y] = null;
    }

    private void validateCoordinates(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            throw new IllegalArgumentException("Coordenada fora dos limites do tabuleiro: " + x + "," + y);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Board board = (Board) o;
        
        // Comparamos as peças célula por célula
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p1 = this.grid[i][j];
                Piece p2 = board.grid[i][j];
                
                if (p1 == null && p2 == null) continue;
                if (p1 == null || p2 == null) return false;
                
                // Verifica tipo e cor da peça
                if (p1.getClass() != p2.getClass() || p1.isWhite() != p2.isWhite()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        // Para simplificar, geramos um hash baseado na disposição das peças
        int result = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = grid[i][j];
                if (p != null) {
                    // Combina o hash da classe da peça com a cor
                    result = 31 * result + p.getClass().hashCode() + (p.isWhite() ? 1 : 0);
                }
            }
        }
        return result;
    }
}
