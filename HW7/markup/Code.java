package markup;

import java.util.List;
import java.lang.StringBuilder;

public class Code extends AbstractTextElement {
    public Code(List<TextElement> temp) {
        super(temp);
        openingMark = closingMark = "`";
        openingHtml = "<code>"; closingHtml = "</code>";
    }
}
