package game;

import java.util.Random;

public class RandomPlayer implements Player {
    String name;
    public RandomPlayer(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public Move move(final Position position, final Cell cell) {
        Random rnd = new Random();
        while (true) {
            Move move = null;
            do {
                int row = rnd.nextInt(position.getN());
                int col = rnd.nextInt(position.getM());
                move = new Move(row, col, cell);
            } while (!position.isValid(move));
            return move;
        }
    }
}
