package com.chess.engine.history;

import com.chess.engine.board.Board;
import com.chess.engine.players.HumanPlayer;
import com.chess.engine.players.Player;
import com.chess.engine.actions.Move;
import com.chess.engine.pieces.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoricTest {
    private Historic historic;
    private Board board;
    private Player[] players;

    @BeforeEach
    public void setup() {
        historic = new Historic();
        board = new Board();
        players = new Player[]{new HumanPlayer(true), new HumanPlayer(false)};
    }

    @Test
    public void testAddAndGetLastTurn() {
        Move move = new Move(new Position(0, 1), new Position(0, 3));
        historic.addEntry(1, board, players, move);

        Map<String, Object> last = historic.getLastTurn();
        
        assertNotNull(last, "O último turno não deveria ser nulo.");
        assertEquals(1, last.get("turn"));
        assertEquals(move, last.get("last-play"));
    }

    @Test
    public void testGetSpecificTurn() {
        historic.addEntry(1, board, players, null);
        historic.addEntry(2, board, players, null);

        Map<String, Object> turnOne = historic.getTurn(1);
        assertNotNull(turnOne);
        assertEquals(1, turnOne.get("turn"));
    }

    @Test
    public void testGetLastPlays() {
        historic.addEntry(1, board, players, null);
        historic.addEntry(2, board, players, null);
        historic.addEntry(3, board, players, null);
        historic.addEntry(4, board, players, null);

        List<Map<String, Object>> lastPlays = historic.getLastPlays();
        
        // Deve retornar apenas os 3 últimos (turnos 2, 3 e 4)
        assertEquals(3, lastPlays.size());
        assertEquals(4, lastPlays.get(2).get("turn"));
    }
}