import java.io.IOException;
import java.util.*;

public class Reverse {

    public static void main(String[] args) {

        List<List<Integer>> table = new ArrayList<List<Integer>>();
        List<Integer> row = new ArrayList<Integer>();

        Scanner scanner = new Scanner(System.in);

        try {
            while (scanner.hasNext()) {
                int lines = scanner.getSkippedLines();
                if (lines == 0) {
                    row.add(scanner.nextInt());
                } else {
                    for (int i = 0; i < lines; i++) {
                        table.add(new ArrayList<>(row));
                        row.clear();
                    }
                    row.add(scanner.nextInt());
                }
            }
            table.add(row);
            int lines = scanner.getSkippedLines();
            if (lines > 1) {
                for (int i = 1; i < lines; i++) {
                    table.add(new ArrayList<>());
                }
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        try {
            scanner.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        for (int rowIndex = table.size() - 1; rowIndex >= 0; rowIndex--) {
            for (int colIndex = table.get(rowIndex).size() - 1; colIndex >= 0; colIndex--) {
                System.out.print(table.get(rowIndex).get(colIndex));
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
