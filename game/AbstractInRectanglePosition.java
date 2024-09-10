package game;

import java.util.Map;

public class AbstractInRectanglePosition implements Position {

    private static final Map<Cell, String> SYMBOLS = Map.of(
            Cell.X, "X",
            Cell.O, "O",
            Cell.E, ".");

    private final AbstractInRectangleBoard board;

    public AbstractInRectanglePosition(AbstractInRectangleBoard board) {
        this.board = board;
    }

    @Override
    public boolean isValid(final Move move) {
        return board.isInBoard(move.getRow(), move.getColumn())
                && getCell(move.getRow(), move.getColumn()) == Cell.E
                && move.getValue() == board.getCell();
    }

    @Override
    public Cell getCell(int r, int c) {
        return board.getCell(r, c);
    }

    public int getN() {
        return board.getN();
    }

    public int getM() {
        return board.getM();
    }

    private static String dopToLength(int n, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n - s.length(); i++) {
            sb.append(' ');
        }
        sb.append(s);
        return sb.toString();
    }

    @Override
    public String toString() {
        int widthRow = Integer.toString(board.getN() - 1).length();
        int widthCol = Integer.toString(board.getM() - 1).length() + 1;
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < widthRow; i++) {
            sb.append(" ");
        }

        for (int j = 0; j < board.getN(); j++) {
            sb.append(dopToLength(widthCol, Integer.toString(j)));
        }

        for (int r = 0; r < board.getM(); r++) {
            sb.append("\n");
            sb.append(dopToLength(widthRow, Integer.toString(r)));
            for (int c = 0; c < board.getM(); c++) {
                sb.append(dopToLength(widthCol,
                        board.isInBoard(r, c) ? SYMBOLS.get(getCell(r, c)) : " "));
            }
        }
        return sb.toString();
    }
}
