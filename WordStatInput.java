import java.io.*;
import java.util.*;

public class WordStatInput {

    private static void addWord(String word, List<String> words, List<Integer> entries) {
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equals(word)) {
                entries.set(i, entries.get(i) + 1);
                return;
            }
        }
        entries.add(1);
        words.add(word);
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
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            return;
        }

        List<Integer> entries = new ArrayList<Integer>();
        List<String> words = new ArrayList<String>();

        Scanner.Expression isPartOfWord = (c) -> (Character.getType(c) == Character.DASH_PUNCTUATION ||
                Character.isLetter(c) ||
                c == '\'');

        try {
            while (scanner.hasNext(isPartOfWord)) {
                addWord(scanner.next(isPartOfWord).toLowerCase(), words, entries);
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
                            "UTF-8"));
            for (int i = 0; i < words.size(); i++) {
                output.write(words.get(i));
                output.write(" ");
                output.write(Integer.toString(entries.get(i)));
                output.write('\n');
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