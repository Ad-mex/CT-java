import java.io.*;
import java.util.*;

public class WordStatCountAffixL {

    private static String getPrefix(String s) {
        return s.substring(0, 2);
    }

    private static String getSuffix(String s) {
        return s.substring(s.length() - 2);
    }

    private static class Affix {
        int cnt;
        String affix;

        public Affix(Integer cnt, String affix) {
            this.cnt = cnt;
            this.affix = affix;
        }

        public static int compare(Affix a, Affix b) {
            return a.cnt - b.cnt;
        }
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

        File inputFile = new File(inputFilename);

        Scanner scanner;
        try {
            scanner = new Scanner(inputFile, "UTF-8");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            return;
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        List<String> order = new ArrayList<>();

        Scanner.Expression isPartOfWord = (c) -> (Character.getType(c) == Character.DASH_PUNCTUATION ||
                Character.isLetter(c) ||
                c == '\'');

        try {
            while (scanner.hasNext(isPartOfWord)) {
                String s = scanner.next(isPartOfWord).toLowerCase();
                if (s.length() < 2) {
                    continue;
                } else {
                    if (!map.containsKey(getPrefix(s))) {
                        order.add(getPrefix(s));
                    }
                    map.put(getPrefix(s), map.getOrDefault(getPrefix(s), 0) + 1);

                    if (!map.containsKey(getSuffix(s))) {
                        order.add(getSuffix(s));
                    }
                    map.put(getSuffix(s), map.getOrDefault(getSuffix(s), 0) + 1);
                }
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

        List<Affix> list = new ArrayList<>();
        for (String element : order) {
            list.add(new Affix(map.get(element), element));
        }
        list.sort(Affix::compare);

        try {
            BufferedWriter output = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(outputFilename),
                            "UTF-8"));
            for (Affix affix : list) {
                output.write(affix.affix);
                output.write(" ");
                output.write(Integer.toString(affix.cnt));
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