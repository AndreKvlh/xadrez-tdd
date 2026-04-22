package com.chess.engine.pieces;

public interface Piece {
    boolean isValidMove(int targetX, int targetY);
    Position getPosition();
    void setPosition(Position position);
    Piece copy();
}
