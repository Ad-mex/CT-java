package markup;

import java.util.List;

public class Strong extends StyleElement{

    
    public Strong(List<Element> elements) {
        super(elements);
    }

    public String getMarkdownString() {
        return "__";
    }

    public String getBBCodeTag() {
        return "b";
    }
}
