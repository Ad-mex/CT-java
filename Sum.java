public class Sum {
    public static void main(String[] args) {

        int sum = 0;

        for (String arg : args) {
            int firstIndexOfNumber = 0;
            for (int ind = 0; ind <= arg.length(); ind++) {

                if (ind == arg.length() || isSeparator(arg.charAt(ind))) {

                    if (firstIndexOfNumber != ind) {
                        sum += Integer.parseInt(arg.substring(firstIndexOfNumber, ind));
                    }
                    firstIndexOfNumber = ind + 1;
                }
            }
        }

        System.out.println(sum);
    }

    private static boolean isSeparator(Character c) {
        return Character.getType(c) == Character.SPACE_SEPARATOR ||
                Character.getType(c) == Character.LINE_SEPARATOR ||
                Character.getType(c) == Character.PARAGRAPH_SEPARATOR ||
                Character.getType(c) == Character.CONTROL;
    }
}