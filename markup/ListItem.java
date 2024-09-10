package markup;

import java.util.List;

public class ListItem implements ConvertableToBBCode {
    private List<Item> elements;

    public ListItem(List<Item>elements){
        this.elements = elements;
    }

    public void toBBCode(StringBuilder s) {
        s.append("[*]");
        for (Item item : elements) {
            item.toBBCode(s);
        }
    }
}
