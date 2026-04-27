package com.chess.engine.pieces;

//Classe base para reduzir duplicação (DRY)
public abstract class AbstractPiece implements Piece {
	protected Position position;
    protected boolean isWhite;

    public AbstractPiece(Position position, boolean isWhite) {
        this.position = position;
        this.isWhite = isWhite;
    }

    // Método que as subclasses devem implementar obrigatoriamente
    protected abstract char getSymbol();
    
    @Override
    public String getDisplay() {
        char symbol = getSymbol();
        return isWhite ? String.valueOf(symbol).toUpperCase() 
                       : String.valueOf(symbol).toLowerCase();
    }
    
    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

	public boolean isWhite() {
		return isWhite;
	}

	public void setWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}
}
