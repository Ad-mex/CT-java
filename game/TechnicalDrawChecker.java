package game;

public class TechnicalDrawChecker {
    private final AbstractInRectangleBoard board;
    private final int k;
    private int balance = 0;

    public boolean isDraw() {
        return balance == 0;
    }

    private class DistanceCheker {
        int dist = 0;

        public void addBalance(int i, int j) {
            dist++;
            if (board.isInBoard(i, j)) {
                if (dist >= k) {
                    balance += 2;
                }
            } else {
                dist = 0;
            }
        }
    }

    private void addStraightBalance(boolean flag) {
        // if flag = false that calc transpose board rows (columns in our board)
        int n = flag ? board.getN() : board.getM();
        int m = board.getN() ^ board.getM() ^ n;
        for (int i = 0; i < n; i++) {
            DistanceCheker checker = new DistanceCheker();
            for (int j = m - 1; j >= 0; j--) {
                checker.addBalance(flag ? i : j, flag ? j : i);
            }
        }
    }

    public TechnicalDrawChecker(AbstractInRectangleBoard board, int k) {
        this.board = board;
        this.k = k;

        // calc right -> left
        addStraightBalance(true);
        // calc down -> up
        addStraightBalance(false);
        // calc downleft -> upright
        for (int diag = 0; diag < board.getN() + board.getM() - 1; diag++) {
            DistanceCheker checker = new DistanceCheker();
            for (int i = (diag < board.getN() ? diag : board.getN() - 1), j = diag - i; i >= 0
                    && j < board.getM(); i--, j++) {
                checker.addBalance(i, j);
            }
        }
        // calc downright -> upleft
        for (int diag = board.getN() - 1; diag >= 1 - board.getM(); diag--) {
            DistanceCheker checker = new DistanceCheker();
            for (int i = (diag >= board.getN() - board.getM() ? board.getN() - 1 : diag + board.getM() - 1),
                    j = i - diag; j >= 0 && i >= 0; i--, j--) {
                checker.addBalance(i, j);
            }
        }

    }

    private void deltaRecalc(Cell cell, int r, int c, int dx, int dy) {
        int nearBadI = r + dx * k;
        int nearBadJ = c + dy * k;
        for (int i = r + dx, j = c + dy; dx * i < dx * r + k && dy * j < dy * c + k; i += dx, j += dy) {
            if (!board.isInBoard(i, j) || board.getCell(i, j) == cell) {
                nearBadI = i;
                nearBadJ = j;
                break;
            }
        }

        for (int i = r, j = c; dx * i > dx * r - k && dy * j > dy * c - k ; i -= dx, j -= dy) {
            if (!board.isInBoard(i, j) || board.getCell(i, j) == cell)
                break;
            else if (k <= Math.max(Math.abs(nearBadI - i), Math.abs(nearBadJ - j))) {
                balance--;
            }
        }
    }

    public void recalc(Move move) {
        Cell cell = move.getValue();
        int r = move.getRow();
        int c = move.getColumn();
        deltaRecalc(cell, r, c, 0, 1);
        deltaRecalc(cell, r, c, 1, 0);
        deltaRecalc(cell, r, c, 1, -1);
        deltaRecalc(cell, r, c, 1, 1);
    }

}
