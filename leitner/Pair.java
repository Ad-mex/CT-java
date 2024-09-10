package leitner;

import java.util.Scanner;

public class Pair {
    String original;
    String translate;

    public Pair(String a, String b) {
        original = a;
        translate = b;
    }

    public static Pair getPair(Scanner scanner) throws Exception{
        if(!scanner.hasNext()) throw new Exception();
        return new Pair(scanner.next(), scanner.next());
    }
}
