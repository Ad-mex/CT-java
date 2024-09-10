package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Md2Html {

    private static final List<String> TAGS = List.of("**", "__", "--", "*", "_", "`", "<<", ">>", "{{", "}}");
    private static final Map<String, String> openClose = Map.of(
            "<<", ">>",
            "}}", "{{");

    private static final Map<String, String> closeOpen = Map.of(
            "{{", "}}",
            ">>", "<<");

    private static final Map<String, String> htmlTags = Map.of(
            "**", "strong",
            "__", "strong",
            "--", "s",
            "*", "em",
            "_", "em",
            "`", "code",
            "<<", "ins",
            ">>", "ins",
            "}}", "del",
            "{{", "del");

    private static String getTag(String s, int start) {
        if (start - 1 >= 0 && s.charAt(start - 1) == '\\') {
            return null;
        }
        for (String tag : TAGS) {
            if (s.startsWith(tag, start)) {
                return tag;
            }
        }
        return null;
    }

    private static String getViewOfSymbol(char sym) {
        return switch (sym) {
            case '<' -> "&lt;";
            case '>' -> "&gt;";
            case '&' -> "&amp;";
            default -> "" + sym;
        };
    }

    private static String parseElement(String s) {
        StringBuilder ans = new StringBuilder();

        int beg = -1;
        for (int i = 0; i < Math.min(s.length(), 6); i++) {
            if (s.charAt(i) == '#') {
                if (s.length() == i + 1 || s.charAt(i + 1) == ' ') {
                    beg = i + 1;
                    break;
                }
            } else {
                break;
            }
        }

        if (beg > 0) {
            ans.append("<h").append(beg).append(">");
        } else {
            ans.append("<p>");
        }

        Map<String, Integer> freeEntry = new HashMap<>();
        Map<String, Set<Integer>> freeEntriesOfPairedElements = new HashMap<>();
        Map<String, Deque<Integer>> stacks = new HashMap<>();
        for (int i = beg + 1; i < s.length(); i++) {
            String tag = getTag(s, i);
            if (tag == null) {
                continue;
            }
            if (closeOpen.containsKey(tag)) {
                if (stacks.computeIfAbsent(closeOpen.get(tag), x -> new ArrayDeque<>()).isEmpty()) {
                    freeEntriesOfPairedElements.computeIfAbsent(tag, x -> new HashSet<>()).add(i);
                } else {
                    stacks.get(closeOpen.get(tag)).removeLast();
                }
            } else if (openClose.containsKey(tag)) {
                stacks.computeIfAbsent(tag, x -> new ArrayDeque<>()).addLast(i);
            } else if (freeEntry.containsKey(tag)) {
                freeEntry.remove(tag);
            } else {
                freeEntry.put(tag, i);
            }
            i += tag.length() - 1;
        }
        for (Map.Entry<String, Deque<Integer>> entry : stacks.entrySet()) {
            freeEntriesOfPairedElements.put(entry.getKey(), new HashSet<>());
            for (int pos : entry.getValue()) {
                freeEntriesOfPairedElements.get(entry.getKey()).add(pos);
            }
        }

        Set<String> openTags = new HashSet<>();
        for (int i = beg + 1; i < s.length(); i++) {
            if (s.charAt(i) == '\\' && i + 1 != s.length()) {
                ans.append(getViewOfSymbol(s.charAt(i + 1)));
                i++;
                continue;
            }
            String tag = getTag(s, i);
            if (tag == null) {
                ans.append(getViewOfSymbol(s.charAt(i)));
            } else {
                if (freeEntry.getOrDefault(tag, -1) == i
                        || freeEntriesOfPairedElements.getOrDefault(tag, new HashSet<>()).contains(i)) {
                    ans.append(tag);
                } else {
                    ans.append("<");
                    if (closeOpen.containsKey(tag) || openTags.contains(tag)) {
                        openTags.remove(tag);
                        ans.append("/");
                    } else {
                        if (!openClose.containsKey(tag)) {
                            openTags.add(tag);
                        }
                    }
                    ans.append(htmlTags.get(tag)).append(">");
                }
                i += tag.length() - 1;
            }

        }

        if (beg > 0) {
            ans.append("</h").append(beg).append(">\n");
        } else {
            ans.append("</p>\n");
        }

        return ans.toString();
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Wrong number of arguments");
            return;
        }

        String inputFilename = args[0];
        String outputFilename = args[1];

        List<String> answer = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(inputFilename), StandardCharsets.UTF_8)) {
            List<StringBuilder> elements = new ArrayList<>();
            boolean flagIsPrevLineEmpty = true;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    flagIsPrevLineEmpty = true;
                } else {
                    if (flagIsPrevLineEmpty) {
                        elements.add(new StringBuilder(line));
                        flagIsPrevLineEmpty = false;
                    } else {
                        elements.get(elements.size() - 1).append("\n");
                        elements.get(elements.size() - 1).append(line);
                    }
                }
            }

            for (StringBuilder s : elements) {
                answer.add(parseElement(s.toString()));
            }

        } catch (IOException e) {
            System.out.println("Input file not found");
            return;
        }

        try (BufferedWriter output = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFilename), StandardCharsets.UTF_8))) {
            for (String line : answer) {
                output.write(line);
            }
        } catch (IOException e) {
            System.out.println("Output file ot found");
        }
    }
}
