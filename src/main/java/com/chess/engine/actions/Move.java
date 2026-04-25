package com.chess.engine.actions;
import com.chess.engine.pieces.Position;

public record Move(Position source, Position target) {}