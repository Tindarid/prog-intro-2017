package markup;

import java.util.List;
import java.lang.StringBuilder;

public class Strikeout extends AbstractTextElement {
    public Strikeout(List<TextElement> temp) {
        super(temp);
        openingMark = closingMark = "~";
        openingHtml = "<s>"; closingHtml = "</s>";
        openingTex = "\\textst{"; closingTex = "}";
    }
}
