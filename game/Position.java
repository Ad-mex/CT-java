package game;

public interface Position {
    boolean isValid(Move move);
    String toString();
    Cell getCell(int r, int c);
    int getN();
    int getM();
}
