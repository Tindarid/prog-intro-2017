package md2html;

import java.util.*;
import java.io.*;

public class Md2Html {
    private static char read(Reader in) {
       try {
           return (char) in.read();
       } catch (IOException e) {
           System.out.println("IOException: " + e.getMessage());
           return (char) -1;
       }
    } 

    private static void write(Writer out, String c) {
        try {
            out.write(c);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static void serve(String curContr, Map<String, Integer> contr, StringBuilder str) {
        List<String> tempList = new ArrayList<String>(contr.keySet());
        if (curContr == "") {
            for (int i = tempList.size() - 1; i >= 0; i--) {
                String temp = tempList.get(i);
                str.insert(contr.get(temp), temp);
                contr.remove(temp);
            }
            return;
        }
        if (!contr.containsKey(curContr)) {
            contr.put(curContr, str.length());
        } else {
            for (int i = tempList.size() - 1; i >= 0; i--) {
                String temp = tempList.get(i);
                if (temp.equals(curContr)) {
                    if (temp.equals("__") || temp.equals("**")) {
                        str.insert(contr.get(temp), "<strong>");
                        str.append("</strong>");
                    } else if (temp.equals("`")) {
                        str.insert(contr.get(temp), "<code>");
                        str.append("</code>");
                    } else if (temp.equals("*") || temp.equals("_")) {
                        str.insert(contr.get(temp), "<em>");
                        str.append("</em>");
                    } else if (temp.equals("--")) {
                        str.insert(contr.get(temp), "<s>");
                        str.append("</s>");
                    } else if (temp.equals("++")) {
                        str.insert(contr.get(temp), "<u>");
                        str.append("</u>");
                    }
                    contr.remove(temp);
                    break;
                } else {
                    str.insert(contr.get(temp), temp);
                    contr.remove(temp);
                }
            }
        }
    }

    private static void dfs(Reader in, Writer out, int level, char last, Map<String, Integer> contr, StringBuilder str) throws IOException {
        boolean flag = false;
        char c = last;
        while (true) {
            if ((c == (char) -1) || (flag && c == '\r')) {
                if (c == '\r') {
                    read(in);
                }
                serve("", contr, str);
                write(out, str.toString());
                str.delete(0, str.length());
                return;
            }
            if (level == 0) {
                StringBuilder tempStr = new StringBuilder();
                while (c == '\r' || c == '\n') {
                    c = read(in);
                }
                while (c == '#') {
                    tempStr.append(c);
                    c = read(in);
                }
                if (tempStr.length() == 0 || c != ' ') {
                    write(out, "<p>" + tempStr.toString());
                    dfs(in, out, 1, c, contr, str);
                    write(out, "</p>\n");
                } else {
                    write(out, "<h" + tempStr.length() + ">");
                    dfs(in, out, 1, (char) in.read(), contr, str);
                    write(out, "</h" + tempStr.length() + ">\n");
                }
                contr.clear();
            } else {
                if (flag) {
                    flag = false;
                    str.append('\n');
                }
                if (c == '\r') {
                    in.read();
                    flag = true;
                } else if (c == '-' || c == '*' || c == '_' || c == '+') {
                    String curContr = "" + c;
                    boolean needToNext = false;
                    char save = c;
                    c = read(in); 
                    if (c == save) {
                        curContr += c;
                        needToNext = true;
                    } else if (save == '-' || save == '+') {
                        str.append(save);
                    }
                    if (!curContr.equals("-") && !curContr.equals("+")) {
                        serve(curContr, contr, str);
                    }
                    if (!needToNext) {
                        continue;
                    }
                } else if (c == '`') {
                    serve(c + "", contr, str);
                } else if (c == '\\') {
                    str.append(read(in));
                } else if (c == '<') {
                    str.append("&lt;");
                } else if (c == '>') {
                    str.append("&gt;");
                } else if (c == '&') {
                    str.append("&amp;");
                } else { 
                    str.append(c);
                }
            }
            c = read(in);
        }
    }

    public static void main(String [] argc) {
        if (argc.length != 2) {
            System.out.println("Need 2 files(in, out)");
            return;
        }

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(argc[0]), "utf-8"));
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(argc[1]), "utf-8"));
                try {
                    dfs(in, out, 0, read(in), new LinkedHashMap<String, Integer>(), new StringBuilder());
                } finally {
                    out.close();
                }
            } catch (FileNotFoundException e) {
                System.out.println("Cannot open output file");
            } catch (SecurityException e) {
                System.out.println("Access for output file denied");
            } catch (UnsupportedEncodingException e) {
                System.out.println("Unsupported encoding(utf-8)");
            } catch (IOException e) {
                System.out.println("Cannot close output file");
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported encoding(UTF-8)");
        } catch (IOException e) {
            System.out.println("Cannot close input file");
        }
    }
}
