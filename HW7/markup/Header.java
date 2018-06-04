package markup;

import java.util.List;
import java.lang.StringBuilder;

public class Header extends AbstractTextElement {
    public Header(List<TextElement> temp, int level) {
        super(temp);
        openingHtml = "<h" + level + ">"; closingHtml = "</h" + level + ">";
    }
}
