package markup;

import java.util.List;

public class Strikeout extends StyleElement {

    public Strikeout(List<Element> elements) {
        super(elements);
    }

    public String getMarkdownString() {
        return "~";
    }

    public String getBBCodeTag() {
        return "s";
    }

}
