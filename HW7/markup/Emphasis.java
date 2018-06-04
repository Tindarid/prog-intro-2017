package markup;

import java.util.List;
import java.lang.StringBuilder;

public class Emphasis extends AbstractTextElement {
    public Emphasis(List<TextElement> temp) {
        super(temp);
        openingMark = closingMark = "*";
        openingHtml = "<em>"; closingHtml = "</em>";
        openingTex = "\\emph{"; closingTex = "}";
    }
}
