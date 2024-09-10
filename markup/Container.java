package markup;

import java.util.List;

public abstract class Container implements Convertable{
    private List<Element> elements;

    public Container(List<Element> elements) {
        this.elements = elements;
    }
    
    public void toMarkdown(StringBuilder s){
        for (Element element : elements) {
            element.toMarkdown(s);
        }
    }

    public void toBBCode(StringBuilder s){
        for (Element element : elements) {
            element.toBBCode(s);
        }
    }
}
