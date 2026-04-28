package com.chess.engine.players;

import com.chess.engine.actions.Move;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import java.util.List;

public interface Player {
    Move selectPiece(Board board);
    List<Piece> getPieces();
    List<Piece> getCapturedPieces();
    boolean isWhite();
    public abstract Player copy(Board newBoard);
}