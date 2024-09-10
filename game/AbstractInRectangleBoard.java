package game;

import java.util.Arrays;

public abstract class AbstractInRectangleBoard implements Board {

    private final Cell[][] cells;
    private Cell turn;
    private final int n, m, k;
    private final Position position;
    private TechnicalDrawChecker checker;

    public AbstractInRectangleBoard(int m, int n, int k) throws Exception {
        if (m <= 0 || n <= 0) {
            throw new IllegalArgumentException("Incorrect size of board");
        } else if (k > Math.max(n, m) || k <= 0) {
            throw new IllegalArgumentException("Incorrect k value");
        }
        this.n = n;
        this.m = m;
        this.k = k;
        this.cells = new Cell[n][m];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        this.turn = Cell.X;
        this.position = new AbstractInRectanglePosition(this);
        this.checker = new TechnicalDrawChecker(this, k);
    }

    public void clear() {
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        this.turn = Cell.X;
        this.checker = new TechnicalDrawChecker(this, k);
    }

    public abstract boolean isInBoard(int r, int c);

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    private boolean checkWin(int r, int c, Cell cell, int dx, int dy) {
        int left = 0, right = 0;
        for (int i = r + dx, j = c + dy, cnt = 0; isInBoard(i, j) && cnt < k; i += dx, j += dy, cnt++) {
            if (cells[i][j] == cell) {
                right++;
            } else {
                break;
            }
        }
        for (int i = r - dx, j = c - dy, cnt = 0; isInBoard(i, j) && cnt < k; i -= dx, j -= dy, cnt++) {
            if (cells[i][j] == cell) {
                left++;
            } else {
                break;
            }
        }
        return left + right + 1 >= k;

    }

    @Override
    public Result makeMove(final Move move) {
        if (!position.isValid(move)) {
            return Result.LOSE;
        }

        checker.recalc(move);
        cells[move.getRow()][move.getColumn()] = move.getValue();

        if (checker.isDraw()) {
            return Result.DRAW;
        }

        if (checkWin(move.getRow(), move.getColumn(), move.getValue(), 0, 1) ||
                checkWin(move.getRow(), move.getColumn(), move.getValue(), 1, 0) ||
                checkWin(move.getRow(), move.getColumn(), move.getValue(), 1, 1) ||
                checkWin(move.getRow(), move.getColumn(), move.getValue(), 1, -1)) {
            return Result.WIN;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    public Cell getCell(int r, int c) {
        return cells[r][c];
    }
}
