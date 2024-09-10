package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static boolean getType(String msg, Scanner in) {
        boolean flag;
        while (true) {
            System.out.println(msg);
            if (!in.hasNext()) {
                in.close();
                System.exit(0);
            }
            String s = in.next();
            if (s.equals("1")) {
                flag = false;
                break;
            } else if (s.equals("2")) {
                flag = true;
                break;
            } else {
                System.out.println("Bad format.");
            }
        }
        return flag;
    }

    private static int getIntegerParameter(String msg, Scanner in) {
        int answer;
        while (true) {
            System.out.println(msg);
            if (!in.hasNext()) {
                in.close();
                System.exit(0);
            }
            if (!in.hasNextInt()) {
                System.out.println("Bad format.");
                continue;
            }
            answer = in.nextInt();
            if (answer <= 0) {
                System.out.println("Bad format.");
                continue;
            } else {
                break;
            }
        }
        return answer;
    }

    private static Board getBoard(Scanner in) {
        boolean boardFlag = getType("Enter `1` if you want play on Circle Board, `2` to play on Rectangle", in);
        Board board = null;
        if (boardFlag) {
            int m = getIntegerParameter("Enter 'm' size of board:", in);
            int n = getIntegerParameter("Enter 'n' size of board:", in);
            int k;
            while (true) {
                k = getIntegerParameter("Enter 'k' size of board:", in);
                if (k > Math.max(n, m)) {
                    System.out.println("k would be less or equal than max(n, m)");
                } else {
                    break;
                }
            }
            try {
                board = new RectangleBoard(m, n, k);
            } catch (Exception e) {
                System.out.println("Something went wrong");
                System.out.println(e.getMessage());
                in.close();
                System.exit(0);
            }
        } else {
            int n = getIntegerParameter("Enter size of board (n)", in);
            int k;
            while (true) {
                k = getIntegerParameter("Enter 'k' size of board:", in);
                if (k > n) {
                    System.out.println("k would be less or equal than n");
                } else {
                    break;
                }
            }
            try {
                board = new CircleBoard(n, k);
            } catch (Exception e) {
                System.out.println("Something went wrong");
                System.out.println(e.getMessage());
                in.close();
                System.exit(0);
            }
        }
        return board;
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        boolean flag = getType("Enter (without quotes) `1` if you want tournir and `2` if you want just a game", in);

        if (flag) { // game
            boolean firstPlayerFlag = getType("First player (X): Human (`1`) or RandomPlayer (`2`) ?", in);
            Player player1 = (firstPlayerFlag ? new RandomPlayer("Random Player 1")
                    : new HumanPlayer(System.out, in, "Human Player 1"));
            boolean secondPlayerFlag = getType("Second player (O): Human (`1`) or RandomPlayer (`2`) ?", in);
            Player player2 = (secondPlayerFlag ? new RandomPlayer("Random Player 2")
                    : new HumanPlayer(System.out, in, "Human Player 2"));

            final Game game = new Game(false, player1, player2);

            Board board = getBoard(in);

            int result = game.play(board);
            if (result == 0) {
                System.out.println("Game result: draw");
            } else if (result == 1) {
                System.out.println("Player 1: " + player1.getName() + " won.");
            } else if (result == 2) {
                System.out.println("Player 2: " + player2.getName() + " won.");
            }
            System.out.println(board.getPosition());

        } else { // tournir
            int countOfPlayers = getIntegerParameter("Enter count of players:", in);
            List<Player> players = new ArrayList<>();
            for (int i = 0; i < countOfPlayers; i++) {
                boolean PlayerFlag = getType("Player (X): Human (`1`) or RandomPlayer (`2`) ?", in);
                System.out.println("Enter player name (without spaces):");
                String playerName;
                if (in.hasNext()) {
                    playerName = in.next();
                } else {
                    in.close();
                    return;
                }
                Player player = (PlayerFlag ? new RandomPlayer(playerName)
                        : new HumanPlayer(System.out, in, playerName));
                players.add(player);
            }

            Board board = getBoard(in);

            OlympicTournir tournament = new OlympicTournir(players, board);
            tournament.play();
            System.out.println("Winner: " + tournament.getWinner().getName());
            System.out.println("Full Results:");
            for (var lvl : tournament.getResults()) {
                for (var player : lvl) {
                    System.out.print(player.getName());
                    System.out.print(" ");
                }
                System.out.println();
            }
        }
    }
}
