import java.math.BigInteger;

public class SumBigIntegerSpace {
    public static void main(String[] args) {

        BigInteger sum = BigInteger.ZERO;

        for (String arg : args) {
            int firstIndexOfNumber = 0;
            for (int ind = 0; ind <= arg.length(); ind++) {

                if (ind == arg.length() || isSeparator(arg.charAt(ind))) {

                    if (firstIndexOfNumber != ind) {
                        sum = sum.add(new BigInteger(arg.substring(firstIndexOfNumber, ind)));
                    }
                    firstIndexOfNumber = ind + 1;
                }
            }
        }
        System.out.println(sum);
    }

    private static boolean isSeparator(Character c) {
        return Character.getType(c) == Character.SPACE_SEPARATOR;
    }
}