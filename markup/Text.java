package markup;

public class Text implements Element {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    public void toBBCode(StringBuilder s) {
        s.append(text);
    }

    public void toMarkdown(StringBuilder s) {
        s.append(text);
    }
}
