package markup;

import java.util.List;

// :NOTE: можно вынести в абстрактный класс
public class UnorderedList extends AbstractList{

    public UnorderedList(List<ListItem> elements) {
        super(elements);
    }

    public String getBBCodeSpecifier(){
        return "";
    }
    
    public void toBBCode(StringBuilder s) {
        s.append("[list]");
        for (ListItem element : elements) {
            element.toBBCode(s);
        }
        s.append("[/list]");
    }
}
