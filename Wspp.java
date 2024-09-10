import java.io.*;
import java.util.*;

public class Wspp {

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

        scanner.setBufferSize(1 << 12);

        Map<String, List<Integer>> mapCount = new HashMap<String, List<Integer>>();
        List<String> order = new ArrayList<>();

        Scanner.Expression isPartOfWord = (c) -> (Character.getType(c) == Character.DASH_PUNCTUATION ||
                Character.isLetter(c) ||
                c == '\'');

        try {
            int index = 1;
            while (scanner.hasNext(isPartOfWord)) {
                String s = scanner.next(isPartOfWord).toLowerCase();
                if (mapCount.containsKey(s)) {
                    mapCount.get(s).add(index);
                } else {
                    List<Integer> list = new ArrayList<>();
                    list.add(index);
                    mapCount.put(s, list);
                    order.add(s);
                }
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
                            "UTF-8"));

            for (String entry : order) {
                output.write(entry);
                output.write(" ");
                output.write(Integer.toString(mapCount.get(entry).size()));
                for (Integer x : mapCount.get(entry)) {
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