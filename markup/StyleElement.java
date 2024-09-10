package markup;

import java.util.List;

public abstract class StyleElement extends Container implements Element {

    public abstract String getMarkdownString();

    public abstract String getBBCodeTag();

    public StyleElement(List<Element> elements){
        super(elements);
    }

    public void toMarkdown(StringBuilder s){
        s.append(getMarkdownString());
        super.toMarkdown(s);
        s.append(getMarkdownString());
    }

    public void toBBCode(StringBuilder s){
        s.append("[").append(getBBCodeTag()).append("]");
        super.toBBCode(s);
        s.append("[/").append(getBBCodeTag()).append("]");
    }
}
