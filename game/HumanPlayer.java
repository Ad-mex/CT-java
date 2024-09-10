package game;

import java.io.PrintStream;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;
    String name;

    public String getName() {
        return name;
    }

    public HumanPlayer(final PrintStream out, final Scanner in, String name) {
        this.out = out;
        this.in = in;
        this.name = name;
    }

    public HumanPlayer(String name) {
        this(System.out, new Scanner(System.in), name);
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println("Position");
            out.println(position);
            out.println(cell + "'s move " + "(" + getName() + ")");
            out.println("Enter row and column");
            
            String rowStr = (in.hasNext() ? in.next() : null);
            String colStr = (in.hasNext() ? in.next() : null);

            int row, col;
            if (isInteger(rowStr) && isInteger(colStr)) {
                row = Integer.parseInt(rowStr);
                col = Integer.parseInt(colStr);
            } else {
                out.println("Not an integer");
                continue;
            }
            final Move move = new Move(row, col, cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }
}
