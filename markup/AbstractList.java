package markup;

import java.util.List;

public abstract class AbstractList implements Item{
    List<ListItem> elements;

    public AbstractList(List<ListItem> elements) {
        this.elements = elements;
    }

    public abstract String getBBCodeSpecifier();

    public void toBBCode(StringBuilder s) {
        s.append("[list" + getBBCodeSpecifier() + "]");
        for (ListItem element : elements) {
            element.toBBCode(s);
        }
        s.append("[/list]");
    }
}
