import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WsppSortedRFirst {

    private static String reversed(String s) {
        StringBuilder t = new StringBuilder(s);
        t.reverse();
        return t.toString();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Exception: no input file");
            return;
        } else if (args.length < 2) {
            System.out.println("Exception: no output file");
            return;
        }
        String inputFilename = args[0];
        String outputFilename = args[1];

        Scanner scanner;
        try {
            scanner = new Scanner(new File(inputFilename),
                    "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            return;
        }

        Map<String, List<Integer>> mapEntries = new TreeMap<>();
        Map<String, Integer> mapCount = new HashMap<>();

        int index = 1;

        try {
            Scanner.Expression isPartOfWord = (c) -> (Character.getType(c) == Character.DASH_PUNCTUATION ||
                    Character.isLetter(c) ||
                    c == '\'');

            int firstIndexInLine = 1;
            while (scanner.hasNext(isPartOfWord)) {

                int lines = scanner.getSkippedLines();

                if (lines > 0) {
                    firstIndexInLine = index;
                }

                String rev = reversed(scanner.next(isPartOfWord).toLowerCase());

                if (mapEntries.containsKey(rev)) {
                    if (mapEntries.get(rev).get(mapEntries.get(rev).size() - 1) < firstIndexInLine) {
                        mapEntries.get(rev).add(index);
                    }
                } else {
                    List<Integer> list = new ArrayList<>();
                    list.add(index);
                    mapEntries.put(rev, list);
                }
                mapCount.put(rev, mapCount.getOrDefault(rev, 0) + 1);
                index++;
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            scanner.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            BufferedWriter output = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(outputFilename),
                            StandardCharsets.UTF_8));

            for (Map.Entry<String, List<Integer>> entry : mapEntries.entrySet()) {
                String unrev = reversed(entry.getKey());
                output.write(unrev);
                output.write(" ");
                output.write(Integer.toString(mapCount.get(entry.getKey())));
                for (Integer x : entry.getValue()) {
                    output.write(" " + Integer.toString(x));
                }
                output.write("\n");
            }
            output.close();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
