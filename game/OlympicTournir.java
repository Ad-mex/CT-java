package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OlympicTournir {
    private Board board;
    private List<Player> players;
    private boolean wasGame = false;
    private List<List<Player>> places;

    public OlympicTournir(List<Player> players, Board board) {
        this.board = board;
        this.players = players;
    }

    public List<List<Player>> getResults() {
        if (!wasGame) {
            return null;
        } else {
            return places;
        }
    }

    public Player getWinner() {
        if (!wasGame) {
            return null;
        } else {
            return places.get(places.size() - 1).get(0);
        }
    }

    public void play() {
        wasGame = true;

        List<Player> currentTour = List.copyOf(players);
        List<List<Player>> tournir = new ArrayList<>();
        Random rnd = new Random();

        while (currentTour.size() != 1) {
            List<Player> nextLevel = new ArrayList<>();
            List<Player> loosers = new ArrayList<>();

            for (int i = 0; i < currentTour.size() - 1; i += 2) {

                Player a, b;
                int result;
                do {
                    boolean whoFirst = rnd.nextBoolean();
                    
                    if (whoFirst) {
                        a = currentTour.get(i);
                        b = currentTour.get(i + 1);
                    } else {
                        b = currentTour.get(i);
                        a = currentTour.get(i + 1);
                    }
                    Game game = new Game(false, a, b);
                    
                    System.out.println("Game between " + a.getName() + " and " + b.getName());
                    board.clear();
                    result = game.play(board);
                    System.out.println("Game between " + a.getName() + " and " + b.getName() + " ended");
                    if (result == 0) {
                        System.out.println("draw, again!");
                    } else if(result == 1) {
                        System.out.println(a.getName() + " won");
                    } else {
                        System.out.println(b.getName() + " won");
                    }
                } while (result == 0);

                if (result == 1) {
                    nextLevel.add(a);
                    loosers.add(b);
                } else {
                    nextLevel.add(b);
                    loosers.add(a);
                }
            }
            if (currentTour.size() % 2 == 1) {
                nextLevel.add(currentTour.get(currentTour.size() - 1));
            }
            currentTour = nextLevel;
            tournir.add(loosers);
        }
        tournir.add(currentTour);

        this.places = tournir;
    }
}
