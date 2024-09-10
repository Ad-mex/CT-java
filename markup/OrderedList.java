package markup;

import java.util.List;

public class OrderedList extends AbstractList {
    List<ListItem> elements;

    public OrderedList(List<ListItem> elements) {
        super(elements);
    }

    public String getBBCodeSpecifier() {
        return "=1";
    }
}
