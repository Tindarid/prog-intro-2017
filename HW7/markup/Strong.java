package markup;

import java.util.List;
import java.lang.StringBuilder;

public class Strong extends AbstractTextElement {
    public Strong(List<TextElement> temp) {
        super(temp);
        openingMark = closingMark = "__";
        openingHtml = "<strong>"; closingHtml = "</strong>";
        openingTex = "\\textbf{"; closingTex = "}";
    }
}
