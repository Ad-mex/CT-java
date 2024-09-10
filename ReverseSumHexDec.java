import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReverseSumHexDec {
    private static int parseHexDec(String arg) {
        if (arg.toLowerCase().startsWith("0x")) { // hex
            return Integer.parseUnsignedInt(arg.substring(2), 16);
        } else { // dec
            return Integer.parseInt(arg);
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<List<Integer>> table = new ArrayList<>();

        int maxLength = 0;

        try {
            List<Integer> row = new ArrayList<>();
            while (scanner.hasNext()) {
                int lines = scanner.getSkippedLines();
                if (lines == 0) {
                    row.add(parseHexDec(scanner.next()));
                } else {
                    for (int i = 0; i < lines; i++) {
                        table.add(new ArrayList<>(row));
                        maxLength = Math.max(maxLength, row.size());
                        row.clear();
                    }
                    row.add(parseHexDec(scanner.next()));
                }
            }

            table.add(row);
            maxLength = Math.max(maxLength, row.size());

            int lines = scanner.getSkippedLines();
            if (lines > 1) {
                for (int i = 1; i < lines; i++) {
                    table.add(new ArrayList<>());
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error while scanner closing");
            return;
        }

        int[][] pref = new int[table.size()][];
        int[] sumInColumn = new int[maxLength];
        for (int row = 0; row < table.size(); row++) {
            pref[row] = new int[table.get(row).size()];
            for (int col = 0; col < pref[row].length; col++) {
                sumInColumn[col] += table.get(row).get(col);
                pref[row][col] = (col > 0 ? pref[row][col - 1] : 0) + sumInColumn[col];
            }
        }

        for (int row = 0; row < table.size(); row++) {
            for (int col = 0; col < pref[row].length; col++) {
                System.out.print(pref[row][col]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
