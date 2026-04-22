package com.chess.engine.pieces;

//Classe base para reduzir duplicação (DRY)
public abstract class AbstractPiece implements Piece {
	protected int x;
	protected int y;
	protected boolean isWhite; // Ou um Enum Color
	
	public AbstractPiece(int x, int y, boolean isWhite) {
	    this.x = x;
	    this.y = y;
	    this.isWhite = isWhite;
	}
 
	// Métodos comuns a todas as peças, como getters, setters ou posição	
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isWhite() {
		return isWhite;
	}

	public void setWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}
}
