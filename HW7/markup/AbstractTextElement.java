package markup;

import java.util.List;
import java.lang.StringBuilder;

public abstract class AbstractTextElement implements TextElement {
    protected List<TextElement> contents;
    protected String openingMark = "", closingMark = "",
                     openingHtml = "", closingHtml = "",
                     openingTex = "", closingTex = "";
    
    public AbstractTextElement(List<TextElement> temp) {
        contents = temp;
    }
    
    public void toMarkdown(StringBuilder s) {
        s.append(openingMark);
        for (TextElement element : contents) {
            element.toMarkdown(s);
        }
        s.append(closingMark);
    }

    public void toHtml(StringBuilder s) {
        s.append(openingHtml);
        for (TextElement element : contents) {
            element.toHtml(s);
        }
        s.append(closingHtml);
    }

    public void toTex(StringBuilder s) {
        s.append(openingTex);
        for (TextElement element : contents) {
            element.toTex(s);
        }
        s.append(closingTex);
    }
}
