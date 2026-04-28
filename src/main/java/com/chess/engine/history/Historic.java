package com.chess.engine.history;

import com.chess.engine.board.Board;
import com.chess.engine.players.Player;
import com.chess.engine.actions.Move;
import java.util.*;

public class Historic {
    private List<Map<String, Object>> history = new ArrayList<>();

    public void addEntry(int turn, Board board, Player[] players, Move lastPlay) {
        Map<String, Object> entry = new HashMap<>();
        
        Board boardCopy = new Board(board);
        Player[] playersCopy = new Player[players.length];
        
        for (int i = 0; i < players.length; i++) {
            playersCopy[i] = players[i].copy(boardCopy);
        }
        
        entry.put("turn", turn);
        entry.put("board", boardCopy);
        entry.put("players", playersCopy);
        entry.put("last-play", lastPlay);
        
        history.add(entry);
    }

    public Map<String, Object> getLastTurn() {
        if (history.isEmpty()) return null;
        return history.get(history.size() - 1);
    }

    public Map<String, Object> getTurn(int turn) {
        // Como o turno inicia em 1 e a lista em 0, subtraímos 1
        int index = turn - 1;
        if (index < 0 || index >= history.size()) return null;
        return history.get(index);
    }

    public List<Map<String, Object>> getLastPlays() {
        if (history.size() <= 3) {
            return new ArrayList<>(history);
        }
        return new ArrayList<>(history.subList(history.size() - 3, history.size()));
    }
    
    public List<Map<String, Object>> getHistory() {
        return new ArrayList<>(this.history); // Retorna cópia para segurança
    }
}