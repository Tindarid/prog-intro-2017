package markup;

import java.lang.StringBuilder;

public class Text implements TextElement {
    private String contents;

    public Text(String s) {
        contents = s;
    }   
    
    public void toMarkdown(StringBuilder s) {
        s.append(contents);
    }
    
    public void toHtml(StringBuilder s) {
        s.append(contents);
    }

    public void toTex(StringBuilder s) {
        s.append(contents);
    }
}
