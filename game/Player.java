package game;

public interface Player {
    String getName();
    Move move(Position position, Cell cell);
}
