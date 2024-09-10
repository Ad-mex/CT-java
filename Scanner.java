import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

public class Scanner {

    private class CacheDeque {
        private int sz = 32;
        private char[] buf = new char[sz];

        int indFirst = 0, indFirstSpace = 0;

        boolean isEmpty() {
            return indFirstSpace == indFirst;
        }

        void addLast(char c) {
            buf[indFirstSpace] = c;
            indFirstSpace++;
            if (indFirstSpace == sz) {
                indFirstSpace = 0;
            }
        }

        void addFirst(char c) {
            if (indFirst == 0)
                indFirst = sz;
            indFirst--;
            buf[indFirst] = c;
        }

        char removeFirst() {
            char x = buf[indFirst];
            indFirst++;
            if (indFirst == sz) {
                indFirst = 0;
            }
            return x;
        }

    }

    private class BufferedReader {
        private int bufSize = 1 << 12;
        private char[] buffer = new char[bufSize];
        private int curpos = 0;
        private int cnt = 0;
        private Reader reader;

        BufferedReader(Reader reader) {
            this.reader = reader;
        }

        public int read() throws IOException {
            if (cnt == -1) {
                return -1;
            }
            if (curpos >= cnt) {
                curpos = 0;
                cnt = reader.read(buffer);
                if (cnt == -1) {
                    return -1;
                } else {
                    return (int) buffer[curpos++];
                }
            } else {
                return (int) buffer[curpos++];
            }
        }

        public boolean ready() throws IOException {
            if (cnt == -1) {
                return false;
            }
            if (curpos >= cnt) {
                curpos = 0;
                cnt = reader.read(buffer);
                if (cnt == -1) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }

        public void close() throws IOException {
            reader.close();
        }
    }

    private final char[] lineSeparator = System.lineSeparator().toCharArray();
    private CacheDeque cache = new CacheDeque();
    private BufferedReader reader;
    private int lastParsedInt;
    private int skippedLines;

    public Scanner(String inputString) {
        reader = new BufferedReader(new StringReader(inputString));
    }

    public Scanner(File inputFile, String codec) throws FileNotFoundException, UnsupportedEncodingException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), codec));
    }

    public Scanner(InputStream inputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public void setBufferSize(int sz) {
        reader.bufSize = sz;
        reader.buffer = new char[sz];
    }

    public boolean hasNextCharacter() throws IOException {
        if (!cache.isEmpty())
            return true;
        if (reader.ready()) {
            int sym = reader.read();
            if (sym == -1) {
                return false;
            } else {
                char c = (char) sym;
                cache.addLast(c);
                return true;
            }
        }
        return false;
    }

    public char nextCharacter() throws IOException {
        if (!hasNextCharacter()) {
            throw new IOException("Nothing to read");
        } else {
            return cache.removeFirst();
        }
    }

    public interface Expression {
        boolean isNeedSymbol(char c);
    }

    public int getSkippedLines(){
        return skippedLines;
    }
    
    public boolean hasNext(Expression e) throws IOException {
        char saved = 0;
        boolean found = false;
        int nextSymToEqWithSep = 0;
        skippedLines = 0;
        while (hasNextCharacter()) {
            char c = nextCharacter();
            if (c == lineSeparator[nextSymToEqWithSep]) {
                nextSymToEqWithSep++;
                if (nextSymToEqWithSep == lineSeparator.length) {
                    nextSymToEqWithSep = 0;
                    skippedLines++;
                }
            } else {
                nextSymToEqWithSep = (c == lineSeparator[0] ? 1 : 0);
            }
            if (e.isNeedSymbol(c)) {
                saved = c;
                found = true;
                break;
            }
        }
        if (found) {
            cache.addFirst(saved);
        }
        return found;
    }

    public boolean hasNext() throws IOException {
        return hasNext(c -> !Character.isWhitespace(c));
    }

    public String next(Expression func) throws IOException {
        if (!hasNext(func)) {
            throw new IOException("Nothing to read");
        } else {
            StringBuilder s = new StringBuilder();
            while (hasNextCharacter()) {
                char c = nextCharacter();
                if (func.isNeedSymbol(c)) {
                    s.append(c);
                } else {
                    cache.addFirst(c);
                    break;
                }
            }
            return s.toString();
        }
    }

    public String next() throws IOException {
        return next(c -> !Character.isWhitespace(c));
    }

    public boolean hasNextInt() throws IOException {
        if (!hasNext())
            return false;
        String s = next();
        boolean flag;
        try {
            lastParsedInt = Integer.parseInt(s);
            flag = true;
        } catch (NumberFormatException e) {
            flag = false;
        }
        return flag;
    }

    public int nextInt() throws IOException {
        if (!hasNextInt()) {
            throw new IOException("Nothing to read");
        } else {
            return lastParsedInt;
        }
    }

    public boolean hasNextLine() throws IOException {
        if (!hasNextCharacter())
            return false;
        return true;
    }

    public String nextLine() throws IOException {
        if (!hasNextCharacter()) {
            throw new IOException("Nothing to read");
        }
        Character c;
        StringBuilder s = new StringBuilder();
        int nextSymToEqWithSep = 0;
        while (hasNextCharacter()) {
            c = nextCharacter();
            if (c == lineSeparator[nextSymToEqWithSep]) {
                nextSymToEqWithSep++;
                if (nextSymToEqWithSep == lineSeparator.length) {
                    nextSymToEqWithSep = 0;
                    break;
                }
            } else {
                if (nextSymToEqWithSep != 0) {
                    for (int i = 0; i < nextSymToEqWithSep; i++) {
                        s.append(lineSeparator[i]);
                    }
                }
                nextSymToEqWithSep = 0;
                if (c == lineSeparator[0]) {
                    nextSymToEqWithSep = 1;
                } else {
                    s.append(c);
                }
            }
        }
        if (nextSymToEqWithSep != 0) {
            for (int i = 0; i < nextSymToEqWithSep; i++) {
                s.append(lineSeparator[i]);
            }
        }
        return s.toString();
    }

    public void close() throws IOException {
        reader.close();
    }

}