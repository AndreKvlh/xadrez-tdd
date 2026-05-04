package com.chess.engine.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chess.engine.actions.Move;
import com.chess.engine.actions.Movement;
import com.chess.engine.board.Board;
import com.chess.engine.history.Historic;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Position;
import com.chess.engine.pieces.Rook;
import com.chess.engine.players.Player;

public class Validation {

    public boolean isAlliedPiece(Board board, Piece movingPiece, Position target) {
        Piece targetPiece = board.getPiece(target.x(), target.y());
        if (targetPiece == null) return false;
        return targetPiece.isWhite() == movingPiece.isWhite();
    }

    public boolean isCovered(Board board, Piece movingPiece, Position target) {
        // Cavalo pula, então não precisa verificar cobertura
        if (movingPiece instanceof Knight) return false;

        int startX = movingPiece.getPosition().x();
        int startY = movingPiece.getPosition().y();
        int targetX = target.x();
        int targetY = target.y();

        int dx = Integer.compare(targetX, startX);
        int dy = Integer.compare(targetY, startY);

        int currentX = startX + dx;
        int currentY = startY + dy;

        // Itera pelo caminho, parando antes da casa de destino
        while (currentX != targetX || currentY != targetY) {
            if (board.getPiece(currentX, currentY) != null) {
                return true; // Existe uma peça bloqueando o caminho
            }
            currentX += dx;
            currentY += dy;
        }
        return false;
    }

    public boolean pawnCanAttack(Board board, Pawn pawn, Position target) {
        int dx = Math.abs(target.x() - pawn.getPosition().x());
        int dy = target.y() - pawn.getPosition().y();
        int direction = pawn.isWhite() ? -1 : 1;

        // Movimento diagonal (captura)
        if (dx == 1 && dy == direction) {
            Piece targetPiece = board.getPiece(target.x(), target.y());
            return targetPiece != null && targetPiece.isWhite() != pawn.isWhite();
        }

        // Movimento vertical (avanço de 1 ou 2 casas)
        if (dx == 0 && (dy == direction || (!pawn.hasMoved() && dy == 2 * direction))) {
            return board.getPiece(target.x(), target.y()) == null;
        }

        return false;
    }

    public boolean isPromoted(Piece piece) {
        if (!(piece instanceof Pawn)) return false;
        // Se branca, atinge linha 0; se preta, atinge linha 7
        return piece.isWhite() ? piece.getPosition().y() == 0 : piece.getPosition().y() == 7;
    }

    public boolean isUnderCheck(Board board, boolean isWhiteKing) {
        Position kingPos = null;

        // 1. Localizar o Rei
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece p = board.getPiece(x, y);
                if (p instanceof King && p.isWhite() == isWhiteKing) {
                    kingPos = p.getPosition();
                    break;
                }
            }
        }

        if (kingPos == null) return false; // Rei não encontrado (situação de erro)

        // 2. Verificar se alguma peça inimiga ataca o Rei
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece enemy = board.getPiece(x, y);
                if (enemy != null && enemy.isWhite() != isWhiteKing) {
                    // Verifica se a peça inimiga alcança o rei (considerando regras de movimento e bloqueios)
                    if (enemy.isValidMove(kingPos.x(), kingPos.y()) && 
                        !isCovered(board, enemy, kingPos)) {
                        return true; 
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Verifica se o jogador está em xeque-mate.
     */
    public boolean isCheckmate(Board board, Player player, Movement movement) {
        if (!isUnderCheck(board, player.isWhite())) {
            return false;
        }

        for (Piece piece : player.getPieces()) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (movement.validateMove(board, piece, new Position(x, y))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Verifica se a posição atual é de afogamento (Stalemate).
     */
    public boolean isStalemate(Board board, Player player, Movement movement) {
        if (isUnderCheck(board, player.isWhite())) {
            return false;
        }

        for (Piece piece : player.getPieces()) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (movement.validateMove(board, piece, new Position(x, y))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    
    /**
     * Verifica se há material insuficiente para prosseguir com o jogo.
     */
    public boolean isInsufficientMaterial(Board board) {
        List<Piece> remainingPieces = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = board.getPiece(i, j);
                if (p != null && !(p instanceof King)) {
                    remainingPieces.add(p);
                }
            }
        }

        if (remainingPieces.isEmpty()) return true;

        if (remainingPieces.size() == 1) {
            Piece p = remainingPieces.get(0);
            return (p instanceof Bishop || p instanceof Knight);
        }

        return false;
    }

    /**
     * Verifica se a posição atual já foi repetida 3 vezes.
     */
    public boolean isRepetition(Board currentBoard, List<Map<String, Object>> history) {
        int occurrences = 1; // Contamos a posição atual como a primeira ocorrência

        for (Map<String, Object> entry : history) {
            Board pastBoard = (Board) entry.get("board");
            // Uso direto do equals que acabamos de implementar
            if (currentBoard.equals(pastBoard)) {
                occurrences++;
            }
        }

        return occurrences >= 3;
    }
    
    public boolean isCastling(Board board, King king, int targetX, int targetY) {
        // 1. O Rei já se moveu?
        if (king.hasMoved()) return false;

        // 2. O movimento é horizontal de 2 casas?
        int kingX = king.getPosition().x();
        int kingY = king.getPosition().y();
        if (Math.abs(targetX - kingX) != 2 || targetY != kingY) return false;

        // 3. Identifica a torre alvo e verifica se está no lugar certo
        // Se targetX > kingX, é roque curto (lado da torre em X=7)
        // Se targetX < kingX, é roque longo (lado da torre em X=0)
        int rookX = (targetX > kingX) ? 7 : 0;
        
        Piece piece = board.getPiece(rookX, kingY);

        if (!(piece instanceof Rook rook)) return false; // Usa Pattern Matching for instanceof (Java 16+)
        if (rook.isWhite() != king.isWhite() || rook.hasMoved()) return false;

        // 4. O caminho entre o Rei e a Torre está livre?
        int startX = Math.min(kingX, rookX);
        int endX = Math.max(kingX, rookX);

        for (int x = startX + 1; x < endX; x++) {
            if (!board.isEmpty(x, kingY)) return false;
        }

        // TODO: Futuramente, adicionar verificação se o Rei passa por casas sob ataque
        // Ex: if (isUnderAttack(board, ...)) return false;

        return true;
    }
    
    public boolean isEnPassant(Board board, Pawn pawn, Position target, Historic historic) {
        Map<String, Object> lastTurn = historic.getLastTurn();
        if (lastTurn == null) return false;

        Move lastMove = (Move) lastTurn.get("last-play");
        if (lastMove == null) return false;

        Piece lastPiece = board.getPiece(lastMove.target().x(), lastMove.target().y());
        if (!(lastPiece instanceof Pawn enemyPawn) || enemyPawn.isWhite() == pawn.isWhite()) {
            return false;
        }

        if (Math.abs(lastMove.target().y() - lastMove.source().y()) != 2) return false;

        int pawnY = pawn.getPosition().y();
        int enemyX = enemyPawn.getPosition().x();

        if (pawnY != enemyPawn.getPosition().y() || Math.abs(pawn.getPosition().x() - enemyX) != 1) {
            return false;
        }

        int capturedY = (lastMove.source().y() + lastMove.target().y()) / 2;
        return target.x() == enemyX && target.y() == capturedY;
    }
}