package markup;

import java.util.List;

public class Emphasis extends StyleElement{

    public Emphasis(List<Element> elements) {
        super(elements);
    }

    public String getMarkdownString() {
        return "*";
    }

    public String getBBCodeTag() {
        return "i";
    }
}
