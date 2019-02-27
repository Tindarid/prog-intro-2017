package markup;

import java.lang.StringBuilder;

public interface TextElement {
    void toMarkdown(StringBuilder s);
    void toTex(StringBuilder s);
    void toHtml(StringBuilder s);
}
